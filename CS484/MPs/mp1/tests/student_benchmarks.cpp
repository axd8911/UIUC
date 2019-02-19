
#include "naivelinalg.h"

#include "basic.h"

#include "multiply.h"
#include "transpose.h"
#include "openmp.h"

#include <random>

#include <chrono>
#include <ctime>
#include <thread>

#include <benchmark/benchmark.h>


static void STUDENT_Example_benchmark(benchmark::State& state) {
  int N = state.range(0);
  int tile_size = state.range(1);//second parameter
  int n_warmup = 3;

	//SETUP CODE


  for (auto _ : state) {
	  //SINGLE ITERATION SETUP

    auto start = std::chrono::high_resolution_clock::now();

	//CODE TO BENCHMARK

    auto end   = std::chrono::high_resolution_clock::now();

    auto elapsed_seconds =
      std::chrono::duration_cast<std::chrono::duration<double>>(
        end - start);

    state.SetIterationTime(elapsed_seconds.count());

	//SINGLE ITERATION TEADRDOWN

  }
  state.SetItemsProcessed(state.iterations()*int64_t(N)*int64_t(N));

  //TEARDOWN
  state.counters["something_you_would_like_to_report"] = 1.234;
}
BENCHMARK(STUDENT_Example_benchmark)->UseManualTime()->ArgNames({"arg0name","arg1name"})->Args({1,1});

BENCHMARK_MAIN();
