#include <iostream>
#include <mpi.h>


#include <gtest/gtest.h>
#include "gtest-mpi-listener.hpp"


#define TESTING_TOLERANCE 1E-8

TEST(EXAMPLE, example_mpi_test){
	double myvar = 1.0;
	double othervar = 1.0;
	EXPECT_NEAR(myvar,othervar,TESTING_TOLERANCE);
}


int main(int argc, char** argv) {
  // Filter out Google Test arguments
  ::testing::InitGoogleTest(&argc, argv);

  // Initialize MPI
  MPI_Init(&argc, &argv);

  int world_rank;
  int world_size;

  MPI_Comm_rank(MPI_COMM_WORLD, &world_rank);
  MPI_Comm_size(MPI_COMM_WORLD, &world_size);

  if(4 > world_size){
	  std::cerr << " Can't run these tests on less than 4 ranks " << std::endl;
	  exit(1);
  }

  // Add object that will finalize MPI on exit; Google Test owns this pointer
  ::testing::AddGlobalTestEnvironment(new MPIEnvironment);

  // Get the event listener list.
  ::testing::TestEventListeners& listeners =
      ::testing::UnitTest::GetInstance()->listeners();

  //only get json/xml output from rank 0
  int rank;
  MPI_Comm_rank(MPI_COMM_WORLD, &rank);
  if(0 != rank){
	  // Remove default listener
	  delete listeners.Release(listeners.default_result_printer());
	  // Remove default file output
	  delete listeners.Release(listeners.default_xml_generator());
  }
  listeners.Append(new MPICollectTestResults);

  // Run tests, then clean up and exit
  int foo = RUN_ALL_TESTS();

  return 0;
}
