// Run: nvcc -arch=sm_50 -Wno-deprecated-gpu-targets main.cu

// Run: create object files with -c tag for both main and cs
// Now compile both obj files together: nvcc -arch=sm_50 -Wno-deprecated-gpu-targets CS17B005.obj main.obj
// run the excutable produced

#include <stdio.h>
#include <stdlib.h>
#include <cuda.h>
#include "kernels.h"

void print_matrix(int m,int n, int *mat){
    for(int i = 0;i < m;++i){
        for(int j = 0;j < n;++j){
            printf("%d ",mat[i*n+j]);
        }
        printf("\n");
    }
}

#define N 1024
int main() {
    // Sample Generated Input
    // const int m = 15,n = 10;
    // int A[n*m],B[n*m],C[n*m];
    // for(int i = 0;i < m*n;++i){
    //     A[i] = (i+1);
    //     B[i] = -1*(i);
    // }

    // Reading from files
    int m,n;
    const char input_file_name[100] = "testcases\\input\\input3.txt"; // windows file path!!
    FILE *file;
    if ((file = fopen (input_file_name, "r")) == NULL){
        printf("Cannot Open File!");
        exit(1);
    }
    fscanf (file, "%d", &m);
    fscanf (file, "%d", &n);
    int l = m*n,v;
    int *A = (int*)malloc(l*sizeof(int));
    int *B = (int*)malloc(l*sizeof(int));
    int *C = (int*)malloc(l*sizeof(int));
    for (int i = 0;i < l && !feof (file);++i){
        fscanf (file, "%d", &v);
        A[i] = v;
    }
    for (int i = 0;i < l && !feof (file);++i){
        fscanf (file, "%d", &v);
        B[i] = v;
    }
    fclose(file);

    // Core Code
    int *gpuA,*gpuB,*gpuC;
    cudaMalloc(&gpuA,m*n*sizeof(int));
    cudaMalloc(&gpuB,m*n*sizeof(int));
    cudaMemcpy(gpuA,A,m*n*sizeof(int),cudaMemcpyHostToDevice);
    cudaMemcpy(gpuB,B,m*n*sizeof(int),cudaMemcpyHostToDevice);
    cudaMalloc(&gpuC,m*n*sizeof(int));
    per_element_kernel<<<dim3(3963,1,1),dim3(328,1,1)>>>(m,n,gpuA,gpuB,gpuC);
    cudaMemcpy(C,gpuC,m*n*sizeof(int),cudaMemcpyDeviceToHost);
    // print_matrix(m,n,C);

    // Checking it with output
    const char output_file_name[100] = "testcases\\output\\output3.txt"; // windows file path!!
    if ((file = fopen (output_file_name, "r")) == NULL){
        printf("Cannot Open File!");
        exit(1);
    }
    bool out = true;
    for (int i = 0;i < l && !feof (file);++i){
        fscanf (file, "%d", &v);
        if(v != C[i]){
            out = false;
            break;
        }
    }
    fclose(file);
    if (out){
        printf("Correct Output");
    }
    else{
        printf("Wrong Output");
    }
    return 0;
}