cmake_minimum_required (VERSION 3.5)
project(mp1-openmp VERSION 1.0.0 LANGUAGES CXX)


if(NOT OPENMP_FOUND)
	message(FATAL_ERROR "This subproject cannot be compiled without OpenMP")
else(NOT OPENMP_FOUND)
	set(CMAKE_C_FLAGS "${CMAKE_C_FLAGS} ${OpenMP_C_FLAGS}")
	set(CMAKE_CXX_FLAGS "${CMAKE_CXX_FLAGS} ${OpenMP_CXX_FLAGS}")
	set(CMAKE_EXE_LINKER_FLAGS "${CMAKE_EXE_LINKER_FLAGS} ${OpenMP_EXE_LINKER_FLAGS}")
endif(NOT OPENMP_FOUND)

add_library(openmp_solution STATIC "openmp.cpp")
#Students might want to override the fatal error above and this lets them compile everything else.
add_executable(openmp_runner "main.cpp" )
target_link_libraries(openmp_runner openmp_solution common)

#target_compile_options(openmp_solution
#PUBLIC $<$<COMPILE_LANGUAGE:CXX>:${OpenMP_CXX_FLAGS}>
#$<$<COMPILE_LANGUAGE:C>:${OpenMP_C_FLAGS}>
#)
