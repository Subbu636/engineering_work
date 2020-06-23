#include "types.h"
#include "stat.h"
#include "user.h"


int
main(int argc, char *argv[])
{
  int pid[3];
  int i;
  int pid_ter1[5];
  for(i = 1;i <= 3;i++)
  {
    pid[i-1] = fork();
    if(pid[i-1] == 0)
    {
      sleep(5);
      printf(1,"(%d)is done\n",getpid());
      exit();
    }
  }
  /*
  -> check for input "-1" which should 
     terminate a random child
  -> like wait()
  */
  printf(1,"call1\n");
  waitpid(-1,pid_ter1);
  i = 0;
  while(pid_ter1[i] != -1)
  {
    printf(1,"waited for (%d)\n",pid_ter1[i]);
    i = i + 1;
  }
  if(i == 0)
  {
    printf(1,"this call did nothing\n");
  }
  exit();
}
