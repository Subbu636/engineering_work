
#include <stdio.h>

int sum(int x, int y)  { return x + y; }

int sum3(int x, int y, int z)  { return x + y + z; }

int main() {
	int a = 10, b = 20, c = 100;
	int result = 0;

	int (*fptr) (int, int);
	fptr = sum;

	result = fptr(a, b);
	printf("\n Result : %d", result);

	fptr = sum3;
	result = fptr(a, b, c);
	printf("\n Result : %d", result);
}
