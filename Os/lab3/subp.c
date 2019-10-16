#include <stdio.h>
#include <stdlib.h>
#include <unistd.h>
#include <fcntl.h>
#include <sys/stat.h>
#include <sys/types.h>
#include <sys/ipc.h>
#include <sys/msg.h>
#include <string.h>

char* concat(const char *s1, const char *s2)
{
    char *result = malloc(strlen(s1)+strlen(s2)+1);//+1 for the null-terminator
    // in real code you would check for errors in malloc here
    strcpy(result, s1);
    strcat(result, s2);
    return result;
}

int main()
{
    key_t ipckey;
    int mq_id;
    struct {
        long type;
        char text[100];
    } Mymsg;

    char buf[30];
    int received;
    /* Генерация IPC ключа */
    ipckey = ftok("/tmp/file", 42);
    printf("My key is %d\n", ipckey);
    /* Подключение к очереди */
    mq_id = msgget(ipckey, 0);
    printf("Message identifier is %d\n", mq_id);
    /* Считывание сообщения */
    received = msgrcv(mq_id, &Mymsg, sizeof(Mymsg), 0, 0);
    // printf(typeof(Mymsg));
    // printf("Mymsg123123123: %s",Mymsg);
    // *buf = Mymsg.text;
    // *Mymsg.text = strcpy(Mymsg.text, buf); 
    // printf("MESSAGE: %s\n", Mymsg.text);
    system(concat("fim -a ", Mymsg.text));
    // printf("MESSAGE: %s\n",concat("fim -a ", Mymsg.text));

    printf("Message from first program: %s\nreceived = %d\n", Mymsg.text, received);
    remove("/tmp/file");

    return 0;
}