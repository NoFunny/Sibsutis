#include <stdio.h>
#include <stdlib.h>
#include <malloc.h>
#include <time.h>

void saxpy(int n, int mul, int x[], int y[]) {
    for (int i = 0; i < n; ++i) {
        y[i] = mul * x[i] + y[i];
    }
//    for (int i = 0; i < n; i++)
//        printf("y[%d] = %d\n", i, y[i]);
}

void dgemm(double** a, int n, int m, double** b, double** c) {
    int i,j,k;
    for(i = 0; i < m; i++)
        for(j = 0; j < m; j++)
        {
            a[i][j] = 0;
            for(k = 0; k < n; k++)
                c[i][j] += a[i][k] * b[k][j];
        }
}
int main() {
    double start, start1;
    double end,end1;
    int n,m;
    printf("Enter N and M\n");
    scanf("%d%d",&n,&m);
    double **a = (double**)malloc(n * sizeof(int*));
    double **b = (double**)malloc(n * sizeof(int*));
    double **c = (double**)malloc(n * sizeof(int*));
   // for(int i = 0; i < n; i++) {
      //  a[i] = (double *)malloc(m * sizeof(double));
      //  b[i] = (double *)malloc(m * sizeof(double));
      //  c[i] = (double *)malloc(m * sizeof(double));
   // }
  //  srand(time(NULL));
   // for(int i = 0; i < n; i++) {
    //    for(int j = 0; j < m; j++) {
     //       a[i][j] = (double)rand()/RAND_MAX*(25.0-0.01)+0.01;
      //      b[i][j] = (double)rand()/RAND_MAX*(25.0-0.01)+0.01;
       // }
   // }

    int x[n];
    int y[n];
    for(int i = 0; i < n; i++) {
        x[i] = rand()%25;
        y[i] = rand()%25;
    }
    const int mul = 5;
    start = clock();
    saxpy(n, mul, x,y);
    end = clock();
    // start1 = clock();
    // dgemm(a,n,m,b,c);
    // end1 = clock();
//    for(int i = 0; i < n; i++) {
//        for (int j = 0; j < m; j++)
//            printf("c[%d][%d] = %f\n",i,j,c[i][j]);
//    }
    printf("Time saxpy = %f\n",(end-start)/1000.0);
    // printf("Time dgemm = %f",(end1-start1)/1000.0);
    return 0;
}
