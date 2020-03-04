#include <stdio.h>
#include <mpi.h>
#include <stdlib.h>

int main(int argc, char **argv) {
    int rank,commsize,len;
    
    char procname[MPI_MAX_PROCESSOR_NAME];
    MPI_Init(&argc, &argv);
    MPI_Comm_rank(MPI_COMM_WORLD, &rank);
    MPI_Comm_size(MPI_COMM_WORLD, &commsize);
    MPI_Get_processor_name(procname, &len);
    MPI_Request reqs[2];
    MPI_Status stats[2];
    
    int count = 1024;
    char *data = malloc(1024 * sizeof(char));
    char *inputdata = malloc((commsize - 1) * 1024 * sizeof(char));
    int h;
    double time = MPI_Wtime();
    for(int i = 0, h = 0; i < commsize; i++) {
	if(rank == i)
	    continue;
	MPI_Isend(data, 1024, MPI_CHAR, i, 0, MPI_COMM_WORLD, &reqs[0]);
	MPI_Irecv(inputdata + 1024 * h, 1024, MPI_CHAR, i, 0, MPI_COMM_WORLD, &reqs[1]);    
	h++;
	MPI_Waitall(2, reqs, stats);
    }
    double end = MPI_Wtime();
    printf("Process: %d of %d on %s recived message with time \t= %.6lf \n", rank, commsize, procname, (end - time));
    free(data);
    free(inputdata);
    MPI_Finalize();
    return 0;
}