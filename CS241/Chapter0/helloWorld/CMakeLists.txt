cmake_minimum_required(VERSION 3.10)

project(helloworld LANGUAGES C)

add_executable(helloworld main.c)

install(TARGETS helloworld RUNTIME DESTINATION bin)
