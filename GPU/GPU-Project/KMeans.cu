#include "util.h"

vector <Point> kmeans_cpu(vector <Point> points, int iter){
    vector <Point> means;
    // write kmeans cpu 
    return means;
} 

__global__ void distance_update(double* dist, Points* points, Points* mean)
{
    int id = (blockIdx.x*blockDim.x) + threadIdx.x;
    int i = blockIdx.x;
    int j = threadIdx.x;
    Point a = points[i];
    Point b = mean[i];
    double xdist = (a.x - b.x)*(a.x - b.x);
    double ydist = (a.y - b.y)*(a.y - b.y);
    dist[id] = xdist + ydist;
}
__global__ void label_update(int* labels,double* dist,int k,int n)
{
    int id = threadIdx.x;
    if(id < n){
        int l = 0;
        double mindist = dist[id*n]; 
        for(int i=1;i<k;i++)
        {
            if(mindist > dist[id*n + i]){
                l = i;
                mindist = dist[id*n + i];
            }
        }
        labels[id] = l;
    }
}
__global__ void centers_update(Points* means,Points* points,int* labels,int n,int k)
{
    int id = threadIdx.x;
    if(id<k){
        means[id].x = 0;
        means[id].y = 0;
        double num_points = 0;
        for(int i=0;i<n;i++){
            if(labels[i]==id)
            {
                num_points+=1;
                means[id].x += points[i].x;
                means[id].y += points[i].y;
            }
        }
        means[id].x /= num_points;
        means[id].y /= num_points;
    }
}
void kmeans_gpu(Points* points, Points* means,int* labels,double* dist, int iter, int n, int k){
    // distances size n*k 
    // write kmeans gpu  
    distance_update<<<n,k>>>(dist,points,mean);
    label_update<<<1,n>>>(labels,dist,k,n);
    centers_update<<<1,k>>>(means,points,labels,n,k);
} 