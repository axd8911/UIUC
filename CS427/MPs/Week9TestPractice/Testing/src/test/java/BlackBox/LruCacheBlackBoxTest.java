package BlackBox;

import BlackBox.Setups.LruCacheSetup;
import Utils.LruCacheMethod;
import org.junit.Test;

public class LruCacheBlackBoxTest extends LruCacheSetup {

    @Test
    public void test1Put1GetSize2(){
        //constructor size = 2
        int[] functionParameters = new int[]{2};
        lruCachePUT.run(LruCacheMethod.Constructor,
                functionParameters,
                null);

        //put key=1, value = 2
        int[] functionParameters2 = new int[]{1,2};
        lruCachePUT.run(LruCacheMethod.Put,
                functionParameters2,
                null);

        //get key = 1
        int[] functionParameters3 = new int[]{1};
        lruCachePUT.run(LruCacheMethod.Get,
                functionParameters3,
                2);
    }

    @Test
    public void test3Put3GetSize2(){
        //constructor size = 2
        int[] functionParameters = new int[]{2};
        lruCachePUT.run(LruCacheMethod.Constructor,
                functionParameters,
                null);

        //first put key=1, value = 2
        int[] functionParameters2 = new int[]{1,2};
        lruCachePUT.run(LruCacheMethod.Put,
                functionParameters2,
                null);

        //second put key=2, value = 3
        int[] functionParameters3 = new int[]{2,3};
        lruCachePUT.run(LruCacheMethod.Put,
                functionParameters3,
                null);

        //first get key = 1, (2,3) is now least used
        int[] functionParameters6 = new int[]{1};
        lruCachePUT.run(LruCacheMethod.Get,
                functionParameters6,
                2);

        //third put key=3, value = 4, (2,3) is removed
        int[] functionParameters4 = new int[]{3,4};
        lruCachePUT.run(LruCacheMethod.Put,
                functionParameters4,
                null);

        //second get key = 1
        int[] functionParameters5 = new int[]{1};
        lruCachePUT.run(LruCacheMethod.Get,
                functionParameters5,
                2);

        //third get key = 3
        int[] functionParameters8 = new int[]{3};
        lruCachePUT.run(LruCacheMethod.Get,
                functionParameters8,
                4);

        //post get key = 2, check to see (2,3) was removed in third put.
        int[] functionParameters7 = new int[]{2};
        lruCachePUT.run(LruCacheMethod.Get,
                functionParameters7,
                -1);
    }

    @Test
    public void test3Put1GetSize2(){
        //constructor size = 2
        int[] functionParameters = new int[]{2};
        lruCachePUT.run(LruCacheMethod.Constructor,
                functionParameters,
                null);

        //first put key=1, value = 2
        int[] functionParameters2 = new int[]{1,2};
        lruCachePUT.run(LruCacheMethod.Put,
                functionParameters2,
                null);

        //second put key=2, value = 3
        int[] functionParameters3 = new int[]{2,3};
        lruCachePUT.run(LruCacheMethod.Put,
                functionParameters3,
                null);

        //third put key=3, value = 4, (1,2) is removed
        int[] functionParameters4 = new int[]{3,4};
        lruCachePUT.run(LruCacheMethod.Put,
                functionParameters4,
                null);

        //first get key = 1, (1,2) should not be present
        int[] functionParameters5 = new int[]{1};
        lruCachePUT.run(LruCacheMethod.Get,
                functionParameters5,
                -1);

        //post get key = 2
        int[] functionParameters7 = new int[]{2};
        lruCachePUT.run(LruCacheMethod.Get,
                functionParameters7,
                3);
    }

    @Test
    public void test3Put2GetSize2(){
        //constructor size = 2
        int[] functionParameters = new int[]{2};
        lruCachePUT.run(LruCacheMethod.Constructor,
                functionParameters,
                null);

        //first put key=1, value = 2
        int[] functionParameters2 = new int[]{1,2};
        lruCachePUT.run(LruCacheMethod.Put,
                functionParameters2,
                null);

        //second put key=2, value = 3
        int[] functionParameters3 = new int[]{2,3};
        lruCachePUT.run(LruCacheMethod.Put,
                functionParameters3,
                null);

        //third put key=3, value = 4, (1,2) is removed
        int[] functionParameters4 = new int[]{3,4};
        lruCachePUT.run(LruCacheMethod.Put,
                functionParameters4,
                null);

        //first get key = 1, (1,2) should not be present
        int[] functionParameters5 = new int[]{1};
        lruCachePUT.run(LruCacheMethod.Get,
                functionParameters5,
                -1);

        //post get key = 2
        int[] functionParameters7 = new int[]{2};
        lruCachePUT.run(LruCacheMethod.Get,
                functionParameters7,
                3);

        //post get key = 2
        int[] functionParameters8 = new int[]{3};
        lruCachePUT.run(LruCacheMethod.Get,
                functionParameters8,
                4);
    }

    @Test
    public void test4Put4GetSize3(){
        //constructor size = 2
        int[] functionParameters = new int[]{3};
        lruCachePUT.run(LruCacheMethod.Constructor,
                functionParameters,
                null);

        //first put key=1, value = 2
        int[] functionParameters2 = new int[]{1,2};
        lruCachePUT.run(LruCacheMethod.Put,
                functionParameters2,
                null);

        //second put key=2, value = 3
        int[] functionParameters3 = new int[]{2,3};
        lruCachePUT.run(LruCacheMethod.Put,
                functionParameters3,
                null);

        //third put key=3, value = 4, (1,2) is removed
        int[] functionParameters4 = new int[]{3,4};
        lruCachePUT.run(LruCacheMethod.Put,
                functionParameters4,
                null);

        //first get key = 1,
        int[] functionParameters5 = new int[]{1};
        lruCachePUT.run(LruCacheMethod.Get,
                functionParameters5,
                2);

        //post get key = 2
        int[] functionParameters7 = new int[]{2};
        lruCachePUT.run(LruCacheMethod.Get,
                functionParameters7,
                3);

        //post get key = 2
        int[] functionParameters8 = new int[]{3};
        lruCachePUT.run(LruCacheMethod.Get,
                functionParameters8,
                4);

        //third put key=3, value = 4, (1,2) is removed
        int[] functionParameters10 = new int[]{5,6};
        lruCachePUT.run(LruCacheMethod.Put,
                functionParameters10,
                null);

        //post get key = 2
        int[] functionParameters11 = new int[]{1};
        lruCachePUT.run(LruCacheMethod.Get,
                functionParameters11,
                -1);

    }

    @Test
    public void test0Put1GetSize2(){
        //constructor size = 2
        int[] functionParameters = new int[]{2};
        lruCachePUT.run(LruCacheMethod.Constructor,
                functionParameters,
                null);

        //get key = 1
        int[] functionParameters3 = new int[]{1};
        lruCachePUT.run(LruCacheMethod.Get,
                functionParameters3,
                -1);
    }

    @Test
    public void testPutSameKeyTwice(){
        //constructor size = 2
        int[] functionParameters = new int[]{2};
        lruCachePUT.run(LruCacheMethod.Constructor,
                functionParameters,
                null);

        //put key=1, value = 2
        int[] functionParameters2 = new int[]{1,2};
        lruCachePUT.run(LruCacheMethod.Put,
                functionParameters2,
                null);

        //put key=1, value = 4
        int[] functionParameters3 = new int[]{1,4};
        lruCachePUT.run(LruCacheMethod.Put,
                functionParameters3,
                null);

        //get key = 1
        int[] functionParameters4 = new int[]{1};
        lruCachePUT.run(LruCacheMethod.Get,
                functionParameters4,
                4);
    }

    @Test
    public void testPutNegativeKey(){
        //constructor size = 2
        int[] functionParameters = new int[]{2};
        lruCachePUT.run(LruCacheMethod.Constructor,
                functionParameters,
                null);

        //put key=-1, value = 2
        int[] functionParameters2 = new int[]{-1,2};
        lruCachePUT.run(LruCacheMethod.Put,
                functionParameters2,
                null);
        //get key = -1
        int[] functionParameters4 = new int[]{-1};
        lruCachePUT.run(LruCacheMethod.Get,
                functionParameters4,
                2);
    }

    @Test
    public void testPutNegativeValue(){
        //constructor size = 2
        int[] functionParameters = new int[]{2};
        lruCachePUT.run(LruCacheMethod.Constructor,
                functionParameters,
                null);

        //put key=1, value = -2
        int[] functionParameters2 = new int[]{1,-2};
        lruCachePUT.run(LruCacheMethod.Put,
                functionParameters2,
                null);
        //get key = 1
        int[] functionParameters4 = new int[]{1};
        lruCachePUT.run(LruCacheMethod.Get,
                functionParameters4,
                -2);
    }

    @Test
    public void testPutNegativeKeyValue(){
        //constructor size = 2
        int[] functionParameters = new int[]{2};
        lruCachePUT.run(LruCacheMethod.Constructor,
                functionParameters,
                null);

        //put key=-1, value = -2
        int[] functionParameters2 = new int[]{-1,-2};
        lruCachePUT.run(LruCacheMethod.Put,
                functionParameters2,
                null);
        //get key = -1
        int[] functionParameters4 = new int[]{-1};
        lruCachePUT.run(LruCacheMethod.Get,
                functionParameters4,
                -2);
    }

    @Test
    public void testPutZeroKeyValue(){
        //constructor size = 2
        int[] functionParameters = new int[]{2};
        lruCachePUT.run(LruCacheMethod.Constructor,
                functionParameters,
                null);

        //put key=0, value = -2
        int[] functionParameters2 = new int[]{0,-2};
        lruCachePUT.run(LruCacheMethod.Put,
                functionParameters2,
                null);
        //get key = -1
        int[] functionParameters4 = new int[]{0};
        lruCachePUT.run(LruCacheMethod.Get,
                functionParameters4,
                -2);
    }

    @Test
    public void testPutSameValueTwice(){
        //constructor size = 2
        int[] functionParameters = new int[]{2};
        lruCachePUT.run(LruCacheMethod.Constructor,
                functionParameters,
                null);

        //put key=1, value = 2
        int[] functionParameters2 = new int[]{1,2};
        lruCachePUT.run(LruCacheMethod.Put,
                functionParameters2,
                null);

        //put key=1, value = 4
        int[] functionParameters3 = new int[]{2,2};
        lruCachePUT.run(LruCacheMethod.Put,
                functionParameters3,
                null);

        //get key = 1
        int[] functionParameters4 = new int[]{1};
        lruCachePUT.run(LruCacheMethod.Get,
                functionParameters4,
                2);

        //get key = 1
        int[] functionParameters5 = new int[]{2};
        lruCachePUT.run(LruCacheMethod.Get,
                functionParameters5,
                2);
    }

    @Test
    public void testPutSameKeyValueTwice(){
        //constructor size = 2
        int[] functionParameters = new int[]{2};
        lruCachePUT.run(LruCacheMethod.Constructor,
                functionParameters,
                null);

        //put key=1, value = 2
        int[] functionParameters2 = new int[]{1,2};
        lruCachePUT.run(LruCacheMethod.Put,
                functionParameters2,
                null);

        //put key=1, value = 4
        int[] functionParameters3 = new int[]{1,2};
        lruCachePUT.run(LruCacheMethod.Put,
                functionParameters3,
                null);

        //get key = 1
        int[] functionParameters4 = new int[]{1};
        lruCachePUT.run(LruCacheMethod.Get,
                functionParameters4,
                2);
    }


//////////////////////////////

    @Test
    public void test1Put1GetSize3(){
        //constructor size = 2
        int[] functionParameters = new int[]{3};
        lruCachePUT.run(LruCacheMethod.Constructor,
                functionParameters,
                null);

        //put key=1, value = 2
        int[] functionParameters2 = new int[]{1,2};
        lruCachePUT.run(LruCacheMethod.Put,
                functionParameters2,
                null);

        //get key = 1
        int[] functionParameters3 = new int[]{1};
        lruCachePUT.run(LruCacheMethod.Get,
                functionParameters3,
                2);
    }

    @Test
    public void test3Put3GetSize3(){
        //constructor size = 2
        int[] functionParameters = new int[]{3};
        lruCachePUT.run(LruCacheMethod.Constructor,
                functionParameters,
                null);

        //first put key=1, value = 2
        int[] functionParameters2 = new int[]{1,2};
        lruCachePUT.run(LruCacheMethod.Put,
                functionParameters2,
                null);

        //second put key=2, value = 3
        int[] functionParameters3 = new int[]{2,3};
        lruCachePUT.run(LruCacheMethod.Put,
                functionParameters3,
                null);

        //first get key = 1, (2,3) is now least used
        int[] functionParameters6 = new int[]{1};
        lruCachePUT.run(LruCacheMethod.Get,
                functionParameters6,
                2);

        //third put key=3, value = 4, (2,3) is removed
        int[] functionParameters4 = new int[]{3,4};
        lruCachePUT.run(LruCacheMethod.Put,
                functionParameters4,
                null);

        //second get key = 1
        int[] functionParameters5 = new int[]{1};
        lruCachePUT.run(LruCacheMethod.Get,
                functionParameters5,
                2);

        //third get key = 3
        int[] functionParameters8 = new int[]{3};
        lruCachePUT.run(LruCacheMethod.Get,
                functionParameters8,
                4);

        //post get key = 2,
        int[] functionParameters7 = new int[]{2};
        lruCachePUT.run(LruCacheMethod.Get,
                functionParameters7,
                3);
    }

    @Test
    public void test4Put3GetSize3(){
        //constructor size = 2
        int[] functionParameters = new int[]{3};
        lruCachePUT.run(LruCacheMethod.Constructor,
                functionParameters,
                null);

        //first put key=1, value = 2
        int[] functionParameters2 = new int[]{1,2};
        lruCachePUT.run(LruCacheMethod.Put,
                functionParameters2,
                null);

        //second put key=2, value = 3
        int[] functionParameters3 = new int[]{2,3};
        lruCachePUT.run(LruCacheMethod.Put,
                functionParameters3,
                null);

        //first get key = 1, (2,3) is now least used
        int[] functionParameters6 = new int[]{1};
        lruCachePUT.run(LruCacheMethod.Get,
                functionParameters6,
                2);

        //third put key=3, value = 4
        int[] functionParameters4 = new int[]{3,4};
        lruCachePUT.run(LruCacheMethod.Put,
                functionParameters4,
                null);

        //third put key=3, value = 4, (2,3) is removed
        int[] functionParameters9 = new int[]{6,10};
        lruCachePUT.run(LruCacheMethod.Put,
                functionParameters9,
                null);

        //second get key = 1
        int[] functionParameters5 = new int[]{1};
        lruCachePUT.run(LruCacheMethod.Get,
                functionParameters5,
                2);

        //third get key = 3
        int[] functionParameters8 = new int[]{3};
        lruCachePUT.run(LruCacheMethod.Get,
                functionParameters8,
                4);

        //post get key = 2,
        int[] functionParameters7 = new int[]{2};
        lruCachePUT.run(LruCacheMethod.Get,
                functionParameters7,
                -1);
    }

    @Test
    public void test3Put1GetSize3(){
        //constructor size = 2
        int[] functionParameters = new int[]{3};
        lruCachePUT.run(LruCacheMethod.Constructor,
                functionParameters,
                null);

        //first put key=1, value = 2
        int[] functionParameters2 = new int[]{1,2};
        lruCachePUT.run(LruCacheMethod.Put,
                functionParameters2,
                null);

        //second put key=2, value = 3
        int[] functionParameters3 = new int[]{2,3};
        lruCachePUT.run(LruCacheMethod.Put,
                functionParameters3,
                null);

        //third put key=3, value = 4, (1,2) is removed
        int[] functionParameters4 = new int[]{3,4};
        lruCachePUT.run(LruCacheMethod.Put,
                functionParameters4,
                null);

        //first get key = 1, (1,2) should be present
        int[] functionParameters5 = new int[]{1};
        lruCachePUT.run(LruCacheMethod.Get,
                functionParameters5,
                2);

        //post get key = 2
        int[] functionParameters7 = new int[]{2};
        lruCachePUT.run(LruCacheMethod.Get,
                functionParameters7,
                3);
    }

    @Test
    public void test4Put1GetSize3(){
        //constructor size = 2
        int[] functionParameters = new int[]{3};
        lruCachePUT.run(LruCacheMethod.Constructor,
                functionParameters,
                null);

        //first put key=1, value = 2
        int[] functionParameters2 = new int[]{1,2};
        lruCachePUT.run(LruCacheMethod.Put,
                functionParameters2,
                null);

        //second put key=2, value = 3
        int[] functionParameters3 = new int[]{2,3};
        lruCachePUT.run(LruCacheMethod.Put,
                functionParameters3,
                null);

        //third put key=3, value = 4
        int[] functionParameters4 = new int[]{3,4};
        lruCachePUT.run(LruCacheMethod.Put,
                functionParameters4,
                null);

        //third put key=3, value = 4, (1,2) is removed
        int[] functionParameters9 = new int[]{6,10};
        lruCachePUT.run(LruCacheMethod.Put,
                functionParameters9,
                null);

        //first get key = 1, (1,2) should not be present
        int[] functionParameters5 = new int[]{1};
        lruCachePUT.run(LruCacheMethod.Get,
                functionParameters5,
                -1);

        //post get key = 2
        int[] functionParameters7 = new int[]{2};
        lruCachePUT.run(LruCacheMethod.Get,
                functionParameters7,
                3);
    }

    @Test
    public void test0Put1GetSize3(){
        //constructor size = 2
        int[] functionParameters = new int[]{3};
        lruCachePUT.run(LruCacheMethod.Constructor,
                functionParameters,
                null);

        //get key = 1
        int[] functionParameters3 = new int[]{1};
        lruCachePUT.run(LruCacheMethod.Get,
                functionParameters3,
                -1);
    }

    @Test
    public void testPutSameKeyTwiceSize3(){
        //constructor size = 2
        int[] functionParameters = new int[]{3};
        lruCachePUT.run(LruCacheMethod.Constructor,
                functionParameters,
                null);

        //put key=1, value = 2
        int[] functionParameters2 = new int[]{1,2};
        lruCachePUT.run(LruCacheMethod.Put,
                functionParameters2,
                null);

        //put key=1, value = 4
        int[] functionParameters3 = new int[]{1,4};
        lruCachePUT.run(LruCacheMethod.Put,
                functionParameters3,
                null);

        //get key = 1
        int[] functionParameters4 = new int[]{1};
        lruCachePUT.run(LruCacheMethod.Get,
                functionParameters4,
                4);
    }

    @Test
    public void testPutNegativeKeySize3(){
        //constructor size = 2
        int[] functionParameters = new int[]{3};
        lruCachePUT.run(LruCacheMethod.Constructor,
                functionParameters,
                null);

        //put key=-1, value = 2
        int[] functionParameters2 = new int[]{-1,2};
        lruCachePUT.run(LruCacheMethod.Put,
                functionParameters2,
                null);
        //get key = -1
        int[] functionParameters4 = new int[]{-1};
        lruCachePUT.run(LruCacheMethod.Get,
                functionParameters4,
                2);
    }

    @Test
    public void testPutNegativeValueSize3(){
        //constructor size = 2
        int[] functionParameters = new int[]{3};
        lruCachePUT.run(LruCacheMethod.Constructor,
                functionParameters,
                null);

        //put key=1, value = -2
        int[] functionParameters2 = new int[]{1,-2};
        lruCachePUT.run(LruCacheMethod.Put,
                functionParameters2,
                null);
        //get key = 1
        int[] functionParameters4 = new int[]{1};
        lruCachePUT.run(LruCacheMethod.Get,
                functionParameters4,
                -2);
    }

    @Test
    public void testPutNegativeKeyValueSize3(){
        //constructor size = 2
        int[] functionParameters = new int[]{3};
        lruCachePUT.run(LruCacheMethod.Constructor,
                functionParameters,
                null);

        //put key=-1, value = -2
        int[] functionParameters2 = new int[]{-1,-2};
        lruCachePUT.run(LruCacheMethod.Put,
                functionParameters2,
                null);
        //get key = -1
        int[] functionParameters4 = new int[]{-1};
        lruCachePUT.run(LruCacheMethod.Get,
                functionParameters4,
                -2);
    }

    @Test
    public void testPutZeroKeyValueSize3(){
        //constructor size = 2
        int[] functionParameters = new int[]{3};
        lruCachePUT.run(LruCacheMethod.Constructor,
                functionParameters,
                null);

        //put key=0, value = -2
        int[] functionParameters2 = new int[]{0,-2};
        lruCachePUT.run(LruCacheMethod.Put,
                functionParameters2,
                null);
        //get key = -1
        int[] functionParameters4 = new int[]{0};
        lruCachePUT.run(LruCacheMethod.Get,
                functionParameters4,
                -2);
    }

    @Test
    public void testPutSameValueTwiceSize3(){
        //constructor size = 2
        int[] functionParameters = new int[]{3};
        lruCachePUT.run(LruCacheMethod.Constructor,
                functionParameters,
                null);

        //put key=1, value = 2
        int[] functionParameters2 = new int[]{1,2};
        lruCachePUT.run(LruCacheMethod.Put,
                functionParameters2,
                null);

        //put key=1, value = 4
        int[] functionParameters3 = new int[]{2,2};
        lruCachePUT.run(LruCacheMethod.Put,
                functionParameters3,
                null);

        //get key = 1
        int[] functionParameters4 = new int[]{1};
        lruCachePUT.run(LruCacheMethod.Get,
                functionParameters4,
                2);

        //get key = 1
        int[] functionParameters5 = new int[]{2};
        lruCachePUT.run(LruCacheMethod.Get,
                functionParameters5,
                2);
    }

    @Test
    public void testPutSameKeyValueTwiceSize3(){
        //constructor size = 2
        int[] functionParameters = new int[]{3};
        lruCachePUT.run(LruCacheMethod.Constructor,
                functionParameters,
                null);

        //put key=1, value = 2
        int[] functionParameters2 = new int[]{1,2};
        lruCachePUT.run(LruCacheMethod.Put,
                functionParameters2,
                null);

        //put key=1, value = 4
        int[] functionParameters3 = new int[]{1,2};
        lruCachePUT.run(LruCacheMethod.Put,
                functionParameters3,
                null);

        //get key = 1
        int[] functionParameters4 = new int[]{1};
        lruCachePUT.run(LruCacheMethod.Get,
                functionParameters4,
                2);
    }

    @Test
    public void test7Put4GetSize3(){
        //constructor size = 2
        int[] functionParameters = new int[]{3};
        lruCachePUT.run(LruCacheMethod.Constructor,
                functionParameters,
                null);

        //first put key=1, value = 2
        int[] functionParameters2 = new int[]{1,2};
        lruCachePUT.run(LruCacheMethod.Put,
                functionParameters2,
                null);

        //second put key=2, value = 3
        int[] functionParameters3 = new int[]{2,3};
        lruCachePUT.run(LruCacheMethod.Put,
                functionParameters3,
                null);

        //third put key=3, value = 4
        int[] functionParameters4 = new int[]{3,4};
        lruCachePUT.run(LruCacheMethod.Put,
                functionParameters4,
                null);

        //third put key=3, value = 4, (1,2) is removed
        int[] functionParameters9 = new int[]{6,10};
        lruCachePUT.run(LruCacheMethod.Put,
                functionParameters9,
                null);

        //third put key=3, value = 4
        int[] functionParameters10 = new int[]{-5,-9};
        lruCachePUT.run(LruCacheMethod.Put,
                functionParameters10,
                null);

        //first get key = 1, (1,2) should not be present
        int[] functionParameters5 = new int[]{1};
        lruCachePUT.run(LruCacheMethod.Get,
                functionParameters5,
                -1);

        //post get key = 2
        int[] functionParameters7 = new int[]{2};
        lruCachePUT.run(LruCacheMethod.Get,
                functionParameters7,
                -1);

        //third put key=3, value = 4
        int[] functionParameters11 = new int[]{0,50};
        lruCachePUT.run(LruCacheMethod.Put,
                functionParameters11,
                null);

        //third put key=3, value = 4, (1,2) is removed
        int[] functionParameters12 = new int[]{-700,7000};
        lruCachePUT.run(LruCacheMethod.Put,
                functionParameters12,
                null);

        //post get key = 2
        int[] functionParameters13 = new int[]{1};
        lruCachePUT.run(LruCacheMethod.Get,
                functionParameters13,
                -1);
    }


    @Test
    public void test7Put4GetSize4(){
        //constructor size = 2
        int[] functionParameters = new int[]{4};
        lruCachePUT.run(LruCacheMethod.Constructor,
                functionParameters,
                null);

        //first put key=1, value = 2
        int[] functionParameters2 = new int[]{1,2};
        lruCachePUT.run(LruCacheMethod.Put,
                functionParameters2,
                null);

        //second put key=2, value = 3
        int[] functionParameters3 = new int[]{2,3};
        lruCachePUT.run(LruCacheMethod.Put,
                functionParameters3,
                null);

        //third put key=3, value = 4
        int[] functionParameters4 = new int[]{3,4};
        lruCachePUT.run(LruCacheMethod.Put,
                functionParameters4,
                null);

        //third put key=3, value = 4, (1,2) is removed
        int[] functionParameters9 = new int[]{6,10};
        lruCachePUT.run(LruCacheMethod.Put,
                functionParameters9,
                null);

        //third put key=3, value = 4
        int[] functionParameters10 = new int[]{-5,-9};
        lruCachePUT.run(LruCacheMethod.Put,
                functionParameters10,
                null);

        //first get key = 1, (1,2) should not be present
        int[] functionParameters5 = new int[]{1};
        lruCachePUT.run(LruCacheMethod.Get,
                functionParameters5,
                -1);

        //post get key = 2
        int[] functionParameters7 = new int[]{2};
        lruCachePUT.run(LruCacheMethod.Get,
                functionParameters7,
                3);

        //third put key=3, value = 4
        int[] functionParameters11 = new int[]{0,50};
        lruCachePUT.run(LruCacheMethod.Put,
                functionParameters11,
                null);

        //third put key=3, value = 4, (1,2) is removed
        int[] functionParameters12 = new int[]{-700,7000};
        lruCachePUT.run(LruCacheMethod.Put,
                functionParameters12,
                null);

        //post get key = 2
        int[] functionParameters13 = new int[]{1};
        lruCachePUT.run(LruCacheMethod.Get,
                functionParameters13,
                -1);
    }
    //@TODO: Create more tests
}
