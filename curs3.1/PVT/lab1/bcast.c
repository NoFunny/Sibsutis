#include <stdio.h>
#include <mpi.h>
#include <malloc.h>

int main(int argc, char **argv) {
    int rank,commsize;
    int root = 0;

    MPI_Init(&argc, &argv);
    MPI_Comm_rank(MPI_COMM_WORLD, &rank);
    MPI_Comm_size(MPI_COMM_WORLD, &commsize);


    int count = 1;

    char **rbuf = (char**)malloc(sizeof(char*) * count);
    for(int i=0;i<commsize;i++){
	     rbuf[i] = (char *)malloc(sizeof(char) * commsize);
    }
    int i;
    double end,start;
    char **sbuf = (char**)malloc(sizeof(char*) * count);
    for(int i=0;i<commsize;i++){
      sbuf[i] = (char *)malloc(sizeof(char) * commsize);
    }
    if(rank == root) {
      start = MPI_Wtime();
      for(i = 0; i < commsize; i++) {
        if(i != rank) {
          MPI_Send(&sbuf[0][0],count, MPI_CHAR, i, 0, MPI_COMM_WORLD);
        }
      }
    }
    if(rank != root)
      MPI_Recv(&rbuf[0][rank], count, MPI_CHAR, root, 0, MPI_COMM_WORLD, MPI_STATUS_IGNORE);
    if(rank == root) {
      MPI_Sendrecv(&sbuf[0][0], count, MPI_CHAR, 0, 0, &rbuf[0][0], count,
          MPI_CHAR, 0, 0, MPI_COMM_WORLD, MPI_STATUS_IGNORE);
          end = MPI_Wtime();
    }
    printf("Procces: %d Send: %d\n", rank, root);
    if(rank == root)
      printf("Time = %f\n", end-start);

    MPI_Finalize();

    return 0;
}    
