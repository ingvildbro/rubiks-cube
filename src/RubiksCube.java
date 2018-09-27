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
    public static final int Z_BACK = 0;
    public static final int Z_MID = 1;
    public static final int Z_FRONT = 2;

    public RubiksCube() {}

    private ArrayList<Cube> filterX(ArrayList<Cube> filtered, int x) {
        int i = 0;
        int j = 7;
        int k = 6;
        for (Cube cube : cubes) {
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

    private ArrayList<Cube> filterY(ArrayList<Cube> filtered, int y) {
        int i = 0;
        int j = 7;
        int k = 6;
        for (Cube cube : cubes) {
            System.out.println(filtered.size());
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

    private ArrayList<Cube> filterZ(ArrayList<Cube> filtered, int z) {
        int i = 0;
        int j = 7;
        int k = 6;
        for (Cube cube : cubes) {
            System.out.println(filtered.size());
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
        ArrayList<Cube> filtered = new ArrayList<>(8);
        for (int a = 0; a < 8; a++){
            filtered.add(cubes.get(a));
        }

        if (axis == 0) {
            rotateSection(filterX(filtered, section[0]), reverse);
        } else if (axis == 1) {
            rotateSection(filterY(filtered, section[1]), reverse);
        } else if (axis == 2) {
            rotateSection(filterZ(filtered, section[2]), reverse);
        }
    }

    private void rotateSection(ArrayList<Cube> fCubes, boolean reverse) {
        int i = 0;
        int index;
        for (Cube f : fCubes) {
            System.out.println("- Filtered -");
            System.out.println("Index: " + fCubes.indexOf(f) + ", x: " + f.getX() + ", y: " + f.getY() + ", z: " + f.getZ());
            if (reverse){
                index = (fCubes.indexOf(f) < 2) ? i+6 : i-2;
            } else {
                index = (fCubes.indexOf(f) > 5) ? i-6 : i+2;
            }
            System.out.println("Index: " + index);
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

        Cube c = getCube(from.getStartX(), from.getStartY(), from.getStartZ());
        //System.out.println("From: " + c.);

        c.setX(to.getX());
        c.setY(to.getY());
        c.setZ(to.getZ());



        c.updateCells(to.getCells());


        /*
        ArrayList<Cell> toCells = to.getCells();

        int[] convertTo = new int[toCells.size()];

        for (Cell cellTo: toCells) {
            convertTo[toCells.indexOf(cellTo)] = cellTo.getSide();
        }

        int i = 0;
        for (Cell cell : c.getCells()) {
            cell.setSide(convertTo[i]);

            i++;
        }
        */
        //System.out.println("New: " + getCube(from.getStartX(), from.getStartY(), from.getStartZ()));
        System.out.println("New - x: " + c.getX() + ", y: " + c.getY() + ", z: " + c.getZ());


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