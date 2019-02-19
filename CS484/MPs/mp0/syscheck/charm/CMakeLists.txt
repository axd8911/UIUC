find_package(Charm)

add_executable(charm_hello "main.cpp" "main.h" "main.ci")
set_charm_target(charm_hello)
