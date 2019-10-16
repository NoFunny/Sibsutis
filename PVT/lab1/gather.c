#include <stdio.h>
#include <mpi.h>
#include <malloc.h>

int main(int argc, char **argv) {
    int rank,commsize;
    int root = 0;

    MPI_Init(&argc, &argv);
    MPI_Comm_rank(MPI_COMM_WORLD, &rank);
    MPI_Comm_size(MPI_COMM_WORLD, &commsize);
    double time = MPI_Wtime();
    //char *sbuf=(char *)malloc(sizeof(*sbuf) * count);
    //char *rBuf=(char*)malloc(sizeof(*rBuf) * count * (commsize - 1));

    if(rank > root){
      char *sbuf=(char *)malloc(sizeof(*sbuf) * count);
      MPI_Send(sbuf,count, MPI_CHAR, 0, 1, MPI_COMM_WORLD);
      free(sbuf);
    }else{
      char *rBuf=(char*)malloc(sizeof(*rBuf) * count * (commsize - 1));
      MPI_Status status;
      for(int i = 1; i < commsize; i++) {
        MPI_Recv(rBuf + (i - 1) * count, count, MPI_CHAR, MPI_ANY_SOURCE, 1, MPI_COMM_WORLD, &status);
      }
      free(rBuf);
    }
    if(rank == root)
      printf("Procces: %d Time: %d\n", rank, MPI_Wtime() - time);
///	for(int j = 0; j < commsize; j++) {
//	    printf("Procces: %d Recv: %d\n", rank, root);
//
//    }

//    printf("Complere!!!");
//    for(i = 0; i < commsize; i++)
//	free(&sbuf[0][i]);

    MPI_Finalize();

    return 0;
}
