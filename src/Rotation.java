public class Rotation {

    public enum Axis { X, Y, Z }

    public enum Direction { POSITIVE, NEGATIVE }

    Axis axis;
    Direction direction;
    int section;


    public Rotation(Axis axis, Direction direction, int section) {
        this.axis = axis;
        this.direction = direction;
        this.section = section;
    }

    public Axis getAxis() {
        return axis;
    }

    public Direction getDirection() {
        return direction;
    }

    public int getSection() {
        return section;
    }

    public boolean isPositive() {
        return (direction == Direction.POSITIVE);
    }

    /*int x, y, z;

    public Rotation(int x, int y, int z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }*/
}