package AnimalExercise;

import java.text.SimpleDateFormat;
import java.util.Date;

public class AnimalFactory {

    public static Animal create(AnimalType type){
        Animal result = null;
        int age = Integer.parseInt((new SimpleDateFormat("dd")).format(new Date()));
        switch(type){
            case Cat:
                result = new Cat();
                break;
            case Dog:
                result = new Dog();
                break;
            case Duck:
                result = new Duck();
                break;
            case Cow:
                result = new Cow();
                break;
        }

        result.setAge(age);

        return result;
    }
}
