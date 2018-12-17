package UnitTests.Components;

import Components.OrderQueue;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

public class OrderQueueTest {

    @Test
    public void createClearOrderQueueTest(){
        assertEquals(null, OrderQueue.getInstance());
        OrderQueue q = OrderQueue.getOrCreateInstance(10);
        assertNotEquals(null, OrderQueue.getInstance());
        OrderQueue.clearInstance();
        assertEquals(null, OrderQueue.getInstance());
    }
}
