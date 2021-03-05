

__global__ void per_row_kernel(int m,int n,int *A,int *B,int *C){
    // int id = blockIdx.x*blockDim.x+threadIdx.x;
    int td = threadIdx.z*blockDim.x*blockDim.y + threadIdx.y*blockDim.x + threadIdx.x;
    int dim = blockDim.x*blockDim.y*blockDim.z;
    int bk = blockIdx.z*gridDim.x*gridDim.y + blockIdx.y*gridDim.x + blockIdx.x;
    int id = bk*dim + td;
    if (id < m){
        for(int i = 0;i < n;++i){
            C[id*n+i] = A[id*n+i]+B[id*n+i];
        }
    }
}

__global__ void per_column_kernel(int m,int n,int *A,int *B,int *C){
    // int id = blockIdx.x*blockDim.x*blockDim.y + threadIdx.y*blockDim.x + threadIdx.x;
    int td = threadIdx.z*blockDim.x*blockDim.y + threadIdx.y*blockDim.x + threadIdx.x;
    int dim = blockDim.x*blockDim.y*blockDim.z;
    int bk = blockIdx.z*gridDim.x*gridDim.y + blockIdx.y*gridDim.x + blockIdx.x;
    int id = bk*dim + td;
    if (id < n){
        for(int i = 0;i < m;++i){
            C[i*n+id] = A[i*n+id] + B[i*n+id];
        }
    }

}

__global__ void per_element_kernel(int m,int n,int *A,int *B,int *C){
    int td = threadIdx.z*blockDim.x*blockDim.y + threadIdx.y*blockDim.x + threadIdx.x;
    int dim = blockDim.x*blockDim.y*blockDim.z;
    int bk = blockIdx.z*gridDim.x*gridDim.y + blockIdx.y*gridDim.x + blockIdx.x;
    int id = bk*dim + td;
    int x = id/n;
    if (x < m){
        int y = id%n;
        C[x*n+y] = A[x*n+y] + B[x*n+y];
    }
}
