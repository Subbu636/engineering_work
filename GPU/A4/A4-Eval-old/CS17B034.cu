
#include <stdbool.h>
#include<stdio.h>
// #include <thrust/sort.h>
#include<stdlib.h>
#include <cuda.h>
using namespace std;
#define ORDER 8
int order = 8;
typedef struct node{
    int num_keys;
    bool is_leaf;
    int* keys;
    struct node* par;
    void** pointers;
} node;
typedef struct record{
    int* val;
}record;
node* currleaf;
int getLeftIndex(node *parent, node *left);
node *ins_leaf(node *leaf, int key, record *pointer);
node *ins_leaf_split(node *root, node *leaf, int key,
                   record *pointer);
node *ins_node_split(node *root, node *parent,
                   int left_index,
                   int key, node *right);
node *ins_par(node *root, node *left, int key, node *right);
node *insert(node *root, int key, int* values,int m);
int height(node* n)
{
    int h = 0;
    node* c = n;
    while(! c->is_leaf){
        c = (node*)c->pointers[0];
        h++;
    }
    return h;
}
__device__ __host__ void printLeaves(node *root, int ord) {
    if (root == NULL) {
        printf("Empty tree.\n");
        return;
    }
    for(int i=0;i<root->num_keys;i++) printf("%d ",root->keys[i]);
    printf("\n");
    int i;
    node *c = root;
    while (!c->is_leaf)
        c =(node*) c->pointers[0];
    while (true) {
        for (i = 0; i < c->num_keys; i++) {
            printf("%d ", c->keys[i]);
        }
        if (c->pointers[ord - 1] != NULL) {
            printf(" | ");
            c = (node*)c->pointers[ord - 1];
        } 
        else break;
    }
    printf("\n");
}
__global__ void pathtrace(node* n,int key, int* out)
{
    node* curr = n;
    int cnt = 0;
    while(! curr-> is_leaf)
    {
        int i = 0;
        while( i < curr->num_keys)
        {
            if(key >= curr->keys[i])
            {
                i++;
            }
            else break;
        }
        out[cnt] = curr->keys[0];
        cnt++;
        curr = (node*)curr->pointers[i];
    }
    out[cnt] =  curr->keys[0];
    cnt++;
}
__global__ void pathtracelen(node* n,int key, int* cnt)
{
    node* curr = n;
    
    while(! curr-> is_leaf)
    {
        int i = 0;
        while( i < curr->num_keys)
        {
            if(key >= curr->keys[i])
            {
                i++;
            }
            else break;
        }
        cnt[0]++;
        curr = (node*)curr->pointers[i];
    }
    cnt[0]++;
}
__global__ void search(node* n,int* keys,int m,int* out,int p)
{
    //key is present or not doesn't matter
    int id = blockIdx.x * blockDim.x + threadIdx.x;
    if(id < p){
        int key = keys[id];
        if (n== NULL)
        {
            out[m*id] = -1;
        }
        else{
            node* curr = n;
            
            while(! curr-> is_leaf)
            {
                int i = 0;
                while( i < curr->num_keys)
                {
                    if(key >= curr->keys[i])
                    {
                        i++;
                    }
                    else break;
                }
                curr = (node*)curr->pointers[i];
            }
            bool f = false;
            for(int i=0;i<curr->num_keys;i++)
            {
                if(curr->keys[i]==key)
                {
                    for(int j=0;j<m;j++)
                    {
                        out[m*id+j] = ((record*)curr->pointers[i])->val[j];
                    }
                    f = true;
                    break;
                }
            }
            if(!f)
            {
                out[m*id] = -1;
            }
        }
    }
}
__global__ void findrange(node* n,int* as, int* bs, int m, int*** ans,int p, int order)
{
    int id =  blockIdx.x * blockDim.x + threadIdx.x;
    if(id < p){
        int a = as[id];
        int b = bs[id];
        if (n== NULL)
        {
            ans[id] = NULL;
            return;
        }
        node* curr = n;
        
        while(! curr-> is_leaf)
        {
            int i = 0;
            while( i < curr->num_keys)
            {
                if(a >= curr->keys[i])
                {
                    i++;
                }
                else break;
            }
            curr = (node*)curr->pointers[i];
        }
        int i = 0;
        while(curr !=NULL)
        {
            while(i< curr->num_keys && curr->keys[i]<a) i++;
            if (i== curr->num_keys )
            {
                curr = (node*) curr->pointers[order-1];
                i=0;
            }
            else break;
        }
        if (curr== NULL)
        {
           ans[id] = NULL;
            return;
        }
        int cnt = 0;
        while(curr != NULL)
        {
            while(i < curr->num_keys && curr->keys[i] <= b)
            {
                ans[id][cnt] = ((record*)curr->pointers[i])->val;
                cnt++;
                i++;
            }
            curr = (node*) curr->pointers[order-1];
            i = 0;
        }
        if(cnt==0)
        {
            ans[id] = NULL;
            return;
        }
    }
}
__global__ void findrangelen(node* n,int* as, int* bs, int m, int* ans, int p, int order)
{
    int id =  blockIdx.x * blockDim.x + threadIdx.x;
    if(id < p){
        int a = as[id];
        int b = bs[id];
        if (n== NULL){
            ans[id] = 0;
            return;
        }
        node* curr = n;
        while(! curr-> is_leaf){
            int i = 0;
            while( i < curr->num_keys){
                if(a >= curr->keys[i])
                {
                    i++;
                }
                else break;
            }
            curr = (node*)curr->pointers[i];
        }
        int i = 0;
        while(curr !=NULL){
            while(i< curr->num_keys && curr->keys[i]<a) i++;
            if (i== curr->num_keys ){
                curr = (node*) curr->pointers[order-1];
                i=0;
            }
            else break;
        }
        if (curr== NULL){
           ans[id] = 0;
            return;
        }
        int cnt = 0;
        while(curr != NULL){
            while(i < curr->num_keys && curr->keys[i] <= b){
                cnt++;
                i++;
            }
            curr = (node*) curr->pointers[order-1];
            i = 0;
        }
        
        ans[id] = cnt;
        return;
        
    }
}
__global__ void addition(node* n, int* keys , int* ans, int* values, int p)
{
    int id =  blockIdx.x * blockDim.x + threadIdx.x;
    if (id<p){
        int key = keys[id];
        int an = ans[id];
        int value = values[id]; 
        if (n== NULL)
        {
            return;
        }
        node* curr = n;
        
        while(! curr-> is_leaf)
        {
            int i = 0;
            while( i < curr->num_keys)
            {
                if(key >= curr->keys[i])
                {
                    i++;
                }
                else break;
            }
            curr = (node*)curr->pointers[i];
        }
        for(int i=0;i<curr->num_keys;i++)
        {
            if(curr->keys[i]==key)
            {
                atomicAdd(  &(((record*)curr->pointers[i])->val[an-1]), value);
                return;
            }
        }
    }
    
}
record* mkrecord(int* val, int m)
{
    record* r = (record*) malloc(sizeof(record));
    r->val = (int*) malloc(m*sizeof(int));
    for(int i=0;i<m;i++) r->val[i] = val[i];
    return r;
}
node *mknode(void) {
  node *new_node;
  new_node = (node*)malloc(sizeof(node));
  new_node->keys = (int*)malloc((order - 1) * sizeof(int));
  new_node->pointers = (void**) malloc(order * sizeof(void *));
  new_node->is_leaf = false;
  new_node->num_keys = 0;
  new_node->par = NULL;
  return new_node;
}
node *mkleaf(void) {
  node* leaf = mknode();
  leaf->is_leaf = true;
  return leaf;
}
node *mkroot(int key, int* val, int m) {
  node *root = mkleaf();
  root->keys[0] = key;
  record* r = mkrecord(val,m);
  root->pointers[0] = r;
  root->pointers[order - 1] = NULL;
  root->par = NULL;
  root->num_keys++;
  return root;
}

int getLeftIndex(node *parent, node *left) {
  int left_index = 0;
  while (left_index <= parent->num_keys &&
       parent->pointers[left_index] != left)
    left_index++;
  return left_index;
}

node *ins_leaf_split(node* n,node* l,int key,record* r)
{
    node* nl = mkleaf();
    int* temp_keys = (int*)malloc(order * sizeof(int));
    void** temp_pointers = (void**)malloc(order * sizeof(void *));
    int k = 0;
    while (k < order - 1 && l->keys[k] < key) k++;
    int i,j;
  for (i = 0, j = 0; i < l->num_keys; i++, j++) {
    if (j == k)
      j++;
    temp_keys[j] = l->keys[i];
    temp_pointers[j] = l->pointers[i];
  }

  temp_keys[k] = key;
  temp_pointers[k] = r;

  l->num_keys = 0;
   int split = 0;
  if ((order - 1)%2==0)
  {
      split = (order-1)/2;
  }
  else split = (order-1)/2 + 1;

  for (i = 0; i < split; i++) {
    l->pointers[i] = temp_pointers[i];
    l->keys[i] = temp_keys[i];
    l->num_keys++;
  }

  for (i = split, j = 0; i < order; i++, j++) {
    nl->pointers[j] = temp_pointers[i];
    nl->keys[j] = temp_keys[i];
    nl->num_keys++;
  }

  free(temp_pointers);
  free(temp_keys);

  nl->pointers[order - 1] = l->pointers[order - 1];
  l->pointers[order - 1] = nl;

  for (i = l->num_keys; i < order - 1; i++)
    l->pointers[i] = NULL;
  for (i = nl->num_keys; i < order - 1; i++)
    nl->pointers[i] = NULL;

  nl->par = l->par;

  return ins_par(n, l, nl->keys[0], nl);

}

node *ins_node_split(node *root, node *old_node, int left_index,
                   int key, node *right) {
  int i, j, split;
  node *new_node, *child;
  int *temp_keys;
  node **temp_pointers;

  temp_pointers = (node**) malloc((order + 1) * sizeof(node *));
  temp_keys = (int*)malloc(order * sizeof(int));
  for (i = 0, j = 0; i < old_node->num_keys + 1; i++, j++) {
    if (j == left_index + 1)
      j++;
    temp_pointers[j] = (node*) old_node->pointers[i];
  }

  for (i = 0, j = 0; i < old_node->num_keys; i++, j++) {
    if (j == left_index)
      j++;
    temp_keys[j] = old_node->keys[i];
  }

  temp_pointers[left_index + 1] = right;
  temp_keys[left_index] = key;

  if ((order-1)%2==0)
  {
      split = (order-1)/2;
  }
  else split = (order-1)/2 + 1;

  new_node = mknode();
  old_node->num_keys = 0;
  for (i = 0; i < split; i++) {
    old_node->pointers[i] = temp_pointers[i];
    old_node->keys[i] = temp_keys[i];
    old_node->num_keys++;
  }
  old_node->pointers[i] = temp_pointers[i];
  int spkey = temp_keys[split];
  for (++i, j = 0; i < order; i++, j++) {
    new_node->pointers[j] = temp_pointers[i];
    new_node->keys[j] = temp_keys[i];
    new_node->num_keys++;
  }
  new_node->pointers[j] = temp_pointers[i];

  free(temp_pointers);
  free(temp_keys);

  new_node->par = old_node->par;
  for (i = 0; i <= new_node->num_keys; i++) {
    child = (node*) new_node->pointers[i];
    child->par = new_node;
  }

  return ins_par(root, old_node, spkey, new_node);
}

node *ins_par(node *root, node *left, int key, node *right) {
  node *parent;

  parent = left->par;

  if (parent == NULL){
    node *r = mknode();
    r->keys[0] = key;
    r->pointers[0] = left;
    r->pointers[1] = right;
    r->num_keys++;
    r->par = NULL;
    left->par = r;
    right->par = r;
    return r;
  }

  int li = getLeftIndex(parent, left);

  if (parent->num_keys < order - 1){
      int i;
        for (i = parent->num_keys; i > li; i--) {
            parent->pointers[i + 1] = parent->pointers[i];
            parent->keys[i] = parent->keys[i - 1];
        }
        parent->pointers[li + 1] = right;
        parent->keys[li] = key;
        parent->num_keys++; 
        return root;
   }

  return ins_node_split(root, parent, li, key, right);
}
node *insert(node *n, int key, int* val, int m)
{
    if (n== NULL)
    {
        return mkroot(key, val , m);
    }
        
        node* curr = n;
        while(! curr-> is_leaf)
        {
            int i = 0;
            while( i < curr->num_keys)
            {
                if(key >= curr->keys[i])
                {
                    i++;
                }
                else break;
            }
            curr = (node*)curr->pointers[i];
        }
        for(int i=0;i<curr->num_keys;i++)
        {
            if(curr->keys[i]==key)
            {
                for(int j=0;j<m;j++)
                {
                    ((record*)curr->pointers[i])->val[j] = val[j];
                }
                return n;
            }
        }
        record* r = mkrecord(val,m);
        // curr leaf found
        if(curr->num_keys < order-1)
        {
            // printf("Leaf Insertion\n");
            int j = 0;
            while(j< curr->num_keys && curr->keys[j]<key) j++;
            int num = curr->num_keys;
            for(int i=num;i>j;i--)
            {
                curr->keys[i] = curr->keys[i-1];
                curr->pointers[i] = curr->pointers[i-1];
            }
            curr->keys[j] = key;
            curr->pointers[j] = r;
            curr->num_keys++;
            // return l;
            // curr = ins_leaf(curr,key,r);
            return n;
        }
        // printf("Leaf Split insertion\n");
        return ins_leaf_split(n,curr,key,r);
}
record* copy_rec(record* r,int  m)
{
     record* gr;
    cudaMalloc(&gr, sizeof(record) ) ;
         int* gval;
        cudaMalloc(&(gval) , m*sizeof(int) );
        record* gt;
        gt = (record*)malloc(sizeof(record));
        cudaMemcpy(gval,r->val,m*sizeof(int),cudaMemcpyHostToDevice);
     gt->val = gval;
    cudaMemcpy(gr,gt,sizeof(record),cudaMemcpyHostToDevice);
    return gr;
}
node* copyleaf(node* root,node* par,int m)
{
    if (root==NULL) return NULL;
     node* gtree = mknode();
    // int num_keys;
    // bool is_leaf;
    // int* keys;
    // struct node* par;
    // void** pointers;
    // cudaMemcpy(gtree->num_keys,root->num_keys,sizeof(int),cudaMemcpyHosttoDevice);
    // cudaMemcpy(gtree->is_leaf,root->is_leaf,sizeof(bool),cudaMemcpyHosttoDevice);
    int* keys;
    void** pointers;
    cudaMalloc(&keys, (order-1)*sizeof(int));
    cudaMalloc(&pointers, (order)*sizeof(void*));
    cudaMemcpy(keys,root->keys,(order-1)*sizeof(int),cudaMemcpyHostToDevice);
    gtree->keys = keys;
    gtree->num_keys = root->num_keys;
    gtree->is_leaf = root->is_leaf;
    gtree->par = par;
    node* gputree;
    cudaMalloc(&gputree,sizeof(node));
    void** gpointers;
    gpointers = (void**) malloc((order)*sizeof(void*));
    for(int i=0;i<root->num_keys;i++)
    {
            gpointers[i] = copy_rec((record*)root->pointers[i],m);
    } 
    for(int i=root->num_keys;i<order;i++) gpointers[i] = NULL;
    cudaMemcpy(pointers,gpointers,(order)*sizeof(void*),cudaMemcpyHostToDevice);
    gtree->pointers = pointers;
    cudaMemcpy(gputree,gtree,sizeof(node),cudaMemcpyHostToDevice);
    return gputree;
}
__global__ void helper(node* l1,node* lnext,int order)
{
    l1->pointers[order-1] = lnext;
}
node *copytree(node* root, node* par,int m)
{
    // printf("copying %d node\n",root->keys[0]);
    if (root == NULL) return NULL;
    if(root->is_leaf)
    {
        node* l =  copyleaf(root,par,m);
        if(currleaf!=NULL){
            helper<<<1,1>>>(currleaf,l,order);
            cudaDeviceSynchronize();
        }
        currleaf = l;
        return l;
    }
    node* gtree = mknode();
    // int num_keys;
    // bool is_leaf;
    // int* keys;
    // struct node* par;
    // void** pointers;
    // cudaMemcpy(gtree->num_keys,root->num_keys,sizeof(int),cudaMemcpyHosttoDevice);
    // cudaMemcpy(gtree->is_leaf,root->is_leaf,sizeof(bool),cudaMemcpyHosttoDevice);
    int* keys;
    void** pointers;
    cudaMalloc(&keys, (order-1)*sizeof(int));
    cudaMalloc(&pointers, (order)*sizeof(void*));
    cudaMemcpy(keys,root->keys,(order-1)*sizeof(int),cudaMemcpyHostToDevice);
    gtree->keys = keys;
    gtree->num_keys = root->num_keys;
    gtree->is_leaf = root->is_leaf;
    gtree->par = par;
    node* gputree;
    cudaMalloc(&gputree,sizeof(node));
    void** gpointers;
    gpointers = (void**) malloc((order)*sizeof(void*));
    for(int i=0;i<root->num_keys + 1;i++)
    {
            node* cpnode = copytree((node*)root->pointers[i],gputree,m);
            gpointers[i] = cpnode;
    }
    cudaMemcpy(pointers,gpointers,(order)*sizeof(void*),cudaMemcpyHostToDevice);
    gtree->pointers = pointers;
    cudaMemcpy(gputree,gtree,sizeof(node),cudaMemcpyHostToDevice);
    return gputree;
}
__global__ void printGPULeaves(node* root,int order){
    // printf("Entered GPU\n");
    printLeaves(root,order);
}
__global__ void printk(int* val,int m)
{
    for(int i=0;i<m;i++) printf("%d ",val[i]);
    printf("\n");
}
int main(int argc,char **argv){

    //variable declarations
    int n,m;
    
    //Input file pointer declaration
    FILE *inputfilepointer;
    
    //File Opening for read
    char *inputfilename = argv[1];
    inputfilepointer    = fopen( inputfilename , "r");
    char *outputfilename = argv[2]; 
    FILE *outputfilepointer;
    outputfilepointer = fopen( outputfilename , "w");
    //Checking if file ptr is NULL
    if ( inputfilepointer == NULL )  {
        printf( "input.txt file failed to open." );
        return 0;
    }
    if( outputfilepointer == NULL) {
        printf(" output.txt file failed to open. ");
        return 0;
    }
    
    fscanf( inputfilepointer, "%d", &n );      //scaning for number of vehicles
    fscanf( inputfilepointer, "%d", &m );      //scaning for number of toll tax zones
    int val[n][m];
    for(int i=0;i<n;i++){
        for(int j=0;j<m;j++){
            fscanf( inputfilepointer, "%d", &val[i][j] );
        }
    }
    node* root;
    node* groot;
    root = NULL;
    groot = NULL;
    for(int i=0;i<n;i++){
        root = insert(root,val[i][0],val[i],m);
    }
    currleaf = NULL;
    groot = copytree(root,NULL,m);
    
    // printLeaves(root,order);
    // printf("GPU Leaves\n");
    // printGPULeaves<<<1,1>>>(groot,order);
    // cudaDeviceSynchronize();
    
    int q;
    fscanf( inputfilepointer, "%d", &q );
    for(int i=0;i<q;i++)
    {
            int op;
            fscanf(inputfilepointer,"%d",&op);
            // printf("operation = %d\n",op);
            if(op==1)
            {
                int num_op;
                fscanf(inputfilepointer,"%d",&num_op);
                int* keys = (int*) malloc(num_op*sizeof(int));
                int* gkeys;
                cudaMalloc(&gkeys,num_op*sizeof(int));
                int* out;
                cudaMalloc(&out,num_op*m*sizeof(int));
                int* pout;
                pout = (int*)malloc(num_op*m*sizeof(int));
                for(int j=0;j<num_op;j++)
                {
                    int k;
                    fscanf(inputfilepointer,"%d",&k);
                    keys[j] = k;
                }
                cudaMemcpy(gkeys,keys,num_op*sizeof(int),cudaMemcpyHostToDevice);
                int a = num_op/1024;
                a+=1;
                search<<<a,1024>>>(groot,gkeys,m,out,num_op);
                
                cudaDeviceSynchronize();
                cudaMemcpy(pout,out,num_op*m*sizeof(int),cudaMemcpyDeviceToHost);
                for(int j=0;j<num_op;j++){
                    if(pout[j*m]!=-1){
                        for(int k=0;k<m;k++){
                            fprintf(outputfilepointer, "%d ",pout[j*m+k]);
                        }
                        fprintf(outputfilepointer,"\n");
                    }
                    else fprintf(outputfilepointer,"-1\n");
                }
            }
            if(op==2)
            {
                int num_op;
                fscanf(inputfilepointer,"%d",&num_op);
                int* as = (int*) malloc(num_op*sizeof(int));
                int* gas;
                cudaMalloc(&gas,num_op*sizeof(int));
                int* bs = (int*) malloc(num_op*sizeof(int));
                int* gbs;
                cudaMalloc(&gbs,num_op*sizeof(int));
                for(int j=0;j<num_op;j++)
                {
                    int a,b;
                    fscanf(inputfilepointer,"%d",&a);
                    fscanf(inputfilepointer,"%d",&b);
                    as[j] = a;
                    bs[j] = b;
                }
                int a = num_op/1024;
                a+=1;
                cudaMemcpy(gas,as,num_op*sizeof(int),cudaMemcpyHostToDevice);
                cudaMemcpy(gbs,bs,num_op*sizeof(int),cudaMemcpyHostToDevice);
                 int* lens = (int*) malloc(num_op*sizeof(int));
                int* glens;
                cudaMalloc(&glens,num_op*sizeof(int));
                findrangelen<<<a,1024>>>(groot,gas,gbs,m,glens,num_op,order);
                cudaDeviceSynchronize();
                cudaMemcpy(lens,glens,num_op*sizeof(int),cudaMemcpyDeviceToHost);
                int*** recs = (int***) malloc(num_op*sizeof(int**));
                int*** grecs;
                cudaMalloc(&grecs,num_op*sizeof(int**));
                for(int j=0;j<num_op;j++)
                {
                    cudaMalloc(&recs[j],lens[j]*sizeof(int*));
                }
                cudaMemcpy(grecs,recs,num_op*sizeof(int**),cudaMemcpyHostToDevice);
                findrange<<<a,1024>>>(groot,gas,gbs,m,grecs,num_op,order);
                cudaDeviceSynchronize();

                 for(int j=0;j<num_op;j++)
                {
                    if(lens[j]==0)
                    {
                        fprintf(outputfilepointer,"-1\n");
                        continue;
                    }
                    // printf("%d\n",j);
                    int** rec = (int**) malloc(lens[j]*sizeof(int*));
                    cudaMemcpy(rec,recs[j],lens[j]*sizeof(int*),cudaMemcpyDeviceToHost);
                    // printk<<<1,1>>>(rec[0],m);
                    // cpkernel<<<1,1>>>(gvals,grecs,j,lens[j]);
                    // cudaDeviceSynchronize();
                    // cudaMemcpy(vals,rec[k],m*sizeof(int),cudaMemcpyDeviceToHost);
                    for(int k=0;k<lens[j];k++)
                    {
                        int* vals = (int*) malloc(m*sizeof(int));
                        cudaMemcpy(vals,rec[k],m*sizeof(int),cudaMemcpyDeviceToHost);
                        for(int l=0;l<m;l++)
                        {
                             fprintf(outputfilepointer, "%d ", vals[l]);
                        }
                        fprintf(outputfilepointer,"\n");
                        free(vals);
                    }
                    free(rec);
                }
            }
            if(op==3)
            {
                int num_op;
                fscanf(inputfilepointer,"%d",&num_op);
                int* keys = (int*) malloc(num_op*sizeof(int));
                int* gkeys;
                cudaMalloc(&gkeys,num_op*sizeof(int));
                int* ans = (int*) malloc(num_op*sizeof(int));
                int* gans;
                cudaMalloc(&gans,num_op*sizeof(int));
                int* incs = (int*) malloc(num_op*sizeof(int));
                int* gincs;
                cudaMalloc(&gincs,num_op*sizeof(int));
                for(int j=0;j<num_op;j++)
                {
                    int k,an,inc;
                    fscanf(inputfilepointer,"%d",&k);
                    fscanf(inputfilepointer,"%d",&an);
                    fscanf(inputfilepointer,"%d",&inc);
                    keys[j] = k;
                    ans[j] = an;
                    incs[j] = inc;
                }
                cudaMemcpy(gkeys,keys,num_op*sizeof(int),cudaMemcpyHostToDevice);
                cudaMemcpy(gans,ans,num_op*sizeof(int),cudaMemcpyHostToDevice);
                cudaMemcpy(gincs,incs,num_op*sizeof(int),cudaMemcpyHostToDevice);
                int a = num_op/1024;
                a+=1;
                addition<<<a,1024>>>(groot,gkeys,gans,gincs,num_op);
                cudaDeviceSynchronize();
            }
            if(op==4)
            {
                int k;
                fscanf(inputfilepointer,"%d",&k);
                int* len =(int*) malloc(sizeof(int));
                len[0] = 0;
                int* glen;
                cudaMalloc(&glen,sizeof(int));
                cudaMemcpy(glen,len,sizeof(int),cudaMemcpyHostToDevice);
                pathtracelen<<<1,1>>>(groot,k,glen);
                cudaDeviceSynchronize();
                cudaMemcpy(len,glen,sizeof(int),cudaMemcpyDeviceToHost);
                int* gout;
                int* out = (int*) malloc(len[0]*sizeof(int));
                cudaMalloc(&gout,len[0]*sizeof(int));
                pathtrace<<<1,1>>>(groot,k,gout);
                cudaDeviceSynchronize();
                cudaMemcpy(out,gout,len[0]*sizeof(int),cudaMemcpyDeviceToHost);
                for(int i=0;i<len[0];i++)
                {
                    fprintf(outputfilepointer,"%d ",out[i]);
                }
                fprintf(outputfilepointer,"\n");
            }
            
    }
    fclose( outputfilepointer );
    fclose( inputfilepointer );
    return 0;
}