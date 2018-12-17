package Entities;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

public abstract class Queue<T> {

    BlockingQueue<T> queue;

    public Queue(int size)
    {

        queue = new ArrayBlockingQueue<T>(size);
    }

    public boolean put(T item){
        boolean result;

        try {
            queue.put(item);
            result = true;
        }catch (InterruptedException e){
            result = false;
        }

        return result;
    }

    public T take(){
        T result;

        try {
            result = queue.take();
        }catch (InterruptedException e){
            result = null;
        }

        return result;
    }

}
