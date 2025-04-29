public class Flower {
    private String name;
    private String color;
    private double height;
    private boolean fragrance;

    // 생성자
    public Flower(String name, String color, double height, boolean fragrance) {
        this.name = name;
        this.color = color;
        this.height = height;
        this.fragrance = fragrance;
    }

    // 꽃 정보 출력 메소드
    public void displayInfo() {
        System.out.println("꽃 이름: " + name);
        System.out.println("색깔: " + color);
        System.out.println("키: " + height + " cm");
        System.out.println("향기가 있나요?: " + (fragrance ? "네" : "아니오"));
        System.out.println("------------------------");
    }
}

// 메인 실행 클래스
class Main {
    public static void main(String[] args) {
        Flower rose = new Flower("장미", "빨강", 55.2, true);
        Flower tulip = new Flower("튤립", "노랑", 40.0, false);
        Flower lavender = new Flower("라벤더", "보라", 60.0, true);
        Flower sunflower = new Flower("해바라기", "노랑", 150.5, false);

        rose.displayInfo();
        tulip.displayInfo();
        lavender.displayInfo();
        sunflower.displayInfo();
    }
}
