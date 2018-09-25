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

    private Cube[][][] state;

    public RubiksCube() {
        this.state = new Cube[3][3][3];
    }

    public RubiksCube(Cube[][][] state) {
        this.state = state;
    }

    public Cube[][][] getState() {
        return state;
    }

    public Cube getCube(CubePosition position) {
        return getCube(position.getX(), position.getY(), position.getZ());
    }

    public Cube getCube(int x, int y, int z) {
        return state[x][y][z];
    }

    public void applyRotation(Rotation rotation) {
        if (rotation.getSection() >= 3) {
            throw new RuntimeException("Specified rotation section is out of bounds: " + rotation.getSection());
        }

        if (rotation.getAxis() == Rotation.Axis.X) {
            applyXRotation(rotation);
        } else if (rotation.getAxis() == Rotation.Axis.Y) {
            applyYRotation(rotation);
        } else if (rotation.getAxis() == Rotation.Axis.Z) {
            applyZRotation(rotation);
        }
    }

    /*public void resetState() {
        for (int x = 0; x < 3; x++) {
            for (int y = 0; y < 3; y++) {
                for (int z = 0; z < 3; z++) {
                    state[x][y][z] = new Cube();
                }
            }
        }
    }*/

    public RubiksCube getCopy() {
        return new RubiksCube(copyState());
    }

    public void rotateSlice(int axis, int sectionX, int sectionY, int sectionZ, boolean direction) {
        if (axis == 0) {
            rotateSliceY(sectionY, sectionZ, direction);
        } else if (axis == 1) {
            rotateSliceY(sectionY, sectionZ, direction);
        }
    }

    private void rotateSliceY(int sectionY, int sectionZ, boolean direction) {
        int[] index = new int[8];
        int ind = 0;
        for (Cube cube : cubes) {
            if ((cube.getY() == sectionY) && cube.getZ() == sectionZ) {
                index[ind] = cubes.indexOf(cube);
            }
            ind++;
        }

        for (int i = 0; i < index.length; i++) {
            Cube cube = cubes.get(index[i]);

            if (direction) {
                //cube.setX();
                //cube.setY();
                //cube.setZ();
            } else {

            }
        }


        for (Cube cube : cubes) {

            if (cube.getY() == sectionY) {
                //cube.front = (direction) ? cubes.get((cubes.indexOf(cube)-2))
            }
        }



        ArrayList<Cube> c1;
        ArrayList<Cube> c2;
        ArrayList<Cube> c3;
        ArrayList<Cube> c4;
        //cubes.set() // index, cube


        //NEW
        int[] ids = new int[8];
        int id = 0;

        for (Cube cube : cubes) {

            if (cube.getY() == sectionY) {
                ids[id] = cubes.indexOf(cube);
                id++;
            }
        }

        if (direction) {
            for (int i = ids.length-1; i >= 0; i--) {
                //updateCube(cubes.get(ids[i]), );
            }
        }

        for (Cube cube : cubes) {
            //if (cubes.i)
            //cube.updateCell();
        }
    }

    private void rotateSliceZ(int sectionZ, boolean direction) {

        ArrayList<Cube> cClone = new ArrayList<Cube>();

        //cubes.removeIf()
        int side = 0;
        for (Cube cube : cubes) {
            if (cube.getZ() == sectionZ) {
                //c
                //cube.getCells()
                //cubesClone.remove(cube);
            }
        }

        for (Object c : cClone) {

        }

        for (Cube cube : cubes) {
            //cubes.set().
        }
    }


    private ArrayList<Cube> filterX(int x) {
        ArrayList<Cube> filtered = new ArrayList<Cube>();
        for (Cube cube : cubes) {
            if (cube.getX() == x) {
                filtered.add(cube);
            }
        }
        return filtered;
    }

    private void applyXRotation(Rotation rotation) {
        int x = rotation.getSection();
        int j = 3-1;

        Cube[][][] copy = copyState();
        for (int i=0, ir=j; i<3; i++, ir--) {
            copy[x][j][i].top    = rotation.isPositive() ? state[x][i][0].front   : state[x][ir][j].back;
            copy[x][0][i].bot = rotation.isPositive() ? state[x][i][j].back    : state[x][ir][0].front;
            copy[x][i][0].front  = rotation.isPositive() ? state[x][0][ir].bot : state[x][j][i].top;
            copy[x][i][j].back   = rotation.isPositive() ? state[x][j][ir].top    : state[x][0][i].bot;
        }
        for (int y=0, yr=j; y<3; y++, yr--) {
            for (int z=0, zr=j; z<3; z++, zr--) {
                copy[x][y][z].left  = rotation.isPositive() ? state[x][z][yr].left  : state[x][zr][y].left;
                copy[x][y][z].right = rotation.isPositive() ? state[x][z][yr].right : state[x][zr][y].right;
            }
        }
        state = copy;
    }

    private void applyYRotation(Rotation rotation) {
        int y = rotation.getSection();
        int j = 3-1;

        Cube[][][] copy = copyState();
        for (int i=0, ir=j; i<3; i++, ir--) {
            copy[0][y][i].left  = rotation.isPositive() ? state[ir][y][0].front : state[i][y][j].back;
            copy[j][y][i].right = rotation.isPositive() ? state[ir][y][j].back  : state[i][y][0].front;
            copy[i][y][0].front = rotation.isPositive() ? state[j][y][i].right  : state[0][y][ir].left;
            copy[i][y][j].back  = rotation.isPositive() ? state[0][y][i].left   : state[j][y][ir].right;
        }
        for (int x=0, xr=j; x<3; x++, xr--) {
            for (int z=0, zr=j; z<3; z++, zr--) {
                copy[x][y][z].top    = rotation.isPositive() ? state[zr][y][x].top    : state[z][y][xr].top;
                copy[x][y][z].bot = rotation.isPositive() ? state[zr][y][x].bot : state[z][y][xr].bot;
            }
        }
        state = copy;
    }

    private void applyZRotation(Rotation rotation) {
        int z = rotation.getSection();
        int j = 3-1;

        Cube[][][] copy = copyState();
        for (int i=0, ir=j; i<3; i++, ir--) {
            copy[i][j][z].top    = rotation.isPositive() ? state[0][i][z].left    : state[j][ir][z].right;
            copy[i][0][z].bot = rotation.isPositive() ? state[j][i][z].right   : state[0][ir][z].left;
            copy[0][i][z].left   = rotation.isPositive() ? state[ir][0][z].bot : state[i][j][z].top;
            copy[j][i][z].right  = rotation.isPositive() ? state[ir][j][z].top    : state[i][0][z].bot;
        }
        for (int x=0, xr=j; x<3; x++, xr--) {
            for (int y=0, yr=j; y<3; y++, yr--) {
                copy[x][y][z].front = rotation.isPositive() ? state[yr][x][z].front : state[y][xr][z].front;
                copy[x][y][z].back  = rotation.isPositive() ? state[yr][x][z].back  : state[y][xr][z].back;
            }
        }
        state = copy;
    }

    private Cube[][][] copyState() {
        Cube[][][] dest = new Cube[3][3][3];
        for (int x=0; x<3; x++) {
            for (int y=0; y<3; y++) {
                for (int z=0; z<3; z++) {
                    dest[x][y][z] = state[x][y][z].getCopy();
                }
            }
        }
        return dest;
    }



    //  CREATE CUBE
    public Cube createCube(int x, int y, int z) {
        for (Cube c : cubes) {
            if (c.getX() == x && c.getY() == y && c.getZ() == z) {
                return c;
            }
        }
        Cube newCube = new Cube(x, y, z);
        addCube(newCube);
        return newCube;
    }


    public int getCountCubes() {
        return cubes.size();
    }

    public void addCube(Cube cube) {
        cubes.add(cube);
    }

    public void updateCube(Cube cube, int x, int y, int z) {
        cube.setX(x);
        cube.setY(y);
        cube.setZ(z);
    }


    public boolean findCube(int x, int y, int z) {
        for (Cube c : cubes) {

        }
        return false;
    }

    /*
    public RubiksCube(Cube[][][] state) {
        this.state = state;
    }

    public Cube[][][] getState() {
        return state;
    }

    public Cube getCube(int x, int y, int z) {
        return state[x][y][z];
    }*/

}