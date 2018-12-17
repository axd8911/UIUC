package Pizza;

import java.util.Stack;

/**
 * Created by Felix on 23.02.2016.
 */
public class Pizza {

    public Stack slices;
    public double calories;

    public Pizza(Topping topping){
        this.calories = (double)topping.calories;
    }

    public void slice(int nrOfSlices){
        slices = new Stack();
        for (int i = 0;i < nrOfSlices; i++){
            slices.push(new Slice(calories/(nrOfSlices+0.0)));
        }
    }
}
