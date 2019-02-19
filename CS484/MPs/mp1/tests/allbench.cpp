
#include "naivelinalg.h"

#include "basic.h"

#include "multiply.h"
#include "transpose.h"
#include "openmp.h"

#include "best_tile_size.h"
#ifndef BEST_TILE_SIZE
#define BEST_TILE_SIZE -1
#endif

#include <random>

#include <chrono>
#include <ctime>
#include <thread>

#include <omp.h>

#include <benchmark/benchmark.h>

#include "benchhelper.h"

#define MATRIX_BENCHMARK_SIZE 2048

#define SQUARES_SIZE 8<<17
#define PRIMES_SIZE 8<<17



static void BM_Transpose_Naive(benchmark::State& state) {
  int N = state.range(0);
  int tile_size = N;
  int n_warmup = 3;

	//SETUP
	matrix A, B;
	matrix_create(&A,N,N);
	matrix_create(&B,N,N);

	for(int i = 0; i < N*N; i++) {
        A.data[i] = rand() % 1000;
    }

	//warmup
	for(int i = 0;i<n_warmup;i++){
		transpose_basic(A.data, B.data, N);
	}


  for (auto _ : state) {
    auto start = std::chrono::high_resolution_clock::now();
	transpose_basic(A.data, B.data, N);
    auto end   = std::chrono::high_resolution_clock::now();

    auto elapsed_seconds =
      std::chrono::duration_cast<std::chrono::duration<double>>(
        end - start);

    state.SetIterationTime(elapsed_seconds.count());
  }
  state.SetItemsProcessed(state.iterations()*int64_t(N)*int64_t(N));

  //TEARDOWN
  matrix_destroy(&A);
  matrix_destroy(&B);

  state.counters["matrix_size"] = N;
  state.counters["tile_size"] = tile_size;
}
BENCHMARK(BM_Transpose_Naive)->UseManualTime()->ArgNames({"matrix_size"})->Arg(MATRIX_BENCHMARK_SIZE);

static void BM_Transpose_Tiled(benchmark::State& state) {
  int N = state.range(0);
  int tile_size = state.range(1);
  if(tile_size < 1){ tile_size = N; }
  int n_warmup = 3;

	//SETUP
	matrix A, B;
	matrix_create(&A,N,N);
	matrix_create(&B,N,N);

	for(int i = 0; i < N*N; i++) {
        A.data[i] = rand() % 1000;
    }

	//warmup
	for(int i = 0;i<n_warmup;i++){
		transpose_tiled(A.data, B.data, N, tile_size);
	}


  for (auto _ : state) {
    auto start = std::chrono::high_resolution_clock::now();
	transpose_tiled(A.data, B.data, N, tile_size);
    auto end   = std::chrono::high_resolution_clock::now();

    auto elapsed_seconds =
      std::chrono::duration_cast<std::chrono::duration<double>>(
        end - start);

    state.SetIterationTime(elapsed_seconds.count());
  }
  state.SetItemsProcessed(state.iterations()*int64_t(N)*int64_t(N));

  //TEARDOWN
  matrix_destroy(&A);
  matrix_destroy(&B);

  state.counters["matrix_size"] = N;
  state.counters["tile_size"] = tile_size;
}
BENCHMARK(BM_Transpose_Tiled)->UseManualTime()->ArgNames({"matrix_size","tile_size"})->Args({MATRIX_BENCHMARK_SIZE, BEST_TILE_SIZE });

static void BM_Multiply_Naive(benchmark::State& state) {
  int N = state.range(0);
  int tile_size = N;
  int n_warmup = 3;

	//SETUP
	matrix A, B, C;
	matrix_create(&A,N,N);
	matrix_create(&B,N,N);
	matrix_create(&C,N,N);

	for(int i = 0; i < N*N; i++) {
        A.data[i] = rand() % 1000;
        B.data[i] = rand() % 1000;
    }

	//warmup
	for(int i = 0;i<n_warmup;i++){
		multiply_basic(A.data, B.data, C.data, N);
	}


  for (auto _ : state) {
    auto start = std::chrono::high_resolution_clock::now();
	multiply_basic(A.data, B.data, C.data, N);
    auto end   = std::chrono::high_resolution_clock::now();

    auto elapsed_seconds =
      std::chrono::duration_cast<std::chrono::duration<double>>(
        end - start);

    state.SetIterationTime(elapsed_seconds.count());
  }
  state.SetItemsProcessed(state.iterations()*int64_t(N)*int64_t(N));

  //TEARDOWN
  matrix_destroy(&A);
  matrix_destroy(&B);
  matrix_destroy(&C);

  state.counters["matrix_size"] = N;
  state.counters["tile_size"] = N;
}
BENCHMARK(BM_Multiply_Naive)->UseManualTime()->ArgNames({"matrix_size"})->Args({MATRIX_BENCHMARK_SIZE});

static void BM_Multiply_Tiled(benchmark::State& state) {
  int N = state.range(0);
  int tile_size = state.range(1);
  if(tile_size < 1){ tile_size = N; }
  int n_warmup = 3;

	//SETUP
	matrix A, B, C;
	matrix_create(&A,N,N);
	matrix_create(&B,N,N);
	matrix_create(&C,N,N);

	for(int i = 0; i < N*N; i++) {
        A.data[i] = rand() % 1000;
        B.data[i] = rand() % 1000;
    }

	//warmup
	for(int i = 0;i<n_warmup;i++){
		multiply_tiled(A.data, B.data, C.data, N, tile_size);
	}


  for (auto _ : state) {
    auto start = std::chrono::high_resolution_clock::now();
	multiply_tiled(A.data, B.data, C.data, N, tile_size);
    auto end   = std::chrono::high_resolution_clock::now();

    auto elapsed_seconds =
      std::chrono::duration_cast<std::chrono::duration<double>>(
        end - start);

    state.SetIterationTime(elapsed_seconds.count());
  }
  state.SetItemsProcessed(state.iterations()*int64_t(N)*int64_t(N));

  //TEARDOWN
  matrix_destroy(&A);
  matrix_destroy(&B);
  matrix_destroy(&C);

  state.counters["matrix_size"] = N;
  state.counters["tile_size"] = tile_size;
}
BENCHMARK(BM_Multiply_Tiled)->UseManualTime()
	->ArgNames({"matrix_size", "tile_size"})->Args({MATRIX_BENCHMARK_SIZE, BEST_TILE_SIZE});

static void BM_Multiply_Transposed(benchmark::State& state) {
  int N = state.range(0);
  int tile_size = N;
  int n_warmup = 3;

	//SETUP
	matrix A, B, C;
	matrix_create(&A,N,N);
	matrix_create(&B,N,N);
	matrix_create(&C,N,N);

	for(int i = 0; i < N*N; i++) {
        A.data[i] = rand() % 1000;
        B.data[i] = rand() % 1000;
    }

	//warmup
	for(int i = 0;i<n_warmup;i++){
		multiply_transposed(A.data, B.data, C.data, N);
	}


  for (auto _ : state) {
    auto start = std::chrono::high_resolution_clock::now();
	multiply_transposed(A.data, B.data, C.data, N);
    auto end   = std::chrono::high_resolution_clock::now();

    auto elapsed_seconds =
      std::chrono::duration_cast<std::chrono::duration<double>>(
        end - start);

    state.SetIterationTime(elapsed_seconds.count());
  }
  state.SetItemsProcessed(state.iterations()*int64_t(N)*int64_t(N));

  //TEARDOWN
  matrix_destroy(&A);
  matrix_destroy(&B);
  matrix_destroy(&C);

  state.counters["matrix_size"] = N;
  state.counters["tile_size"] = N;//No tiling when the second matrix is stored transposed in memory
}
BENCHMARK(BM_Multiply_Transposed)->UseManualTime()->ArgNames({"matrix_size"})->Arg(MATRIX_BENCHMARK_SIZE);



void squares_serial(double* A, int n) {
    for (int i = 0; i < n; i++) {
        A[i] = pow(A[i], 2);
    }
}

void primes_serial(bool* A, int n) {
    for (int i = 0; i < n; i++) {
        A[i] = prime_test(i+2);
    }
}

void squares_warmup(double* A, int n) {
    for (int i = 0; i < n; i++)
        A[i] = A[i] + 1;
}

void primes_warmup(bool* B, int n) {
    for (int i = 0; i < n; i++)
        B[i] = B[i] || (i%2 == 0);
}

static void BM_OpenMP_squares_serial(benchmark::State& state) {
  int N = state.range(0);
  int num_threads = 1; //state.range(1); //serial code, always 1
  int n_warmup = 3;

	//SETUP
	matrix A;
	matrix_create(&A,1,N);

	for(int i = 0; i < N; i++) {
        A.data[i] = rand() % 100;
    }

	//warmup
	for(int i = 0;i<n_warmup;i++){
		squares_warmup(A.data,N);
	}


  for (auto _ : state) {
    auto start = std::chrono::high_resolution_clock::now();
	squares_serial(A.data, N);
    auto end   = std::chrono::high_resolution_clock::now();

    auto elapsed_seconds =
      std::chrono::duration_cast<std::chrono::duration<double>>(
        end - start);

    state.SetIterationTime(elapsed_seconds.count());
  }
  state.SetItemsProcessed(state.iterations()*int64_t(N));

  //TEARDOWN
  matrix_destroy(&A);
  state.counters["N"] = N;
  state.counters["num_threads"] = 1;
}
BENCHMARK(BM_OpenMP_squares_serial)->UseManualTime()->ArgNames({"N", "num_threads"})->Args({SQUARES_SIZE,1});


static void BM_OpenMP_squares_parallel(benchmark::State& state) {
  int N = state.range(0);
  int n_warmup = 3;
  int num_threads = state.range(1);

	//SETUP
	matrix A;
	matrix_create(&A,1,N);

	for(int i = 0; i < N; i++) {
        A.data[i] = rand() % 100;
    }

	//warmup
	for(int i = 0;i<n_warmup;i++){
		squares_warmup(A.data,N);
	}


  for (auto _ : state) {
    auto start = std::chrono::high_resolution_clock::now();
  	omp_set_num_threads(num_threads);
	squares_parallel(A.data, N);
    auto end   = std::chrono::high_resolution_clock::now();

    auto elapsed_seconds =
      std::chrono::duration_cast<std::chrono::duration<double>>(
        end - start);

    state.SetIterationTime(elapsed_seconds.count());

  }
  state.SetItemsProcessed(state.iterations()*int64_t(N));

  //TEARDOWN
  matrix_destroy(&A);
  state.counters["N"] = N;
  state.counters["num_threads"] = num_threads;
}
BENCHMARK(BM_OpenMP_squares_parallel)->UseManualTime()->ArgNames({"N", "num_threads"})->Args({SQUARES_SIZE,2})
	->Args({SQUARES_SIZE,4});

static void BM_OpenMP_primes_serial(benchmark::State& state) {
  int N = state.range(0);
  int n_warmup = 3;
  int num_threads = 1; //state.range(1); //serial code, always 1

	//SETUP
	bool* A = (bool*)malloc(N*sizeof(bool));

	//warmup
	for(int i = 0;i<n_warmup;i++){
		primes_warmup(A,N);
	}


  for (auto _ : state) {
    auto start = std::chrono::high_resolution_clock::now();
	primes_serial(A, N);
    auto end   = std::chrono::high_resolution_clock::now();

    auto elapsed_seconds =
      std::chrono::duration_cast<std::chrono::duration<double>>(
        end - start);


    state.SetIterationTime(elapsed_seconds.count());

  }
  state.SetItemsProcessed(state.iterations()*int64_t(N));

  //TEARDOWN
  free(A);
  state.counters["N"] = N;
  state.counters["num_threads"] = num_threads;//Always 1
}
BENCHMARK(BM_OpenMP_primes_serial)->UseManualTime()->ArgNames({"N", "num_threads"})->Args({SQUARES_SIZE,1});

static void BM_OpenMP_primes_parallel(benchmark::State& state) {
  int N = state.range(0);
  int n_warmup = 3;
  int num_threads = state.range(1);

	//SETUP
	bool* A = (bool*)malloc(N*sizeof(bool));

	//warmup
	for(int i = 0;i<n_warmup;i++){
		primes_warmup(A,N);
	}


  for (auto _ : state) {
    auto start = std::chrono::high_resolution_clock::now();
  	omp_set_num_threads(num_threads);
	primes_parallel(A, N);
    auto end   = std::chrono::high_resolution_clock::now();

    auto elapsed_seconds =
      std::chrono::duration_cast<std::chrono::duration<double>>(
        end - start);



    state.SetIterationTime(elapsed_seconds.count());
  }
  state.SetItemsProcessed(state.iterations()*int64_t(N));

  //TEARDOWN
  free(A);
  state.counters["N"] = N;
  state.counters["num_threads"] = num_threads;
}
BENCHMARK(BM_OpenMP_primes_parallel)->UseManualTime()->ArgNames({"N", "num_threads"})->Args({SQUARES_SIZE,2})
	->Args({SQUARES_SIZE,4});

BENCHMARK_MAIN();
