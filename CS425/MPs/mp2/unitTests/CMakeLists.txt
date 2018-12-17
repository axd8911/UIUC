cmake_minimum_required(VERSION 3.12)

include_directories(${gtest_SOURCE_DIR}/include ${gtest_SOURCE_DIR})

add_executable(MP2NodeTests
        MP2NodeTest.cpp)

target_link_libraries(MP2NodeTests gtest gtest_main )
target_link_libraries(MP2NodeTests MP2Static)