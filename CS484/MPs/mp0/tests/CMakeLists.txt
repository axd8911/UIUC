cmake_minimum_required (VERSION 3.5)
project(mp0-tests VERSION 1.0.0 LANGUAGES CXX)

include(CTest)
find_package(GTest)#comes with CMake
if(GTEST_FOUND)
	#include(GoogleTest)
else(GTEST_FOUND)
	message(FATAL_ERROR "DANGER: Could not find GoogleTest")
endif(GTEST_FOUND)

#Benchmarking
find_package(GBench)
if(GBENCH_FOUND)

else(GBENCH_FOUND)
	message(FATAL_ERROR "DANGER: Could not find GoogleBenchmark")
endif(GBENCH_FOUND)

#we additionally need mpi
find_package(MPI)
if(MPI_CXX_FOUND)
	message("Found MPI")
else(MPI_CXX_FOUND)
	message(FATAL_ERROR "Could not find MPI")
endif(MPI_CXX_FOUND)


#testing targets
add_executable(test_example "test_example.cpp")
#include_directories(test_gassim3d "gas/gassim/include")
#target_link_libraries(test_gassim3d gassim pthread)
include_directories(test_example ${GTEST_INCLUDE_DIRS})
target_link_libraries(test_example pthread ${GTEST_BOTH_LIBRARIES})

#Testing with MPI
add_executable(test_mpi_example "test_mpi_example.cpp" "gtest-mpi-listener.hpp")
include_directories(test_mpi_example ${GTEST_INCLUDE_DIRS} ${MPI_CXX_INCLUDE_PATH})
target_link_libraries(test_mpi_example pthread ${GTEST_LIBRARIES} ${MPI_CXX_LIBRARIES})


#Benchmark targets
add_executable(bench_example "bench_example.cpp")
target_link_libraries(bench_example pthread)
include_directories(bench_example ${GBENCH_INCLUDE_DIRS})
target_link_libraries(bench_example ${GBENCH_BOTH_LIBRARIES})

add_executable(bench_mpi_example "bench_mpi_example.cpp")
target_compile_features(bench_mpi_example PRIVATE cxx_auto_type)
target_link_libraries(bench_mpi_example pthread)
target_link_libraries(bench_mpi_example ${MPI_LIBRARIES} ${GBENCH_LIBRARIES})
