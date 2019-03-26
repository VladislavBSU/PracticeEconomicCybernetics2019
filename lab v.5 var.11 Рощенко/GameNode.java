package pack;

class GameNode {
    private String name;
    private int year;
    private String system;
    private int price;

    GameNode(String name, String year, String system, String price) {
        this.name = name;
        try {
            this.year = Integer.parseInt(year);
        } catch (Exception e) {
            this.year = -1;
        }
        this.system = system;
        try {
            this.price = Integer.parseInt(price);
        } catch (Exception e) {
            this.price = -1;
        }
    }

    String getName() {
        return name;
    }

    String getYear() {
        return Integer.toString(year);
    }

    String getSystem() {
        return system;
    }

    int getPrice() {
        return price;
    }
}
