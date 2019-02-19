/**
* This program was originally created in 2017 by <donghun2@illinois.edu>
* https://gitlab.engr.illinois.edu/users/donghun2
*
* It was created for the Computer Science Department of the
* University of Illinois at Urbana-Champaign .
* It is certainly a "work made for hire" under US copyright law.
*
*
* Under that legal theory, acting as the agent of the department and professor
* who directly supervised the original creation, I (<bjlunt2@illinois.edu>) am
* releasing it under a modified MIT license.
*
* The modification is that this explanation _also_ be kept in the code
* unless/until the original author can personally create a license header OR
* the work is more clearly established to be a "work for hire" and our right to
* release it under an unmodified MIT license is clearly established.
*
********************************************************************************
* Copyright 2017 University of Illinios at Urbana-Champaign
*
* Permission is hereby granted, free of charge, to any person obtaining a copy
* of this software and associated documentation files (the "Software"),
* to deal in the Software without restriction, including without limitation
* the rights to use, copy, modify, merge, publish, distribute, sublicense,
* and/or sell copies of the Software, and to permit persons to whom the
* Software is furnished to do so, subject to the following conditions:
*
* The above copyright notice and this permission notice shall be included in
* all copies or substantial portions of the Software.
*
* THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND,
* EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF
* MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT.
* IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM,
* DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT,
* TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE
* OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
*
*/


#include "stdio.h"
#include "stdlib.h"
#include "sys/time.h"
#define MAXSIZE 70000000
#include <cassert>

void timerTest();
double get_clock();

void initValues(int *A, int *indirection, int maxsize,int stride, int size)
{
  int i;
  indirection[0] = stride % size;
  for (i=1; i<maxsize; i++) {
    A[i] = rand();
    indirection[i] = (indirection[i-1]+stride)%size;
  }
}

// Measures time in seconds

double get_clock()
{
        struct timeval tv;
	int ok;
	ok = gettimeofday(&tv,0);
	if(ok < 0)
	{
	   printf("gettimeofday error");
	   exit(-1);
	}
	  return (tv.tv_sec*1.0+tv.tv_usec*1.0E-6);
}


int main(int argc, char**argv)
{
  double t1,t2,t3;
  int size, k, count1, stride, repetitions;
  int i;
  int * A;
  int *indirection;

  A = (int *) malloc(sizeof(int)* MAXSIZE);
  indirection = (int *) malloc(sizeof(int)* MAXSIZE);
  assert (A != NULL);
  assert (indirection != NULL);

  timerTest();

  repetitions = 1 << 27; // 2^27 or approx 100 million repetitions for each size (Constant)
  stride = 1000;  // initial value of stride (stride is constant at 1000)

  for (size = 1<<6; size < MAXSIZE; size = size*2) {
    initValues(A, indirection, MAXSIZE, stride, size);

    t1 = get_clock(); // record start timer after indirection array has been set.

    for (i = 0; i < repetitions/size; i++)
      for (count1 =0; count1 < size ; count1++) {
        A[count1] += A[ indirection[count1] ];
      }

    t2 = get_clock();
    printf("time for size = %d was: %lf. per access: %lf nanoseconds\n",
        size, t2-t1, (1000000.0* (t2-t1)*1000.0)/repetitions);
  }
}

// Testing function for the getclock routine.

void timerTest()
{
  double t0, times[100];
  int i;
  for (i=0; i<10; i++)
    times[i] = get_clock();
  for (i=0; i<10; i++)
    printf("time at %d: %lf\n", i, times[i]-times[0]);
}
