#include <stdio.h>
#include <stdlib.h>
#include <unistd.h>
#include <malloc.h>
#include <fcntl.h>
#include <dirent.h>
#include <sys/stat.h>
#include <sys/types.h>

#define BUFFER_SIZE 100

char *get_process_name_by_pid(int pid)
{
    char *name = (char *)calloc(1024, sizeof(char));
    if (name)
    {
        sprintf(name, "/proc/%d/comm", pid);
        FILE *f = fopen(name, "r");
        if (f)
        {
            size_t size;
            size = fread(name, sizeof(char), 1024, f);
            if (size > 0)
            {
                if ('\n' == name[size - 1])
                {
                    name[size - 1] = '\0';
                }
            }
            fclose(f);
        }
    }
    return name;
}

int count_dir_size(char *dir)
{
    struct dirent *pD;
    struct stat file_stat;
    int size = 0;
    DIR *pDirec = opendir(dir);
    while ((pD = readdir(pDirec)) != NULL)
    {
        if (stat(pD->d_name, &file_stat) == 0)
        {
            size += file_stat.st_size;
        }
    }
    printf("Общий размер файлов каталога %s равен %d байт\n\n", dir, size);
    closedir(pDirec);

    return 0;
}

int main(/*int argc, char *argv[]*/)
{
    // int i = 0;
    // char buffer[BUFFER_SIZE] = {};

    // write(STDIN_FILENO, "Enter your name: ", 17);
    // read (STDIN_FILENO, buffer, BUFFER_SIZE);
    // write(STDIN_FILENO, "Hi,     ", 7);

    // while(buffer[i] != '\n') {
    //   i++;
    // }

    // buffer[i] = ')';
    // buffer[i + 1] = '\n';
    // buffer[i + 2] = '\n';
    // write(STDIN_FILENO, buffer, BUFFER_SIZE);

    //count_dir_size(argv[1]);

    // char yes;
    // printf("Use part 2? [y/N]");
    // scanf("%c", &yes);
    // if (yes == 'y') {
    char *specfor3lab = (char *)malloc(sizeof(char) * 7);
    specfor3lab = "/proc/\0";
    struct dirent *pDirent;
    DIR *pDir = opendir(/*argv[1]*/ specfor3lab);
    char *name;
    int i = 1;

    while ((pDirent = readdir(pDir)) != NULL)
    {
        if (atoi(pDirent->d_name))
        {
            name = get_process_name_by_pid(atoi(pDirent->d_name));
            printf("%d - [%s](%d); ", i, name, atoi(pDirent->d_name));
            if (i % 1 == 0)
            {
                printf(" \n");
            }
            i++;
        }
    }
    // }
    printf("\n");

    //exit(0);

    return 0;
}