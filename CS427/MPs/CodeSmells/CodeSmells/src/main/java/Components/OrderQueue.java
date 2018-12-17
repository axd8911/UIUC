package Components;

import Entities.Order;
import Entities.Queue;

public class OrderQueue extends Queue<Order> {

    private static OrderQueue instance;

    private OrderQueue(int size)
    {
        super(size);
    }

    public static OrderQueue getInstance() {
        return instance;
    }

    public static OrderQueue getOrCreateInstance(int size){
        synchronized (OrderQueue.class) {
            if (instance == null) {
                instance = new OrderQueue(size);
                System.out.println("First instance of OrderQueue created.");
            }
        }
        return instance;
    }

    public static void clearInstance(){
        instance = null;
    }
}
