#include <cuda.h>
#include <stdio.h>
#include <math.h>
#include <sys/time.h>

double wtime() {
    struct timeval t;
    gettimeofday(&t, NULL);
    return (double)t.tv_sec + (double)t.tv_usec * 1E-6;
}

__global__ void gpuSum(float *a, float *b, float *c) {
    int i = threadIdx.x + blockDim.x * blockIdx.x;
    c[i] = a[i] + b[i];
    
//    c[i] *= c[i];

}

int main() {
    float *h_a, *h_b, *h_c, *d_a, *d_b, *d_c;
    int threads_per_block = 1024;
    int N = pow(2,10);
    int num_of_blocks = N / threads_per_block;
    
    h_a = (float*)calloc(N, sizeof(float));
    h_b = (float*)calloc(N, sizeof(float));
    h_c = (float*)calloc(N, sizeof(float));
    
    for(int i = 0; i < N; i++) {
	h_a[i] = i * 2;
	h_b[i] = i * 3; 
    }
    cudaMalloc((void**)&d_a, N*sizeof(float));
    cudaMalloc((void**)&d_b, N*sizeof(float));
    cudaMalloc((void**)&d_c, N*sizeof(float));

    cudaMemcpy(d_a, h_a, N*sizeof(float), cudaMemcpyHostToDevice);
    cudaMemcpy(d_b, h_b, N*sizeof(float), cudaMemcpyHostToDevice);
    
    double t = wtime();
    gpuSum<<<dim3(num_of_blocks),dim3(threads_per_block)>>>(d_a, d_b, d_c);
    
    cudaDeviceSynchronize();
    t = wtime() - t;
    
    cudaMemcpy(h_c, d_c, N*sizeof(float), cudaMemcpyDeviceToHost);
    
//    for(int i = 0; i < N; i++) {
//	printf("%g\n", h_c[i]);
//    }
    
    printf("Elapsed time: %.6fsec. \n", t);
    free(h_a);
    free(h_b);
    free(h_c);
    cudaFree(d_a);
    cudaFree(d_b);
    cudaFree(d_c);
    
    return 0;
}
