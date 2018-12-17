package StockExercise;

import StockExercise.Given.ObservableStock;
import StockExercise.Given.ObserverStockExchangeCenter;
import StockExercise.Given.StockType;

public class ObserverStockExchangeCenterImpl extends ObserverStockExchangeCenter {
    public ObserverStockExchangeCenterImpl() {
        super();
    }

    public void notifyChange(StockType type, double price){
        ownedStock.put(type, price);
    }

    //@TODO: Override any necessary methods

    public void observe(ObservableStock o){
        o.registerStockExchangeCenter(this);
    }

    @Override
    public void buyStock(ObservableStock s) {
        super.buyStock(s);
        observe(s);
    }
}
