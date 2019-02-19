#include <iostream>

#include <gtest/gtest.h>


#define TESTING_TOLERANCE 1E-8

TEST(EXAMPLE, example_test){
	double myvar = 1.0;
	double othervar = 1.0;
	EXPECT_NEAR(myvar,othervar,TESTING_TOLERANCE);
}


int main(int argc, char** argv) {
  // Filter out Google Test arguments
  ::testing::InitGoogleTest(&argc, argv);

  // Run tests, then clean up and exit
  int foo = RUN_ALL_TESTS();

  return 0;
}
