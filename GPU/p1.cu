#include <stdio.h>
#include <cuda.h>

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

int main(){
    const int n = 10;
    int arr[n],i;
    int *garr;
    cudaMalloc(&garr,n*sizeof(int));
    init<<<1,n>>>(garr,n);
    cudaDeviceSynchronize();
    add<<<1,n>>>(garr,n);
    cudaDeviceSynchronize();
    cudaMemcpy(arr,garr,n*sizeof(int),cudaMemcpyDeviceToHost);
    for(i = 0;i < n;++i){
        printf("%d ",arr[i]);
    }
    printf("\n");
    return 0;
}