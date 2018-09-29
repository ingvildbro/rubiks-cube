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

    private static final int[] X_SIDES = new int[] {0, 2, 1, 3};
    private static final int[] Y_SIDES = new int[] {0, 4, 1, 5};
    private static final int[] Z_SIDES = new int[] {2, 4, 3, 5};


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

    public int[] getSides() {
        int[] sides = new int[cells.size()];
        int i = 0;
        for (Cell c : cells) {
            sides[i] = c.getSide();
            i++;
        }
        return sides;
    }


    public void setSides(int[] sides) {
        int i = 0;
        for (Cell c : cells) {
            //int side = c.getSide();
            c.setSide(sides[i]);
            i++;
        }
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

    public void updateCells(ArrayList<Cell> newCells, int axis, boolean reverse){

        //  .   .   .   .   .   .   .   .   .   .   .   .   .   .   .   .   .   .   .   .   .   .   .
        //  x:  front   ---(+)-->   top     |   front   ---(-)-->   bot     |   0 + 2       0 - 3   |   0 + 2       0 - 3
        //  x:  top     ---(+)-->   back    |   bot     ---(-)-->   back    |   2 + 1       3 - 1   |   1 + 3       1 - 2
        //  x:  back    ---(+)-->   bot     |   back    ---(-)-->   top     |   1 + 3       1 - 2   |   2 + 1       2 - 0
        //  x:  bot     ---(+)-->   front   |   top     ---(-)-->   front   |   3 + 0       2 - 0   |   3 + 0       3 - 1
        //  .   .   .   .   .   .   .   .   .   .   .   .   .   .   .   .   .   .   .   .   .   .   .

        //  .   .   .   .   .   .   .   .   .   .   .   .   .   .   .   .   .   .   .   .   .   .   .
        //  y:  front   ---(+)-->   right   |   front   ---(-)-->   left    |   0 + 4       0 - 5   |
        //  y:  right   ---(+)-->   back    |   left    ---(-)-->   back    |   4 + 1       5 - 1   |
        //  y:  back    ---(+)-->   left    |   back    ---(-)-->   right   |   1 + 5       1 - 4   |
        //  y:  left    ---(+)-->   front   |   right   ---(-)-->   front   |   5 + 0       4 - 0   |
        //  .   .   .   .   .   .   .   .   .   .   .   .   .   .   .   .   .   .   .   .   .   .   .

        //  .   .   .   .   .   .   .   .   .   .   .   .   .   .   .   .   .   .   .   .   .   .   .
        //  z:  top     ---(+)-->   right   |   top     ---(-)-->   left    |   2 + 4       2 - 5   |
        //  z:  right   ---(+)-->   bot     |   left    ---(-)-->   bot     |   4 + 3       5 - 3   |
        //  z:  bot     ---(+)-->   left    |   bot     ---(-)-->   right   |   3 + 5       3 - 4   |
        //  z:  left    ---(+)-->   top     |   right   ---(-)-->   top     |   5 + 2       4 - 2   |
        //  .   .   .   .   .   .   .   .   .   .   .   .   .   .   .   .   .   .   .   .   .   .   .


        //ArrayList<Cell> from = this.getCells();

        int[] sides = new int[cells.size()];

        for (Cell c1 : cells) {
            int side = c1.getSide();
            if (axis == 0) {
                if (reverse){
                    c1.setSide();
                }

            }

            System.out.println("c1 -  side: " + c1.getSide() + ", color: " + c1.getColor());

        }

        for (Cell c2 : newCells) {
            System.out.println("c2 -  side: " + c2.getSide() + ", color: " + c2.getColor());
        }


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
