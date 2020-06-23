#include <stdio.h>

// Principle : Single Responsibilty Design
// Single Units
int sum(int x, int y)  { return x + y; }

// Single Units
int sub(int x, int y)  { return x - y; }

// Single Units
int div(int x, int y)  { return x / y; }

// Single Units
int mul(int x, int y)  { return x / y; }

// Can I Say My Calculator is Polymorphic
int calculator(int x, int y, int (*operation)(int, int)) {
	return operation(x, y);
}

int main() {
	int a = 10, b = 20, c = 100;
	int result = 0;

	result = calculator(a, b, sum);
	// result = sum(a, b);
	printf("\n Result : %d", result);

	result = calculator(a, b, sub);
	printf("\n Result : %d", result);

	result = calculator(a, b, div);
	printf("\n Result : %d", result);
}
