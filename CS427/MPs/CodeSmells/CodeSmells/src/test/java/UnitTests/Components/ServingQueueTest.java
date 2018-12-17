package UnitTests.Components;

import Components.ServingQueue;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

public class ServingQueueTest {

    @Test
    public void createClearServingQueueTest(){
        assertEquals(null, ServingQueue.getInstance());
        ServingQueue q = ServingQueue.getOrCreateInstance(10);
        assertNotEquals(null, ServingQueue.getInstance());
        ServingQueue.clearInstance();
        assertEquals(null, ServingQueue.getInstance());
    }
}
