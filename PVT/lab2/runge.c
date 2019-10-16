#include <stdio.h>
#include <mpi.h>
#include <stdlib.h>
#include <math.h>


double func(double x) {
    return pow(x,4)/(0.5*(pow(x,2))+x+6);
}

int main(int argc, char **argv) {

    const double eps = 1E-6;
    const double a = 0.4;
    const double b = 1.5;
    const int n0 = 100;
    int commsize, rank;
    MPI_Init(&argc, &argv);
    MPI_Comm_size(MPI_COMM_WORLD, &commsize);
    MPI_Comm_rank(MPI_COMM_WORLD, &rank);
    int n = n0, k;
    double sq[2], delta = 1;
    for (k = 0; delta > eps; n *= 2, k ^= 1) {
        int points_per_proc = n / commsize;
        int lb = rank * points_per_proc;
        int ub = (rank == commsize - 1) ? (n - 1) : (lb + points_per_proc - 1);
        double h = (b - a) / n;

        double s = 0.0;
        for (int i = lb; i <= ub; i++)
            s += func(a + h * (i + 0.5));
        MPI_Allreduce(&s, &sq[k], 1, MPI_DOUBLE, MPI_SUM, MPI_COMM_WORLD);
        sq[k] *= h;
        if (n > n0)
            delta = fabs(sq[k] - sq[k ^ 1]) / 3.0;
	if(rank == 0)
	    printf("S= %f",s);
    }

    if (rank == 0) {
        printf("Result Pi: %.12f; Runge rule: EPS %e, n %d\n",sq[k] * sq[k], eps, n / 2);
    }

    MPI_Finalize();
    return 0;
}
