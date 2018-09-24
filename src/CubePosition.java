public class CubePosition {

    private final int startX;
    private final int startY;
    private final int startZ;

    private int x;
    private int y;
    private int z;

    public CubePosition(int startX, int startY, int startZ) {
        this.startX = startX;
        this.startY = startY;
        this.startZ = startZ;
        this.x = startX;
        this.y = startY;
        this.z = startZ;
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

    //  x-coordinates
    public boolean isX0() {
        return this.x == RubiksCube.X_LEFT;
    }
    public boolean isX1() {
        return this.x == RubiksCube.X_MID;
    }
    public boolean isX2() {
        return this.x == RubiksCube.X_RIGHT;
    }

    //  y-coordinates
    public boolean isY0() {
        return this.y == RubiksCube.Y_BOT;
    }
    public boolean isY1() {
        return this.y == RubiksCube.Y_MID;
    }
    public boolean isY2() {
        return this.y == RubiksCube.Y_TOP;
    }

    //  z-coordinates
    public boolean isZ0() {
        return this.z == RubiksCube.Z_FRONT;
    }
    public boolean isZ1() {
        return this.z == RubiksCube.Z_MID;
    }
    public boolean isZ2() {
        return this.z == RubiksCube.Z_BACK;
    }


    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null) return false;
        if (getClass() != obj.getClass()) return false;
        CubePosition other = (CubePosition) obj;
        if (x != other.x) return false;
        if (y != other.y) return false;
        if (z != other.z) return false;
        return true;
    }
}