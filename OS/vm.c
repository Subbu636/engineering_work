int main() {
  int x = 0; 
  int *y = (int*) malloc(sizeof(int));
  x = x + *y;
  return 0;
}