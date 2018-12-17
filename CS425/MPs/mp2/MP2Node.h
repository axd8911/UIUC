/**********************************
 * FILE NAME: MP2Node.h
 *
 * DESCRIPTION: MP2Node class header file
 **********************************/

#ifndef MP2NODE_H_
#define MP2NODE_H_

/**
 * Header files
 */
#include "stdincludes.h"
#include "EmulNet.h"
#include "Node.h"
#include "HashTable.h"
#include "Log.h"
#include "Params.h"
#include "Message.h"
#include "Queue.h"

#define QUORUMSIZE 3


/**
 * CLASS NAME: MP2Node
 *
 * DESCRIPTION: This class encapsulates all the key-value store functionality
 * 				including:
 * 				1) Ring
 * 				2) Stabilization Protocol
 * 				3) Server side CRUD APIs
 * 				4) Client side CRUD APIs
 */
class MP2Node {
private:
	// Vector holding the next two neighbors in the ring who have my replicas
	vector<Node> hasMyReplicas;
	// Vector holding the previous two neighbors in the ring whose replicas I have
	vector<Node> haveReplicasOf;
	// Ring
	vector<Node> ring;
	// Hash Table
	HashTable * ht;
	// Member representing this member
	Member *memberNode;
	// Params object
	Params *par;
	// Object of EmulNet
	EmulNet * emulNet;
	// Object of Log
	Log * log;
	static int msgCount;

	// transID to vector of Messages.
	map<int, vector<Message>> transIDVotes;

	map<int, vector<Message>> transIDSentMessages;

	map<int, vector<Node>> transIDSentTo;

public:
	static vector<string> myLog;
	MP2Node(Member *memberNode, Params *par, EmulNet *emulNet, Log *log, Address *addressOfMember);
	Member * getMemberNode() {
		return this->memberNode;
	}

	// ring functionalities
	void updateRing();
	bool isRingChanged(vector<Node> &curMemList);
	vector<Node> getMembershipList();
	size_t hashFunction(string key);
	void findNeighbors();
	bool haveSameNodes(vector<Node> a, vector<Node> b);
	vector<Node> updatedHaveReplicasOf();
	vector<Node> updatedHasMyReplicas();
	bool needReplicaUpdate(ReplicaType repType, vector<Node> updatedHaveReplicasOf, vector<Node> updatedHasMyReplicas);

	// client side CRUD APIs
	void clientCreate(string key, string value);
	void clientRead(string key);
	void clientUpdate(string key, string value);
	void clientDelete(string key);

	// receive messages from Emulnet
	bool recvLoop();
	static int enqueueWrapper(void *env, char *buff, int size);
	bool isMsgTimedOut();
	string mostFrequent(vector<Message> votes);
	bool allSuccess(vector<Message> votes);
	bool majoritySuccess(vector<Message> votes);
	bool isFailedMessage(vector<Node> a, vector<Node> b);

	// handle messages from receiving queue
	void checkMessages();
	void checkSentMessageStatuses();

	// coordinator dispatches messages to corresponding nodes
	void dispatchMessages(Message message);

	// find the addresses of nodes that are responsible for a key
	vector<Node> findNodes(string key);

	// server
	bool createKeyValue(string key, string value, ReplicaType replica);
	string readKey(string key);
	bool updateKeyValue(string key, string value, ReplicaType replica);
	bool deletekey(string key);


	// stabilization protocol - handle multiple failures
	void stabilizationProtocol();

	~MP2Node();

    const vector<Node> &getRing() const;

    void setRing(const vector<Node> &ring);

    int concatenateInts(int a, int b);
	int getTransID();
	int modulo(int a, int b);
};



#endif /* MP2NODE_H_ */
