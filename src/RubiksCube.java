import java.util.ArrayList;

public class RubiksCube {

    private ArrayList<Cube> cubes = new ArrayList<>();


    //  X values
    public static final int X_LEFT = 0;
    public static final int X_MID = 1;
    public static final int X_RIGHT = 2;

    //  Y values
    public static final int Y_BOT = 0;
    public static final int Y_MID = 1;
    public static final int Y_TOP = 2;

    //  Z values
    public static final int Z_FRONT = 0;
    public static final int Z_MID = 1;
    public static final int Z_BACK = 2;

    public RubiksCube() {}

    private ArrayList<Cube> filterX(int x) {
        ArrayList<Cube> filtered = new ArrayList<>();
        //filtered.clear();
        int i = 0;
        int j = 7;
        int k = 6;
        for (Cube cube : cubes) {
            System.out.println("i: " + i + ", j: " + j + ", k: " + k);
            if (cube.getX() == x) {
                if (cube.getZ() == 2){
                    filtered.set(i, cube);
                    i++;
                }
                if (cube.getZ() == 1) {
                    filtered.set(j, cube);
                    j=3;
                }
                if (cube.getZ() == 0) {
                    filtered.set(k, cube);
                    k--;
                }
            }
        }
        return filtered;
    }

    private ArrayList<Cube> filterY(int y) {
        ArrayList<Cube> filtered = new ArrayList<>();
        //filtered.();
        int i = 0;
        int j = 7;
        int k = 6;
        for (Cube cube : cubes) {
            if (cube.getY() == y) {
                if (cube.getZ() == 2){
                    filtered.set(i, cube);
                    i++;
                }
                if (cube.getZ() == 1) {
                    filtered.set(j, cube);
                    j=3;
                }
                if (cube.getZ() == 0) {
                    filtered.set(k, cube);
                    k--;
                }
            }
        }
        return filtered;
    }

    private ArrayList<Cube> filterZ(int z) {
        ArrayList<Cube> filtered = new ArrayList<>();
        //filtered.clear();
        int i = 0;
        int j = 7;
        int k = 6;
        for (Cube cube : cubes) {
            if (cube.getZ() == z) {
                if (cube.getY() == 2){
                    filtered.set(i, cube);
                    i++;
                }
                if (cube.getY() == 1) {
                    filtered.set(j, cube);
                    j=3;
                }
                if (cube.getY() == 0) {
                    filtered.set(k, cube);
                    k--;
                }
            }
        }
        return filtered;
    }

    // ROTATE
    public void rotateByAxis(int axis, int[] section, boolean reverse) {
        if (axis == 0) {
            rotateSection(filterX(section[0]), reverse);
        } else if (axis == 1) {
            rotateSection(filterY(section[1]), reverse);
        } else if (axis == 2) {
            rotateSection(filterZ(section[2]), reverse);
        }
    }

    private void rotateSection(ArrayList<Cube> fCubes, boolean reverse) {
        int i = 0;
        int index;
        for (Cube f : fCubes) {
            if (reverse){
                index = (fCubes.indexOf(f) < 2) ? i+6 : i-2;
            } else {
                index = (fCubes.indexOf(f) > 5) ? i-6 : i+2;
            }
            updateCubes(f, fCubes.get(index));
            i++;
        }
    }



    public Cube createNewCube(int x, int y, int z) {
        if (getCube(x, y, z) != null) {
            return getCube(x, y, z);
        }

        Cube newCube = new Cube(x, y, z);

        return registerNewCube(newCube);
    }

    public Cube registerNewCube(Cube cube) {
        cube.setCells();
        cubes.add(cube);
        System.out.println("Added cube");
        return cube;
    }

    public int getNrOfCubes() {
        return cubes.size();
    }

    public void updateCubes(Cube from, Cube to) {
        from.setX(to.getX());
        from.setY(to.getY());
        from.setZ(to.getZ());
    }


    public Cube getCube(int x, int y, int z) {
        for (Cube c : cubes) {
            if ((c.getStartX() == x) && (c.getStartY() == y) && (c.getStartZ() == z)) {
                return c;
            }
        }
        return null;
    }

}