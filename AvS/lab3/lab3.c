#include <stdio.h>
#include <sys/time.h>
#include <math.h>
#include <iostream>
#include <bits/stdc++.h>
// #include <tchar.h>
// #include <windows.h>


double f(double y) {return(4.0/(1.0+y*y));}

double _pi()
{
   double w, x, sum, pi;
   int i;
   int n = 1000000;
   w = 1.0/n;
   sum = 0.0;
   for(i=0; i < n; i++)
   {
      x = w*(i-0.5);
      sum = sum + f(x);
   }
   pi = w*sum;
   printf("pi = %f\n", pi);
   return pi;
}
void f_TSC() {
  printf("TSC METHOD\n");
  uint32_t high, low;
 __asm__ __volatile__ (
 "rdtsc\n"
 "movl %%edx, %0\n"
 "movl %%eax, %1\n"
 : "=r" (high), "=r" (low)
 :: "%rax", "%rbx", "%rcx", "%rdx"
 );
 uint64_t ticks = ((uint64_t)high << 32) | low;

 _pi();

 __asm__ __volatile__ (
 "rdtsc\n"
 "movl %%edx, %0\n"
 "movl %%eax, %1\n"
 : "=r" (high), "=r" (low)
 :: "%rax", "%rbx", "%rcx", "%rdx"
);
 ticks = (((uint64_t)high << 32) | low) - ticks;

printf("Elapsed ticks: %" PRIu64 "\n", ticks);
}

void f_gettimeofday(){
    printf("GETTIMEOFDAY METHOD\n");
    struct timeval start, end;
    gettimeofday(&start, NULL);
    // ios_base::sync_with_stdio(false);

	_pi();

	gettimeofday(&end, NULL);
	double time_taken;
	time_taken = (end.tv_sec - start.tv_sec) * 1e6;
    time_taken = (time_taken + (end.tv_usec -  start.tv_usec)) * 1e-6;
    // cout << "gettimeofday: " << fixed << time_taken << setprecision(6);
    // cout << " sec" << endl;
    printf("Time of day: %f\n",time_taken );
}

int main() {
  unsigned int start;
  unsigned int end;
  printf("Calculation Pi...\n");
  f_TSC();
  f_gettimeofday();
  start = clock();
  printf("CLOCK METHOD\n");
  double pi = _pi();
  end = clock();
  printf("Time(c) = %f\n", (end-start)/1000.0);
  printf("Acuracy = %f%\n", (1 - ((pi - M_PI) * M_PI) * 100));
  printf("Abs infelicity = %f\n", fabs(M_PI - pi));
  printf("Relative infelicity = %f%\n", (fabs(pi - M_PI)/M_PI) * 100);
  return 0;
}
