#include <stdio.h>
#include <stdlib.h>
#include <pthread.h>

typedef struct __myret_t {
  int h;
  int b;
} myret_t;

void *find_pythogorean_triplet(int a) {
  myret_t *rv;
  if (a%2 == 1) {
    rv->b = (a*a - 1) / 2;
    rv->h = rv->b + 1;
  } else {
    rv->b = (a*a - 2) / 2;
    rv->h = rv->b + 2;
  }
  return (void *) rv;
}

int main(int argc, char **argv) {
  pthread_t t;
  int a = atoi(argv[1]);
  myret_t *rv_;
  pthread_create(&t, NULL, find_pythogorean_triplet, a);
  pthread_join(t, (void **) &rv_);
  printf("a = %d, b = %d, h = %d\n", a, rv_->b, rv_->h);
  exit(0);
}
