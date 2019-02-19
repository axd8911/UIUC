cmake_minimum_required (VERSION 3.5)
project(mp0-part2 VERSION 1.0.0 LANGUAGES C CXX)

set(PART2_SOURCE_FILES "./part2.cpp" "./part2.h"
"./test1.cpp" "./test2.cpp" "./test3.cpp" "./test4.cpp" "./test5.cpp"
"./timers.cpp" "./timers.h" "./util.cpp")

add_executable(part2_0 ${PART2_SOURCE_FILES})
target_compile_options(part2_0 PUBLIC "-O0")

add_executable(part2_1 ${PART2_SOURCE_FILES})
target_compile_options(part2_1 PUBLIC "-O1")

add_executable(part2_2 ${PART2_SOURCE_FILES})
target_compile_options(part2_2 PUBLIC "-O2")

add_executable(part2_3 ${PART2_SOURCE_FILES})
target_compile_options(part2_3 PUBLIC "-O3")

#phony target to build all of part2
add_custom_target(part2)
add_dependencies(part2 part2_0 part2_1 part2_2 part2_3)
