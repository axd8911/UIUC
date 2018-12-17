cmake_minimum_required(VERSION 3.10)

project(ctypes LANGUAGES C)

add_executable(ctypes main.c)

install(TARGETS ctypes RUNTIME DESTINATION bin)
