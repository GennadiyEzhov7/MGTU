package Lab4_5;

public class Technical implements Room{
    private final String function;

    public Technical(String function) {
        this.function = function;
    }
    @Override
    public String getInfo() {
        return "Used for " + function;
    }
}
