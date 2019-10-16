#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include "calc.h"

void calc(double a, double b, int c, char operation);

int main() {
  double a,b;
  int c;// для факториала;
  char operation;
  char help[4];
  printf("write /help for a hint\n");
  scanf("%s",help);
  if(strstr(help, "/help")) {
    _help();
    // printf("+++\n");
  }
  printf("Enter operation: ");
  scanf("%s", &operation);
  printf("Enter operands: ");
  scanf("%lf%lf", &a,&b);
  printf("Enter a for factorial");
  scanf("%d", &c);
  calc(a, b, c, operation);
}

void calc(double a, double b, int c, char operation) {
  double result;
  char flag[] = "no";
    while(strstr(flag, "no")) {
      switch(operation) {
        case '+':
          // result = a + b;
          result = add(a,b);
          break;
        case '-':
            // result = a - b;
            result = sub(a,b);
            break;
        case '*':
              result = mul(a,b);
              break;
        case '/':
              result = division(a,b);
              break;
        // case '%':
        //       result = div_rem(a,b);
        //       break;
        case '^':
              result = extent(a,b);
              break;
        case '!':
              result = factorial(c);
              break;
        default:
                printf("Incorrect operation, repeat...\n");
                printf("Enter operation: ");
                scanf("%s", &operation);
                calc(a,b, c, operation);
      }
      printf("Result Calculate: %lf\n", result);
      if(strstr(flag, "yes"))
        exit(1);
      printf("To finish?yes/no: ");
      scanf("%s", flag);
      printf("flag = %s\n", flag);
      if(strstr(flag, "no")) {
        printf("Enter operation: ");
        scanf("%s", &operation);
        printf("Enter operands: ");
        scanf("%lf%lf", &a,&b);
        calc(a, b, c, operation);
      }
      // if(strstr())
    }
}
