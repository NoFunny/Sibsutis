#include <stdio.h>
#include "calc.h"

double add(double a, double b) {
  return a+b;
}

double sub(double a, double b) {
  return a-b;
}

double mul(double a, double b) {
  return a*b;
}

double division(double a, double b) {
  return a/b;
}

double extent(double a, double b) {
  for(int i = 0; i < b; i++) {
    a*=a;
  }
  return a;
}

int factorial(int a){
  return (a < 2) ? 1 : a * factorial(a - 1);
}

void _help() {
  printf("OPERATIONS:\n");
  printf("(+) - сложение\n(-) - вычитание\n(*) - умножение\n(/) - деление\n(^) - возведение в степень\n(!) - факториал\n");
}
