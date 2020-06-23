/*
This program accepts a commandline input of number of sectors (512 bytes)
the max sectors available for a file is 12 direct + 128 indirect is 140
so for any input greater than 140 it cant allocate any memory
so it just allocates 140 and quits there by depicting max size 512*140
= 71,680 bytes.

CAUTION: big allocations takes time so one should wait until final output

*/
#include "types.h"
#include "stat.h"
#include "user.h"
#include "fcntl.h"


int
main(int argc, char *argv[])
{
  int fd;
  if(argc != 2)
  {
    printf(1, "inputs reqd.\n");
    exit();
  }
  int num_sectors = atoi(argv[1]);
  char buf[512];
  fd = open("new_file", O_CREATE | O_RDWR);
  if(fd >= 0) 
  {
      printf(1, "created\n");
  } 
  else 
  {
      printf(1, "error\n");
      exit();
  }
  int i = 0;
  while(i < num_sectors)
  {
    int var = write(fd, buf, 512);
    printf(1,"x");
    if(var <= 0)
    {
      // reaches here if it cant allocate anymore
      printf(1,"\nmemory limit exceeded\n");
      break;
    }
    i++;
  }
  printf(1, "done sectors %d\n",i);
  close(fd);
  exit();
}
