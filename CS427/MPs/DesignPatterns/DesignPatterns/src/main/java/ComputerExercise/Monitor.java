package ComputerExercise;

public class Monitor extends ComputerPeripheral{
    public Monitor(Computer computer) {
        super(computer);
    }

    @Override
    public String run() {
        return "Monitor turned on\n" + super.run() + "Display colorful interface\n";
    }

    @Override
    public String getDescription() {
        return super.getDescription() + " with monitor";
    }
}
