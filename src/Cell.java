public class Cell {
    // 54 total

    private final int startX;
    private final int startY;
    private final int startZ;
    
    private final int color;

    private int x;
    private int y;
    private int z;

    private int side;

    public static final int RED = 0;
    public static final int ORANGE = 1;
    public static final int BLUE = 2;
    public static final int GREEN = 3;
    public static final int YELLOW = 4;
    public static final int WHITE = 5;


    // [0][0][0] = 0, [0][0][1] = 0, [0][0][2] = 2
    // [0][1][0] = 0, [0][1][1] = 1, [0][1][2] = 2
    // [0][2][0] = 0, [0][2][1] = 2, [0][2][2] = 2
    // [1][0][0] = 0, [1][0][1] = 0, [1][0][2] = 2
    // [1][1][0] = 0, [1][1][1] = 1, [1][1][2] = 2
    // [1][2][0] = 0, [1][2][1] = 2, [1][2][2] = 2
    // [2][0][0] = 0, [2][0][1] = 0, [2][0][2] = 2
    // [2][1][0] = 0, [2][1][1] = 1, [2][1][2] = 2
    // [2][2][0] = 0, [2][2][1] = 2, [2][2][2] = 2

    public static final int[][][][] COLOR_POS = new int[][][][] {
            {
                    {
                            //000
                            {ORANGE, GREEN, WHITE},
                            //001
                            {GREEN, WHITE},
                            //002
                            {RED, GREEN, WHITE},


                            //010
                            {ORANGE, WHITE},
                            //011
                            {WHITE}, //MID
                            //012
                            {RED, WHITE},


                            //020
                            {ORANGE, BLUE, WHITE},
                            //021
                            {BLUE, WHITE},
                            //022
                            {RED, BLUE, WHITE},



                            //100
                            {ORANGE, GREEN},
                            //101
                            {GREEN},
                            //102
                            {RED, GREEN},


                            //110
                            {ORANGE},
                            //111
                            {},
                            //112
                            {RED},


                            //120
                            {ORANGE, BLUE},
                            //121
                            {BLUE},
                            //122
                            {RED, BLUE},



                            //200
                            {ORANGE, GREEN, YELLOW},
                            //201
                            {GREEN, YELLOW},
                            //202
                            {RED, GREEN, YELLOW},


                            //210              211       212
                            {ORANGE, YELLOW}, {YELLOW}, {RED, YELLOW},


                            //220
                            {ORANGE, BLUE, YELLOW},
                            //221
                            {BLUE, YELLOW},
                            //222
                            {RED, BLUE, YELLOW},
                    }
            }
    };

    public static final int[][][] POSITIONS = new int[][][]
            {
                    {
                        {0, 1, 2}, {0, 1, 2}
                    }
            };





    // [0][0] = 0, [0][1] = 0, [0][2] = 2
    // [1][0] = 0, [1][1] = 1, [1][2] = 2
    // [2][0] = 0, [2][1] = 2, [2][2] = 2

    // [3][0] = 1, [3][1] = 1, [3][0] = 2
    // [4][0] = 1, [3][1] = 1, [3][0] = 2
    // [5][0] = 1, [3][1] = 1, [3][0] = 2

    // [6][0] = 2, [3][1] = 1, [3][0] = 2
    // [7][0] = 2, [3][1] = 1, [3][0] = 2
    // [8][0] = 2, [3][1] = 1, [3][0] = 2
    public static final int[][] FRONT_POS = new int[][] {
            {0, 0, 2}, {0, 1, 2}, {0, 2, 2},
            {1, 0, 2}, {1, 1, 2}, {1, 2, 2},
            {2, 0, 2}, {2, 1, 2}, {2, 2, 2}
    };

    public static final int[][] BACK_POS = new int[][] {
            {0, 0, 0}, {0, 1, 0}, {0, 2, 0},
            {1, 0, 0}, {1, 1, 0}, {1, 2, 0},
            {2, 0, 0}, {2, 1, 0}, {2, 2, 0}
    };

    public static final int[][] TOP_POS = new int[][] {
            {0, 2, 0}, {0, 2, 1}, {0, 2, 2},
            {1, 2, 0}, {1, 2, 1}, {1, 2, 2},
            {2, 2, 0}, {2, 2, 1}, {2, 2, 2}
    };

    public static final int[][] BOT_POS = new int[][] {
            {0, 0, 0}, {0, 0, 1}, {0, 0, 2},
            {1, 0, 0}, {1, 0, 1}, {1, 0, 2},
            {2, 0, 0}, {2, 0, 1}, {2, 0, 2}
    };

    public static final int[][] RIGHT_POS = new int[][] {
            {2, 0, 0}, {2, 0, 1}, {2, 0, 2},
            {2, 1, 0}, {2, 1, 1}, {2, 1, 2},
            {2, 2, 0}, {2, 2, 1}, {2, 2, 2}
    };

    public static final int[][] LEFT_POS = new int[][] {
            {0, 0, 0}, {0, 0, 1}, {0, 0, 2},
            {0, 1, 0}, {0, 1, 1}, {0, 1, 2},
            {0, 2, 0}, {0, 2, 1}, {0, 2, 2}
    };

    public Cell(int startX, int startY, int startZ, int color) {
        this.startX = startX;
        this.startY = startY;
        this.startZ = startZ;
        this.color = color;

        this.x = startX;
        this.y = startY;
        this.z = startZ;
        this.side = color;
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

    public int getColor() {
        return color;
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

    public int getSide() {
        return side;
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

    public void setSide(int side) {
        this.side = side;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null) return false;
        if (getClass() != obj.getClass()) return false;
        Cell other = (Cell) obj;
        if (x != other.x) return false;
        if (y != other.y) return false;
        if (z != other.z) return false;
        if (color != other.color) return false;

        return false;
    }
}
