package AnimalExercise;

public class Dog extends Animal{
    public Dog(String backpack){
        super(backpack);
    }

    public Dog(){
        this("Bone");
    }

    @Override
    public String speak() {
        return "Woof";
    }
}
