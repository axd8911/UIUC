#include "multiply.h"
#include "util.h"


void multiply_tiled(double* A, double* B, double* C, int N, int tile_size) {
// TODO: implement this function.
    for (int i = 0; i < N; i+= tile_size){
        for (int j = 0; j < N; j+= tile_size){
            for(int k = 0; k < N; k += tile_size) {
                for (int p = 0; p < tile_size; p++) {
                    for (int q = 0; q < tile_size; q++) {
                        for (int r = 0; r < tile_size; r++) {
                            C[IND(i+p,j+q,N)] += A[IND(i+p,k+r,N)] * B[IND(k+r,j+q,N)];
                        }
                    }
                }
            }
        }
    }
}


void multiply_transposed(double* A, double *B_T, double *C, int N) {
    // TODO: Implement this function.
    for(int i = 0; i < N; i++){
        for (int j = 0; j < N; j++){
            for (int k = 0; k < N; k++){
                C[IND(i,j,N)] += A[IND(i,k,N)] * B_T[IND(j,k,N)];
            }
        }
    }
}
