cmake_minimum_required (VERSION 3.5)
project(mp1-transpose VERSION 1.0.0 LANGUAGES CXX)


add_library(transpose_solution STATIC "transpose.cpp")
add_executable(transpose_runner "main.cpp" )
target_link_libraries(transpose_runner transpose_solution common)
