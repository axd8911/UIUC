/**********************************
 * FILE NAME: MP2Node.cpp
 *
 * DESCRIPTION: MP2Node class definition
 **********************************/
#include "MP2Node.h"

vector<string> MP2Node::myLog;
int MP2Node::msgCount = 0;

/**
 * constructor
 */
MP2Node::MP2Node(Member *memberNode,
    Params *             par,
    EmulNet *            emulNet,
    Log *                log,
    Address *            address) {
    this->memberNode       = memberNode;
    this->par              = par;
    this->emulNet          = emulNet;
    this->log              = log;
    ht                     = new HashTable();
    this->memberNode->addr = *address;
}

/**
 * Destructor
 */
MP2Node::~MP2Node() {
    delete ht;
    delete memberNode;
}

/**
 * FUNCTION NAME: updateRing
 *
 * DESCRIPTION: This function does the following:
 * 				1) Gets the current membership list from the Membership
 * Protocol (MP1Node) The membership list is returned as a vector of Nodes. See
 * Node class in Node.h 2) Constructs the ring based on the membership list 3)
 * Calls the Stabilization Protocol
 */
void MP2Node::updateRing() {
    /*
     * Implement this. Parts of it are already implemented
     */
    vector<Node> curMemList;
    bool         change = false;

    /*
     *  Step 1. Get the current membership list from Membership Protocol / MP1
     */
    curMemList = getMembershipList();

    /*
     * Step 2: Construct the ring
     */
    // Sort the list based on the hashCode
    sort(curMemList.begin(), curMemList.end());


    /*
     * Step 3: Run the stabilization protocol IF REQUIRED
     */
    // Run stabilization protocol if the hash table size is greater than zero
    // and if there has been a change in the ring

    if (isRingChanged(curMemList)){
        ring = curMemList;
        if (ht->currentSize() > 0){
            stabilizationProtocol();
        }
    }
}

//returns true if replica updates are needed
//        false otherwise
vector<Node> MP2Node::updatedHaveReplicasOf() {
    int myPos;
    vector<Node> result;

    for (int i = 0; i < ring.size(); i++){
        if (*ring[i].getAddress() == memberNode->addr){
            myPos = i;
        }
    }



    result.push_back(ring[modulo(myPos-1, ring.size())]);
    result.push_back(ring[modulo(myPos-2, ring.size())]);

    return result;
}

vector<Node> MP2Node::updatedHasMyReplicas() {
    int myPos;
    vector<Node> result;

    for (int i = 0; i < ring.size(); i++){
        if (*ring[i].getAddress() == memberNode->addr){
            myPos = i;
        }
    }



    result.push_back(ring[modulo(myPos+1, ring.size())]);
    result.push_back(ring[modulo(myPos+2, ring.size())]);

    return result;
}

bool MP2Node::needReplicaUpdate(ReplicaType repType, vector<Node> updatedHaveReplicasOf, vector<Node> updatedHasMyReplicas) {
    bool result = false;

    switch (repType){
        case PRIMARY:
        {
            if(!(*updatedHasMyReplicas[0].getAddress() == *hasMyReplicas[0].getAddress() ||
               *updatedHasMyReplicas[1].getAddress() == *hasMyReplicas[1].getAddress())) {
                result = true;
            }
            break;
        }
        case SECONDARY:
        {
            if(!(*updatedHasMyReplicas[0].getAddress() == *hasMyReplicas[0].getAddress() ||
               *updatedHaveReplicasOf[1].getAddress() == *haveReplicasOf[1].getAddress())) {
                result = true;
            }
            break;
        }
        case TERTIARY:
        {
            if(!(*updatedHaveReplicasOf[0].getAddress() == *haveReplicasOf[0].getAddress() ||
               *updatedHaveReplicasOf[1].getAddress() == *haveReplicasOf[1].getAddress())) {
                result = true;
            }
            break;
        }
    }
    return result;
}

int MP2Node::modulo(int a, int b){
    int result;
    if (a < 0 && b > 0)
        result = (a%b)+b;
    if (a >= 0)
        result = a%b;
    if (a < 0 && b < 0)
        result = a%b;
    if (a > 0 && b < 0)
        result = (a%b)+b;
    return result;
}

bool MP2Node::isRingChanged(vector<Node> &curMemList) {
    // if new list and previous list are not the same length, return true.
    if (curMemList.size() != ring.size()) { return true; }

    for (int i = 0; i < ring.size(); i++) {

        // if lists' nodes' addresses don't match in order, then return true.
        if (!(*ring[i].getAddress() == *curMemList[i].getAddress())) {
            return true;
        }
    }

    // both lists' have the same nodes in same order, nothing has changed.
    return false;
}

/**
 * FUNCTION NAME: getMemberhipList
 *
 * DESCRIPTION: This function goes through the membership list from the
 * Membership protocol/MP1 and i) generates the hash code for each member ii)
 * populates the ring member in MP2Node class It returns a vector of Nodes. Each
 * element in the vector contain the following fields: a) Address of the node b)
 * Hash code obtained by consistent hashing of the Address
 */
vector<Node> MP2Node::getMembershipList() {
    unsigned int i;
    vector<Node> curMemList;
    for (i = 0; i < this->memberNode->memberList.size(); i++) {
        Address addressOfThisMember;
        int     id   = this->memberNode->memberList.at(i).getid();
        short   port = this->memberNode->memberList.at(i).getport();
        memcpy(&addressOfThisMember.addr[0], &id, sizeof(int));
        memcpy(&addressOfThisMember.addr[4], &port, sizeof(short));
        curMemList.emplace_back(Node(addressOfThisMember));
    }
    return curMemList;
}

/**
 * FUNCTION NAME: hashFunction
 *
 * DESCRIPTION: This functions hashes the key and returns the position on the
 * ring HASH FUNCTION USED FOR CONSISTENT HASHING
 *
 * RETURNS:
 * size_t position on the ring
 */
size_t MP2Node::hashFunction(string key) {
    std::hash<string> hashFunc;
    size_t            ret = hashFunc(key);
    return ret % RING_SIZE;
}

/**
 * FUNCTION NAME: clientCreate
 *
 * DESCRIPTION: client side CREATE API
 * 				The function does the following:
 * 				1) Constructs the message
 * 				2) Finds the replicas of this key
 * 				3) Sends a message to the replica
 */
void MP2Node::clientCreate(string key, string value) {
    /*
     * Implement this
     */

    // Message construction:
    //     long _transID,
    //     Address _fromAddr,
    //     MessageType _type,
    //     string _key,
    //     string _value,
    //     ReplicaType _replica

    vector<Node> replicaNodes = findNodes(key);
    MessageType msgt = CREATE;
    ReplicaType rept;
    int transID = getTransID();
    transIDSentTo[transID] = replicaNodes;

    for (int i = 0; i < replicaNodes.size(); i++){

        // determine replica type
        switch (i){
            case 0:
            {
                rept = PRIMARY;
                break;
            }
            case 1:
            {
                rept = SECONDARY;
                break;
            }
            case 2:
            {
                rept = TERTIARY;
                break;
            }
            default:
            {
                assert(false); //should never come here!
            }
        }

        // Construct the message
        Message msg(transID,
                    memberNode->addr,
                    msgt,
                    key,
                    value,
                    rept);

        //log sent message
        transIDSentMessages[transID].push_back(msg);

        // Send the message to the replica
        emulNet->ENsend(&memberNode->addr,
                        replicaNodes[i].getAddress(),
                        msg.toString());


        // debugging info
        myLog.push_back(msg.toString() +
                        "::::" +
                        replicaNodes[i].getAddress()->getAddress());
    }
}

/**
 * FUNCTION NAME: clientRead
 *
 * DESCRIPTION: client side READ API
 * 				The function does the following:
 * 				1) Constructs the message
 * 				2) Finds the replicas of this key
 * 				3) Sends a message to the replica
 */
void MP2Node::clientRead(string key) {
    /*
     * Implement this
     */
    // Message construction:
    //     long _transID,
    //     Address _fromAddr,
    //     MessageType _type,
    //     string _key,
    //     ReplicaType _replica

    vector<Node> replicaNodes = findNodes(key);
    MessageType msgt = READ;
    int transID = getTransID();
    transIDSentTo[transID] = replicaNodes;

    for (int i = 0; i < replicaNodes.size(); i++){

        // Construct the message
        Message msg(transID,
                    memberNode->addr,
                    msgt,
                    key);

        // Send the message to the replica
        emulNet->ENsend(&memberNode->addr,
                        replicaNodes[i].getAddress(),
                        msg.toString());

        //log sent message
        transIDSentMessages[transID].push_back(msg);

        // debugging info
        myLog.push_back(msg.toString() +
                        "::::" +
                        replicaNodes[i].getAddress()->getAddress());
    }
}

/**
 * FUNCTION NAME: clientUpdate
 *
 * DESCRIPTION: client side UPDATE API
 * 				The function does the following:
 * 				1) Constructs the message
 * 				2) Finds the replicas of this key
 * 				3) Sends a message to the replica
 */
void MP2Node::clientUpdate(string key, string value) {
    /*
     * Implement this
     */
// Message construction:
    //     long _transID,
    //     Address _fromAddr,
    //     MessageType _type,
    //     string _key,
    //     string _value,
    //     ReplicaType _replica

    vector<Node> replicaNodes = findNodes(key);
    MessageType msgt = UPDATE;
    ReplicaType rept;
    int transID = getTransID();
    transIDSentTo[transID] = replicaNodes;

    for (int i = 0; i < replicaNodes.size(); i++){

        // determine replica type
        switch (i){
            case 0:
            {
                rept = PRIMARY;
                break;
            }
            case 1:
            {
                rept = SECONDARY;
                break;
            }
            case 2:
            {
                rept = TERTIARY;
                break;
            }
            default:
            {
                assert(false); //should never come here!
            }
        }

        // Construct the message
        Message msg(transID,
                    memberNode->addr,
                    msgt,
                    key,
                    value,
                    rept);

        //log sent message
        transIDSentMessages[transID].push_back(msg);

        // Send the message to the replica
        emulNet->ENsend(&memberNode->addr,
                        replicaNodes[i].getAddress(),
                        msg.toString());


        // debugging info
        myLog.push_back(msg.toString() +
                        "::::" +
                        replicaNodes[i].getAddress()->getAddress());
    }
}

/**
 * FUNCTION NAME: clientDelete
 *
 * DESCRIPTION: client side DELETE API
 * 				The function does the following:
 * 				1) Constructs the message
 * 				2) Finds the replicas of this key
 * 				3) Sends a message to the replica
 */
void MP2Node::clientDelete(string key) {
    /*
     * Implement this
     */
    // Message construction:
    //     long _transID,
    //     Address _fromAddr,
    //     MessageType _type,
    //     string _key,
    //     ReplicaType _replica

    vector<Node> replicaNodes = findNodes(key);
    MessageType msgt = DELETE;
    int transID = getTransID();
    transIDSentTo[transID] = replicaNodes;

    for (int i = 0; i < replicaNodes.size(); i++){

        // Construct the message
        Message msg(transID,
                    memberNode->addr,
                    msgt,
                    key);

        // Send the message to the replica
        emulNet->ENsend(&memberNode->addr,
                        replicaNodes[i].getAddress(),
                        msg.toString());

        //log sent message
        transIDSentMessages[transID].push_back(msg);

        // debugging info
        myLog.push_back(msg.toString() +
                        "::::" +
                        replicaNodes[i].getAddress()->getAddress());
    }
}

/**
 * FUNCTION NAME: createKeyValue
 *
 * DESCRIPTION: Server side CREATE API
 * 			   	The function does the following:
 * 			   	1) Inserts key value into the local hash table
 * 			   	2) Return true or false based on success or
 * failure
 */
bool MP2Node::createKeyValue(string key, string value, ReplicaType replica) {
    /*
     * Implement this
     */
    // Insert key, value, replicaType into the hash table

    // create entry to insert into hashtable
    Entry e(value, par->getcurrtime(), replica);

    // insert entry into hashtable
    bool result = ht->create(key, e.convertToString());

    // debugging
    myLog.push_back("CreateKeyValue:  " + key + " " + e.convertToString() + " Success? " + (result ? "true" : "false"));


    return result;

}

/**
 * FUNCTION NAME: readKey
 *
 * DESCRIPTION: Server side READ API
 * 			    This function does the following:
 * 			    1) Read key from local hash table
 * 			    2) Return value
 */
string MP2Node::readKey(string key) {
    /*
     * Implement this
     */
    // Read key from local hash table and return value

    return ht->read(key);
}

/**
 * FUNCTION NAME: updateKeyValue
 *
 * DESCRIPTION: Server side UPDATE API
 * 				This function does the following:
 * 				1) Update the key to the new value in the local hash
 * table 2) Return true or false based on success or failure
 */
bool MP2Node::updateKeyValue(string key, string value, ReplicaType replica) {
    /*
     * Implement this
     */
    // Update key in local hash table and return true or false

    // create entry to insert into hashtable
    Entry e(value, par->getcurrtime(), replica);

    // insert entry into hashtable
    bool result = ht->update(key, e.convertToString());

    // debugging
    myLog.push_back("UpdateKeyValue:  " + key + " " + e.convertToString() + " Success? " + (result ? "true" : "false"));


    return result;
}

/**
 * FUNCTION NAME: deleteKey
 *
 * DESCRIPTION: Server side DELETE API
 * 				This function does the following:
 * 				1) Delete the key from the local hash table
 * 				2) Return true or false based on success or
 * failure
 */
bool MP2Node::deletekey(string key) {
    /*
     * Implement this
     */
    // Delete the key from the local hash table

    return ht->deleteKey(key);
}

/**
 * FUNCTION NAME: checkMessages
 *
 * DESCRIPTION: This function is the message handler of this node.
 * 				This function does the following:
 * 				1) Pops messages from the queue
 * 				2) Handles the messages according to message
 * types
 */
void MP2Node::checkMessages() {
    /*
     * Implement this. Parts of it are already implemented
     */
    char *data;
    int   size;

    /*
     * Declare your local variables here
     */
    bool success;

    checkSentMessageStatuses();


    // dequeue all messages and handle them
    while (!memberNode->mp2q.empty()) {
        /*
         * Pop a message from the queue
         */
        data = (char *) memberNode->mp2q.front().elt;
        size = memberNode->mp2q.front().size;
        memberNode->mp2q.pop();
        string mostVotedValue = "";

        string message(data, data + size);

        Message msgObj(message);
        //message format:
        //      transID::fromAddr::CREATE::key::value::ReplicaType
        //      transID::fromAddr::READ::key
        //      transID::fromAddr::UPDATE::key::value::ReplicaType
        //      transID::fromAddr::DELETE::key
        //      transID::fromAddr::REPLY::sucess
        //      transID::fromAddr::READREPLY::value

        switch(msgObj.type){
            case CREATE:
            {
                // if transID is -1, then it is a unicasted Replica Update message, not a create message.
                if (msgObj.transID == -1){
                    deletekey(msgObj.key);

                    createKeyValue(msgObj.key, msgObj.value, msgObj.replica);
                    break;
                }
                // attempt create
                success = createKeyValue(msgObj.key,
                                         msgObj.value,
                                         msgObj.replica);

                // construct reply message
                Message reply(msgObj.transID,
                              memberNode->addr,
                              REPLY,
                              success);

                // send reply
                emulNet->ENsend(&memberNode->addr,
                                &msgObj.fromAddr,
                                reply.toString());

                // log success/failure
                success ? log->logCreateSuccess(&memberNode->addr, false, msgObj.transID, msgObj.key, msgObj.value)
                        : log->logCreateFail   (&memberNode->addr, false, msgObj.transID, msgObj.key, msgObj.value);

                break;
            }
            case READ:
            {
                // attempt to read value
                string entryValue = readKey(msgObj.key);

                // create readreply message
                Message reply(msgObj.transID,
                              memberNode->addr,
                              entryValue);

                // Send readreply message
                emulNet->ENsend(&memberNode->addr,
                                &msgObj.fromAddr,
                                reply.toString());

                //log read success/failure
                if (!entryValue.empty()){
                    Entry e(entryValue);
                    log->logReadSuccess(&memberNode->addr, false, msgObj.transID, msgObj.key, e.value);
                } else{
                    log->logReadFail(&memberNode->addr, false, msgObj.transID, msgObj.key);
                }

                break;
            }
            case UPDATE:
            {
                // attempt update
                success = updateKeyValue(msgObj.key,
                                         msgObj.value,
                                         msgObj.replica);

                // construct reply message
                Message reply(msgObj.transID,
                              memberNode->addr,
                              REPLY,
                              success);

                // send reply
                emulNet->ENsend(&memberNode->addr,
                                &msgObj.fromAddr,
                                reply.toString());

                // log success/failure
                success ? log->logUpdateSuccess(&memberNode->addr, false, msgObj.transID, msgObj.key, msgObj.value)
                        : log->logUpdateFail   (&memberNode->addr, false, msgObj.transID, msgObj.key, msgObj.value);

                break;
            }
            case DELETE:
            {
                // attempt update
                success = deletekey(msgObj.key);

                // construct reply message
                Message reply(msgObj.transID,
                              memberNode->addr,
                              REPLY,
                              success);

                // send reply
                emulNet->ENsend(&memberNode->addr,
                                &msgObj.fromAddr,
                                reply.toString());

                // log success/failure
                success ? log->logDeleteSuccess(&memberNode->addr, false, msgObj.transID, msgObj.key)
                        : log->logDeleteFail   (&memberNode->addr, false, msgObj.transID, msgObj.key);

                break;
            }
            case READREPLY:
            {
                transIDVotes[msgObj.transID].push_back(msgObj);

                if (transIDVotes[msgObj.transID].size() >= QUORUMSIZE / 2 + 1) {
                    mostVotedValue = mostFrequent(transIDVotes[msgObj.transID]);


                    if (mostVotedValue != "") {
                        log->logReadSuccess(&memberNode->addr, true, msgObj.transID,
                                            transIDSentMessages[msgObj.transID][0].key, mostVotedValue);
                    } else {
                        log->logReadFail(&memberNode->addr, true, msgObj.transID,
                                         transIDSentMessages[msgObj.transID][0].key);
                    }

                    transIDSentMessages.erase(msgObj.transID);
                    transIDVotes.erase(msgObj.transID);
                }

                break;
            }
            case REPLY:
            {

                transIDVotes[msgObj.transID].push_back(msgObj);

                if (transIDVotes[msgObj.transID].size() >= QUORUMSIZE / 2 + 1) {

                    MessageType grpType = transIDSentMessages[msgObj.transID][0].type;
                    vector<Message> votes = transIDVotes[msgObj.transID];
                    string key = transIDSentMessages[msgObj.transID][0].key;
                    string value = transIDSentMessages[msgObj.transID][0].value;

                    switch(grpType){
                        case CREATE:
                        {
                            if (allSuccess(votes)){
                                log->logCreateSuccess(&memberNode->addr, true, msgObj.transID, key, value);
                            } else{
                                log->logCreateFail(&memberNode->addr, true, msgObj.transID, key, value);
                            }

                            break;
                        }
                        case UPDATE:
                        {
                            if (allSuccess(votes)){
                                log->logUpdateSuccess(&memberNode->addr, true, msgObj.transID, key, value);
                            } else{
                                log->logUpdateFail(&memberNode->addr, true, msgObj.transID, key, value);
                            }
                            break;
                        }
                        case DELETE:
                        {
                            if (allSuccess(votes)){
                                log->logDeleteSuccess(&memberNode->addr, true, msgObj.transID, key);
                            } else{
                                log->logDeleteFail(&memberNode->addr, true, msgObj.transID, key);
                            }
                            break;
                        }
                        default:
                            assert(false); //should never come here!
                    }
                    transIDSentMessages.erase(msgObj.transID);
                    transIDVotes.erase(msgObj.transID);
                }
                break;
            }
            default:
                assert(false);// should never come here!
        } // end switch


        /*
         * Handle the message types here
         */
    }

    /*
     * This function should also ensure all READ and UPDATE operation
     * get QUORUM replies
     */
}

string MP2Node::mostFrequent(vector<Message> votes) {
    map<string, int> voteCount;

    for (auto& v : votes){
        if (v.value != "") {
            Entry e(v.value);
            voteCount[e.value]++;
        } else{
            voteCount[""]++;
        }
    }

    for (auto& p : voteCount){
        if (p.second >= QUORUMSIZE / 2 + 1){
            return p.first;
        }
    }

    return "";
}

bool MP2Node::allSuccess(vector<Message> votes){
    for (auto& v : votes){
        if (!v.success)
            return false;
    }
    return true;
}

void MP2Node::checkSentMessageStatuses() {
    vector<Node> currReps;
    MessageType msgType;
    for ( auto& msgGrp : transIDSentMessages){
        currReps = findNodes(msgGrp.second[0].key);
        // stored set of replicas not being the same as current computed set of replicas indicates
        // a sent message was failed.
        if (isFailedMessage(transIDSentTo[msgGrp.first], currReps)){
            msgType = msgGrp.second[0].type;
            switch(msgType){
                case READ:
                {

                    log->logReadFail(&memberNode->addr, true, msgGrp.second[0].transID,
                                     transIDSentMessages[msgGrp.second[0].transID][0].key);
                    transIDSentMessages.erase(msgGrp.second[0].transID);
                    transIDVotes.erase(msgGrp.second[0].transID);
                    break;
                }
                case UPDATE:
                {

                    log->logUpdateFail(&memberNode->addr, true, msgGrp.second[0].transID,
                                     transIDSentMessages[msgGrp.second[0].transID][0].key, transIDSentMessages[msgGrp.second[0].transID][0].value);
                    transIDSentMessages.erase(msgGrp.second[0].transID);
                    transIDVotes.erase(msgGrp.second[0].transID);

                    break;
                }
            }
        }
    }
}

bool MP2Node::isFailedMessage(vector<Node> a, vector<Node> b){
    int scount = 0;

    for (int i = 0; i < a.size() ; i++){
        if (*a[i].getAddress() == *b[i].getAddress()){
            scount++;
        }
    }

    return (scount <= QUORUMSIZE / 2 + 1);
}

bool MP2Node::majoritySuccess(vector<Message> votes){
    int scount = 0;
    for (auto& v : votes){
        if (v.success)
            scount++;
    }
    return (scount == QUORUMSIZE / 2 + 1);
}

bool MP2Node::isMsgTimedOut() {
    return false;
}

/**
 * FUNCTION NAME: findNodes
 *
 * DESCRIPTION: Find the replicas of the given keyfunction
 * 				This function is responsible for finding the replicas of
 * a key
 */
vector<Node> MP2Node::findNodes(string key) {
    size_t       pos = hashFunction(key);
    vector<Node> addr_vec;
    if (ring.size() >= 3) {
        // if pos <= min || pos > max, the leader is the min
        if (pos <= ring.at(0).getHashCode()
            || pos > ring.at(ring.size() - 1).getHashCode()) {
            addr_vec.emplace_back(ring.at(0));
            addr_vec.emplace_back(ring.at(1));
            addr_vec.emplace_back(ring.at(2));
        } else {
            // go through the ring until pos <= node
            for (int i = 1; i < ring.size(); i++) {
                Node addr = ring.at(i);
                if (pos <= addr.getHashCode()) {
                    addr_vec.emplace_back(addr);
                    addr_vec.emplace_back(ring.at((i + 1) % ring.size()));
                    addr_vec.emplace_back(ring.at((i + 2) % ring.size()));
                    break;
                }
            }
        }
    }
    return addr_vec;
}

/**
 * FUNCTION NAME: recvLoop
 *
 * DESCRIPTION: Receive messages from EmulNet and push into the queue (mp2q)
 */
bool MP2Node::recvLoop() {
    if (memberNode->bFailed) {
        return false;
    } else {
        return emulNet->ENrecv(&(memberNode->addr),
            this->enqueueWrapper,
            NULL,
            1,
            &(memberNode->mp2q));
    }
}

/**
 * FUNCTION NAME: enqueueWrapper
 *
 * DESCRIPTION: Enqueue the message from Emulnet into the queue of MP2Node
 */
int MP2Node::enqueueWrapper(void *env, char *buff, int size) {
    Queue q;
    return q.enqueue((queue<q_elt> *) env, (void *) buff, size);
}
/**
 * FUNCTION NAME: stabilizationProtocol
 *
 * DESCRIPTION: This runs the stabilization protocol in case of Node joins and
 *leaves It ensures that there always 3 copies of all keys in the DHT at all
 *times The function does the following: 1) Ensures that there are three
 *"CORRECT" replicas of all the keys in spite of failures and joins Note:-
 *"CORRECT" replicas implies that every key is replicated in its two neighboring
 *nodes in the ring
 */
void MP2Node::stabilizationProtocol() {
    /*
     * Implement this
     */
    // assume ring has been updated,
    // assume haveReplicasOf and hasMyReplicas are old


    Address myAddr = memberNode->addr;
    int myReplicaPos;

    vector<Node> newHaveReplicasOf = updatedHaveReplicasOf();
    vector<Node> newHasMyReplicas = updatedHasMyReplicas();
    vector<Node> replicas;

    if (!hasMyReplicas.empty() && ! haveReplicasOf.empty()) {

        Address newSucc1 = *newHasMyReplicas[0].getAddress();
        Address newSucc2 = *newHasMyReplicas[1].getAddress();

        for (auto &node : ring) {
            for (auto &p : ht->hashTable) {

                string key = p.first;
                string entryValue = p.second;


                Entry e(entryValue);
                replicas = findNodes(key);

                for (int i = 0; i < replicas.size(); i++) {
                    if (*replicas[i].getAddress() == memberNode->addr) {
                        myReplicaPos = i;
                    }
                }

                ReplicaType newRept = (ReplicaType) myReplicaPos;

                // update replicas
                if (newRept == PRIMARY) {
                    //send updateorcreate to self, n1, n2
                    Message msg1(-1,
                                 myAddr,
                                 CREATE,
                                 key,
                                 e.value,
                                 PRIMARY);

                    Message msg2(-1,
                                 myAddr,
                                 CREATE,
                                 key,
                                 e.value,
                                 SECONDARY);

                    Message msg3(-1,
                                 myAddr,
                                 CREATE,
                                 key,
                                 e.value,
                                 TERTIARY);

                    emulNet->ENsend(&myAddr, &myAddr, msg1.toString());
                    emulNet->ENsend(&myAddr, &newSucc1, msg2.toString());
                    emulNet->ENsend(&myAddr, &newSucc2, msg3.toString());
                }
            }
        }
    }

    //update neighbors
    hasMyReplicas = newHasMyReplicas;
    haveReplicasOf = newHaveReplicasOf;
}


bool MP2Node::haveSameNodes(vector<Node> a, vector<Node> b){
    assert(a.size() == b.size());

    for (int i = 0; i < a.size(); i++){
        if (!(*a[i].getAddress() == *b[i].getAddress())){
            return false;
        }
    }
    return true;
}

const vector<Node> &MP2Node::getRing() const {
    return ring;
}

void MP2Node::setRing(const vector<Node> &ring) {
    MP2Node::ring = ring;
}

int MP2Node::concatenateInts(int a, int b) {
    int multiplier = 1;
    while (multiplier <= b){
        multiplier *= 10;
    }

    return a*multiplier+b;
}

int MP2Node::getTransID() {

    return msgCount++;
}

