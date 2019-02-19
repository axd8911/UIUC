#include "basic.h"
#include "util.h"

void transpose_basic(double* A, double* B, int N) {
    for (int i = 0; i < N; i++) {
        for (int j = 0; j < N; j++) {
            B[IND(i,j,N)] = A[IND(j,i,N)];
        }
    }
}

void multiply_basic(double* A, double* B, double* C, int N) {
    for (int i = 0; i < N; i++) {
        for (int j = 0; j < N; j++) {
            for (int k = 0; k < N; k++) {
                C[IND(i,j,N)] += A[IND(i,k,N)] * B[IND(k,j,N)];
            }
        }
    }
}
