find_package(MPI)

add_executable(mpi_hello "main.cpp")
target_compile_options(mpi_hello PUBLIC ${MPI_CXX_COMPILE_FLAGS})
target_include_directories(mpi_hello PUBLIC ${MPI_CXX_INCLUDE_PATH})
target_link_libraries(mpi_hello PUBLIC ${MPI_CXX_LIBRARIES})
