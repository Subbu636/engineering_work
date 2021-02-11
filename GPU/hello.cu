// RUN: nvcc -arch=sm_50 -Wno-deprecated-gpu-targets hello.cu

#include <stdio.h>
#include <cuda.h>

__global__ void dkernel() {
 printf("%d\n",threadIdx.x);
}
int main() {
 dkernel<<<1, 100>>>();
 cudaDeviceSynchronize();
 return 0;
}

// xorg-edgers/ppa