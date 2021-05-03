#include <cuda.h>
#include <cuComplex.h>
#include <bits/stdc++.h>
#include <cuda_runtime.h>
#include <sys/time.h> 
#include "util.h"
using namespace std;
Point* read(char *filename, int* num){
    FILE *file;
    file = fopen(filename, "r");
    if (!file)  {
        printf("#cannot open input file\n");
        return NULL;
    }
    vector<Point> points;
    float x,y;
    int n = 0;
    while(fscanf(file,"%f %f",&x,&y) != EOF){
        Point p;
        p.x = x;
        p.y = y;
        points.push_back(p);
        n++;
    }
     Point* ps = (Point*) malloc(n*sizeof(Point));
     for(int i=0;i<n;i++)
     {
         ps[i] = points[i];
     }
     num[0] = n;
     return ps;
}
int main(int argc,char **argv){
    int seed = 1234;
    struct timeval t1, t2;
    char *fname = argv[1];
    if(fname == NULL)
    {
        printf("give the input\n");
    }
    int k = 20;
    Point* points;
    int p[1];
    p[0] = 0;
    points = read(fname,p);
    
    // for(int i=0;i<100;i++)
    // {
    //     printf("%f %f\n", points[i].x,points[i].y);
    // }
    // printf("%d\n", sizeof(points));
    int num_points = p[0];
    printf("Points Allocated\n");
    // return 0;
    Point* gpupoints;
    cudaMalloc(&gpupoints, num_points*sizeof(Point));
    printf("GPU Points Allocated\n");
    // Point* cpoints;
    // cpoints = (Point*) malloc(n*sizeof(Point));
    cudaMemcpy(gpupoints,points,num_points*sizeof(Point),cudaMemcpyHostToDevice); 
    printf("GPU Points Copied\n");
    Point* means = (Point*) malloc(k*sizeof(Point));
    int* labels = (int*) malloc(num_points*sizeof(int));
    printf("Means and Labels Allocated\n");
    int num[k];
    for(int i=0;i<k;i++) { num[i] = 0;}
    
    for(int i=0;i<num_points;i++)
    {
        // cout<<(i%k)<<endl;
        labels[i] = i%k;
    }
    // printf("\n");
    
    for(int i=0;i<num_points;i++)
    {
        num[labels[i]] ++;
        means[labels[i]].x += points[i].x;
        means[labels[i]].y += points[i].y;
    }
    // for(int i=0;i<k;i++){ printf("%d ",num[i]);}
    // printf("\n");
    for(int i=0;i<k;i++)
    {
        means[i].x /= (float) num[i];
         means[i].y /= (float) num[i];
    }

    Point* imeans = (Point*) malloc(k*sizeof(Point));
    int* ilabels = (int*) malloc(num_points*sizeof(int));
    for(int i=0;i<num_points;i++) ilabels[i] = labels[i];
    for(int i=0;i<k;i++) { imeans[i].x = means[i].x; imeans[i].y = means[i].y;} 
    printf("imeans and ilabels Allocated\n");
    Point* gpumeans;
    cudaMalloc(&gpumeans, k*sizeof(Point));
    cudaMemcpy(gpumeans,means,k*sizeof(Point),cudaMemcpyHostToDevice); 
    printf("GPU means Allocated\n");
    // for(int i=0;i<k;i++){ printf("%d ",num[i]);}
    // printf("\n");
    int* gpulabels;
    cudaMalloc(&gpulabels, num_points*sizeof(int));
    cudaMemcpy(gpulabels,labels,num_points*sizeof(int),cudaMemcpyHostToDevice); 
    printf("GPU labels Allocated\n");
    int* gpuilabels;
    cudaMalloc(&gpuilabels, num_points*sizeof(int));
    cudaMemcpy(gpuilabels,ilabels,num_points*sizeof(int),cudaMemcpyHostToDevice); 
    printf("GPU ilabels Allocated\n");
    // for(int i=0;i<k;i++){ printf("%d ",num[i]);}
    // printf("\n");
    //Updating above variables
    int iter = 1000;
    // cpu
    float dist[num_points*k];
    printf("Dist Allocated\n");
    float* gpudist;
    cudaMalloc(&gpudist, num_points*k*sizeof(float));
    printf("GPU Dist Allocated\n");
    // for(int i=0;i<k;i++){ printf("%d ",num[i]);}
    // printf("\n");
    int i = 0;
    float time = 0;
    while(i < iter){
       
        // printf("%d \n",i);
        float ct = kmeans_cpu( points,means,labels,dist,iter,num_points,k);
        
        time+=ct;
        i+=1;
    }

    printf("CPU Time taken: %.6f ms\n", time/(float)iter);
    for(int i=0;i<num_points;i++){ printf("%d ",labels[i]);}
    printf("\n");
    // gpu
    i = 0;
    time = 0;
    while(i < iter){
            
            float gt = kmeans_gpu(gpupoints,points,gpumeans,gpulabels,gpudist,dist,iter,num_points,k);
            time+=gt;
            i++;
    }
     printf("GPU Time taken: %.6f ms\n", time/iter);
    cudaMemcpy(labels,gpulabels,num_points*sizeof(int),cudaMemcpyDeviceToHost); 
    for(int i=0;i<num_points;i++){ printf("%d ",labels[i]);}
    printf("\n");
   
     i = 0;
    time = 0;
    float* icd = (float*) malloc(k*k*sizeof(float));
    int* rid = (int*) malloc(k*k*sizeof(int));
    while(i < iter){
        // printf("%d \n",i);
        float ct = kmeans_cpu_ineq( points,imeans,ilabels,icd,rid,iter,num_points,k);
        time+=ct;
        i+=1;
    }

    printf("CPU Time taken for ineq cpu: %.6f ms\n", time/iter);
    for(int i=0;i<num_points;i++){ printf("%d ",ilabels[i]);}
    printf("\n");
     
    i = 0;
    time = 0;
    
    while(i<iter){
        float gt = kmeans_gpu_ineq( gpupoints, imeans, gpuilabels, icd, rid, iter, num_points, k);
        time+=gt;
        i++;
    }

    printf("GPU Time taken for ineq gpu: %.6f ms\n", time/iter);
    cudaMemcpy(ilabels,gpuilabels,num_points*sizeof(int),cudaMemcpyDeviceToHost); 
    for(int i=0;i<num_points;i++){ printf("%d ",ilabels[i]);}
    printf("\n");
    
    return 0;
}


