//
// Originally from https://gist.github.com/mdavezac/eb16de7e8fc08e522ff0d420516094f5
//
// Distributed under the MIT license
// The following demonstrates how to use google/benchmark with MPI enabled
// codes. There are three core aspects:
//
// 1. The time it takes to run a single iteration of a particular benchmark is
//    the maximum time across all processes. In a two process job, if process 0
//    takes 1 second to do its work, and process 1 takes 1.5 seconds, then 1.5
//    seconds is the time recorded for benchmarking purposes.
// 2. Only the root process is allowed to report. Other processes report via a
//    "NullReporter".
// 3. The main is modified to initialize and finalize MPI. It is also modified
// for the purpose of point 2 above.
//
// Note: The efficiency of MPI algorithm often depends on interweaving compute
// and communication. Depending on how it is applied, benchmarking, and
// especially micro-benchmarking, may not capture this aspect.
//
#include <mpi.h>
#include <benchmark/benchmark.h>
#include <chrono>
#include <thread>

namespace {
// The unit of code we want to benchmark
void i_am_sleepy(int macsleepface) {
  // Pretend to work ...
  std::this_thread::sleep_for(std::chrono::milliseconds(macsleepface));
  // ... as a team
  MPI_Barrier(MPI_COMM_WORLD);
}

void mpi_benchmark(benchmark::State &state) {
  double max_elapsed_second;
  int rank;
  MPI_Comm_rank(MPI_COMM_WORLD, &rank);
  while(state.KeepRunning()) {
    // Do the work and time it on each proc
    auto start = std::chrono::high_resolution_clock::now();
    i_am_sleepy(rank % 5);
    auto end = std::chrono::high_resolution_clock::now();
    // Now get the max time across all procs:
    // for better or for worse, the slowest processor is the one that is
    // holding back the others in the benchmark.
    auto const duration = std::chrono::duration_cast<std::chrono::duration<double>>(end - start);
    auto elapsed_seconds = duration.count();
    MPI_Allreduce(&elapsed_seconds, &max_elapsed_second, 1, MPI_DOUBLE, MPI_MAX, MPI_COMM_WORLD);
    state.SetIterationTime(max_elapsed_second);
  }
}
}

BENCHMARK(mpi_benchmark)->UseManualTime();

// This reporter does nothing.
// We can use it to disable output from all but the root process
class NullReporter : public ::benchmark::BenchmarkReporter {
public:
  NullReporter() {}
  virtual bool ReportContext(const Context &) {return true;}
  virtual void ReportRuns(const std::vector<Run> &) {}
  virtual void Finalize() {}
};

// The main is rewritten to allow for MPI initializing and for selecting a
// reporter according to the process rank
int main(int argc, char **argv) {

  MPI_Init(&argc, &argv);

  int rank, size;
  MPI_Comm_rank(MPI_COMM_WORLD, &rank);
  MPI_Comm_size(MPI_COMM_WORLD, &size);

  ::benchmark::Initialize(&argc, argv);

  if(rank == 0)
    // root process will use a reporter from the usual set provided by
    // ::benchmark
    ::benchmark::RunSpecifiedBenchmarks();
  else {
    // reporting from other processes is disabled by passing a custom reporter
    NullReporter null;
    ::benchmark::RunSpecifiedBenchmarks(&null,NULL);
  }

  MPI_Finalize();
  return 0;
}
