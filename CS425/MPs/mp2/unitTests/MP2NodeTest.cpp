//
// Created by nathan on 11/26/18.
//

#include "MP2Node.h"

#include "EmulNet.h"
#include "Log.h"
#include "MP2Node.h"
#include "Member.h"
#include "Params.h"

#include "gtest/gtest.h"

using namespace std;

namespace {

class MP2NodeTest : public testing::Test {
  protected:
    virtual void SetUp() {
        member  = new Member();
        par     = new Params();
        emulNet = new EmulNet(par);
        log     = new Log(par);
        address = new Address();
        n       = new MP2Node(member, par, emulNet, log, address);
    }


    Member * member;
    Params * par;
    EmulNet *emulNet;
    Log *    log;
    Address *address;
    MP2Node *n;
};

TEST_F(MP2NodeTest, isRingChangedEmptyRings) {
    vector<Node> curMemList;

    EXPECT_EQ(n->getRing().size(), 0);
    EXPECT_EQ(curMemList.size(), 0);
    EXPECT_FALSE(n->isRingChanged(curMemList));
}

TEST_F(MP2NodeTest, isRingChangedDifferentSizes) {
    vector<Node> curMemList;
    curMemList.push_back(*(new Node()));

    EXPECT_EQ(curMemList.size(), 1);
    EXPECT_TRUE(n->isRingChanged(curMemList));
}

TEST_F(MP2NodeTest, isRingChangedSameSizeDifferentOrder) {
    vector<Node> curMemList;
    vector<Node> prevMemList;

    Node n1 = *(new Node());
    Node n2 = *(new Node());

    curMemList.push_back(n1);
    curMemList.push_back(n2);

    prevMemList.push_back(n2);
    prevMemList.push_back(n1);

    n->setRing(prevMemList);


    EXPECT_EQ(curMemList.size(), 2);
    EXPECT_EQ(n->getRing().size(), 2);
    EXPECT_TRUE(n->isRingChanged(curMemList));
}

TEST_F(MP2NodeTest, isRingChangedSameSizeSameOrder) {
    vector<Node> curMemList;
    vector<Node> prevMemList;

    Node n1 = *(new Node(*(new Address("1:0"))));
    Node n2 = *(new Node(*(new Address("2:0"))));

    curMemList.push_back(n1);
    curMemList.push_back(n2);

    prevMemList.push_back(n1);
    prevMemList.push_back(n2);

    n->setRing(prevMemList);


    EXPECT_EQ(curMemList.size(), 2);
    EXPECT_EQ(n->getRing().size(), 2);
    EXPECT_FALSE(n->isRingChanged(curMemList));
}

TEST_F(MP2NodeTest, concatenateInts) {
    int a = 123;
    int b = 456;

    int expected = 123456;
    EXPECT_EQ(expected, n->concatenateInts(a, b));
}

TEST_F(MP2NodeTest, getTransID) {
    int a = n->getTransID();
    int b = n->getTransID();

    EXPECT_NE(a, b);
}

    TEST_F(MP2NodeTest, negativeAModulo) {
        int a = -1;
        int b = 10;


        EXPECT_EQ(9, n->modulo(a, b));
    }

    TEST_F(MP2NodeTest, positiveModulo) {
        int a = 1;
        int b = 10;

        EXPECT_EQ(1, n->modulo(a, b));
    }

    TEST_F(MP2NodeTest, zeroModulo) {
        int a = 0;
        int b = 10;

        EXPECT_EQ(0, n->modulo(a, b));
    }

    TEST_F(MP2NodeTest, negativeModulo) {
        int a = -1;
        int b = -10;

        EXPECT_EQ(-1, n->modulo(a, b));
    }

    TEST_F(MP2NodeTest, negativeBModulo) {
        int a = 1;
        int b = -10;

        EXPECT_EQ(-9, n->modulo(a, b));
    }



class clientServerActions : public testing::Test {
  protected:
    virtual void SetUp() {
        member  = new Member();
        par     = new Params();
        emulNet = new EmulNet(par);
        log     = new Log(par);
        address = new Address();
        n       = new MP2Node(member, par, emulNet, log, address);
        curMemList.push_back(n1);
        curMemList.push_back(n2);
        curMemList.push_back(n3);
        curMemList.push_back(n4);

        n->setRing(curMemList);
    }


    virtual void TearDown(){
        n->myLog.clear();
    }

    Member * member;
    Params * par;
    EmulNet *emulNet;
    Log *    log;
    Address *address;
    MP2Node *n;

    vector<Node> curMemList;

    Node n1 = *(new Node(*(new Address("1:0"))));
    Node n2 = *(new Node(*(new Address("2:0"))));
    Node n3 = *(new Node(*(new Address("3:0"))));
    Node n4 = *(new Node(*(new Address("4:0"))));
};

TEST_F(clientServerActions, clientCreateLogSend) {

    n->clientCreate("a", "cat");

    EXPECT_EQ(3, MP2Node::myLog.size());

    EXPECT_EQ("::0:0::0::a::cat::0::::1:0", MP2Node::myLog[0].substr(19));
    EXPECT_EQ("::0:0::0::a::cat::1::::2:0", MP2Node::myLog[1].substr(19));
    EXPECT_EQ("::0:0::0::a::cat::2::::3:0", MP2Node::myLog[2].substr(19));
}

TEST_F(clientServerActions, clientReadLogSend) {

    n->clientCreate("a", "cat");
    n->clientRead("a");

    EXPECT_EQ(6, MP2Node::myLog.size());

    EXPECT_EQ("::0:0::1::a::::1:0", MP2Node::myLog[3].substr(19));
    EXPECT_EQ("::0:0::1::a::::2:0", MP2Node::myLog[4].substr(19));
    EXPECT_EQ("::0:0::1::a::::3:0", MP2Node::myLog[5].substr(19));
}

    TEST_F(clientServerActions, clientUpdateLogSend) {

        n->clientCreate("a", "cat");
        n->clientRead("a");
        n->clientUpdate("a", "dog");

        EXPECT_EQ(9, MP2Node::myLog.size());

        EXPECT_EQ("::0:0::2::a::dog::0::::1:0", MP2Node::myLog[6].substr(19));
        EXPECT_EQ("::0:0::2::a::dog::1::::2:0", MP2Node::myLog[7].substr(19));
        EXPECT_EQ("::0:0::2::a::dog::2::::3:0", MP2Node::myLog[8].substr(19));

    }

    TEST_F(clientServerActions, clientDeleteLogSend) {

        n->clientCreate("a", "cat");
        n->clientDelete("a");

        EXPECT_EQ(6, MP2Node::myLog.size());

        EXPECT_EQ("::0:0::3::a::::1:0", MP2Node::myLog[3].substr(19));
        EXPECT_EQ("::0:0::3::a::::2:0", MP2Node::myLog[4].substr(19));
        EXPECT_EQ("::0:0::3::a::::3:0", MP2Node::myLog[5].substr(19));
    }

    TEST_F(clientServerActions, serverCreateKeyValue) {

        n->createKeyValue("a", "dog", PRIMARY);

        EXPECT_EQ("CreateKeyValue:  a dog:0:0 Success? true", MP2Node::myLog[0]);
    }

    TEST_F(clientServerActions, serverUpdateKeyValue) {

        n->createKeyValue("a", "dog", PRIMARY);
        n->updateKeyValue("a", "cat", PRIMARY);

        EXPECT_EQ("UpdateKeyValue:  a cat:0:0 Success? true", MP2Node::myLog[1]);

        n->updateKeyValue("b", "careful", SECONDARY);

        EXPECT_EQ("UpdateKeyValue:  b careful:0:1 Success? false", MP2Node::myLog[2]);
    }


    class testMessages : public testing::Test {
    protected:
        virtual void SetUp() {
            member  = new Member();
            par     = new Params();
            emulNet = new EmulNet(par);
            log     = new Log(par);
            address = new Address();
            n       = new MP2Node(member, par, emulNet, log, address);
            curMemList.push_back(n1);
            curMemList.push_back(n2);
            curMemList.push_back(n3);
            curMemList.push_back(n4);

            n->setRing(curMemList);



        }


        virtual void TearDown(){
            n->myLog.clear();
        }

        Member * member;
        Params * par;
        EmulNet *emulNet;
        Log *    log;
        Address *address;
        MP2Node *n;

        vector<Node> curMemList;

        Node n1 = *(new Node(*(new Address("1:0"))));
        Node n2 = *(new Node(*(new Address("2:0"))));
        Node n3 = *(new Node(*(new Address("3:0"))));
        Node n4 = *(new Node(*(new Address("4:0"))));
    };

    TEST_F(clientServerActions, readValue) {

        bool t = n->createKeyValue("a", "cat", PRIMARY);
        string res = n->readKey("a");


        Message reply(1,
                      n->getMemberNode()->addr,
                      res);

        string send = reply.toString();

        Message rec(send);


        cout << rec.toString() << endl;
        cout << rec.value << endl;

        Entry e(rec.value);
    }
}    // namespace

// TEST(StrCompare, CStrEqual) {
//    EXPECT_STREQ(expectVal, actualValTrue);
//}
//
// TEST(StrCompare, CStrNotEqual) {
//    EXPECT_STRNE(expectVal, actualValFalse);
//}