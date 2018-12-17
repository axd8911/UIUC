package Pizza;
import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

/**
 * Created by Felix on 23.02.2016(https://github.com/Felix0301/DebuggingSession).
 */
public class PizzaParty {

    public Pizza pizza;
    public String hostName;
    public Stack beers = new Stack();
    public List<Guest> guests;
    //how many calories were consumend?
    public int consumedCalories;
    private double cc;
    //how many calories did the host provide?
    public int providedCalories;

    public PizzaParty(int nrOfGuests, Topping pizzaTopping, int nrOfBeers){
        //add all groceries to list of provided calories
        providedCalories = pizzaTopping.calories + nrOfBeers * Drink.BEER.calories + Drink.BIRTHDAYSHOT.calories;

        //invite guests
        guests = new ArrayList<Guest>();
        for (int i = 0; i < nrOfGuests; i++){
            guests.add(new Guest(Util.NAMES[i]));
        }

        //define host
        hostName =  guests.get(nrOfGuests-1).name;

        //buy beer
        for(int i = 0; i < nrOfBeers; i++){
            beers.push(Drink.BEER);
        }

        //order and slice Pizza
        pizza = new Pizza(pizzaTopping);
        pizza.slice(nrOfGuests);
    }

    public void startTheFeast(){
        //eat pizza and drink beer
        for (Guest guest : guests){
            guest.takeSlice(pizza);
            guest.drink(beers);

        }

        int guestCounter = 0;

        while (!beers.empty()) {
            guests.get(guestCounter).drink(beers);

            guestCounter = (guestCounter + 1) % guests.size();
        }

        //give special birthday shot to host
        for (Guest guest : guests){
            if (guest.name.equals(hostName)) {
                guest.consume(Drink.BIRTHDAYSHOT.calories);
            }
        }
        countCalories();
    }

    private void countCalories() {
        for (Guest guest : guests){
            cc += guest.consumedCalories;
        }
        consumedCalories = (int) cc;
    }
}
