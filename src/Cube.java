import java.util.ArrayList;

public class Cube {
    // 26 total + 1 in center

    private ArrayList<Cell> cells = new ArrayList<>();

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


    //private int front;
    private int front = START_COLOR_FRONT;
    private int back = START_COLOR_BACK;
    private int left = START_COLOR_LEFT;
    private int right = START_COLOR_RIGHT;
    private int top = START_COLOR_TOP;
    private int bot = START_COLOR_BOT;


    public Cube(int startX, int startY, int startZ) {
        this.startX = startX;
        this.startY = startY;
        this.startZ = startZ;

        this.x = startX;
        this.y = startY;
        this.z = startZ;
    }

    public Cube(int startX, int startY, int startZ, ArrayList<Cell> cells) {
        this.startX = startX;
        this.startY = startY;
        this.startZ = startZ;

        this.x = startX;
        this.y = startY;
        this.z = startZ;

        this.cells = new ArrayList<>();
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

    public int getStartX() {
        return startX;
    }

    public int getStartY() {
        return startY;
    }

    public int getStartZ() {
        return startZ;
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


    /*public Cube getCopy() {
        return new Cube(startX, startY, startZ, front, back, top, bot, right, left);
    }*/


    public ArrayList<Cell> getCells() {
        return cells;
    }


    public void setCells(){
        if (this.getStartZ() == 2) this.createNewCell(0);
        if (this.getStartZ() == 0) this.createNewCell(1);

        if (this.getStartY() == 2) this.createNewCell(2);
        if (this.getStartY() == 0) this.createNewCell(3);

        if (this.getStartX() == 2) this.createNewCell(4);
        if (this.getStartX() == 0) this.createNewCell(5);

        System.out.println("Cells set");
    }

    public void updateCells(ArrayList<Cell> newCells){
        //ArrayList<Cell> from = this.getCells();

        for (Cell c1 : cells) {
            System.out.println("c1 -  side: " + c1.getSide() + ", color: " + c1.getColor());
        }

        for (Cell c2 : newCells) {
            System.out.println("c2 -  side: " + c2.getSide() + ", color: " + c2.getColor());
        }

        this.cells = newCells;

        System.out.println("Cells updated");
    }

    public Cell findSide(int side) {
        for (Cell cell : cells) {
            if (cell.getSide() == side) {
                return cell;
            }
        }
        return null;
    }


    public Cell createNewCell(int side) {
        if (findCellByColor(side)!= null){
            System.out.println("Cell exists");
            return findCellByColor(side);
        }

        Cell newCell = new Cell(side);
        cells.add(newCell);

        return newCell;

        //System.out.println("Size cells: " + cells.size());

        //return newCell;
    }


    public void updateCell(int from, int to) {
        //cells.add();

    }


    public Cell findCellByColor(int color) {
        for (Cell c : cells) {
            if (c.getColor() == color) {
                return c;
            }
        }
        return null;

    }

    public Cell findCellBySide(int side) {
        for (Cell c : cells) {
            if (c.getSide() == side) {
                return c;
            }
        }
        return null;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null) return false;
        if (getClass() != obj.getClass()) return false;
        Cube other = (Cube) obj;
        if (startX != other.startX) return false;
        if (startY != other.startY) return false;
        if (startZ != other.startZ) return false;

        return false;
    }
}
