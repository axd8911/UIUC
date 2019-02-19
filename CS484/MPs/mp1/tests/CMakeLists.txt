cmake_minimum_required (VERSION 3.5)
project(mp1-tests VERSION 1.0.0 LANGUAGES CXX)



if(NOT OPENMP_FOUND)
	message(FATAL_ERROR "This subproject cannot be compiled without OpenMP")
else(NOT OPENMP_FOUND)
	set(CMAKE_C_FLAGS "${CMAKE_C_FLAGS} ${OpenMP_C_FLAGS}")
	set(CMAKE_CXX_FLAGS "${CMAKE_CXX_FLAGS} ${OpenMP_CXX_FLAGS}")
	set(CMAKE_EXE_LINKER_FLAGS "${CMAKE_EXE_LINKER_FLAGS} ${OpenMP_EXE_LINKER_FLAGS}")
endif(NOT OPENMP_FOUND)

include(CTest)
find_package(GTest)#comes with CMake
if(GTEST_FOUND)
	#include(GoogleTest)
else(GTEST_FOUND)
	message(FATAL_ERROR "DANGER: Could not find GoogleTest")
endif(GTEST_FOUND)

#Benchmarking
find_package(GBench)
if(NOT GBENCH_FOUND)
	message(FATAL_ERROR "DANGER: Could not find GoogleBenchmark")
endif(NOT GBENCH_FOUND)


#TESTING
add_executable(run_tests "alltests.cpp" )
target_link_libraries(run_tests naivelinalg common )
target_link_libraries(run_tests multiply_solution transpose_solution openmp_solution )
include_directories(run_tests ${GTEST_INCLUDE_DIRS})
target_link_libraries(run_tests pthread ${GTEST_BOTH_LIBRARIES})
GTEST_ADD_TESTS(run_tests "" AUTO)

#BENCHMARKING
add_executable(run_benchmarks "allbench.cpp")
target_link_libraries(run_benchmarks naivelinalg common )
target_link_libraries(run_benchmarks  multiply_solution transpose_solution openmp_solution )
include_directories(run_benchmarks pthread ${GBENCH_INCLUDE_DIRS})
target_link_libraries(run_benchmarks ${GBENCH_BOTH_LIBRARIES})

#TILE SIZE SEARCH
add_executable(find_tile_size "find_tile_size.cpp")
target_link_libraries(find_tile_size naivelinalg common )
target_link_libraries(find_tile_size  multiply_solution transpose_solution openmp_solution )
include_directories(find_tile_size pthread ${GBENCH_INCLUDE_DIRS})
target_link_libraries(find_tile_size ${GBENCH_BOTH_LIBRARIES})

#CUSTOM BENCHMARKS
add_executable(student_benchmarks "student_benchmarks.cpp")
target_link_libraries(student_benchmarks naivelinalg common )
target_link_libraries(student_benchmarks  multiply_solution transpose_solution openmp_solution )
include_directories(student_benchmarks pthread ${GBENCH_INCLUDE_DIRS})
target_link_libraries(student_benchmarks ${GBENCH_BOTH_LIBRARIES})
