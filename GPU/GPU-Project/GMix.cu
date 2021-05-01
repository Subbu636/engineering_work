#include "util.h"


double *create(int m, int n){
    return (double*)malloc(sizeof(double)*n*m);
}

void init(double *v, int m, int n, double val){
    for(int i = 0;i < m;i++){
        for(int j = 0;j < n;++j){
            v[i*n+j] = val;
        }
    }
}

void copy(double *a, double *b, int m, int n){
    for(int i = 0;i < m;i++){
        for(int j = 0;j < n;++j){
            a[i*m+j] = b[i*m+j];
        }
    }
}


double *transpose(double *trans, double *v, int m, int n){
    for(int i = 0;i < m;i++){
        for(int j = 0;j < n;j++){
            trans[j*m+i] = v[i*n+j];
        }
    }
    return trans;
}

double *matsub(double *res, double *v1, double *v2, int m, int n){
    for(int i = 0;i < m;i++){
        for(int j = 0;j < n;++j){
            res[i*n+j] = v1[i*n+j]-v2[i*n+j];
        }
    }
    return res;
}

double *matmul(double *res, double *a, double *b, int m, int s, int n){
    init(res, m, n, 0.0);
    for (int i = 0; i < m; i++) {
        for (int j = 0; j < n; j++) {
            for (int k = 0; k < s; k++) res[i*n+j] += a[i*s+k]*b[k*n+j];
        }
    }
    return res;
}

double *matadd(double *res, double *v1, double *v2, int m, int n){
    for(int i = 0;i < m;i++){
        for(int j = 0;j < n;++j){
            res[i*n+j] = v1[i*n+j]+v2[i*n+j];
        }
    }
    return res;
}

double *matsmul(double *res, double *v, double val, int m, int n){
    for(int i = 0;i < m;i++){
        for(int j = 0;j < n;++j){
            res[i*n+j] = val*v[i*n+j];
        }
    }
    return res;
}

double *cofactor(double *res, double *a, int p, int q, int n)
{
    int r = 0, c = 0;
    for (int i = 0; i < n; i++)
    {
        for (int j = 0; j < n; j++)
        {
            if (i != p && j != q)
            {
                res[r*(n-1)+(c++)] = a[i*n+j];
                if (c == n - 1)
                {
                    c = 0;
                    r++;
                }
            }
        }
    }
    return res;
}

double matdet(double *a, int n)
{
    double res = 0.0;
    if (n == 1) return a[0];
    double s = 1.0,temp[(n-1)*(n-1)];
    for (int i = 0; i < n; i++)
    {
        res += (s*a[i]*matdet(cofactor(temp,a,0,i,n),n-1));
        s = -1.0*s;
    }
    return res;
}

double *matadj(double *res, double *a, int n)
{
    if(n == 1){
        res[0] = 1.0;
        return res;
    }
    double s = 1.0, temp[n*n];
    for(int i = 0;i < n;i++)
    {
        for(int j = 0;j < n;++j)
        {
            if((i+j)%2 == 0) s = 1.0;
            else s = -1.0;
            res[j*n+i] = (s)*(matdet(cofactor(temp,a,i,j,n), n-1));
        }
    }
    return res;
}

double *matinv(double *res, double *a, int n)
{
    double d = matdet(a,n);
    if (d == 0.0)
    {
        cout<<"#singular - inverse not possible"<<endl;
        assert(false);
    }
    double adj[n*n];
    matadj(adj,a,n);
    for(int i = 0;i < n;++i){
        for(int j = 0;j < n;++j){
            res[i*n+j] = adj[i*n+j]/d;
        }
    }
    return res;
}

double norm(double *x, double *mu, double *sigma, int d){
    double deno = sqrt(pow((double)(2.0*M_PI),(double)d)*matdet(sigma,d));
    double dif[d],temp[d],inv[d*d],val[1],trans[d];
    matsub(dif,x,mu,d,1);
    double expo = (-0.5)*(matmul(val,matmul(temp,transpose(trans,dif,d,1),matinv(inv,sigma,d),1,d,d),dif,1,d,1)[0]);
    // cout<<expo<<endl;
    return exp(expo)/deno;
}

void matprint(double *a, int m, int n){
    for(int i = 0;i < m;i++){
        for(int j = 0;j < n;j++){
            cout<<a[i*n+j]<<" ";
        }
        cout<<endl;
    }
}


void gmix_cpu(double *p,double *r, int k, int iter, int l, int d){

    // Debugging
    // double arr[4] = {2.0,1.0,1.0,2.0};
    // double mu[2] = {1.0, 1.0}, x[2] = {1.0, 0.0};
    // cout<<norm(x,mu,arr,2)<<endl;

    double n[k],pi[k],mu[k*d],sigma[k*d*d];
    double temp[d], var[d*d],trans[d];

    for(int t = 0;t < iter;++t){
        // M-step
        init(n,1,k,0.0);
        for(int i = 0;i < k;++i){
            for(int j = 0;j < l;++j){
                n[i] += r[j*k+i];
            }
        }
        for(int i = 0;i < k;++i){
            pi[i] = n[i]/(double)l;
        }
        init(mu,k,d,0.0);
        for(int i = 0;i < k;++i){
            for(int j = 0;j < l;++j){
                matadd(&mu[i*d],&mu[i*d],matsmul(temp,&p[j*d],r[j*k+i],d,1),d,1);
            }
        }
        for(int i = 0;i < k;++i){
            matsmul(&mu[i*d],&mu[i*d],1/n[i],d,1);
        }
        init(sigma,k,d*d,0.0);
        for(int i = 0;i < k;++i){
            for(int j = 0;j < l;++j){
                matsub(temp,&p[j*d],&mu[i*d],d,1);
                matmul(var,temp,transpose(trans,temp,d,1),d,1,d);
                matsmul(var,var,r[j*k+i],d,d);
                matadd(&sigma[i*d*d],&sigma[i*d*d],var,d,d);
            }
        }
        for(int i = 0;i < k;++i){
            matsmul(&sigma[i*d*d],&sigma[i*d*d],1/n[i],d,d);
        }
        // E-step
        for(int i = 0;i < l; ++i){
            double s = 0;
            for(int j = 0;j < k; ++j){
                r[i*k+j] = pi[j]*norm(&p[i*d],&mu[j*d],&sigma[j*d*d],d);
                s+=r[i*k+j];
            }
            for(int j = 0;j < k;++j){
                r[i*k+j] = r[i*k+j]/s;
            }
        }
    }
    return;  
} 

__global__ void compute_n_pi(double *pi, double *n, double *r, int l, int k){
    int id = (blockIdx.x*blockDim.x)+threadIdx.x;
    if(id >= k) return;
    for(int i = 0;i < l;++i){
        n[id] += r[i*k+id];
    }
    pi[id] = n[id]/(double)l;
}

void gmatmul(double *res, double *a, double *b, int m, int s, int n){
    double x = 1.0, y = 0.0;
    cuComplex *gx,*gy;
    cudaMalloc(&gx,sizeof(double));
    cudaMalloc(&gy,sizeof(double));
    cudaMemcpy(gx,&x,sizeof(double),cudaMemcpyHostToDevice);
    cudaMemcpy(gy,&y,sizeof(double),cudaMemcpyHostToDevice);
    cublasStatus_t stat;
    cublasHandle_t handle;
    stat = cublasCreate(&handle);
    if (stat != CUBLAS_STATUS_SUCCESS) {
        printf ("create problem\n");
    }
    stat = cublasCgemm3m(handle, CUBLAS_OP_N, CUBLAS_OP_T, m, n, s, gx, (cuComplex*)a, m, (cuComplex*)b, s, gy,(cuComplex*)res, m);
    if (stat != CUBLAS_STATUS_SUCCESS) {
        printf ("multiply problem\n");
    }
    return;
}
 
void gmix_gpu(double *p,double *r, int k, int iter, int l, int d){

    // debugging
    double *res, *a, *b;
    cudaMalloc(&res, d*d*sizeof(double));
    cudaMalloc(&a, d*d*sizeof(double));
    cudaMalloc(&b, d*d*sizeof(double));
    double ca[4] = {1.0,2.0,3.0,4.0}, cb[4] = {1.0,0.0,0.0,1.0};
    cudaMemcpy(a,ca,d*d*sizeof(double),cudaMemcpyHostToDevice);
    cudaMemcpy(b,cb,d*d*sizeof(double),cudaMemcpyHostToDevice);
    gmatmul(res,a,b,2,2,2);
    double cres[4];
    cudaMemcpy(cres,res,d*d*sizeof(double),cudaMemcpyDeviceToHost);
    matprint(cres,2,2);

    // double *n,*pi,*mu,*sigma;
    // cudaMalloc(&n,k*sizeof(double));
    // cudaMalloc(&pi,k*sizeof(double));
    // cudaMalloc(&mu,k*d*sizeof(double));
    // cudaMalloc(&sigma,k*d*d*sizeof(double));
    // double *temp,*var,*trans;
    // cudaMalloc(&temp,d*sizeof(double));
    // cudaMalloc(&var,d*d*sizeof(double));
    // cudaMalloc(&trans,d*sizeof(double));

    // double *gp,*gr;
    // cudaMalloc(&gp,l*d*sizeof(double));
    // cudaMalloc(&gr,l*k*sizeof(double));
    // cudaMemcpy(gp,p,l*d*sizeof(double),cudaMemcpyHostToDevice);
    // cudaMemcpy(gr,r,l*k*sizeof(double),cudaMemcpyHostToDevice);

    // // rik -> aprior or P, pi -> alpha, 

    // for(int t = 0; t < iter;++t){
    //     compute_n_pi<<<k,1>>>(pi,n,r,k,l);
    //     cudaDeviceSynchronize();

    // }
    return;
}


