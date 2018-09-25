import java.util.ArrayList;

public class Cube {
    // 26 total + 1 in center

    private ArrayList<Cell> cells = new ArrayList<>();

    public static final int FACELET_FRONT  = (1 << 0);
    public static final int FACELET_REAR   = (1 << 1);
    public static final int FACELET_LEFT   = (1 << 2);
    public static final int FACELET_RIGHT  = (1 << 3);
    public static final int FACELET_TOP    = (1 << 4);
    public static final int FACELET_BOTTOM = (1 << 5);

    public static final int START_COLOR_FRONT = 0; //RED
    public static final int START_COLOR_BACK = 1; //ORANGE
    public static final int START_COLOR_TOP = 2; //BLUE
    public static final int START_COLOR_BOT = 3; //GREEN
    public static final int START_COLOR_RIGHT = 4; //YELLOW
    public static final int START_COLOR_LEFT = 5; //WHITE


    private final int startX;
    private final int startY;
    private final int startZ;

    private int x;
    private int y;
    private int z;

    int front = START_COLOR_FRONT;
    int back = START_COLOR_BACK;
    int left = START_COLOR_LEFT;
    int right = START_COLOR_RIGHT;
    int top = START_COLOR_TOP;
    int bot = START_COLOR_BOT;


    public Cube(int startX, int startY, int startZ,
    int front, int back, int top, int bot, int right, int left) {
        this.startX = startX;
        this.startY = startY;
        this.startZ = startZ;

        this.x = startX;
        this.y = startY;
        this.z = startZ;

        this.front = front;
        this.back = back;
        this.top = top;
        this.bot = bot;
        this.right = right;
        this.left = left;
    }

    public Cube(int x, int y, int z) {
        this.startX = x;
        this.startY = y;
        this.startZ = z;
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

    public void setX(int x) {
        this.x = x;
    }

    public void setY(int y) {
        this.y = y;
    }

    public void setZ(int z) {
        this.z = z;
    }


    public Cube getCopy() {
        return new Cube(startX, startY, startZ, front, back, top, bot, right, left);
    }


    public ArrayList<Cell> getCells() {
        return cells;
    }

    /*
    public ArrayList<Cell> getCellsBySide() {
        ArrayList<Cell> cells1 = new ArrayList<>();

        for (Cell cell : cells) {
            if (cell.)
        }

        return cells1;
    }
    */

    public boolean addCell() {
        if (cells.size() >= 54) {
            return false;
        }

        for (Cell c : cells) {
            //if (cells.)
        }
        return false;
    }


    public boolean setNewPosition() {
        for (Cell c : cells) {
            //if ()
        }

        return false;
    }


    public Cell findSide(int side) {
        for (Cell cell : cells) {
            if (cell.getSide() == side) {
                return cell;
            }
        }
        return null;
    }


    public Cell createCell(int color) {
        for (Cell c : cells) {
            if (c.getColor() == color) {
                return c;
            }
        }

        Cell newCell = new Cell(color);
        addCell(newCell);
        return newCell;
    }

    public void addCell(Cell cell) {
        cells.add(cell);
    }

    public void updateCell(Cell cell) {
        //cell.setX();
    }

    public Cell findCellsByPos(int x, int y, int z) {
        for (Cell c : cells) {
            if (c.getX() == x && c.getY() == y && c.getZ() == z) {
                return c;
            }
        }

        return null;
    }

    public Cell findCellsByColor(int color) {
        for (Cell c : cells) {
            if (c.getColor() == color) {
                return c;
            }
        }
        return null;

    }

    public int findFront(){
        return front;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null) return false;
        if (getClass() != obj.getClass()) return false;
        Cube other = (Cube) obj;
        if (x != other.x) return false;
        if (y != other.y) return false;
        if (z != other.z) return false;

        return false;
    }
}
