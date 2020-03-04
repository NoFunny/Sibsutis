#include <stdio.h>
#include <math.h>
#include <cuda.h>

#define BLOCK SIZE 16

__global void Muld(float* A float* B int wA int wB float* C) { global__ void Muld(float* A, float* B, int wA, int wB, float* C) {
  int bx = blockIdx.x; // Block index
  int by = blockIdx.y;
  i t t th dId int tx = threadIdx.x; // Th d i d // Thread index
  int ty = threadIdx.y;
  int aBegin = wA * BLOCK_SIZE * by; // Index of the first sub-matrix of A processed by the block
  int aEnd = aBegin + wA - 1; // Index of the last sub-matrix of A processed by the block
  int aStep = BLOCK_SIZE; // Step size used to iterate through the sub-matrices of A
  int bBegin = BLOCK_SIZE * bx; // Index of the first sub-matrix of B processed by the block
  int bStep = BLOCK_SIZE * wB; // Step size used to iterate through the sub-matrices of B
  float Csub = 0; // The element of the block sub-matrix that is computed by the thread
  for (int a = aBegin, b = bBegin; a <= aEnd; a += aStep, b += bStep) {
      // Shared memory for the sub-matrix of A
    __shared__ float As[BLOCK_SIZE][BLOCK_SIZE];
// Shared memory for the sub-matrix of B
    __shared__ float Bs[BLOCK_SIZE][BLOCK_SIZE];
    As[ty][tx] = A[a + wA * ty + tx]; // Load the matrices from global memory to shared memory;
    Bs[ty][tx] = B[b + wB * ty + tx]; // each thread loads one element of each matrix
    __syncthreads(); // Synchronize to make sure the matrices are loaded
// Multiply the two matrices together;
// each thread computes one element
// of the block sub-matrix
      for (int k = 0; k < BLOCK_SIZE; ++k)
        Csub += As[ty][k] * Bs[k][tx];
// Synchronize to make sure that the preceding
// computation is done before loading two new
// sub-matrices of A and B in the next iteration
    __syncthreads();
  }
// Write the block sub-matrix to global memory;
// each thread writes one element
  int c = wB * BLOCK_SIZE * by + BLOCK_SIZE * bx;
  C[c + wB * ty + tx] = Csub;
}

int main(const float* A, const float* B, int hA, int wA, int wB, float* C) {
  int size;
  // L d A d B t th d i // Load A and B to the device
  float* Ad; size = hA * wA * sizeof(float); cudaMalloc((void**)&Ad, size);
  cudaMemcpy(Ad, A, size, cudaMemcpyHostToDevice);
  float* Bd; size = wA * wB * sizeof(float); cudaMalloc((void**)&Bd, size);
  cudaMemcpy(Bd, B, size, cudaMemcpyHostToDevice);
// Allocate C on the device
  float* Cd;
  size = hA * wB * sizeof(float);
  cudaMalloc((void**)&Cd, size);
  // Compute the execution configuration assuming the matrix dimensions are multiples of BLOCK_SIZE
  dim3 dimBlock(BLOCK_SIZE, BLOCK_SIZE);
  dim3 dimGrid(wB / dimBlock.x, hA / dimBlock.y);
  // Launch the device computation
  Muld<<<dimGrid, dimBlock>>>(Ad, Bd, wA, wB, Cd);
  // Read C from the device
  cudaMemcpy(C, Cd, size, cudaMemcpyDeviceToHost);
  // Free device memory
  cudaFree(Ad);
  cudaFree(Bd);
  cudaFree(Cd);

  return 0;
}
