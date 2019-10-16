#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <unistd.h>
#include <fcntl.h>
#include <dirent.h>
#include <sys/stat.h>
#include <sys/types.h>

#include <sys/ipc.h>

#include <sys/msg.h>
#include <string.h>

void ex_1() {
  char *arg[] = {"/home/nofuny/Рабочий стол/study/Os/lab2/", 0};
  pid_t pid;

  switch (pid = fork()) {
  case -1:
    perror("fork"); /* произошла ошибка */
    exit(-1);       /*выход из родительского процесса*/
  case 0:
    printf(" CHILD: Это процесс-потомок!\n");
    printf(" CHILD: Мой PID -- %d\n", getpid());
    printf(" CHILD: PID моего родителя -- %d\n", getppid());

    execv(arg[0], arg);
    exit(0); //  not need
  default:
    printf("PARENT: Это процесс-родитель!\n");
    printf("PARENT: Мой PID -- %d\n", getpid());
    printf("PARENT: PID моего потомка %d\n", pid);
    printf("PARENT: Я жду, пока потомок не вызовет exit()...\n");
    wait();
  }

  char *pidkill = (char *)malloc(sizeof(char) * 5);
  printf("Inter pidkill : ");
  scanf("%s", pidkill);
  printf("Killed process: %s", pidkill);
  kill(atoi(pidkill), 9);
  printf("\n");
}

void ex_2() {
  int pipedes[2];
  pid_t pid;
  char buf[1024] = "Unfinished";
  int len;

  pipe(pipedes);

  switch (pid = fork()) {
  case -1:
    perror("fork"); /* happened error */
    exit(-1);       /*exit from the parent process*/

    break;
  case 0:
    system("wget http://bochkarev.site/pr/OS.lecture.3.pdf");

    char *str = "Succed";
    close(pipedes[0]);
    write(pipedes[1], (void *)str, strlen(str) + 1); // pipe[1] refers to the write end of the pipe
    close(pipedes[1]);

    break;
  default:
    while (strcmp(buf, "Unfinished") == 0) {
      sleep(3);
      len = read(pipedes[0], buf, 1024); // pipe[0] refers to the read end of the pipe
      write(2, buf, len);
      printf(" downloading.\n");
    }
    close(pipedes[1]);
    close(pipedes[0]);
  }
}

// char concat(const char *s1, const char *s2)
// {
//     char *result = malloc(strlen(s1)+strlen(s2)+1);//+1 for the null-terminator
//     // in real code you would check for errors in malloc here
//     strcpy(result, s1);
//     strcat(result, s2);
//     return result;
// }

void ex_3() {
  FILE *fo;
  fo = fopen("/tmp/file", "w");
  
  // if(fseek(fo, 8, SEEK_SET) == 0){
  	// printf("++++");
  // }else {
  	// printf("----");
  // }

  pid_t pid;
  char *arg[] = {"/home/nofunny/Рабочий стол/study/Os/lab3/subp", 0};

  struct
  {
    long type;
    char text[100];
  } Mymsg;

  key_t ipckey;
  int mq_id;
  // char buf1 = "\"fim -a picture.jpg\"";
  // char buf2[30];
  
  /* Генерация IPC ключа */
  ipckey = ftok("/tmp/file", 42);
  printf("My key is %d\n", ipckey);
  // printf("Enter second path message\n");
  // scanf("%s", buf2);
  // for(int i = 0; i < 20; i++){
  	// Mymsg.text[0] = buf1[i] ;
  // }
  // Mymsg.text = "\"";
  // Mymsg.text = buf1;
  // printf("MESSAGE: %s\n", Mymsg.text);
  /* Создание очереди */
  mq_id = msgget(ipckey, IPC_CREAT | 0666);
  printf("Message identifier is %d\n", mq_id);
  /* Отправка сообщения */
  memset(Mymsg.text, 0, 100); /* Clear out the space */
  printf("First program waiting for a Message...\n");
  scanf("%s", Mymsg.text);
  // snprintf(Mymsg.text, sizeof Mymsg.text, "%s%s", buf1, buf2);
  // printf("MESSAGE: %s\n", Mymsg.text);
  // strcpy(Mymsg.text, "Hello, world!");
   // передаем сообщение
  Mymsg.type = 1;
  msgsnd(mq_id, &Mymsg, sizeof(Mymsg), 0);
  printf("\n\n\n");

  switch (pid = fork()) {
  case -1:
    perror("fork"); /* произошла ошибка */
    exit(-1);       /*выход из родительского процесса*/
  case 0:
    execv(arg[0], arg);
    break;
  }
  fclose(fo);
}


int main() {
  int numEx;
  printf("What must been run?\nEX_1 - 1\nEX_2 - 2\nEX_3 - 3\n ");
  scanf("%d", &numEx);
  printf("\n");
  switch (numEx) {
  case 1:
    ex_1();
    break;
  case 2:
    ex_2();
    break;
  case 3:
    ex_3();
    break;
  }
}
