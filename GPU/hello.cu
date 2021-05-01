// RUN: nvcc -arch=sm_50 -Wno-deprecated-gpu-targets hello.cu

#include <stdio.h>
#include <cuda.h>
#include <cuComplex.h>
#include <bits/stdc++.h>

__global__ void dkernel(cuDoubleComplex* d_cplx) {
    int id = threadIdx.x;
    if (id <= 3){
        printf("%f\n",d_cplx[id]);
    }
}

int main() {
    std::vector<int> h_cplx;
    // h_cplx.push_back(1.0);
    // h_cplx.push_back(2.3);
    // h_cplx.push_back(3.4);
    // cuDoubleComplex* d_cplx;
    // cudaMalloc(&d_cplx, h_cplx.size()*sizeof(cuDoubleComplex)); 
    // cudaMemcpy(d_cplx, h_cplx.data(), h_cplx.size()*sizeof(cuDoubleComplex), cudaMemcpyHostToDevice);
    // dkernel<<<2, 30>>>(d_cplx);
    // cudaDeviceSynchronize();
    return 0;
}

// xorg-edgers/ppa