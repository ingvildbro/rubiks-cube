public class Cell {
    // 54 total
    private int x;
    private int y;
    private int z;
    private int color;

    public Cell(int x, int y, int z, int color) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.color = color;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getZ() {
        return z;
    }

    public int getColor() {
        return color;
    }

}