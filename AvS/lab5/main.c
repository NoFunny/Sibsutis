#include <stdio.h>
#include <stdlib.h>
#include <sys/time.h>
#include <inttypes.h>
#include <omp.h>



double wtime()
{
	struct timeval t;
	gettimeofday(&t, NULL);
	return (double)t.tv_sec + (double)t.tv_usec * 1E-6;
}

void matrix_vector_product_omp(double *a, double *b, double *c, int m, int n, int thread) {
    #pragma omp parallel num_threads(thread)
    {
        int nthreads = omp_get_num_threads();
        int threadid = omp_get_thread_num();
        int items_per_thread = m / nthreads;
        int lb = threadid * items_per_thread;
        int ub = (threadid == nthreads - 1) ? (m - 1) : (lb + items_per_thread - 1);
        for (int i = lb; i <= ub; i++) {
            c[i] = 0.0;
            for(int j = 0; j < n; j++)
                c[i] += a[i * n + j] * b[j];
        }
    }
}


void run_parallel(int m,int n, int thread) {
    double *a, *b, *c;
// Allocate memory for 2-d array a[m, n]
    a = malloc(sizeof(*a) * m * n);
    b = malloc(sizeof(*b) * n);
    c = malloc(sizeof(*c) * m);
    for (int i = 0; i < m; i++) {
    for (int j = 0; j < n; j++)
    a[i * n + j] = i + j;
    }
    for (int j = 0; j < n; j++)
    b[j] = j;
    double t = wtime();
    matrix_vector_product_omp(a, b, c, m, n, thread);
    t = wtime() - t;
    printf("Elapsed time (parallel): %.6f sec.\n", t);
    free(a);
    free(b);
    free(c);
}


void matrix_vector_product(int *a, int *b, int *c, int m, int n) {
	for (int i = 0; i < m; i++) {
		c[i] = 0.0;
		for (int j = 0; j < n; j++)
			c[i] += a[i * n + j] * b[j];
	}
}

void run_serial(int m, int n) {
	srand(time(NULL));
	int *a, *b, *c;
	a = malloc(sizeof(*a) * m * n);
	b = malloc(sizeof(*b) * n);
	c = malloc(sizeof(*c) * m);
	for (int i = 0; i < m; i++) {
		for (int j = 0; j < n; j++)
			a[i * n + j] = rand () % 100;
		c[i] = 0;
	}
	for (int j = 0; j < n; j++)
		b[j] = rand () % 100;
	double t = wtime();
	matrix_vector_product(a, b, c, m, n);
	t = wtime() - t;
	printf("Elapsed time (serial): %.7f sec.\n", t);
	free(a);
	free(b);
	free(c);
}



int main(int argc, char **argv) {
		int m,n,thread;
		printf("Input count rows and cols\n");
		scanf("%d%d",&m,&n);
		printf("Input count threads\n");
		scanf("%d",&thread);
		#pragma omp parallel num_threads(thread)
    {
    printf("Hello it's %d Thread of %d Threads\n",omp_get_thread_num(),omp_get_num_threads());
    }
    printf("Matrix-vector product (c[m] = a[m, n] * b[n]; m = %d, n = %d)\n", m, n);
    printf("Memory used: %" PRIu64 " MiB\n", ((m * n + m + n) * sizeof(double)) >> 20);
    run_serial(m,n);
    run_parallel(m,n,thread);
    return 0;
}
