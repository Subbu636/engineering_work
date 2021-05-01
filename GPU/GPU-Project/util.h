#include <cuda.h>
#include <cuComplex.h>
#include <bits/stdc++.h>
#include <cuda_runtime.h>
#include <sys/time.h> 
#include "cublas.h"
using namespace std;

struct Point{
    double x, y;
};

void read_file(char *filename, vector <Point> &points);

vector <Point> kmeans_cpu(vector <Point> points, int iter);

vector <Point> kmeans_gpu(vector <Point> points, int iter);


// Gmix Functions
double *read_file_array(char *fname, int d,int *l);
double *create(int m, int n);
double *init_prob(int k, int l);
void gmix_cpu(double *p,double *r, int k, int iter, int l, int d);
void gmix_gpu(double *p,double *r, int k, int iter, int l, int d);
void print_prob(char *fname, double *prob, int k, int l);




