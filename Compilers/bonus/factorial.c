#include<stdio.h>


struct Foo
{
int* x;
double y;
};

int main()
{
    int x;
    int y;
    x = 1;
    y = x;
    printf("%d,%d\n",x,y);
    return 0;
}