cmake_minimum_required (VERSION 3.5)
project(mp0 VERSION 1.0.0 LANGUAGES C CXX)

#
# Location of additional cmake modules
#
set(CMAKE_MODULE_PATH
	${CMAKE_MODULE_PATH}
	${PROJECT_SOURCE_DIR}/cmake
)

include("cs484common")

add_subdirectory("part1")
add_subdirectory("part2")

#Unit tests and microbenchmarks
add_subdirectory("tests")

#Tries to ensure that students are using an environment that will work for cs484
#
add_subdirectory("syscheck")
