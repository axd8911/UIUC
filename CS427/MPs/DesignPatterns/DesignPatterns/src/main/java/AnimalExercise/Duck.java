package AnimalExercise;

public class Duck extends Animal{
    public Duck(String backpack){
        super(backpack);
    }

    public Duck(){
        this("Rice");
    }

    @Override
    public String speak() {
        return "Quack";
    }
}
