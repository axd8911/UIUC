#ifndef BASIC_H_
#define BASIC_H_

// Basic matrix transpose of matrix A to matrix B (B = A^T)
void transpose_basic(double* A, double* B, int N);

// Basic matrix multiplication function that multiplies two NxN matrices
// A and B and stores the resulting NxN matrix in C.
void multiply_basic(double* A, double* B, double* C, int N);

#endif
