#include <stdio.h>
#include <stdlib.h>
#include <mpi.h>

int main(int argc, char **argv) {
  int rank, comsize;

  MPI_Init(&argc, &argv);
  MPI_Comm_rank(MPI_COMM_WORLD, &rank);
  MPI_Comm_size(MPI_COMM_WORLD, &comsize);

  int next = (rank + 1) % comsize;
  int prev = (rank - 1 + comsize) % comsize;
  int send_index = rank;
  int recv_index = prev;

  int **sbuf = malloc(sizeof(int*) * comsize);
  for(int i = 0; i < comsize; i++) {
    sbuf[i] = (int*) malloc(sizeof(int)* comsize);
  }
  for(int i = 0; i < comsize; i++) {
    sbuf[i][0] = -1;
  }
  sbuf[rank][0] = rank;
  int count = 1;

  for(int i = 0; i < comsize - 1; i++) {
    MPI_Sendrecv(&sbuf[send_index][0], count, MPI_INT, next, 0,
      &sbuf[recv_index][0], count, MPI_INT, prev, 0, MPI_COMM_WORLD, MPI_STATUS_IGNORE);
    send_index = (send_index - 1 + comsize) % comsize;
    recv_index = (recv_index - 1 + comsize) % comsize;
  }
  printf("Proc %d sendto %d recv from %d \n", rank, next, prev);

  MPI_Finalize();
  return 0;
}
