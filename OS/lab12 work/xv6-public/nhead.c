#include "types.h"
#include "stat.h"
#include "user.h"

int
main(int argc, char *argv[])
{
    char buff1[512];
    //gets(buff,512);
    int fd,count = 0,n,max = 10;
    if((fd = open(argv[1], 0)) < 0){
      printf(1, "head: cannot open %s\n", argv[1]);
      exit();
    }
    if(argc == 3)
    {
        max = atoi(argv[2]);
    }
    //printf(1,"%s\n", buff);
    while((n = read(fd, buff1, sizeof(buff1))) > 0) {
        int i;
     for(i = 0;i < sizeof(buff1);i++)
     {
        if(buff1[i] == '\n')
        {
            count++;
        }
        if(count < max)
        {
            printf(1,"%c",buff1[i]);
        }
        else
        {
            break;
        }
     }
 }
     printf(1,"\n");
    if(n < 0){
    printf(1, "head: read error\n");
    exit();
  }
    close(fd);
    exit();
}