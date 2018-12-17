// this will sort the numbers given by user, very slowly :) 
// It forks for every number given and each fork waits as long as the number is in seconds before printing the number.
int main(int c, char **v)
{
        while (--c > 1 && !fork());
        int val  = atoi(v[c]);
        sleep(val);
        printf("%d\n", val);
        return 0;
}
