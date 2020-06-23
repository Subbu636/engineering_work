#include <stdio.h>
#include <stdlib.h>
#include <pthread.h>

typedef struct __myarg_t {
  char c;
  int count;
} myarg_t;

void *work_fn(void *ptr) {
  myarg_t *msg = (myarg_t *) ptr;
  for (int i = 0; i < msg->count; i++) {
    printf("%c\n", msg->c);
  }
  return 0;
}

int main() {
  pthread_t t1, t2;
  myarg_t arg1, arg2;
  arg1.c = 'h'; arg1.count = 1000;
  arg2.c = 'w'; arg2.count = 1000;
  pthread_create(&t1, NULL, work_fn, &arg1);
  pthread_create(&t2, NULL, work_fn, &arg2);
  pthread_join(t1, NULL);
  pthread_join(t2, NULL); 
  exit(0);
}
