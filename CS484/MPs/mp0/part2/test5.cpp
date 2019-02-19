#include "timers.h"
#include "part2.h"

/* change dimension size as needed */

void test5(int dimension)
{
        int i, j, k;
        double *A, *B, *C, t1, t2;

        A = (double*)malloc(dimension*dimension*sizeof(double));
        B = (double*)malloc(dimension*dimension*sizeof(double));
        C = (double*)malloc(dimension*dimension*sizeof(double));

        srand(292);

        for(i = 0; i < dimension; i++) {
            for(j = 0; j < dimension; j++)
            {
                    A[dimension*i+j] = (rand()/(RAND_MAX + 1.0));
                    B[dimension*i+j] = (rand()/(RAND_MAX + 1.0));
                    C[dimension*i+j] = 0.0;
            }
	}

        t1 = get_clock();

	for(i = 0; i < dimension; i++)
            for(j = 0; j < dimension; j++)
                for(k = 0; k < dimension; k++)
                    C[dimension*i+j] += A[dimension*i+k] * B[dimension*k+j];

        t2 = get_clock();

        free(A);
        free(B);
        free(C);

	printf("time taken for test5 (dimensions %d) = %7.16f\n", dimension, t2-t1);
}
