package objects;

public class Apple {

    public int posX;
    public int posY;

    private int width;
    private int height;

    public Apple(int startX, int startY, int width, int height) {
        this.posX = startX;
        this.posY = startY;
        this.width = width;
        this.height = height;
    }

    public void setRandomPosition() {
        posX = (int) (Math.random() * width);
        posY = (int) (Math.random() * height);
    }
}
