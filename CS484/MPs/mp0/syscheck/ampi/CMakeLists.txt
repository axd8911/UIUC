find_package(Charm)

#really don't like doing it this way, but we haven't finished with the
#CMake ampi finder yet.

set(CMAKE_C_COMPILER ${AMPI_C_COMPILER})
set(CMAKE_CXX_COMPILER ${AMPI_CXX_COMPILER})

add_executable(ampi_hello "main.cpp")
#set_charm_target(ampi_hello)
