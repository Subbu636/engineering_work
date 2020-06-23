#include "types.h"
#include "stat.h"
#include "user.h"

int
main(int argc, char *argv[])
{
	printf(1,"(%d)hello",getpid());
	int p = fork();
	if(p == 0)
	{
		printf(1,"(%d)world",getpid());
		exit();
	}
	wait();
  exit();
}
