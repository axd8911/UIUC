package AnimalExercise;

public class Cat extends Animal {

    public Cat(String backpack){
        super(backpack);
    }

    public Cat(){
        this("Salmon");
    }

    @Override
    public String speak() {
        return "Meow";
    }
}
