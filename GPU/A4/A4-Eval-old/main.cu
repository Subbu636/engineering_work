#include <cuda.h>
#include <cuComplex.h>
#include <bits/stdc++.h>
#include <cuda_runtime.h>
#include <sys/time.h> 

#define fanout 7

struct bp_node{
    bool leaf;
    int  key[fanout],loc[fanout],len; // loc varible for leaf points to index of 2d array
    bp_node *next[fanout+1],*parent;
};

__host__ __device__ void search_trace(bp_node *root,int k, int *trace){
    trace[0] = -1;
    if(!root) return;
    int c = 0;
    bp_node *ptr = root;
    trace[c] = ptr->key[0];
    c++;
    while(!ptr->leaf){
        for(int i = 0;i < ptr->len;i++){
            if(k < ptr->key[i]){
                ptr = ptr->next[i];
                trace[c] = ptr->key[0];
                c++;
                break;
            }
            if (i == ptr->len - 1) {
                ptr = ptr->next[i + 1];
                trace[c] = ptr->key[0];
                c++;
                break;
            }
        }
    }
    trace[c] = -1;
    return;
}

__host__ __device__ int search_loc(bp_node *root,int k){
    if(!root) return -1;
    bp_node *ptr = root;
    while(!ptr->leaf){
        for(int i = 0;i < ptr->len;i++){
            if(k < ptr->key[i]){
                ptr = ptr->next[i];
                break;
            }
            if (i == ptr->len - 1) {
                ptr = ptr->next[i + 1];
                break;
            }
        }
    }
    for(int i = 0;i < ptr->len;i++){
        if(ptr->key[i] == k){
            return ptr->loc[i];
        }
    }
    return -1;
}

__host__ __device__ bp_node* search_lnode(bp_node *root,int k){
    if(!root) return NULL;
    bp_node *ptr = root;
    while(!ptr->leaf){
        for(int i = 0;i < ptr->len;i++){
            if(k < ptr->key[i]){
                ptr = ptr->next[i];
                break;
            }
            if (i == ptr->len - 1) {
                ptr = ptr->next[i + 1];
                break;
            }
        }
    }
    return ptr;
}

__host__ __device__ void print_dfs(bp_node *root){
    if(!root) return;
    printf("%d/%d :",root->len,(int)root->leaf);
    for(int i = 0;i < root->len;++i){
        printf(" %d",root->key[i]);
    }
    printf("\n");
    if(root->leaf) return;
    for(int i = 0;i < root->len+1;++i){
        print_dfs(root->next[i]);
    }
}

bp_node* insert_inode(int k, bp_node* inode, bp_node* cnode, bp_node* root){
	if(inode->len < fanout){
        int p = 0;
        for(int i = 0; i < inode->len;++i){
            if(k > inode->key[i]){
                p++;
                continue;
            }
            int v = inode->len + p - i;
            inode->key[v] = inode->key[v-1];
            inode->next[v+1] = inode->next[v];
        }
		inode->key[p] = k;
		inode->len++;
		inode->next[p + 1] = cnode;
        return root;
	}
    int ktemp[fanout+1];
    bp_node* ntemp[fanout+2];
    std::copy(inode->key,(inode->key)+fanout,ktemp);
    std::copy(inode->next,(inode->next)+fanout+1,ntemp);
    int p = 0;
    for(int i = 0;i < fanout;i++){
        if(k > ktemp[i]){
            p++;
            continue;
        }
        int v = fanout + p - i;
        ktemp[v] = ktemp[v-1];
        ntemp[v+1] = ntemp[v];
    }
    ktemp[p] = k;
    ntemp[p+1] = cnode;

    bp_node *ninode = (bp_node*)malloc(sizeof(bp_node));
    ninode->leaf = false;
    inode->len = ceil((float)fanout/2.0);
    ninode->len = fanout - inode->len;
    std::copy(ktemp,ktemp+inode->len,inode->key);
    std::copy(ntemp,ntemp+inode->len+1,inode->next);
    std::copy(ktemp+inode->len+1,ktemp+fanout+1,ninode->key);
    std::copy(ntemp+inode->len+1,ntemp+fanout+2,ninode->next);
    ninode->parent = inode->parent;
    for(int i = 0;i < ninode->len+1;i++){
        ninode->next[i]->parent = ninode;
    }
    for(int i = 0;i < inode->len+1;++i){
        inode->next[i]->parent = inode;
    }
    if (inode == root) {
        bp_node *nroot = (bp_node*)malloc(sizeof(bp_node));
        nroot->key[0] = ktemp[inode->len];
        nroot->next[0] = inode;
        nroot->next[1] = ninode;
        nroot->leaf = false;
        nroot->len = 1;
        inode->parent = nroot;
        ninode->parent = nroot;
        return nroot;
    }
    return insert_inode(ktemp[inode->len],inode->parent,ninode,root);
}

bp_node* insert_key(bp_node *root,int k,int l){
    if (!root) {
        bp_node *root = (bp_node*)malloc(sizeof(bp_node));
        root->key[0] = k;
        root->loc[0] = l;
        root->leaf = true;
        root->len = 1;
        return root;
    }
    bp_node *ptr = root;
    while(!ptr->leaf){
        for(int i = 0;i < ptr->len;i++){
            if(k < ptr->key[i]){
                ptr = ptr->next[i];
                break;
            }
            if (i == ptr->len - 1) {
                ptr = ptr->next[i + 1];
                break;
            }
        }
    }
    if(ptr->len < fanout){
        int p = 0;
        for(int i = 0;i < ptr->len;i++){
            if(k > ptr->key[i]){
                p++;
                continue;
            }
            int v = ptr->len + p - i;
            ptr->key[v] = ptr->key[v-1];
            ptr->loc[v] = ptr->loc[v-1];
        }
        ptr->len++;
        ptr->key[p] = k;
        ptr->loc[p] = l;
        ptr->next[ptr->len] = ptr->next[ptr->len - 1];
        ptr->next[ptr->len - 1] = NULL;
        return root;
    }
    int ktemp[fanout+1], ltemp[fanout+1];
    std::copy(ptr->key,(ptr->key)+fanout,ktemp);
    std::copy(ptr->loc,(ptr->loc)+fanout,ltemp);
    int p = 0;
    for(int i = 0;i < fanout;i++){
        if(k > ktemp[i]){
            p++;
            continue;
        }
        int v = fanout + p - i;
        ktemp[v] = ktemp[v-1];
        ltemp[v] = ltemp[v-1];
    }
    ktemp[p] = k;
    ltemp[p] = l;

    bp_node *nleaf = (bp_node*)malloc(sizeof(bp_node));
    nleaf->leaf = true;
    ptr->len = ceil((float)fanout/2.0);
    nleaf->len = fanout + 1 - ptr->len;
    ptr->next[ptr->len] = nleaf;
    nleaf->next[nleaf->len] = ptr->next[fanout];
    ptr->next[fanout] = NULL;
    nleaf->parent = ptr->parent;
    std::copy(ktemp,ktemp+ptr->len,ptr->key);
    std::copy(ltemp,ltemp+ptr->len,ptr->loc);
    std::copy(ktemp+ptr->len,ktemp+fanout+1,nleaf->key);
    std::copy(ltemp+ptr->len,ltemp+fanout+1,nleaf->loc);
    
    if(ptr == root){
        bp_node *nroot = (bp_node*)malloc(sizeof(bp_node));
        nroot->key[0] = nleaf->key[0];
        nroot->next[0] = ptr;
        nroot->next[1] = nleaf;
        nroot->leaf = false;
        nroot->len = 1;
        ptr->parent = nroot;
        nleaf->parent = nroot;
        return nroot;
    }
    return insert_inode(nleaf->key[0],ptr->parent,nleaf,root);
}

void bpt_cudamem(bp_node *croot, bp_node *groot,bp_node **lfs,bp_node **cfrom,bp_node **cto,int *cnum,int *c){
    if(!croot) return;
    cudaMemcpy(&groot->leaf,&croot->leaf,sizeof(bool),cudaMemcpyHostToDevice);
    cudaMemcpy(&groot->len,&croot->len,sizeof(int),cudaMemcpyHostToDevice);
    cudaMemcpy(groot->key,croot->key,fanout*sizeof(int),cudaMemcpyHostToDevice);
    if(croot->leaf){
        cudaMemcpy(groot->loc,croot->loc,fanout*sizeof(int),cudaMemcpyHostToDevice);
        lfs[c[0]] = groot;
        c[0]++;
    }
    else{
        for(int i = 0;i < croot->len+1;++i){
            bp_node *gnode;
            cudaMalloc(&gnode,sizeof(bp_node));
            cfrom[c[1]] = groot;
            cnum[c[1]] = i;
            cto[c[1]] = gnode;
            c[1]++;
            bpt_cudamem(croot->next[i],gnode,lfs,cfrom,cto,cnum,c);
        }
    }

}

__global__ void just_join_lfs(bp_node **glfs, int c){
    int id = (blockIdx.x*blockDim.x)+threadIdx.x;
    if(id >= c-1) return;
    glfs[id]->next[glfs[id]->len] = glfs[id+1];
    // printf("%d",glfs[id]->key[0]);
}

__global__ void just_join_inodes(bp_node **gfrom,int *gnum,bp_node **gto, int c){
    int id = (blockIdx.x*blockDim.x)+threadIdx.x;
    if(id >= c) return;
    gfrom[id]->next[gnum[id]] = gto[id];
}

__global__ void just_print(bp_node *groot){
    print_dfs(groot);
}

__global__ void cuda_search(int *garr,bp_node *groot,int p){
    int id = (blockIdx.x*blockDim.x)+threadIdx.x;
    if(id >= p) return;
    garr[id] = search_loc(groot,garr[id]);
}

__global__ void cuda_range_len(int *garr,int *glen,bp_node **gptr,bp_node *groot,int p){
    int id = (blockIdx.x*blockDim.x)+threadIdx.x;
    if(id >= p) return;
    bp_node *ptr = search_lnode(groot,garr[id*2]);
    gptr[id] = ptr;
    glen[id] = 0;
    while(ptr){
        // printf("In:%d.%d.%d\n",ptr->key[0],garr[id*2],garr[id*2+1]);
        if(ptr->key[0] > garr[2*id+1]) break;
        for(int i = 0;i < ptr->len;++i){
            if(ptr->key[i] >= garr[2*id] && ptr->key[i] <= garr[2*id+1]) glen[id]++;
        }
        ptr = ptr->next[ptr->len];
    }
    // printf("len:%d,%d\n",id,glen[id]);
}

__global__ void cuda_range_val(int *garr,bp_node **gptr,int **gans,int p){
    int id = (blockIdx.x*blockDim.x)+threadIdx.x;
    if(id >= p) return;
    bp_node *ptr = gptr[id];
    int c = 0;
    // printf("ptr1:%p\n",ptr);
    while(ptr){
        if(ptr->key[0] > garr[2*id+1]) break;
        for(int i = 0;i < ptr->len;++i){
            if(ptr->key[i] >= garr[2*id] && ptr->key[i] <= garr[2*id+1]){
                gans[id][c] = ptr->loc[i];
                // printf("ptr:%d\n",ptr->loc[i]);
                c++;
            }
        }
        ptr = ptr->next[ptr->len];
    }
    // printf("done:%d\n",id);
}

__global__ void cuda_update(int *garr,int *gloc,int **ggloc,int *gpu_db,bp_node *groot,int p,int m){
    int id = (blockIdx.x*blockDim.x)+threadIdx.x;
    if(id >= p) return;
    int l = search_loc(groot,garr[3*id]);
    if(l == -1) return;
    atomicAdd(&gpu_db[l*m + garr[3*id+1]-1],garr[3*id+2]);
    gloc[id] = l*m + garr[3*id+1]-1;
    ggloc[id] = &gpu_db[l*m + garr[3*id+1]-1];
}

int main(int argc,char **argv){
    cudaDeviceSetLimit(cudaLimitStackSize, 1 << 25);

    // timings
    struct timeval t1, t2;
    std::vector <double> kerneltime;

    // input reading
    FILE *inp;
    char *fname = argv[1];
    inp = fopen(fname,"r");
    if (!inp)  {
        printf("#cannot open input file");
        return 0;
    }
    bp_node *root = NULL;
    int n,m,q;
    fscanf(inp,"%d",&n);
    fscanf(inp,"%d",&m);
    int *db = (int*) malloc(n*m*sizeof(int));
    for(int i = 0;i < n;i++){
        int v;
        fscanf(inp,"%d",&v);
        root = insert_key(root,v,i);
        db[i*m] = v;
        for(int j = 1;j < m;++j){
            fscanf(inp,"%d",&db[i*m+j]);
        }
    }

    // allocate and copy gpu mem
    bp_node *groot;
    cudaMalloc(&groot,sizeof(bp_node));
    bp_node **lfs = (bp_node**)malloc(n*sizeof(bp_node*));
    bp_node **cfrom = (bp_node**)malloc(2*n*sizeof(bp_node*));
    bp_node **cto = (bp_node**)malloc(2*n*sizeof(bp_node*));
    int *cnum = (int*) malloc(2*n*sizeof(int));
    int *c = (int*) malloc(2*sizeof(int));
    c[0] = 0;c[1] = 0;
    bpt_cudamem(root,groot,lfs,cfrom,cto,cnum,c);
    cudaDeviceSynchronize();
    bp_node **glfs;
    cudaMalloc(&glfs,c[0]*sizeof(bp_node*));
    cudaMemcpy(glfs,lfs,c[0]*sizeof(bp_node*),cudaMemcpyHostToDevice);
    just_join_lfs<<<n,1>>>(glfs,c[0]);
    bp_node **gfrom,**gto;
    int *gnum;
    cudaMalloc(&gnum,c[1]*sizeof(int));
    cudaMemcpy(gnum,cnum,c[1]*sizeof(int),cudaMemcpyHostToDevice);
    cudaMalloc(&gfrom,c[1]*sizeof(bp_node*));
    cudaMemcpy(gfrom,cfrom,c[1]*sizeof(bp_node*),cudaMemcpyHostToDevice);
    cudaMalloc(&gto,c[1]*sizeof(bp_node*));
    cudaMemcpy(gto,cto,c[1]*sizeof(bp_node*),cudaMemcpyHostToDevice);
    just_join_inodes<<<2*n,1>>>(gfrom,gnum,gto,c[1]);
    cudaDeviceSynchronize();
    int *gpu_db;
    cudaMalloc(&gpu_db,n*m*sizeof(int));
    cudaMemcpy(gpu_db,db,n*m*sizeof(int),cudaMemcpyHostToDevice);

    // output file 
    char *ofname = argv[2]; 
    FILE *op;
    op = fopen(ofname,"w");

    // print_dfs(root);

    // just_print<<<1,1>>>(groot);

    // scan and implement 
    fscanf(inp,"%d",&q);
    for(int l = 0;l < q;++l){
        int t;
        fscanf(inp,"%d",&t);
        if(t == 1){
            int p;
            fscanf(inp,"%d",&p);
            int *arr = (int*) malloc(p*sizeof(int));
            int *garr;
            cudaMalloc(&garr,p*sizeof(int));
            for(int i = 0;i < p;++i){
                fscanf(inp,"%d",&arr[i]);
            }
            cudaMemcpy(garr,arr,p*sizeof(int),cudaMemcpyHostToDevice);
            gettimeofday(&t1, 0);
            cuda_search<<<p,1>>>(garr,groot,p);
            cudaDeviceSynchronize();
            gettimeofday(&t2, 0);
            cudaMemcpy(arr,garr,p*sizeof(int),cudaMemcpyDeviceToHost);
            for(int i = 0;i < p;++i){
                if(arr[i] == -1){
                    fprintf(op,"-1\n");
                    continue;
                }
                for(int j = 0;j < m;++j){
                    fprintf(op,"%d ",db[arr[i]*m+j]);
                }
                fprintf(op,"\n");
            }
        }
        else if(t == 2){
            int p;
            fscanf(inp,"%d",&p);
            int *arr = (int*) malloc(2*p*sizeof(int));
            int *garr,*glen,**gans;
            bp_node **gptr;
            cudaMalloc(&garr,2*p*sizeof(int));
            cudaMalloc(&glen,p*sizeof(int));
            cudaMalloc(&gans,p*sizeof(int*));
            cudaMalloc(&gptr,p*sizeof(bp_node*));
            int **ans = (int**)malloc(p*sizeof(int*)), **gloc = (int**)malloc(p*sizeof(int*));
            for(int i = 0;i < 2*p;++i){
                fscanf(inp,"%d",&arr[i]);
            }
            cudaMemcpy(garr,arr,2*p*sizeof(int),cudaMemcpyHostToDevice);
            cuda_range_len<<<p,1>>>(garr,glen,gptr,groot,p);
            cudaDeviceSynchronize();
            int *len = (int*)malloc(p*sizeof(int));
            cudaMemcpy(len,glen,p*sizeof(int),cudaMemcpyDeviceToHost);
            for(int i = 0;i < p;++i){
                cudaMalloc(&gloc[i],len[i]*sizeof(int));
            }
            cudaMemcpy(gans,gloc,p*sizeof(int*),cudaMemcpyHostToDevice);
            gettimeofday(&t1, 0);
            cuda_range_val<<<p,1>>>(garr,gptr,gans,p);
            cudaDeviceSynchronize();
            gettimeofday(&t2, 0);
            // printf("%p,%p,%p,%p,%p\n",glen,gloc[0],garr,len,arr);
            for(int i = 0;i < p;++i){
                if(len[i] == 0){
                    fprintf(op,"-1\n");
                    continue;
                }
                ans[i] = (int*)malloc(len[i]*sizeof(int));
                cudaMemcpy(ans[i],gloc[i],len[i]*sizeof(int),cudaMemcpyDeviceToHost);
                for(int j = 0;j < len[i];++j){
                    for(int k = 0;k < m;++k){
                        fprintf(op,"%d ",db[ans[i][j]*m+k]);
                    }
                    fprintf(op,"\n");
                }
            }
        }
        else if(t == 3){
            int p;
            fscanf(inp,"%d",&p);
            int *arr = (int*) malloc(3*p*sizeof(int));
            for(int i = 0;i < 3*p;++i){
                fscanf(inp,"%d",&arr[i]);
            }
            int *garr,*gloc,**ggloc,*loc = (int*) malloc(p*sizeof(int)),**cgloc = (int**) malloc(p*sizeof(int*));
            cudaMalloc(&garr,3*p*sizeof(int));
            cudaMalloc(&gloc,p*sizeof(int));
            cudaMalloc(&ggloc,p*sizeof(int*));
            cudaMemcpy(garr,arr,3*p*sizeof(int),cudaMemcpyHostToDevice);
            gettimeofday(&t1, 0);
            cuda_update<<<p,1>>>(garr,gloc,ggloc,gpu_db,groot,p,m);
            cudaDeviceSynchronize();
            gettimeofday(&t2, 0);
            cudaMemcpy(loc,gloc,p*sizeof(int),cudaMemcpyDeviceToHost);
            cudaMemcpy(cgloc,ggloc,p*sizeof(int*),cudaMemcpyDeviceToHost);
            for(int i = 0;i < p;++i){
                cudaMemcpy(&db[loc[i]],cgloc[i],sizeof(int),cudaMemcpyDeviceToHost);
            }
        }
        else if(t == 4){
            int trace[25] = {0};
            int k;
            fscanf(inp,"%d",&k);
            gettimeofday(&t1, 0);
            search_trace(root,k,trace);
            cudaDeviceSynchronize();
            gettimeofday(&t2, 0);
            for(int i = 0;i < 25 && trace[i] != -1;++i){
                fprintf(op,"%d ", trace[i]);
            }
            fprintf(op,"\n");
        }
        double td = (1000000.0*(t2.tv_sec-t1.tv_sec) + t2.tv_usec-t1.tv_usec)/1000.0; // Time taken by kernel in seconds 
		kerneltime.push_back(td);  
        printf("Time taken by kernel to execute is: %.6f ms\n", td);
    }
    int nall = kerneltime.size();
	double sumtime=0;
	for(int i=0;i<nall;i++){
		sumtime += kerneltime[i];
	}
	// print the time taken by all the kernels of the current test-case
    printf("total time taken by the current test-case is %.6f ms\n",sumtime);
    return 0;

    // debugging b+ tree
    // bp_node *root = insert_key(NULL,6,0);
    // print_dfs(root);
    // printf("--------------------\n");
    // root = insert_key(root,16,0);
    // print_dfs(root);
    // printf("--------------------\n");
    // root = insert_key(root,26,0);
    // print_dfs(root);
    // printf("--------------------\n");
    // root = insert_key(root,36,0);
    // print_dfs(root);
    // printf("--------------------\n");
    // root = insert_key(root,46,0);
    // print_dfs(root);
    // printf("--------------------\n");
    // root = insert_key(root,56,0);
    // print_dfs(root);
    // printf("--------------------\n");
    // root = insert_key(root,27,0);
    // print_dfs(root);
    // printf("--------------------\n");
    // root = insert_key(root,28,0);
    // print_dfs(root);
    // printf("--------------------\n");
    // root = insert_key(root,29,0);
    // print_dfs(root);
    // printf("--------------------\n");
    // root = insert_key(root,30,0);
    // print_dfs(root);
    // printf("--------------------\n");
    // root = insert_key(root,31,0);
    // print_dfs(root);
    // printf("--------------------\n");
    // root = insert_key(root,32,0);
    // cudaDeviceSetLimit(cudaLimitStackSize, 1 << 16); // might need be reduced to run on lower end systems
    // print_dfs(root);
    // printf("GPU\n");
    // bp_node *groot;
    // cudaMalloc(&groot,sizeof(bp_node));
    // bpt_cudamem(root,groot);
    // printf("memCopied\n");
    // just_print<<<1,1>>>(groot);
    // cudaDeviceSynchronize();
    // return 0;
}