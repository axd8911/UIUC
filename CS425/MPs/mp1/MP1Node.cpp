/**********************************
 * FILE NAME: MP1Node.cpp
 *
 * DESCRIPTION: Membership protocol run by this Node.
 * 				Definition of MP1Node class functions.
 **********************************/

#include "MP1Node.h"

/*
 * Note: You can change/add any functions in MP1Node.{h,cpp}
 */

/**
 * Overloaded Constructor of the MP1Node class
 * You can add new members to the class if you think it
 * is necessary for your logic to work
 */
MP1Node::MP1Node(Member *member, Params *params, EmulNet *emul, Log *log, Address *address) {
	for( int i = 0; i < 6; i++ ) {
		NULLADDR[i] = 0;
	}
	this->memberNode = member;
	this->emulNet = emul;
	this->log = log;
	this->par = params;
	this->memberNode->addr = *address;
}

/**
 * Destructor of the MP1Node class
 */
MP1Node::~MP1Node() {}

/**
 * FUNCTION NAME: recvLoop
 *
 * DESCRIPTION: This function receives message from the network and pushes into the queue
 * 				This function is called by a node to receive messages currently waiting for it
 */
int MP1Node::recvLoop() {
    if ( memberNode->bFailed ) {
    	return false;
    }
    else {
    	return emulNet->ENrecv(&(memberNode->addr), enqueueWrapper, NULL, 1, &(memberNode->mp1q));
    }
}

/**
 * FUNCTION NAME: enqueueWrapper
 *
 * DESCRIPTION: Enqueue the message from Emulnet into the queue
 */
int MP1Node::enqueueWrapper(void *env, char *buff, int size) {
	Queue q;
	return q.enqueue((queue<q_elt> *)env, (void *)buff, size);
}

/**
 * FUNCTION NAME: nodeStart
 *
 * DESCRIPTION: This function bootstraps the node
 * 				All initializations routines for a member.
 * 				Called by the application layer.
 */
void MP1Node::nodeStart(char *servaddrstr, short servport) {
    Address joinaddr;
    joinaddr = getJoinAddress();

    // Self booting routines
    if( initThisNode(&joinaddr) == -1 ) {
#ifdef DEBUGLOG
        log->LOG(&memberNode->addr, "init_thisnode failed. Exit.");
#endif
        exit(1);
    }

    if( !introduceSelfToGroup(&joinaddr) ) {
        finishUpThisNode();
#ifdef DEBUGLOG
        log->LOG(&memberNode->addr, "Unable to join self to group. Exiting.");
#endif
        exit(1);
    }

    return;
}

/**
 * FUNCTION NAME: initThisNode
 *
 * DESCRIPTION: Find out who I am and start up
 */
int MP1Node::initThisNode(Address *joinaddr) {
	/*
	 * This function is partially implemented and may require changes
	 */
	int id = *(int*)(&memberNode->addr.addr);
	int port = *(short*)(&memberNode->addr.addr[4]);

	memberNode->bFailed = false;
	memberNode->inited = true;
	memberNode->inGroup = false;
    // node is up!
	memberNode->nnb = 0;
	memberNode->heartbeat = 0;
	memberNode->pingCounter = TFAIL;
	memberNode->timeOutCounter = -1;
    initMemberListTable(memberNode);

    return 0;
}

/**
 * FUNCTION NAME: introduceSelfToGroup
 *
 * DESCRIPTION: Join the distributed system
 */
int MP1Node::introduceSelfToGroup(Address *joinaddr) {
	MessageHdr *msg;
#ifdef DEBUGLOG
    static char s[1024];
#endif

    if ( 0 == memcmp((char *)&(memberNode->addr.addr), (char *)&(joinaddr->addr), sizeof(memberNode->addr.addr))) {
        // I am the group booter (first process to join the group). Boot up the group
        #ifdef DEBUGLOG
        log->LOG(&memberNode->addr, "Starting up group...");
        log->logNodeAdd(&memberNode->addr, &memberNode->addr);
        #endif
        memberNode->inGroup = true;
        
        //add self to group member ship list
        MemberListEntry newMLE(*(int*)(&memberNode->addr.addr),
                               *(short*)(&memberNode->addr.addr[4]),
                                memberNode->heartbeat, 
                                (long)par->getcurrtime());
        memberNode->memberList.push_back(newMLE);
    }
    else {
        size_t msgsize = sizeof(MessageHdr) + sizeof(joinaddr->addr) + sizeof(long);
        msg = (MessageHdr *) malloc(msgsize * sizeof(char));

        // create JOINREQ message: format of data is {struct Address myaddr}
        msg->msgType = JOINREQ;
        memcpy((char *)(msg+1), &memberNode->addr.addr, sizeof(memberNode->addr.addr));
        memcpy((char *)(msg+1) + sizeof(memberNode->addr.addr), &memberNode->heartbeat, sizeof(long));
        
        #ifdef DEBUGLOG
        sprintf(s, "Trying to join...");
        log->LOG(&memberNode->addr, s);
        #endif

        
        // send JOINREQ message to introducer member
        emulNet->ENsend(&memberNode->addr, joinaddr, (char *)msg, msgsize);
        free(msg);
    }

    return 1;

}

/**
 * FUNCTION NAME: finishUpThisNode
 *
 * DESCRIPTION: Wind up this node and clean up state
 */
int MP1Node::finishUpThisNode(){
   /*
    * Your code goes here
    */
   return 0;
}

/**
 * FUNCTION NAME: nodeLoop
 *
 * DESCRIPTION: Executed periodically at each member
 * 				Check your messages in queue and perform membership protocol duties
 */
void MP1Node::nodeLoop() {
    if (memberNode->bFailed) {
    	return;
    }

    // Check my messages
    checkMessages();

    // Wait until you're in the group...
    if( !memberNode->inGroup ) {
    	return;
    }

    // ...then jump in and share your responsibilites!
    nodeLoopOps();

    return;
}

/**
 * FUNCTION NAME: checkMessages
 *
 * DESCRIPTION: Check messages in the queue and call the respective message handler
 */
void MP1Node::checkMessages() {
    void *ptr;
    int size;

    // Pop waiting messages from memberNode's mp1q
    while ( !memberNode->mp1q.empty() ) {
    	ptr = memberNode->mp1q.front().elt;
    	size = memberNode->mp1q.front().size;
    	memberNode->mp1q.pop();
    	recvCallBack((void *)memberNode, (char *)ptr, size);
    }
    return;
}

/**
 * FUNCTION NAME: recvCallBack
 *
 * DESCRIPTION: Message handler for different message types
 */
bool MP1Node::recvCallBack(void *env, char *data, int size ) {
	/*
	 * Your code goes here
	 */
    
    long currentTime = (long)par->getcurrtime();
    
    //Info about receiver
    Member* rcvrNode = (Member*) env;
    int rcvrID     = *(int*)(&rcvrNode->addr.addr);
    short rcvrPort = *(short*)(&rcvrNode->addr.addr[4]);
    
    //Read in data
    MessageHdr* theMsg = (MessageHdr* ) malloc(size *sizeof(char));
    memcpy((char*)theMsg, data, size);
    
    //Read sender
    Address sender;
    memcpy(&sender, (char*)(theMsg)+ sizeof(MessageHdr), sizeof(Address));
    int sndrID  = *(int*)(sender.addr);
    short sndrPort = (short)(sender.addr[4]);
    
    //heartbeat
    if (rcvrNode->inGroup){
        rcvrNode->heartbeat += 1;
        rcvrNode->memberList[0].heartbeat = rcvrNode->heartbeat;
        rcvrNode->memberList[0].timestamp = currentTime;
    }
    
    if (rcvrNode->inited && !rcvrNode->bFailed){
        switch(theMsg->msgType){
            case(JOINREQ):
            {
                //Sender heartbeat
                long sndrHBeat;
                memcpy(&sndrHBeat, (char*)(theMsg)+sizeof(MessageHdr)+sizeof(Address), sizeof(long));
                
                size_t rspSize = sizeof(MessageHdr) + sizeof(rcvrNode->addr.addr) + sizeof(long);
                
                MessageHdr* rsp = (MessageHdr*) malloc(sizeof(rspSize));
                rsp->msgType = JOINREP;
                
                memcpy((char*)(rsp)+sizeof(MessageHdr), &rcvrNode->addr.addr, sizeof(Address));
                memcpy((char*)(rsp)+sizeof(MessageHdr) + sizeof(Address), &rcvrNode->heartbeat, sizeof(long));
                
                emulNet->ENsend(&rcvrNode->addr, &sender, (char*)rsp, rspSize);
                
                //Add joining node to memberlist
                #ifdef DEBUGLOG
                log->logNodeAdd(&rcvrNode->addr, &sender);
                #endif
                
                MemberListEntry newMLE(sndrID, sndrPort, sndrHBeat, currentTime);
                rcvrNode->memberList.push_back(newMLE);
                
                free(rsp);
            }
                break;
                
            case(JOINREP):
            {
                rcvrNode->inGroup = true;
                
                //Sender heartbeat
                long sndrHBeat;
                memcpy(&sndrHBeat, (char*)(theMsg)+sizeof(MessageHdr)+sizeof(Address), sizeof(long));
                
                MemberListEntry selfMLE(rcvrID, rcvrPort, rcvrNode->heartbeat, currentTime);
                MemberListEntry intrMLE(sndrID, rcvrPort, sndrHBeat, currentTime);
                
                rcvrNode->memberList.push_back(selfMLE);
                rcvrNode->memberList.push_back(intrMLE);
                
                #ifdef DEBUGLOG
                log->logNodeAdd(&rcvrNode->addr, &rcvrNode->addr);
                log->logNodeAdd(&rcvrNode->addr, &sender);
                #endif
            }
                break;
                
            case(HEARTBEAT):
            {
                size_t mlesize = sizeof(int)+sizeof(short)+sizeof(long);
                size_t hdrsize = sizeof(MessageHdr) + sizeof(Address) + sizeof(int);
                
                int nRcvdEnts;
                
                memcpy(&nRcvdEnts, (char*)(theMsg) + sizeof(MessageHdr) + sizeof(Address), sizeof(int));
                
                for (int i = 0; i < nRcvdEnts; i++){
                    int   mlID;
                    short mlPort;
                    long  mlHBeat;
                    
                    memcpy(&mlID,    (char*)(theMsg)+hdrsize+i*mlesize, sizeof(int));
                    memcpy(&mlPort,  (char*)(theMsg)+hdrsize+sizeof(int)+i*mlesize, sizeof(short));
                    memcpy(&mlHBeat, (char*)(theMsg)+hdrsize+sizeof(int)+sizeof(short)+i*mlesize, sizeof(long));
                    
                    //don't want to hear about myself
                    if (mlID == rcvrID && mlPort == rcvrPort)
                        continue;
                    
                    bool entryExists = false;
                    
                    for (auto& MLE : rcvrNode->memberList){
                        
                        if (mlID == MLE.id && mlPort == MLE.port){
                            
                            entryExists = true;
                            
                            if (mlHBeat > MLE.heartbeat){
                                MLE.heartbeat = mlHBeat;
                                MLE.timestamp = currentTime;
                            }
                        }
                    }
                    
                    // new member to add to list
                    if (!entryExists){
                        MemberListEntry newMLE(mlID, mlPort, mlHBeat, currentTime);
                        Address newAddr = getNodeAddress(mlID, mlPort);
                        
                        rcvrNode->memberList.push_back(newMLE);
                        #ifdef DEBUGLOG
                        log->logNodeAdd(&rcvrNode->addr, &newAddr);
                        #endif
                    }
                }
            }
                break;
                
            default:
                return false;
            
        }
    }
    return true;
}

/**
 * FUNCTION NAME: nodeLoopOps
 *
 * DESCRIPTION: Check if any node hasn't responded within a timeout period and then delete
 * 				the nodes
 * 				Propagate your membership list
 */
void MP1Node::nodeLoopOps() {

	/*
	 * Your code goes here
	 */
    
    long currentTime = (long)par->getcurrtime();
    
    //prevent flooding message inboxes
    if (currentTime % TGOSSIP != 0)
        return;
    
    //Heartbeat
    memberNode->heartbeat += 1;
    memberNode->memberList[0].heartbeat = memberNode->heartbeat;
    memberNode->memberList[0].timestamp = currentTime;
    
    
    // find number alive and remove failed
    
    int numberAlive = 0;
    int mlecount = 0;
    int expMlecount = memberNode->memberList.size();
    for (auto MLE = memberNode->memberList.begin(); MLE < memberNode->memberList.end(); MLE++){
        
        if (currentTime - MLE->gettimestamp() < TFAIL){
            numberAlive++;
        }
        
        // remove failed members
        if ((long)par->getcurrtime() - MLE->gettimestamp() >= TREMOVE){
            Address removed = getNodeAddress(MLE->getid(), MLE->getport());
            
            #ifdef DEBUGLOG       
            log->logNodeRemove(&memberNode->addr, &removed);
            #endif
    
            memberNode->memberList.erase(MLE);
            MLE--;
        }
        mlecount++;
    }
    
    assert(mlecount == expMlecount);
               
    
    //construct hbeat message
    size_t mlesize = sizeof(int)+sizeof(short)+sizeof(long);
    size_t hdrsize = sizeof(MessageHdr) + sizeof(Address) + sizeof(int);
    
    size_t hbsize = hdrsize + numberAlive * mlesize;
    
    MessageHdr *hbeat;
    hbeat = (MessageHdr*) malloc(hbsize);
    
    hbeat->msgType = HEARTBEAT;
    memcpy((char*)(hbeat)+sizeof(MessageHdr), &memberNode->addr.addr, sizeof(Address));
    memcpy((char*)(hbeat)+sizeof(MessageHdr)+sizeof(Address), &numberAlive, sizeof(int));
    
    int eCount = 0;
    
    for (auto& MLE : memberNode->memberList){
        
        //skip failed nodes
        if ( currentTime - MLE.gettimestamp() > TFAIL){
            continue;
        }
        
        memcpy((char*)(hbeat)+hdrsize+eCount*mlesize, 
               &MLE.id,
               sizeof(int));
        
        memcpy((char*)(hbeat)+hdrsize+sizeof(int)+eCount*mlesize,
               &MLE.port,
               sizeof(short));
        
        memcpy((char*)(hbeat)+hdrsize+sizeof(int)+sizeof(short)+eCount*mlesize,
               &MLE.heartbeat,
               sizeof(long));
               
        eCount++;
    }
    
    
    assert(eCount == numberAlive);
    
    
    //determine who to send it to...
    
    int checksize = memberNode->memberList.size();
    
    vector<MemberListEntry> gossipTargets;
    vector<MemberListEntry> candidates = memberNode->memberList;
    
    // choose 3 random targets to gossip to.
    if (memberNode->memberList.size() < CONTACTS+1){
        for (int i = 1; i < memberNode->memberList.size(); i++){
            gossipTargets.push_back(memberNode->memberList[i]);
        }
    } else{
        while ( gossipTargets.size() < CONTACTS){
            int pos = rand() % (candidates.size() - 1) + 1;
            gossipTargets.push_back(candidates[pos]);
            candidates.erase(candidates.begin()+pos);
        }
    }

    assert(checksize == memberNode->memberList.size());
    assert(gossipTargets.size() <= CONTACTS);
    
    //send the hbeat to gossip targets
    for (auto& gt : gossipTargets){
        Address tAddr;
        
        memcpy(&tAddr.addr[0], &gt.id, sizeof(int));
        memcpy(&tAddr.addr[4], &gt.port, sizeof(short));
        
        emulNet->ENsend(&memberNode->addr, &tAddr, (char*)hbeat, hbsize);
    }
    
    free(hbeat);
}

/**
 * FUNCTION NAME: isNullAddress
 *
 * DESCRIPTION: Function checks if the address is NULL
 */
int MP1Node::isNullAddress(Address *addr) {
	return (memcmp(addr->addr, NULLADDR, 6) == 0 ? 1 : 0);
}

/**
 * FUNCTION NAME: getJoinAddress
 *
 * DESCRIPTION: Returns the Address of the coordinator
 */
Address MP1Node::getJoinAddress() {
    Address joinaddr;

    memset(&joinaddr, 0, sizeof(Address));
    *(int *)(&joinaddr.addr) = 1;
    *(short *)(&joinaddr.addr[4]) = 0;

    return joinaddr;
}

Address MP1Node::getNodeAddress(int id, short port) {
    Address addr;

    memset( &addr, 0, sizeof( Address ) );
    *(int*) ( &addr.addr )      = id;
    *(short*) ( &addr.addr[4] ) = port;//CHANGE back to 0
    return addr;
}

/**
 * FUNCTION NAME: initMemberListTable
 *
 * DESCRIPTION: Initialize the membership list
 */
void MP1Node::initMemberListTable(Member *memberNode) {
	memberNode->memberList.clear();
}

/**
 * FUNCTION NAME: printAddress
 *
 * DESCRIPTION: Print the Address
 */
void MP1Node::printAddress(Address *addr)
{
    printf("%d.%d.%d.%d:%d \n",  addr->addr[0],addr->addr[1],addr->addr[2],
                                                       addr->addr[3], *(short*)&addr->addr[4]) ;    
}
