#include<bits/stdc++.h>
#include<stdio.h>
#include <thrust/sort.h>
#include<stdlib.h>
#include<cuda.h>
#include <sys/time.h>


using namespace std;


//Complete the following function
struct Car{
    int id;
    float zonetime;
    
};
struct CarSorter1
{
   __host__ __device__ bool operator()(const Car& a, const Car& b)
  {
    if (a.zonetime != b.zonetime) return a.zonetime < b.zonetime;
    else return a.id < b.id;
  }
};


struct CarSorter2
{
   __host__ __device__ bool operator()(const Car& a, const Car& b)
  {
    return a.id < b.id;
  }
};

__global__ void travelkernel(Car* gcars,int* speeds, int n, int dis)
{
    int id = blockIdx.x * blockDim.x + threadIdx.x;
    if(id < n)
    {
        gcars[id].zonetime +=   ((float)dis*60)/(float)speeds[gcars[id].id];
    }
}

__global__ void waitingkernel(Car* gcars, int n,int m,int x)
{
    int id = blockIdx.x * blockDim.x + threadIdx.x;
    if(id < m)
    {
        
        for(int j=id;j<n;j+=m)
        {
            if(j<m)
            {
                gcars[j].zonetime = gcars[j].zonetime + (float) x;
            }
            else
            {
                if(gcars[j-m].zonetime >= gcars[j].zonetime)
                {
                        gcars[j].zonetime = gcars[j-m].zonetime + (float)x ;
                }
                else gcars[j].zonetime = gcars[j].zonetime + (float)x ;
            }
        }
    }
}
__global__ void initkernel(Car* gcars,int n)
{
    int id = blockIdx.x * blockDim.x + threadIdx.x;
    if(id < n)
    {
        gcars[id].zonetime = 0;
        gcars[id].id = id;
    }
}
__global__ void printcar(Car* gcars,int n)
{
    int id = blockIdx.x * blockDim.x + threadIdx.x;
    if(id < n)
    {
        printf("%d, %f\n", gcars[id].id,gcars[id].zonetime);
    }
}
__global__ void printspeed(int* speeds,int n)
{
    int id = blockIdx.x * blockDim.x + threadIdx.x;
    if(id < n)
    {
        printf("%d, %d\n",id, speeds[id]);
    }
}
void operations ( int n, int k, int m, int x, int dis, int *speed, int **results )  {
         Car cars[n];
         Car *gcars;
         cudaMalloc(&gcars, n* sizeof(Car));
         int blocks = (n/1024) + 1;
        initkernel<<<blocks,1024>>>(gcars,n);
        cudaDeviceSynchronize();
        for (int i=0; i<=k;i++)
        {
           // printf("%d\n",i);
            int *gspeeds;
            cudaMalloc(&gspeeds, n* sizeof(int)); 
            int* offspeed = (int*) malloc(n*sizeof(int));
            for(int j=0;j<n;j++){offspeed[j] = speed[i*n+j];}
            cudaMemcpy(gspeeds,offspeed,n*sizeof(int),cudaMemcpyHostToDevice);

         //   printspeed<<<blocks,1024>>>(gspeeds,n);
         //   cudaDeviceSynchronize();
            travelkernel<<<blocks,1024>>>(gcars,gspeeds,n,dis);
            cudaMemcpy(cars,gcars,n*sizeof(Car),cudaMemcpyDeviceToHost);

            sort(cars,cars+n,CarSorter1());
            
            results[0][i] = cars[0].id + 1;
            results[1][i] = cars[n-1].id + 1;
            
            if(i==k) break;
            cudaMemcpy(gcars,cars,n*sizeof(Car),cudaMemcpyHostToDevice);
            
            waitingkernel<<<1,m>>>(gcars,n,m,x);
            cudaDeviceSynchronize();
        }
        for(int i=0;i<n;i++)
        {
            results[2][cars[i].id] = floor(cars[i].zonetime);
        }
  
}

int main(int argc,char **argv){

    //variable declarations
    int n,k,m,x;
    int dis;
    
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
    
    fscanf( inputfilepointer, "%d", &dis );    //scaning for distance between two consecutive toll tax zones


    // scanning for speeds of each vehicles for every subsequent toll tax combinations
    int *speed = (int *) malloc ( n*( k+1 ) * sizeof (int) );
    for ( int i=0; i<=k; i++ )  {
        for ( int j=0; j<n; j++ )  {
            fscanf( inputfilepointer, "%d", &speed[i*n+j] );
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
    operations ( n, k, m, x, dis, speed, results );


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