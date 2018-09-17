public class CubiePosition {

    int x, y, z;

    public CubiePosition(int x, int y, int z) {
        this.x = x;
        this.y = y;
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
        CubiePosition other = (CubiePosition) obj;
        if (x != other.x) return false;
        if (y != other.y) return false;
        if (z != other.z) return false;
        return true;
    }
}