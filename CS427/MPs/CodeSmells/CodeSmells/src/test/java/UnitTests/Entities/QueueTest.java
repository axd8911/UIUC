package UnitTests.Entities;

import Entities.Queue;
import org.junit.Test;

import static org.junit.Assert.*;

class fakeQueue extends Queue<Integer>{
    public fakeQueue(int size){
        super(size);
    }
}

public class QueueTest {
    @Test
    public void putTest(){
        fakeQueue q = new fakeQueue(2);
        int[] nums = new int[] {1,2,3};

        assertTrue(q.put(1));
        assertTrue(q.put(2));
        Thread.currentThread().interrupt();
        assertFalse(q.put(3));

    }

    @Test
    public void takeTest(){
        fakeQueue q = new fakeQueue(2);
        q.put(1);
        q.put(2);

        int e1 = q.take();
        assertEquals(1, e1);
        int e2 = q.take();
        assertEquals(2, e2);
        Thread.currentThread().interrupt();
        assertNull(q.take());

    }
}
