#include <stdio.h>
#include <stdlib.h>

int sum (int x, int y){
	int result = x+y;
	if (x > 0 && y > 0 && result < x){
		printf("Cant calc\n");
	}
	if (x < 0 && y < 0 && result > x){
		printf("Cant calc\n");
	}
	return result;
}

int* ppptr(int *ptr){
	int b = 80;
	ptr = &b;
	b += 100;
	return ptr;
}

int main(){
	int *ptr;
	int a = 90;
	ptr = &a;
	ptr = ppptr(ptr);
	printf("%d\n",*ptr);
}
