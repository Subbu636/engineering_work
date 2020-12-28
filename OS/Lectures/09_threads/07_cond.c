#include <stdio.h> 
#include <stdlib.h>
#include <pthread.h> 

#define LEN 2
pthread_mutex_t lock;
pthread_cond_t cond;
int arr[LEN], flag=0;

void *fill() {
  printf("Enter\n");
  for(int i=0; i<LEN; i++) 
    scanf("%d",&arr[i]);
  pthread_mutex_lock(&lock); 
  pthread_cond_signal(&cond);
  pthread_mutex_unlock(&lock);
  return 0;
}

void *read() {
  pthread_mutex_lock(&lock);
  pthread_cond_wait(&cond, &lock);
  pthread_mutex_unlock(&lock);
  printf("Reading:\n");
  for(int i=0; i<LEN; i++) 
    printf("%d\n",arr[i]);
  return 0;
}

int main() {
  pthread_mutex_init(&lock, NULL);
  pthread_cond_init(&cond, NULL);
  pthread_t t1, t2;
  pthread_attr_t attr;
  pthread_create(&t1, NULL, &fill, NULL);
  pthread_create(&t2, NULL, &read, NULL);
  pthread_join(t1, NULL);
  pthread_join(t2, NULL);
  return 0;
}
