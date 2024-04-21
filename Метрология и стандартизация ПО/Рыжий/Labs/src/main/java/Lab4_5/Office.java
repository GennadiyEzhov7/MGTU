package Lab4_5;

public class Office implements Room {
    private final int placesCount;

    public Office(int placesCount) {
        super();
        this.placesCount = placesCount;
    }

    @Override
    public String getInfo() {
        return "Work places count: " + placesCount;
    }
}
