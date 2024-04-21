package Lab4_5;

public class Shop implements Room{
    final String name;

    public Shop(String name) {
        super();
        this.name = name;
    }

    @Override
    public String getInfo() {
        return "Shop name: " + name;
    }
}
