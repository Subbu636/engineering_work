#include <stdio.h>
#include <cuda.h>
__global__ void dkernel(unsigned *matrix) {
    unsigned id = threadIdx.x * blockDim.y + threadIdx.y;
    matrix[id] = id;
}
#define x_lim = 100
#define y_lim = 100
int main() {
    const int n = 1024;
    int x[n],y[n];
    for(int i = 0;i < n;++i){
        scanf("%d%d",&x[i],&y[i]);
    }
    
    return 0;
}




