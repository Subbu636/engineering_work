#include <stdio.h>

int sum(int x, int y) { 
	int result = 0;
	result = x + y;
	return result;
}

int main() {
	int a = 10, b = 20;
	
	scanf("%d %d", &a, &b);
	printf("\n Result Is: %d", sum( a, b ));

	return 0;
}

