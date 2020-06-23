#include "types.h"
#include "stat.h"
#include "user.h"


int
main(int argc, char *argv[])
{
  int pid[3];
  int i;
  int pid_ter1[5];
  int pid_ter2[5];
  // forking and creating 3 children
  // each will sleep for 5 and terminate
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
   -> The second argument is array of pids terminated 
      by call (LEVEL 3) ending with -1
   -> Below is check for terminating certain pid
  */
  printf(1,"call1\n");
  waitpid(pid[2],pid_ter2);
  i = 0;
  while(pid_ter2[i] != -1)
  {
  	printf(1,"waited for (%d)\n",pid_ter2[i]);
  	i = i + 1;
  }
  if(i == 0)
  {
  	printf(1,"this call did nothing\n");
  }
  /*
  -> Below is check for input that is not 
     pid of child of this process
  */
  printf(1,"call2\n");
  waitpid(100,pid_ter1);
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
