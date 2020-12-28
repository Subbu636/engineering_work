#include <stdio.h>
#include <stdlib.h>
#include <pthread.h>

void *work_fn(void *ptr) {
  char *msg;
  msg = (char *) ptr;
  printf("%s\n", msg);
  return 0;
}

int main() {
  pthread_t t1, t2;
  char *msg1 = "hello";
  char *msg2 = "world";
  int rv1, rv2;
  rv1 = pthread_create(&t1, NULL, work_fn, (void*) msg1);
  rv2 = pthread_create(&t2, NULL, work_fn, (void*) msg2);
  printf("Thread 1 create returns: %d\n",rv1);
  printf("Thread 2 create returns: %d\n",rv2);
  rv1 = pthread_join(t1, NULL);
  rv2 = pthread_join(t2, NULL); 
  printf("Thread 1 join returns: %d\n",rv1);
  printf("Thread 2 join returns: %d\n",rv2);
  exit(0);
}
