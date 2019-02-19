# Building

## Building on VMFarm
On VMFarm, you can build the project by running:
```
bash ./scripts/compile_script.sh
```
This will build and test your code.

## Building and Benchmarking on Campus Cluster
Your code should be correct before benchmarking on Campus Cluster.

On Campus Cluster, move to the the base directory of your cloned repo, and submit the batch script.
```
git clone <URL to my turnin repo/mp1.git>
cd mp1
qsub ./scripts/batch_script.run
```
This script builds, tests, and benchmarks your code.

# Manual Building

Note: If you choose to do this on Campus Cluster (you should not need to), be sure that you are using the appropriate Singularity shell.

To build, create a new directory, anywhere. We will call it "build".
Change to that directory and type

```
cmake <path_to_source_directory>
make
```
in your shell.

This will create programs named

* `build/bin/multiply_runner`
* `build/bin/transpose_runner`
* `build/bin/openmp_runner`
* `build/bin/run_tests`
* `build/bin/run_benchmarks`
* `build/bin/student_benchmarks`
* `build/bin/find_tile_size`

# Running tests

You can run the tests we provide by executing the program
`build/bin/run_tests`

Try the "--help" option to see various options.

## Running benchmarks

Run benchmarks by executing the program
`build/bin/run_benchmarks`

Try the "--help" option to see various options.

## Custom benchmarks

You may add benchmarks of your own to the file `tests/student_benchmarks.cpp` in the source tree.
We will not run yours when grading.

See the documentation at [Google Benchmark GitHub Project](https://github.com/google/benchmark) .

## Running the programs
You can use (and modify) these programs during development to do individual benchmarks.

You can run the programs by typing:
* `build/bin/multiply_runner <array_size> <tile_size> [num_trials (default 5)]`
* `build/bin/transpose_runner <array_size> <tile_size> [num_trials (default 5)]`
* `build/bin/openmp_runner <array_size> [num_trials=1]`
