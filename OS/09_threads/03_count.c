#include <stdio.h>
#include <stdlib.h>
#include <pthread.h>

int counter = 0;

void *work_fn() {
  for (int i = 0; i < 10000; i++) {
    counter++; 
  }
  printf("%d\n", counter);
  return 0;
}

int main() {
  pthread_t t1, t2;
  pthread_create(&t1, NULL, work_fn, NULL);
  pthread_create(&t2, NULL, work_fn, NULL);
  pthread_join(t1, NULL);
  pthread_join(t2, NULL); 
  printf("In main: %d\n", counter);
  exit(0);
}
