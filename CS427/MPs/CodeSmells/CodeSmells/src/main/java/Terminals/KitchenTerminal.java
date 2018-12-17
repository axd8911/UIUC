package Terminals;

import Components.OrderQueue;
import Components.ServingQueue;
import Entities.Order;
import Entities.Serving;

public class KitchenTerminal extends Terminal{

    KitchenTerminal(){
        super();
    }

    public Order getOrder(){
        OrderQueue oq = OrderQueue.getInstance();
        Order order = oq.take();
        return order;
    }

    public void serveOrder(Serving serving){
        ServingQueue sq = ServingQueue.getInstance();
        sq.put(serving);
    }


}
