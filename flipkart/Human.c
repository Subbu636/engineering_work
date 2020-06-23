#include <stdio.h>

// class Human 
typedef struct human_type {
	int id;
	char name[100];

	// This Management
	void (*dance)();
	// void doBhangra() {
	// 	printf("\nBalleeee Balleeee... Oye Hoyee!!!\n");
	// }
} Human;

// This is Outside
// Binded With Human Type
void doBhangra(Object this ) { // In C++/Java Imlicitly
	printf("\nBalleeee Balleeee... Oye Hoyee!!!\n");
}

int main() {
								 // Initliase
	Human h1 = { 10, "Ram Singh", doBhangra }; // Constructor Call
	Human h2 = { 11, "Shyam Singh", doBhangra }; // Constructor Call

	printf("\nHuman : %d, %s\n", h.id, h.name ); // State Directly
	
	// C++ | Java
	h.dance();
	
	// Message Passing : h recieves message dance
	// dance message getting mapped to method doBhangra()
}
