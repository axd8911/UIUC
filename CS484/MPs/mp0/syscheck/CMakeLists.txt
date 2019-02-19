add_subdirectory("mpi")
add_subdirectory("charm")
add_subdirectory("ampi")

add_custom_target(syscheck)
add_dependencies(syscheck mpi_hello ampi_hello charm_hello)
