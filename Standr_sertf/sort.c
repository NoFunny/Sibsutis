#include <stdio.h>
#include <time.h>
#include <math.h>

void bSort(int array[],int n){
	int i,j;
	for(i = 0;i<n-1;i++) {
		for(j = 0;j < n-i-1;j++){
			if(array[j - 1] > array[j]){
				int tmp = array[j-1];
				array[j-1] = array[j];
				array[j] = tmp;
			}
		}
	}
}

int main() {
	srand(time(NULL));
	int n=10, i;
	int array[n];
	for(i = 0; i < n;i++) {
		array[i] = rand() % 100;
	}
	printf("Printing array...\n");
	for(i = 0; i < n; i++) {
		printf("Array[%d] = %d\n", i, array[i]);
	}
	bSort(array,n);
}