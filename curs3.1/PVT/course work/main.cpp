#include <iostream>
#include <stdlib.h>
#include <math.h>
#include "mpi.h"

using namespace std;

const double error = 0.001;
const int n = 100;
double a[n*n];
double b[n];
double x_old[n];
double x_new[n];

void init_matr(int n) {

	for (int i = 0; i < n; ++i) {

		for (int j = 0; j < n; ++j) {

			if (i == j) a[i*n + j] = 2.0 * (n - 1);
			else a[i*n + j] = 1.0;
		}
		b[i] = 1.0;
	}
}


int main(int argc, char* argv[]) {

	int rank,threads,i, j,lb,ub,comm,iter = 0;

	double sum,x_err,max_x_err = 0.0,start,finish,row_err,max_row_err = 0.0;

	int sendcount[n];
	int sendcount2[n];
	int displ[n];
	int displ2[n];

	MPI_Init(&argc, &argv);

	MPI_Comm_size(MPI_COMM_WORLD, &threads);
	MPI_Comm_rank(MPI_COMM_WORLD, &rank);

	comm = MPI_COMM_WORLD;

	if (rank == 0) {
		init_matr(n);
	}

	lb = rank * n / threads;
	ub = (rank + 1)*n / threads - 1;

	for (i = 0; i < threads; ++i) {
		sendcount[i] = (i + 1) * n / threads - i*n / threads;
		displ[i] = i*n / threads;
	}

	for (i = 0; i < threads; ++i) {
		sendcount2[i] = sendcount[rank];
		displ2[i] = displ[rank];
	}

	start = MPI_Wtime();

	MPI_Bcast(a, n*n, MPI_DOUBLE_PRECISION, 0, comm);
	MPI_Bcast(b, n, MPI_DOUBLE_PRECISION, 0, comm);

	for (i = 0; i < n; ++i) {
		x_old[i] = b[i] / a[i*n + i];

		if (abs(x_old[i]) > max_x_err) max_x_err = abs(x_old[i]);
	}

	iter = 1;
	do {
		x_err = 0.0;


		for (i = lb; i <= ub; ++i) {
			x_new[i] = b[i];
			for (j = 0; j < n; ++j) if (i != j) x_new[i] -= a[i*n + j] * x_old[j];
			x_new[i] /= a[i*n + i];

			if (x_err < abs(x_new[i] - x_old[i])) x_err = abs(x_new[i] - x_old[i]);
		}

		MPI_Allreduce(&x_err, &max_x_err, 1, MPI_DOUBLE_PRECISION, MPI_MAX, comm);

		MPI_Alltoallv(x_new, sendcount2, displ2, MPI_DOUBLE_PRECISION, x_old, sendcount, displ, MPI_DOUBLE_PRECISION, comm);

		row_err = 0.0;
		sum = 0;

		for (i = lb; i <= ub; ++i) {
			sum = b[i];
			for (j = 0; j < n; ++j) sum -= a[i*n + j] * x_old[j];

			if (row_err < abs(sum)) row_err = abs(sum);
		}

		MPI_Allreduce(&row_err, &max_row_err, 1, MPI_DOUBLE_PRECISION, MPI_MAX, comm);

		++iter;
	} while (max_x_err > error || max_row_err > error);

	if (rank == 0) {
		finish = MPI_Wtime();
		cout << "Iter: " << iter << endl;
		cout<<"Seconds: "<<finish - start << endl;
		/*for(i = 0; i < n; i++)
			cout << x_old[i] << endl;
		*/
	}

	MPI_Finalize();
	return 0;
}
