#include <sys/types.h>
#include <sys/stat.h>
#include <fcntl.h>
#include <unistd.h>
#include <errno.h>
#include <stdio.h>

//not everything is a system call
int main() {

    char* filename = "";
    int flags      = O_CREAT | O_TRUNC | O_RDWR;
    mode_t mode    = S_IRUSR | S_IWUSR;

    //close error stream, send error messsages to errors.txt
    close (2);
    int errdes = open ("errors.txt", flags, mode);

    //attempt to open new file to write to
    int fildes = open (filename, flags, mode);

    if (fildes == -1)
        perror ("bad file name");
    else
        write (fildes, "Hello!\n", 7);


    close (fildes);
    close (errdes);

    return 0;
}

// writing to files
// int main() {
//
//     char* filename = "message.txt";
//     int flags      = O_CREAT | O_APPEND | O_RDWR;
//     mode_t mode    = S_IRUSR | S_IWUSR | S_IROTH;
//
//
//
//     int fildes = open (filename, flags, mode);
//
//     write (fildes, "Hello!\n", 7);
//
//
//     close (fildes);
//
//     return 0;
// }

// hello standard error stream
// int main() {
//
//
//     for (int len = 0; len < 7; len++) {
//         write (STDOUT_FILENO, "I think", 7 - len);
//         write (STDOUT_FILENO, "\n", 1);
//     }
//
//     return 0;
// }
