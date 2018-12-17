package Components;

import Entities.Queue;
import Entities.Serving;

public class ServingQueue extends Queue<Serving> {

    private static ServingQueue instance;

    private ServingQueue(int size)
    {
        super(size);
    }

    public static ServingQueue getInstance() {
        return instance;
    }

    public static ServingQueue getOrCreateInstance(int size){
        synchronized (ServingQueue.class) {
            if (instance == null) {
                instance = new ServingQueue(size);
                System.out.println("First instance of ServingQueue created.");
            }
        }
        return instance;
    }

    public static void clearInstance(){
        instance = null;
    }
}
