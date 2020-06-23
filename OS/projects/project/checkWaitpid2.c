#include "types.h"
#include "stat.h"
#include "user.h"


int
main(int argc, char *argv[])
{
  int pid[3];
  int i;
  int pid_ter[5];
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
  -> check for input "0" which should 
     terminate all children
  */
  waitpid(0,pid_ter);
  i = 0;
  while(pid_ter[i] != -1)
  {
    printf(1,"waited for (%d)\n",pid_ter[i]);
    i = i + 1;
  }
  if(i == 0)
  {
    printf(1,"this call did nothing\n");
  }
  exit();
}
