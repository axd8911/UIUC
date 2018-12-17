cmake_minimum_required(VERSION 3.12)
project(mp2)

set(CMAKE_CXX_STANDARD 14)

include_directories(.
                    unitTests)
add_library(MP2Static STATIC
        common.h
        EmulNet.cpp
        EmulNet.h
        Entry.cpp
        Entry.h
        HashTable.cpp
        HashTable.h
        Log.cpp
        Log.h
        Member.cpp
        Member.h
        Message.cpp
        Message.h
        MP1Node.cpp
        MP1Node.h
        MP2Node.cpp
        MP2Node.h
        Node.cpp
        Node.h
        Params.cpp
        Params.h
        Queue.h
        stdincludes.h
        Trace.cpp
        Trace.h)

add_executable(mp2
        Application.cpp
        Application.h
        common.h
        EmulNet.cpp
        EmulNet.h
        Entry.cpp
        Entry.h
        HashTable.cpp
        HashTable.h
        Log.cpp
        Log.h
        Member.cpp
        Member.h
        Message.cpp
        Message.h
        MP1Node.cpp
        MP1Node.h
        MP2Node.cpp
        MP2Node.h
        Node.cpp
        Node.h
        Params.cpp
        Params.h
        Queue.h
        stdincludes.h
        Trace.cpp
        Trace.h)
add_executable(scratch /home/nathan/.CLion2018.2/config/scratches/scratch.cpp)

target_link_libraries(
        scratch
        /home/nathan/Dropbox/School/UIUC/CS425/week9/mp2/EmulNet.o
        /home/nathan/Dropbox/School/UIUC/CS425/week9/mp2/Entry.o
        /home/nathan/Dropbox/School/UIUC/CS425/week9/mp2/HashTable.o
        /home/nathan/Dropbox/School/UIUC/CS425/week9/mp2/Log.o
        /home/nathan/Dropbox/School/UIUC/CS425/week9/mp2/Member.o
        /home/nathan/Dropbox/School/UIUC/CS425/week9/mp2/Message.o
        /home/nathan/Dropbox/School/UIUC/CS425/week9/mp2/MP1Node.o
        /home/nathan/Dropbox/School/UIUC/CS425/week9/mp2/MP2Node.o
        /home/nathan/Dropbox/School/UIUC/CS425/week9/mp2/Node.o
        /home/nathan/Dropbox/School/UIUC/CS425/week9/mp2/Params.o
        /home/nathan/Dropbox/School/UIUC/CS425/week9/mp2/Trace.o
)

enable_testing()
add_subdirectory(unitTests)