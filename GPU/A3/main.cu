#include<bits/stdc++.h>
#include<cuda.h>
#include <sys/time.h>
#include<thrust/reduce.h>
using namespace std;

struct pairs{
    float first;
    int second;
};

__host__ __device__ bool compareTimes(pairs i1, pairs i2)
{
    if (i1.first == i2.first){
        return i1.second < i2.second;
    }
    return i1.first < i2.first;
}

__global__ void toll_booths(pairs *times,int n,int m,int x){
    int id = (blockIdx.x*blockDim.x)+threadIdx.x;
    if (id < m){
        float last = times[id].first;
        for(int q = id;q < n;q+=m){
            times[q].first = max(last,times[q].first)+(float)x;
            last = times[q].first;
        }
    }
}

__global__ void travel(pairs *times,int n,int i,float dis,float *speed){
    int id = (blockIdx.x*blockDim.x)+threadIdx.x;
    if (id < n){
        times[id].first = times[id].first+(dis/speed[((i+1)*n)+times[id].second]);
    }
}

//Complete the following function
void operations_gpu ( int n, int k, int m, int x, float dis, float *speed, int **results )  {
    pairs *times = (pairs*) malloc(n*sizeof(pairs));
    pairs *gpu_times;
    cudaMalloc(&gpu_times,n*sizeof(pairs));
    float *gpu_speed;
    cudaMalloc(&gpu_speed,n*(k+1)*sizeof(float));
    cudaMemcpy(gpu_speed,speed,n*(k+1)*sizeof(float),cudaMemcpyHostToDevice);
    for(int j = 0;j < n;++j){
        times[j].first = (dis/speed[j]);
        times[j].second = j;
    }
    cudaMemcpy(gpu_times,times,n*sizeof(pairs),cudaMemcpyHostToDevice);
    for(int i = 0;i < k;++i){
        thrust::sort(times,times+n,compareTimes);
        cudaDeviceSynchronize();
        results[0][i] = times[0].second+1;
        results[1][i] = times[n-1].second+1;
        cudaMemcpy(gpu_times,times,n*sizeof(pairs),cudaMemcpyHostToDevice);
        toll_booths<<<m,1>>>(gpu_times,n,m,x);
        cudaDeviceSynchronize();
        travel<<<n,1>>>(gpu_times,n,i,dis,gpu_speed);
        cudaDeviceSynchronize();
        cudaMemcpy(times,gpu_times,n*sizeof(pairs),cudaMemcpyDeviceToHost);
    }
    thrust::sort(times,times+n,compareTimes);
    for(int j = 0;j < n;++j){
        results[2][times[j].second] = (int)times[j].first;
    }
    results[0][k] = times[0].second+1;
    results[1][k] = times[n-1].second+1;
    return;
}


// void operations_cpu ( int n, int k, int m, int x, float dis, float *speed, int **results )  {
//     pairs *times = (pairs*) malloc(n*sizeof(pairs));
//     for(int j = 0;j < n;++j){
//         times[j].first = 0.0;
//         times[j].second = j;
//     }
//     for(int i = 0;i < k;++i){
//         for(int j = 0;j < n;++j){
//             times[j].first = times[j].first+(dis/speed[(i*n)+times[j].second]);
//         }
//         sort(times,times+n,compareTimes);
//         results[0][i] = times[0].second+1;
//         results[1][i] = times[n-1].second+1;
//         for(int p = 0;p < m;++p){
//             float last = times[p].first;
//             for(int q = p;q < n;q+=m){
//                 times[q].first = max(last,times[q].first)+(float)x;
//                 last = times[q].first;
//             }
//         }
//     }
//     for(int j = 0;j < n;++j){
//         times[j].first = times[j].first+(dis/speed[(k*n)+times[j].second]);
//         results[2][times[j].second] = (int)times[j].first;
//     }
//     sort(times,times+n,compareTimes);
//     results[0][k] = times[0].second+1;
//     results[1][k] = times[n-1].second+1;
//     return;
// }

int main(int argc,char **argv){

    //variable declarations
    int n,k,m,x;
    float dis;
    
    //Input file pointer declaration
    FILE *inputfilepointer;
    
    //File Opening for read
    char *inputfilename = argv[1];
    inputfilepointer    = fopen( inputfilename , "r");
    
    //Checking if file ptr is NULL
    if ( inputfilepointer == NULL )  {
        printf( "input.txt file failed to open." );
        return 0;
    }
    
    
    fscanf( inputfilepointer, "%d", &n );      //scaning for number of vehicles
    fscanf( inputfilepointer, "%d", &k );      //scaning for number of toll tax zones
    fscanf( inputfilepointer, "%d", &m );      //scaning for number of toll tax points
    fscanf( inputfilepointer, "%d", &x );      //scaning for toll tax zone passing time
    
    fscanf( inputfilepointer, "%f", &dis );    //scaning for distance between two consecutive toll tax zones


    // scanning for speeds of each vehicles for every subsequent toll tax combinations
    float *speed = (float *) malloc ( n*( k+1 ) * sizeof (float) );
    for ( int i=0; i<=k; i++ )  {
        for ( int j=0; j<n; j++ )  {
            fscanf( inputfilepointer, "%f", &speed[i*n+j] );
            speed[i*n+j] = speed[i*n+j]/60.0;
        }
    }
    
    // results is in the format of first crossing vehicles list, last crossing vehicles list 
    //               and total time taken by each vehicles to pass the highway
    int **results = (int **) malloc ( 3 * sizeof (int *) );
    results[0] = (int *) malloc ( (k+1) * sizeof (int) );
    results[1] = (int *) malloc ( (k+1) * sizeof (int) );
    results[2] = (int *) malloc ( (n) * sizeof (int) );


    cudaEvent_t start, stop;
    cudaEventCreate(&start);
    cudaEventCreate(&stop);
    float milliseconds = 0;
    cudaEventRecord(start,0);


    // Function given to implement
    operations_gpu ( n, k, m, x, dis, speed, results );


    cudaDeviceSynchronize();

    cudaEventRecord(stop,0);
    cudaEventSynchronize(stop);
    cudaEventElapsedTime(&milliseconds, start, stop);
    printf("Time taken by function to execute is: %.6f ms\n", milliseconds);
    
    // Output file pointer declaration
    char *outputfilename = argv[2]; 
    FILE *outputfilepointer;
    outputfilepointer = fopen(outputfilename,"w");

    // First crossing vehicles list
    for ( int i=0; i<=k; i++ )  {
        fprintf( outputfilepointer, "%d ", results[0][i]);
    }
    fprintf( outputfilepointer, "\n");


    //Last crossing vehicles list
    for ( int i=0; i<=k; i++ )  {
        fprintf( outputfilepointer, "%d ", results[1][i]);
    }
    fprintf( outputfilepointer, "\n");


    //Total time taken by each vehicles to pass the highway
    for ( int i=0; i<n; i++ )  {
        fprintf( outputfilepointer, "%d ", results[2][i]);
    }
    fprintf( outputfilepointer, "\n");

    fclose( outputfilepointer );
    fclose( inputfilepointer );
    return 0;
}