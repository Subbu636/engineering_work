CC := gcc
CFLAGS := -pthread -O0

SRCS := $(wildcard *.c)
PROGS := $(patsubst %.c,%,$(SRCS))

08_sem : CFLAGS+=-lrt
03_count : CFLAGS+=-g
01_create : CFLAGS+=-g

all : $(PROGS)

%: %.c
	$(CC) $(CFLAGS) -o $@ $<

clean :
	-rm *.o $(PROGS) 
