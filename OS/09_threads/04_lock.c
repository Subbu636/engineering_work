#include <stdio.h>
#include <stdlib.h>
#include <pthread.h>

int counter = 0;
pthread_mutex_t lock;

void *work_fn() {
  pthread_mutex_lock(&lock);
  for (int i = 0; i < 10000; i++) {
    counter++; 
  }
  pthread_mutex_unlock(&lock);
  printf("%d\n", counter);
  return 0;
}

int main() {
  pthread_mutex_init(&lock, NULL);
  pthread_t t1, t2;
  pthread_create(&t1, NULL, work_fn, NULL);
  pthread_create(&t2, NULL, work_fn, NULL);
  pthread_join(t1, NULL);
  pthread_join(t2, NULL); 
  printf("In main: %d\n", counter);
  pthread_mutex_destroy(&lock);
  exit(0);
}
