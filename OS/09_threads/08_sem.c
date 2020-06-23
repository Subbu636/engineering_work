#include <stdio.h> 
#include <pthread.h> 
#include <semaphore.h> 
#include <unistd.h> 
  
sem_t my_sem; 
void *work(int tid) 
{
  sem_wait(&my_sem); 
  printf("%d started\n", tid); 
  sleep(2); 
  printf("%d ended\n", tid); 
  sem_post(&my_sem); 
  return 0;
} 
int main() 
{ 
  sem_init(&my_sem, 0, 1); 
  pthread_t t[5]; 
  for (int i = 0; i < 5; i++) 
    pthread_create(&t[i], NULL, work, i);
  for (int i = 0; i < 5; i++) { 
    pthread_join(t[i], NULL);
    printf("%d joined\n", i);
  }
  sem_destroy(&my_sem); 
  return 0; 
}
