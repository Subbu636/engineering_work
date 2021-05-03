#include <stdio.h>
#include <cuda.h>
#include <cuda_runtime.h>

__global__ void init(int *arr,int n){
    if(threadIdx.x < n){
        arr[threadIdx.x] = 0;
    }
}

__global__ void add(int *arr,int n){
    if(threadIdx.x < n){
        arr[threadIdx.x] += threadIdx.x;
    }
}

__global__ void print(){
    printf("GPU\n");
}

int main(){
    // cudaError_t cudaStat;
    // const int n = 10;
    // int arr[n],i;
    // int *garr;
    // cudaStat = cudaMalloc(&garr,n*sizeof(int));
    // if (cudaStat != cudaSuccess) {
    //     printf ("device memory allocation failed\n");
    // }
    // init<<<1,n>>>(garr,n);
    // cudaDeviceSynchronize();
    // add<<<1,n>>>(garr,n);
    // cudaDeviceSynchronize();
    // cudaMemcpy(arr,garr,n*sizeof(int),cudaMemcpyDeviceToHost);
    // for(i = 0;i < n;++i){
    //     printf("%d ",arr[i]);
    // }
    // printf("\n");
    print<<<1,1>>>();
    cudaDeviceSynchronize();
    return 0;
}