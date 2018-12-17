package BlackBox;

import BlackBox.Setups.SortSetup;
import org.junit.After;
import org.junit.Test;

import java.util.Arrays;
import java.util.Random;

public class SortAlgorithmBlackBoxTest extends SortSetup {


    @Test
    public void testOneNegative(){
        int[] input = new int[]{-1};
        int[] expectedOutput = new int[]{-1};
        sortAlgorithmPUT.run(input,expectedOutput);
    }

    @Test
    public void test2Input23(){
        int[] input = new int[]{2,1};
        int[] expectedOutput = new int[]{1,2};
        sortAlgorithmPUT.run(input,expectedOutput);
    }

    @Test
    public void test2Input(){
        int[] input = new int[]{-1,-2};
        int[] expectedOutput = new int[]{-2,-1};
        sortAlgorithmPUT.run(input,expectedOutput);
    }

    @Test
    public void test2Input1(){
        int[] input = new int[]{2,2};
        int[] expectedOutput = new int[]{2,2};
        sortAlgorithmPUT.run(input,expectedOutput);
    }

    @Test
    public void test2Input2(){
        int[] input = new int[]{1,2};
        int[] expectedOutput = new int[]{1,2};
        sortAlgorithmPUT.run(input,expectedOutput);
    }

    @Test
    public void test3Input34(){
        int[] input = new int[]{1,2,2};
        int[] expectedOutput = new int[]{1,2,2};
        sortAlgorithmPUT.run(input,expectedOutput);
    }

    @Test
    public void test3Input(){
        int[] input = new int[]{1,2,3};
        int[] expectedOutput = new int[]{1,2,3};
        sortAlgorithmPUT.run(input,expectedOutput);
    }


    @Test
    public void test2Input3(){
        int[] input = new int[]{-2,-2};
        int[] expectedOutput = new int[]{-2,-2};
        sortAlgorithmPUT.run(input,expectedOutput);
    }

    @Test
    public void test2Input4(){
        int[] input = new int[]{-3,-2};
        int[] expectedOutput = new int[]{-3,-2};
        sortAlgorithmPUT.run(input,expectedOutput);
    }

    @Test
    public void test3Input245(){
        int[] input = new int[]{-3,-3,-2};
        int[] expectedOutput = new int[]{-3,-3,-2};
        sortAlgorithmPUT.run(input,expectedOutput);
    }

    @Test
    public void test3Input256(){
        int[] input = new int[]{3,3,2};
        int[] expectedOutput = new int[]{2,3,3};
        sortAlgorithmPUT.run(input,expectedOutput);
    }

    @Test
    public void test3Input2(){
        int[] input = new int[]{-2,-3,-3};
        int[] expectedOutput = new int[]{-3,-3,-2};
        sortAlgorithmPUT.run(input,expectedOutput);
    }

    @Test
    public void test3Input367(){
        int[] input = new int[]{-4,-3,-2};
        int[] expectedOutput = new int[]{-4,-3,-2};
        sortAlgorithmPUT.run(input,expectedOutput);
    }

    @Test
    public void test3Input378(){
        int[] input = new int[]{-2,-3,-4};
        int[] expectedOutput = new int[]{-4,-3,-2};
        sortAlgorithmPUT.run(input,expectedOutput);
    }

    @Test
    public void test3Input3(){
        int[] input = new int[]{4,3,2};
        int[] expectedOutput = new int[]{2,3,4};
        sortAlgorithmPUT.run(input,expectedOutput);
    }


    @Test
    public void testEmptyInput(){
        int[] input = new int[]{};
        int[] expectedOutput = new int[]{};
        sortAlgorithmPUT.run(input,expectedOutput);
    }

    @Test
    public void testSingleInput(){
        int[] input = new int[]{1};
        int[] expectedOutput = new int[]{1};
        sortAlgorithmPUT.run(input,expectedOutput);
    }

    @Test
    public void testEvenInput(){
        int[] input = new int[]{2,1,5,67,23,4,2,8,9,7};
        int[] expectedOutput = input.clone();
        Arrays.sort(expectedOutput);

        sortAlgorithmPUT.run(input,expectedOutput);
    }

    @Test
    public void testOddInput(){
        int[] input = new int[]{2,1,5,67,23,4,2,8,9};
        int[] expectedOutput = input.clone();
        Arrays.sort(expectedOutput);

        sortAlgorithmPUT.run(input,expectedOutput);
    }

    @Test
    public void testAllNegativeInputEven(){
        int[] input = new int[]{-1,-2,-4,-3};
        int[] expectedOutput = new int[]{-4,-3,-2,-1};
        sortAlgorithmPUT.run(input,expectedOutput);
    }

    @Test
    public void testAllNegativeInputOdd(){
        int[] input = new int[]{-1,-2,-5,-4,-3};
        int[] expectedOutput = new int[]{-5,-4,-3,-2,-1};
        sortAlgorithmPUT.run(input,expectedOutput);
    }

    @Test
    public void testOddAroundZero(){
        int[] input = new int[]{-1,-2,0,4,3};
        int[] expectedOutput = input.clone();
        Arrays.sort(expectedOutput);
        sortAlgorithmPUT.run(input,expectedOutput);
    }

    @Test
    public void testEvenAroundZero(){
        int[] input = new int[]{-1,-2,0,4,3,5};
        int[] expectedOutput = input.clone();
        Arrays.sort(expectedOutput);
        sortAlgorithmPUT.run(input,expectedOutput);
    }

    @Test
    public void testSomeNegativeInput(){
        int[] input = new int[]{-1,-2,4,5,-10};
        int[] expectedOutput = input.clone();
        Arrays.sort(expectedOutput);

        sortAlgorithmPUT.run(input,expectedOutput);
    }

    @Test
    public void testSomeDuplicatedInput(){
        int[] input = new int[]{-1,-2,4,4,-10};
        int[] expectedOutput = input.clone();
        Arrays.sort(expectedOutput);

        sortAlgorithmPUT.run(input,expectedOutput);
    }

    @Test
    public void testAlreadySortedInput(){
        int[] input = new int[]{1,2,3,4,5};
        int[] expectedOutput = input.clone();
        Arrays.sort(expectedOutput);

        sortAlgorithmPUT.run(input,expectedOutput);
    }

    @Test
    public void testReverseOrderInput(){
        int[] input = new int[]{5,4,3,2,1};
        int[] expectedOutput = input.clone();
        Arrays.sort(expectedOutput);

        sortAlgorithmPUT.run(input,expectedOutput);
    }

    @Test
    public void testNearlySortedInput(){
        int[] input = new int[]{2,3,4,5,6,7,1};
        int[] expectedOutput = input.clone();
        Arrays.sort(expectedOutput);

        sortAlgorithmPUT.run(input,expectedOutput);
    }

    @Test
    public void testFewUnique(){
        int[] input = new int[]{5,5,3,3,1,1,2,2,4,4,4};
        int[] expectedOutput = input.clone();
        Arrays.sort(expectedOutput);

        sortAlgorithmPUT.run(input,expectedOutput);
    }

    @Test
    public void testSortedInCycle(){
        int[] input = new int[]{4,5,6,7,1,2,3};
        int[] expectedOutput = input.clone();
        Arrays.sort(expectedOutput);

        sortAlgorithmPUT.run(input,expectedOutput);
    }

    @Test
    public void testSortedInCycleReverse(){
        int[] input = new int[]{3,2,1,7,6,5,4};
        int[] expectedOutput = input.clone();
        Arrays.sort(expectedOutput);

        sortAlgorithmPUT.run(input,expectedOutput);
    }

    @Test
    public void testOutOfOrderDuplicates(){
        int[] input = new int[]{4,5,6,-5,7,1,0,2,3,4,5,6,7,0,-5,1,2,3};
        int[] expectedOutput = input.clone();
        Arrays.sort(expectedOutput);

        sortAlgorithmPUT.run(input,expectedOutput);
    }

    @Test
    public void testAllSameInput(){
        int[] input = new int[]{5,5,5,5,5};
        int[] expectedOutput = input.clone();
        Arrays.sort(expectedOutput);

        sortAlgorithmPUT.run(input,expectedOutput);
    }

    @Test
    public void testFullHouse(){
        int[] input = new int[]{5,5,2,2,5};
        int[] expectedOutput = input.clone();
        Arrays.sort(expectedOutput);

        sortAlgorithmPUT.run(input,expectedOutput);
    }

    @Test
    public void testAllSameButOne(){
        int[] input = new int[]{5,5,-2,5,5};
        int[] expectedOutput = input.clone();
        Arrays.sort(expectedOutput);

        sortAlgorithmPUT.run(input,expectedOutput);
    }

    @Test
    public void testAllSameButOneBig(){
        int[] input = new int[]{500,500,-200,500,500};
        int[] expectedOutput = input.clone();
        Arrays.sort(expectedOutput);

        sortAlgorithmPUT.run(input,expectedOutput);
    }

    @Test
    public void testEverything(){
        Random rand = new Random();


        int[][] inputs = new int[][] {
                                         {},
                                         { 0 },
                                         { 0, 0 },
                                         { 0, 0, 0 },
                                         { 0, 1 },
                                         { 1, 0 },
                                         { 0, 1, 2 },
                                         { 0, 2, 1 },
                                         { 1, 0, 2 },
                                         { 1, 2, 0 },
                                         { 2, 0, 1 },
                                         { 2, 1, 0 },
                                         { 0, 1, 1 },
                                         { 1, 0, 1 },
                                         { 1, 1, 0 },
                                         { 1, 2, 3, 4, 5, 6, 7, 8, 9, 10 },
                                         { 10, 9, 8, 7, 6, 5, 4, 3, 2, 1 },
                                         { 42, 9, 17, 54, 602, -3, 54, 999, -11 },
                                         { -11, -3, 9, 17, 42, 54, 54, 602, 999 }};

        for (int[] input : inputs){
            int[] expectedOutput = input.clone();
            Arrays.sort(expectedOutput);
            sortAlgorithmPUT.run(input,expectedOutput);
        }

    }

    @Test
    public void testRandomsEvenSize(){
        Random rand = new Random();


        int[][] inputs = new int[100][10];

        for (int i = 0; i < inputs.length; i++){
            for (int j = 0; j < inputs[i].length; j++){
                inputs[i][j] = rand.nextInt() * (rand.nextBoolean() ? -1 : 1);
            }
        }

        for (int[] input : inputs){
            int[] expectedOutput = input.clone();
            Arrays.sort(expectedOutput);
            sortAlgorithmPUT.run(input,expectedOutput);
        }

    }

    @Test
    public void testRandomsOddSize(){
        Random rand = new Random();


        int[][] inputs = new int[100][11];

        for (int i = 0; i < inputs.length; i++){
            for (int j = 0; j < inputs[i].length; j++){
                inputs[i][j] = rand.nextInt() * (rand.nextBoolean() ? -1 : 1);
            }
        }

        for (int[] input : inputs){
            int[] expectedOutput = input.clone();
            Arrays.sort(expectedOutput);
            sortAlgorithmPUT.run(input,expectedOutput);
        }

    }

    @After
    public void printResults(){
        String s;
        for (int[] i : sortAlgorithmPUT.inputs){
            s = "";
            for (int j : i){
                s += j + ", ";
            }
            System.out.println(s);
        }
    }

}
