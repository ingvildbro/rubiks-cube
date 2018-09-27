public class Rotation {

    //public enum Axis { X, Y, Z }

    //public enum Direction { POSITIVE, NEGATIVE }

    int axis;
    boolean reverse;
    int section;


    public Rotation(int axis, boolean reverse, int section) {
        this.axis = axis;
        this.reverse = reverse;
        this.section = section;
    }

    public int getAxis() {
        return axis;
    }

    public boolean isReverse() {
        return reverse;
    }

    public int getSection() {
        return section;
    }

    /*int x, y, z;

    public Rotation(int x, int y, int z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }*/
}