CC=gcc
CFLAGS1= -I 
CFLAGS2= -g

all: prox

prox: HTTP-proxy.c
	$(CC) $(CFLAGS1) ./lib -c proxy_parse.c HTTP-proxy.c
	$(CC) $(CFLAGS2) -o prox proxy_parse.o HTTP-proxy.o
# 	$(CC) $(CFLAGS) -o proxy proxy_parse.o proxy.o

clean:
	rm -f proxy *.o HTTP-proxy.o

# tar:
# 	tar -cvzf cos461_ass1_$(USER).tgz proxy.c README Makefile proxy_parse.c proxy_parse.h
