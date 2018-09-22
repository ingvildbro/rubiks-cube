import java.util.ArrayList;

public class Cube {
    // 26 total + 1 in center
    //
    /*
    private ArrayList<Cell> cells = new ArrayList<>();

    private int x;
    private int y;
    private int z;


    public Cube() {}

    public Cube(int x, int y, int z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public int getX(){
        return x;
    }

    public int getY() {
        return y;
    }

    public int getZ() {
        return z;
    }

    public int getSides() {
        return cells.size();
    }

    public void addCell(int color) {
        Cell cell = new Cell(x, y, z, color);
        cells.add(cell);
    }


    public static final int RED = 0;
    public static final int ORANGE = 1;
    public static final int WHITE = 2;
    public static final int YELLOW = 3;
    public static final int BLUE = 4;
    public static final int GREEN = 5;


    public void isCorner(int x, int y, int z) {
        if (x == 0 && y == 0 && z == 0) {
            // red
            // white
            // green
        } else if (x == 0 && y == 0 && z == 2) {
            // orange
            // white
            // green
        } else if (x == 0 && y == 0 && z == 0) {
            //
            //
            //
        } else if (x == 0 && y == 0 && z == 0) {
            //
            //
            //
        }
    }*/

    /*
    public static final int[] CUBE_SIDES = new int[] {
            0, 1, 2,
            1, 2, 3,


    };

    public static final int[] positionX = new int[] {0, 1, 2};
    public static final int[] positionY = new int[] {0, 1, 2};
    public static final int[] positionZ = new int[] {0, 1, 2};


    public static final int[][][] START_POS_FRONT = {{
        {positionX[0], positionY[0], positionZ[2]},
        {positionX[0], positionY[0], positionZ[2]},
        {positionX[0], positionY[0], positionZ[2]},
    }};
    */

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


    int front = START_COLOR_FRONT;
    int back = START_COLOR_BACK;
    int left = START_COLOR_LEFT;
    int right = START_COLOR_RIGHT;
    int top = START_COLOR_TOP;
    int bot = START_COLOR_BOT;


    public Cube() {}

    public Cube(int front, int back, int top, int bot, int right, int left) {
        this.front = front;
        this.back = back;
        this.top = top;
        this.bot = bot;
        this.right = right;
        this.left = left;
    }

    public Cube getCopy() {
        return new Cube(front, back, left, right, top, bot);
    }

    public boolean addCell() {
        if (cells.size() >= 54) {
            return false;
        }

        for (Cell c : cells) {
            //if (cells.)
        }
        return false;
    }

    public void getPosition() {

    }

    public boolean setNewPosition() {
        for (Cell c : cells) {
            //if ()
        }

        return false;
    }


    public int getSides() {
        int sides = 0;
        int[][][] position;
        for (Cell c : cells) {
            //if (c.)
        }
        return sides;
    }

    /*
    public void assignDefaultPositions() {
        int[][][] positions = new int[3][3][3];

        positions[0][0][0] = 0;
        positions[0][0][1] = 1;
        positions[0][0][2] = 2;
    }
    */
}
