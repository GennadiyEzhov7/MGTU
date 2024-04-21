package Lab4_5;

public class Flat implements Room {
    private final int roomsCount;

    public Flat(int roomsCount) {
        super();
        this.roomsCount = roomsCount;
    }

    @Override
    public String getInfo() {
        return "Rooms count: " + roomsCount;
    }
}
