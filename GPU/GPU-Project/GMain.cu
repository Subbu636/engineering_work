#include "util.h"
using namespace std;

int main(int argc,char **argv){
    // struct timeval t1, t2;
    char *fname = argv[1];
    int *l = (int*)malloc(sizeof(int)), d = 2;
    double *points = read_file_array(fname, d,l);
    int iter = 500,k = 20;
    double *prob = init_prob(*l,k);
    // cpu
    // gettimeofday(&t1, 0);
    // gmix_cpu(points,prob,k,iter,*l,d);
    // gettimeofday(&t2, 0);
    // double ct = (1000000.0*(t2.tv_sec-t1.tv_sec) + t2.tv_usec-t1.tv_usec)/1000.0;
    // printf("CPU Time taken: %.6f ms\n", ct);
    // char ofname[20] = "out_cpu.txt";
    // print_prob(ofname,prob,*l,k);
    // gpu
    // gettimeofday(&t1, 0);
    gmix_gpu(points,prob,k,iter,*l,d);
    // gettimeofday(&t2, 0);
    // double gt = (1000000.0*(t2.tv_sec-t1.tv_sec) + t2.tv_usec-t1.tv_usec)/1000.0;
    // printf("GPU Time taken: %.6f ms\n", gt);
    // char ofnam[20] = "out_gpu.txt";
    // print_prob(ofnam,prob,*l,k);
}


