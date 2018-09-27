public class Cell {
    // 54 total
    
    private final int color;

    private int side;


    public static final int RED = 0;
    public static final int ORANGE = 1;
    public static final int BLUE = 2;
    public static final int GREEN = 3;
    public static final int YELLOW = 4;
    public static final int WHITE = 5;


    public Cell(int color) {
        this.color = color;
        this.side = color;
    }

    public int getColor() {
        return color;
    }



    public int getSide() {
        return side;
    }


    public void setSide(int side) {
        this.side = side;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null) return false;
        if (getClass() != obj.getClass()) return false;
        Cell other = (Cell) obj;
        if (color != other.color) return false;

        return false;
    }
}
