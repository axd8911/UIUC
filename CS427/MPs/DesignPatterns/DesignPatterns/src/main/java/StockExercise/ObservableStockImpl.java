package StockExercise;

import StockExercise.Given.ObservableStock;
import StockExercise.Given.ObserverStockExchangeCenter;
import StockExercise.Given.StockType;

import java.util.ArrayList;
import java.util.List;

public class ObservableStockImpl extends ObservableStock {

    //@TODO: Add any necessary fields
    private List<ObserverStockExchangeCenter> observers = new ArrayList<>();

    public ObservableStockImpl(StockType name){
        super(name);
        super.setPrice(0);
    }

    public void notifyPriceChange(double price){
        for (ObserverStockExchangeCenter o : observers){
            o.notifyChange(super.getName(), price);
        }
    }
    public void registerStockExchangeCenter(ObserverStockExchangeCenter oc){
        observers.add(oc);
    }

    @Override
    public void setPrice(double price) {
        super.setPrice(price);
        notifyPriceChange(price);
    }
}
