package AnimalExercise;

public class Cow extends Animal{
    public Cow(String backpack){
        super(backpack);
    }

    public Cow(){
        this("Grass");
    }

    @Override
    public String speak() {
        return "Moo";
    }
}
