#include <stdio.h>
#include <stdlib.h>
#include <unistd.h>
#include <pthread.h>
#include <signal.h>

char a = 'A',
b = 'B';
int flag = 0;
pthread_mutex_t mutex;
pthread_mutex_t mutex1;

void* threadFunc(void* thread_data);
void* threadFunc1(void* thread_data);
void func();

void* threadFunc(void* thread_data) {

  while(flag) {
    printf("%c\n", a);
  }
  return 0;
}

void func() {
  flag = !flag;
}

void* threadFunc1(void* thread_data) {

  while(!flag) {
    printf("%c\n",b);
  }
  return 0;
}

int main() {
  pthread_mutex_init(&mutex, NULL);
  pthread_mutex_init(&mutex1, NULL);

  void* thread_data = NULL;
  pthread_t tid;
  pthread_t tid2;
  int start = 1;
  while(start) {
    int check = pthread_create(&tid, NULL, threadFunc, thread_data);
    int check1 = pthread_create(&tid2, NULL, threadFunc1, thread_data);
    signal(SIGINT,func);
  }
  pthread_join(tid2, NULL);
  pthread_join(tid, NULL);
  return 0;
}
