import static com.jogamp.opengl.GL.*;
import static com.jogamp.opengl.GL2.GL_QUADS;
import static com.jogamp.opengl.GL2ES1.GL_PERSPECTIVE_CORRECTION_HINT;
import static com.jogamp.opengl.fixedfunc.GLLightingFunc.GL_COLOR_MATERIAL;
import static com.jogamp.opengl.fixedfunc.GLLightingFunc.GL_SMOOTH;
import static com.jogamp.opengl.fixedfunc.GLMatrixFunc.GL_MODELVIEW;
import static com.jogamp.opengl.fixedfunc.GLMatrixFunc.GL_PROJECTION;

import com.jogamp.opengl.GL2;
import com.jogamp.opengl.GLAutoDrawable;
import com.jogamp.opengl.GLCapabilities;
import com.jogamp.opengl.GLEventListener;
import com.jogamp.opengl.GLProfile;
import com.jogamp.opengl.awt.GLCanvas;
import com.jogamp.opengl.glu.GLU;

import com.jogamp.newt.event.KeyEvent;
import com.jogamp.newt.event.KeyListener;
import com.jogamp.newt.event.MouseEvent;
import com.jogamp.newt.event.MouseListener;
import com.jogamp.newt.event.WindowAdapter;
import com.jogamp.newt.event.WindowEvent;
import com.jogamp.newt.opengl.GLWindow;
import com.jogamp.opengl.util.FPSAnimator;


public class Rubiks_Canvas extends GLCanvas implements GLEventListener, KeyListener, MouseListener {
    private static final String TITLE = "Rubik's Cube";
    private static final int CANVAS_WIDTH = 640;
    private static final int CANVAS_HEIGHT = 480;
    private static final int FPS = 60;

    // Default view
    private static final float DEFAULT_VIEW_X = 22.5f;
    private static final float DEFAULT_VIEW_Y = -22.5f;
    private static final float DEFAULT_VIEW_Z = 0.0f;
    private static final float DEFAULT_ZOOM = -18.0f;


    // Rotate degrees
    private static final double VIEW_ROT_STEP = 22.5;
    private static final double VIEW_ZOOM_STEP = 22.5;
    private static final int CUBE_STRIP_ROT_STEP = 90;


    // START VALUES - VIEW
    private float viewX = DEFAULT_VIEW_X;
    private float viewY = DEFAULT_VIEW_Y;
    private float viewZ = DEFAULT_VIEW_Z;

    private float zoom = DEFAULT_ZOOM;


    // Angles
    private float[] anglesX;
    private float[] anglesY;
    private float[] anglesZ;

    private int rotationSectionX = -1;
    private int rotationSectionY = -1;
    private int rotationSectionZ = -1;
    private float angularVelocity = 5.0f;

    private GLU glu;
    private RubiksCube rubiksCube;

    private int cubes = 0;

    private int disp1 = 1;

    private int selectedCube;
    private int selectedX;
    private int selectedY;
    private int selectedZ;
    private int[] selectedCubePos = new int[3];
    private boolean isSelected = false;

    private int selectedDirection;

    private int colorSide;

    private Rubiks_Canvas() {
        rubiksCube = new RubiksCube();
        this.anglesX = new float[3];
        this.anglesY = new float[3];
        this.anglesZ = new float[3];
    }

    public static void main(String[] args) {
        GLProfile glp = GLProfile.getDefault();
        GLCapabilities caps = new GLCapabilities(glp);
        GLWindow window = GLWindow.create(caps);

        final FPSAnimator animator = new FPSAnimator(window, FPS, true);
        window.addWindowListener(new WindowAdapter() {
            @Override
            public void windowDestroyNotify(WindowEvent e) {
                new Thread() {
                    @Override
                    public void run() {
                        animator.stop();
                        System.exit(0);
                    }
                }.start();
            };
        });

        Rubiks_Canvas rubiksCanvas = new Rubiks_Canvas();
        window.addGLEventListener(rubiksCanvas);
        window.addKeyListener(rubiksCanvas);
        window.addMouseListener(rubiksCanvas);

        window.setSize(CANVAS_WIDTH, CANVAS_HEIGHT);
        window.setTitle(TITLE);
        window.setVisible(true);
        animator.start();
    }

    // GLEventListener (init, reshape, display, dispose)
    public void init(GLAutoDrawable drawable) {
        GL2 gl = drawable.getGL().getGL2();
        glu = new GLU();
        gl.glClearColor(0.0f, 0.0f, 0.0f, 0.0f);
        gl.glClearDepth(1.0f);
        gl.glEnable(GL_DEPTH_TEST);
        gl.glDepthFunc(GL_LEQUAL);
        gl.glHint(GL_PERSPECTIVE_CORRECTION_HINT, GL_NICEST);
        gl.glShadeModel(GL_SMOOTH);


        float[] b = new float[2];

        gl.glGetFloatv(GL2.GL_ALIASED_LINE_WIDTH_RANGE, b, 0);
        System.out.println("b[0]: "+b[0]+" b[1]: "+b[1]);
        gl.glGetFloatv(GL2.GL_SMOOTH_LINE_WIDTH_RANGE, b, 0);
        System.out.println("b[0]: "+b[0]+" b[1]: "+b[1]);
        gl.glGetFloatv(GL2.GL_SMOOTH_LINE_WIDTH_GRANULARITY, b, 0);
        System.out.println("b[0]: "+b[0]);
    }

    public void reshape(GLAutoDrawable drawable, int x, int y, int width, int height) {
        GL2 gl = drawable.getGL().getGL2();

        if (height == 0) height = 1;
        float aspect = (float) width/height;

        gl.glViewport(0, 0, width, height);

        gl.glMatrixMode(GL_PROJECTION);
        gl.glLoadIdentity();
        glu.gluPerspective(45.0, aspect, 0.1, 100.0);

        gl.glMatrixMode(GL_MODELVIEW);
        gl.glLoadIdentity();
    }

    public void display(GLAutoDrawable drawable) {
        //updateRotationAngles();
        drawRubiksCube(drawable.getGL().getGL2());

    }

    private void drawRubiksCube(GL2 gl) {
        gl.glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
        gl.glLoadIdentity();

        // camera transformations
        gl.glTranslatef(0.0f, 0.0f, zoom);
        gl.glRotatef(viewX, 1.0f, 0.0f, 0.0f);
        gl.glRotatef(viewY, 0.0f, 1.0f, 0.0f);
        gl.glRotatef(viewZ, 0.0f, 0.0f, 1.0f);




        for(int x = 0; x < 3; x++) {
            for(int y = 0; y < 3; y++) {
                for (int z = 0; z < 3; z++) {
                    gl.glPushMatrix();

                    //gl.glRotatef(anglesX[x], 1.0f, 0.0f, 0.0f);
                    //gl.glRotatef(anglesY[y], 0.0f, 1.0f, 0.0f);
                    //gl.glRotatef(anglesZ[z], 0.0f, 0.0f, 1.0f);

                    gl.glTranslatef((x-1)*2.251f, (y-1)*2.251f, (z-1)*2.251f);


                    //Cube cube = new Cube(x, y, z);

                    if (rubiksCube.getCube(x, y, z) == null) {
                        // first drawing, create cube
                        drawCube(gl, rubiksCube.createNewCube(x, y, z));
                    } else {
                        // find cube
                        drawCube(gl, rubiksCube.getCube(x, y, z));
                    }

                    gl.glPopMatrix();
                }
            }
        }

        gl.glTranslatef(0.0f, 0.0f, 0.0f);
        drawBackgroundDefault(gl);
    }

    private void drawBackgroundZ(GL2 gl) {
        gl.glBegin(GL_QUADS);
        gl.glColor3f(0.5f, 0.5f, 0.5f);         // Grey background

        if(selectedZ == 0) {
            gl.glVertex3f(0.0f, 0.0f, 0.0f);      // LEFT  -  BOT  |  ( -- , -- )
            gl.glVertex3f(0.0f, 0.0f, 0.0f);      // RIGHT -  BOT  |  (  - , -- )
            gl.glVertex3f(0.0f, 0.0f, 0.0f);      // RIGHT -  TOP  |  (  - ,  - )
            gl.glVertex3f(0.0f, 0.0f, 0.0f);      // LEFT  -  TOP  |  ( -- ,  - )
        }

        gl.glEnd();
    }

    private void drawBackgroundY(GL2 gl) {
        gl.glBegin(GL_QUADS);
        gl.glColor3f(0.5f, 0.5f, 0.5f);         // Grey background

        if(selectedY == 1) {
            gl.glVertex3f(0.0f, 0.0f, 0.0f);      // LEFT  -  BOT  |  ( -- , -- )
            gl.glVertex3f(0.0f, 0.0f, 0.0f);      // RIGHT -  BOT  |  (  - , -- )
            gl.glVertex3f(0.0f, 0.0f, 0.0f);      // RIGHT -  TOP  |  (  - ,  - )
            gl.glVertex3f(0.0f, 0.0f, 0.0f);      // LEFT  -  TOP  |  ( -- ,  - )
        }

        gl.glEnd();
    }

    private void drawBackgroundX(GL2 gl) {
        gl.glBegin(GL_QUADS);
        gl.glColor3f(0.5f, 0.5f, 0.5f);         // Grey background

        if(selectedX == 2) {
            gl.glVertex3f(0.0f, 0.0f, 0.0f);      // LEFT  -  BOT  |  ( -- , -- )
            gl.glVertex3f(0.0f, 0.0f, 0.0f);      // RIGHT -  BOT  |  (  - , -- )
            gl.glVertex3f(0.0f, 0.0f, 0.0f);      // RIGHT -  TOP  |  (  - ,  - )
            gl.glVertex3f(0.0f, 0.0f, 0.0f);      // LEFT  -  TOP  |  ( -- ,  - )
        }

        gl.glEnd();
    }

    private void drawBackgroundDefault(GL2 gl) {
        gl.glBegin(GL_QUADS);
        gl.glColor3f(0.05f, 0.05f, 0.05f);            // Grey background

        //  FRONT - ok
        gl.glVertex3f(-3.375f, -3.375f, 3.375f);       // LEFT  -  BOT  -  FRONT  |  ( -- , -- , ++ )
        gl.glVertex3f(3.375f,  -3.375f, 3.375f);       // RIGHT -  BOT  -  FRONT  |  ( ++ , -- , ++ )
        gl.glVertex3f(3.375f,  3.375f,  3.375f);       // RIGHT -  TOP  -  FRONT  |  ( ++ , ++ , ++ )
        gl.glVertex3f(-3.375f, 3.375f,  3.375f);       // LEFT  -  TOP  -  FRONT  |  ( -- , ++ , ++ )

        //  BACK - ok
        gl.glVertex3f(-3.375f, -3.375f, -3.375f);      // LEFT  -  BOT  -  BACK   |  ( -- , -- , -- )
        gl.glVertex3f(3.375f,  -3.375f, -3.375f);      // RIGHT -  BOT  -  BACK   |  ( ++ , -- , -- )
        gl.glVertex3f(3.375f,  3.375f,  -3.375f);      // RIGHT -  TOP  -  BACK   |  ( ++ , ++ , -- )
        gl.glVertex3f(-3.375f, 3.375f,  -3.375f);      // LEFT  -  TOP  -  BACK   |  ( -- , ++ , -- )

        //  TOP -
        gl.glVertex3f(-3.375f, 3.375f, -3.375f);       // LEFT  -  TOP  -  BACK  |  ( -- , ++ , -- )
        gl.glVertex3f(3.375f,  3.375f, -3.375f);       // RIGHT -  TOP  -  BACK  |  ( ++ , ++ , -- )
        gl.glVertex3f(3.375f,  3.375f, 3.375f);        // RIGHT -  TOP  -  FRONT |  ( ++ , ++ , ++ )
        gl.glVertex3f(-3.375f, 3.375f, 3.375f);        // LEFT  -  TOP  -  FRONT |  ( -- , ++ , ++ )

        //  BOT -
        gl.glVertex3f(-3.375f, -3.375f, -3.375f);      // LEFT  -  BOT  -  BACK   |  ( -- , -- , -- )
        gl.glVertex3f(3.375f,  -3.375f, -3.375f);      // RIGHT -  BOT  -  BACK   |  ( ++ , -- , -- )
        gl.glVertex3f(3.375f,  -3.375f, 3.375f);       // RIGHT -  BOT  -  FRONT  |  ( ++ , -- , ++ )
        gl.glVertex3f(-3.375f, -3.375f, 3.375f);       // LEFT  -  BOT  -  FRONT  |  ( -- , -- , ++ )

        //  RIGHT - OK
        gl.glVertex3f(3.375f, -3.375f, -3.375f);       // RIGHT -  BOT  -  BACK   |  ( ++ , -- , -- )
        gl.glVertex3f(3.375f, -3.375f, 3.375f);        // RIGHT -  BOT  -  FRONT  |  ( ++ , -- , ++ )
        gl.glVertex3f(3.375f, 3.375f,  3.375f);        // RIGHT -  TOP  -  FRONT  |  ( ++ , ++ , ++ )
        gl.glVertex3f(3.375f, 3.375f,  -3.375f);       // RIGHT -  TOP  -  BACK   |  ( ++ , ++ , -- )

        //  LEFT - OK
        gl.glVertex3f(-3.375f, -3.375f, -3.375f);      // LEFT  -  BOT  -  BACK   |  ( -- , -- , -- )
        gl.glVertex3f(-3.375f, -3.375f, 3.375f);       // LEFT  -  BOT  -  FRONT  |  ( -- , -- , ++ )
        gl.glVertex3f(-3.375f, 3.375f,  3.375f);       // LEFT  -  TOP  -  FRONT  |  ( -- , ++ , ++ )
        gl.glVertex3f(-3.375f, 3.375f,  -3.375f);      // LEFT  -  TOP  -  BACK   |  ( -- , ++ , -- )


        gl.glEnd();
    }

    private void drawBackgroundCell(GL2 gl) {
        // Front face cells

        float fX = 1.0f;
        float fY = 1.0f;
        float fZ = (selectedCubePos[2] == 0) ? -3.375f : (selectedCubePos[2] == 2) ? 3.375f : 0.0f;



        gl.glBegin(GL_QUADS);
        gl.glColor3f(0.5f, 0.5f, 0.5f);         // Grey background


        //  x: -1, y: -1    (LEFT col,  BOT row)            # 1
        if(selectedCube == 1) {
            gl.glVertex3f(-3.375f, -3.375f, fZ);      // LEFT  -  BOT  |  ( -- , -- )
            gl.glVertex3f(-1.125f, -3.375f, fZ);      // RIGHT -  BOT  |  (  - , -- )
            gl.glVertex3f(-1.125f, -1.125f, fZ);      // RIGHT -  TOP  |  (  - ,  - )
            gl.glVertex3f(-3.375f, -1.125f, fZ);      // LEFT  -  TOP  |  ( -- ,  - )
        }


        //  x: 0, y: -1     (MID col,   BOT row)            # 2
        if(selectedCube == 2) {
            gl.glVertex3f(-1.125f, -3.375f, fZ);      // LEFT  -  BOT  |  (  - , -- )
            gl.glVertex3f(1.125f,  -3.375f, fZ);      // RIGHT -  BOT  |  (  + , -- )
            gl.glVertex3f(1.125f,  -1.125f, fZ);      // RIGHT -  TOP  |  (  + ,  - )
            gl.glVertex3f(-1.125f, -1.125f, fZ);      // LEFT  -  TOP  |  (  - ,  - )
        }

        //  x: 1, y: -1     (RIGHT col, BOT row)            # 3
        if(selectedCube == 3) {
            gl.glVertex3f(1.125f, -3.375f, fZ);       // LEFT  -  BOT  |  (  + , -- )
            gl.glVertex3f(3.375f, -3.375f, fZ);       // RIGHT -  BOT  |  ( ++ , -- )
            gl.glVertex3f(3.375f, -1.125f, fZ);       // RIGHT -  TOP  |  ( ++ ,  - )
            gl.glVertex3f(1.125f, -1.125f, fZ);       // LEFT  -  TOP  |  (  + ,  - )
        }

        //  x: 1, y: 0      (LEFT col,  MID row)            # 4
        if(selectedCube == 4) {
            gl.glVertex3f(-3.375f,  -1.125f, fZ);     // LEFT  -  BOT  |  ( -- ,  - )
            gl.glVertex3f(-1.125f,  -1.125f, fZ);     // RIGHT -  BOT  |  (  - ,  - )
            gl.glVertex3f(-1.125f,  1.125f,  fZ);     // RIGHT -  TOP  |  (  - ,  + )
            gl.glVertex3f(-3.375f,  1.125f,  fZ);     // LEFT  -  TOP  |  ( -- ,  + )
        }


        //  x: -1, y: 0     (MID col,   MID row)            # 5
        if(selectedCube == 5) {
            gl.glVertex3f(-1.125f,  -1.125f, fZ);     // LEFT  -  BOT  |  (  - ,  - )
            gl.glVertex3f(1.125f,   -1.125f, fZ);     // RIGHT -  BOT  |  (  + ,  - )
            gl.glVertex3f(1.125f,   1.125f,  fZ);     // RIGHT -  TOP  |  (  + ,  + )
            gl.glVertex3f(-1.125f,  1.125f,  fZ);     // LEFT  -  TOP  |  (  - ,  + )
        }


        //  x: 1, y: 0      (RIGHT col, MID row)            # 6
        if(selectedCube == 6) {
            gl.glVertex3f(1.125f, -1.125f, fZ);       // LEFT  -  BOT  |  (  + ,  - )
            gl.glVertex3f(3.375f, -1.125f, fZ);       // RIGHT -  BOT  |  ( ++ ,  - )
            gl.glVertex3f(3.375f, 1.125f,  fZ);       // RIGHT -  TOP  |  ( ++ ,  + )
            gl.glVertex3f(1.125f, 1.125f,  fZ);       // LEFT  -  TOP  |  (  + ,  + )
        }

        //  x: -1, y: 1     (LEFT col, TOP row)             # 7
        if(selectedCube == 7) {
            gl.glVertex3f(-3.375f, 1.125f, fZ);       // LEFT  -  BOT  |  ( -- ,  + )
            gl.glVertex3f(-1.125f, 1.125f, fZ);       // RIGHT -  BOT  |  (  - ,  + )
            gl.glVertex3f(-1.125f, 3.375f, fZ);       // RIGHT -  TOP  |  (  - , ++ )
            gl.glVertex3f(-3.375f, 3.375f, fZ);       // LEFT  -  TOP  |  ( -- , ++ )
        }

        //  x: 0, y: 1      (MID col,   TOP row)            # 8
        if(selectedCube == 8) {
            gl.glVertex3f(-1.125f, 1.125f, fZ);       // LEFT  -  BOT  |  (  - ,  + )
            gl.glVertex3f( 1.125f, 1.125f, fZ);       // RIGHT -  BOT  |  (  + ,  + )
            gl.glVertex3f( 1.125f, 3.375f, fZ);       // RIGHT -  TOP  |  (  + , ++ )
            gl.glVertex3f(-1.125f, 3.375f, fZ);       // LEFT  -  TOP  |  (  - , ++ )
        }

        //  x: 1, y: 1      (RIGHT col, TOP row)            # 9
        if(selectedCube == 9) {
            gl.glVertex3f(1.125f, 1.125f, fZ);        // LEFT  -  BOT  |  (  + ,  + )
            gl.glVertex3f(3.375f, 1.125f, fZ);        // RIGHT -  BOT  |  ( ++ ,  + )
            gl.glVertex3f(3.375f, 3.375f, fZ);        // RIGHT -  TOP  |  ( ++ , ++ )
            gl.glVertex3f(1.125f, 3.375f, fZ);        // LEFT  -  TOP  |  (  + , ++ )
        }

        gl.glEnd();

    }

    private void drawCube(GL2 gl, Cube cube) {
        // Start drawing quads to form a single cube

        gl.glEnable(GL_COLOR_MATERIAL);

        gl.glBegin(GL_QUADS);


        // FRONT - red
        gl.glColor3f(0.0f, 0.0f, 0.0f);              // inside black

        if (cube.findCellBySide(0) != null) {
            findColor(gl, cube.findCellBySide(0).getColor());
        }

        gl.glVertex3f(1.0f, -1.0f, 1.125f);          // bot left front
        gl.glVertex3f(-1.0f,-1.0f, 1.125f);          // bot right front
        gl.glVertex3f(-1.0f,1.0f,  1.125f);          // top right front
        gl.glVertex3f(1.0f, 1.0f,  1.125f);          // top left front


        // mini cube - red
        gl.glColor3f(0.5f, 0.0f, 0.0f);

        gl.glVertex3f(0.5f, -0.5f, 1.126f);          // bot left front
        gl.glVertex3f(-0.5f,-0.5f, 1.126f);          // bot right front
        gl.glVertex3f(-0.5f,0.5f,  1.126f);          // top right front
        gl.glVertex3f(0.5f, 0.5f,  1.126f);          // top left front



        // BACK - orange
        gl.glColor3f(0.0f, 0.0f, 0.0f);              // inside black

        if (cube.findCellBySide(1) != null) {
            findColor(gl, cube.findCellBySide(1).getColor());   //
        }

        gl.glVertex3f(1.0f, -1.0f,-1.125f);          // bot left back    +  -  -
        gl.glVertex3f(-1.0f,-1.0f,-1.125f);          // bot right back   -  -  -
        gl.glVertex3f(-1.0f,1.0f, -1.125f);          // top right back   -  +  -
        gl.glVertex3f(1.0f, 1.0f, -1.125f);          // top left back    +  +  -

        // mini cube - orange
        gl.glColor3f(0.5f, 0.25f, 0.0f);

        gl.glVertex3f(0.5f, -0.5f, -1.126f);         // bot left front
        gl.glVertex3f(-0.5f,-0.5f, -1.126f);         // bot right front
        gl.glVertex3f(-0.5f,0.5f,  -1.126f);         // top right front
        gl.glVertex3f(0.5f, 0.5f,  -1.126f);         // top left front



        /*           --------     TOP      --------             */
        gl.glColor3f(0.0f, 0.0f, 0.0f);              // inside black

        if (cube.findCellBySide(2) != null) {
            findColor(gl, cube.findCellBySide(2).getColor());
        }
        gl.glVertex3f(1.0f, 1.125f, 1.0f);           // bot/front right  +  +  +
        gl.glVertex3f(-1.0f,1.125f, 1.0f);           // bot/front left   -  +  +
        gl.glVertex3f(-1.0f,1.125f, -1.0f);          // top/back left    -  +  -
        gl.glVertex3f(1.0f, 1.125f, -1.0f);          // top/back right   +  +  -


        // mini cube - blue .   .   .   .   .   .   .   .   .   .   .   .   .   .
        gl.glColor3f(0.0f, 0.0f, 0.5f);

        gl.glVertex3f(0.5f, 1.126f, 0.5f);           // bot/front right
        gl.glVertex3f(-1.0f,1.126f, 0.5f);           // bot/front left
        gl.glVertex3f(-0.5f,1.126f, -0.5f);          // top/back left
        gl.glVertex3f(0.5f, 1.126f, -0.5f);          // top/back right



        /*           --------    BOTTOM    --------             */
        gl.glColor3f(0.0f, 0.0f, 0.0f);              // inside black

        if (cube.findCellBySide(3) != null) {
            findColor(gl, cube.findCellBySide(3).getColor());
        }

        gl.glVertex3f(1.0f, -1.125f, 1.0f);          // bot/front right
        gl.glVertex3f(-1.0f,-1.125f, 1.0f);          // bot/front left
        gl.glVertex3f(-1.0f,-1.125f, -1.0f);         // top/back left
        gl.glVertex3f(1.0f, -1.125f, -1.0f);         // top/back right


        // mini cube - green    .   .   .   .   .   .   .   .   .   .   .   .   .   .   .   .
        gl.glColor3f(0.0f, 0.5f, 0.0f);

        gl.glVertex3f(0.5f, -1.126f, 0.5f);          // bot/front right
        gl.glVertex3f(-0.5f,-1.126f, 0.5f);          // bot/front left
        gl.glVertex3f(-0.5f,-1.126f, -0.5f);         // top/back left
        gl.glVertex3f(0.5f, -1.126f, -0.5f);         // top/back right
        /*  .   .   .   .   .   .   .   .   .   .   .   .   .   .   .   .   .   .   .   .   .   */
        //  .   .   .   .   .   .   .


        /*           --------    RIGHT     --------             */
        gl.glColor3f(0.0f, 0.0f, 0.0f);              // inside black

        if (cube.findCellBySide(4) != null) {
            findColor(gl, cube.findCellBySide(4).getColor());
        }

        gl.glVertex3f(1.125f, -1.0f,-1.0f);          // bot left/        +  -  -
        gl.glVertex3f(1.125f, -1.0f,1.0f);           // bot right       +  -  +
        gl.glVertex3f(1.125f, 1.0f, 1.0f);           // top
        gl.glVertex3f(1.125f, 1.0f, -1.0f);          // top


        // mini cube - yellow
        gl.glColor3f(0.5f, 0.5f, 0.0f);

        gl.glVertex3f(1.126f, -0.5f,-0.5f);          // top left
        gl.glVertex3f(1.126f, -0.5f,0.5f);           // top right
        gl.glVertex3f(1.126f, 0.5f, 0.5f);           //
        gl.glVertex3f(1.126f, 0.5f, -0.5f);          //
        /*  .   .   .   .   .   .   .   .   .   .   .   .   .   .   .   .   .   .   .   .   .   */


        /*  .   .   .   .   .   .   .   .   .   .   .   .   .   .   .   .   .   */
        /*          --------     LEFT     --------              */
        gl.glColor3f(0.0f, 0.0f, 0.0f);              // inside black

        if (cube.findCellBySide(5) != null) {
            findColor(gl, cube.findCellBySide(5).getColor());
        }

        gl.glVertex3f(-1.125f, -1.0f,-1.0f);         //
        gl.glVertex3f(-1.125f, -1.0f,1.0f);          //
        gl.glVertex3f(-1.125f, 1.0f, 1.0f);          //
        gl.glVertex3f(-1.125f, 1.0f, -1.0f);         //


        // mini cube - white
        gl.glColor3f(0.5f, 0.5f, 0.5f);

        gl.glVertex3f(-1.126f, -0.5f,-0.5f);         //
        gl.glVertex3f(-1.126f, -0.5f,0.5f);          //
        gl.glVertex3f(-1.126f, 0.5f, 0.5f);          //
        gl.glVertex3f(-1.126f, 0.5f, -0.5f);         //

        gl.glEnd();

        gl.glLoadIdentity();
        gl.glEnable(GL2.GL_LIGHTING);
        gl.glEnable(GL2.GL_LIGHT0);
        // gl.glEnable(GL2.GL_NORMALIZE);

        float[] spotdirection = {0, 0, 0};
        float[] lightposition = {0.0f, 50.0f, 0.0f};
        float[] diffuselight = {1.0f, 1.0f, 1.0f, 1.0f };  //strong yellow diffuse
        gl.glLightfv(GL2.GL_LIGHT0, GL2.GL_POSITION, lightposition, 0);
        gl.glLightfv(GL2.GL_LIGHT0, GL2.GL_DIFFUSE, diffuselight, 0);
        gl.glLightfv(GL2.GL_LIGHT0, GL2.GL_SPOT_DIRECTION, spotdirection, 0);

    }

    private void updateView() {
        // 315.1  -  0  -  44.9
        if ((viewX >= 0 && viewX < 45) || (viewX > 315 && viewX <= 365)) {
            if ((viewY >= 0 && viewY < 45) || (viewY > 315 && viewY <= 365))  {
                colorSide = 0;  // red
            } else if (viewY > 45 && viewY < 135){
                colorSide = 5;  // white
            } else if (viewY > 135 && viewY < 225){
                colorSide = 1;  // orange
            } else if (viewY > 225 && viewY < 315){
                colorSide = 4;  // yellow
            }
        } else if (viewX > 45 && viewX < 135) {
            if ((viewZ >= 0 && viewZ < 45) || (viewZ > 315 && viewZ <= 365)) {
                colorSide = 2; // blue

            } else if (viewZ > 45 && viewZ < 135){
                colorSide = 4;  // yellow
            }  else if (viewY > 135 && viewY < 225){
                colorSide = 3;  // green
            } else if (viewZ > 225 && viewZ < 315){
                colorSide = 5;  // white
            }

        } else if (viewX > 135 && viewX < 225) {
            if ((viewY >= 0 && viewY < 45) || (viewY > 315 && viewY <= 365))  {
                colorSide = 1;  // orange
            } else if (viewY > 45 && viewY < 135){
                colorSide = 4;  // yellow
            } else if (viewY > 135 && viewY < 225){
                colorSide = 0;  // red
            } else if (viewY > 225 && viewY < 315){
                colorSide = 5;  // white
            }

        } else if (viewX > 225 && viewX < 315) {
            if ((viewZ >= 0 && viewZ < 45) || (viewZ > 315 && viewZ <= 365)) {
                colorSide = 3; // green
            } else if (viewZ > 45 && viewZ < 135){
                colorSide = 5;  // white
            }  else if (viewY > 135 && viewY < 225){
                colorSide = 2;  // blue
            } else if (viewZ > 225 && viewZ < 315){
                colorSide = 4;  // yellow
            }
        }

        System.out.println("View-side: " + colorSide);
    }

    private void findColor(GL2 gl, int color) {
        switch (color) {
            case 0: // RED
                gl.glColor3f(1.0f, 0.0f, 0.0f);
                break;

            case 1: // ORANGE
                gl.glColor3f(1.0f, 0.5f, 0.0f);
                break;

            case 2: // BLUE
                gl.glColor3f(0.0f, 0.0f, 1.0f);
                break;

            case 3: // GREEN
                gl.glColor3f(0.0f, 1.0f, 0.0f);
                break;

            case 4: // YELLOW
                gl.glColor3f(1.0f, 1.0f, 0.0f);
                break;

            case 5: // WHITE
                gl.glColor3f(1.0f, 1.0f, 1.0f);
                break;
        }
    }
    private boolean isRotating() {
        return rotationSectionX + rotationSectionY + rotationSectionZ > -3;
    }

    private void updateRotationAngles() {
        /*
        Rotation.Direction direction = (angularVelocity > 0) ?  Rotation.Direction.NEGATIVE : Rotation.Direction.POSITIVE;

        if (rotationSectionX >= 0) {
            anglesX[rotationSectionX] += angularVelocity;
            if (anglesX[rotationSectionX] % CUBE_STRIP_ROT_STEP == 0) {
                anglesX[rotationSectionX] = 0;
                rubiksCube.applyRotation(new Rotation(Rotation.Axis.X, direction, rotationSectionX));
                rotationSectionX = -1;
            }

        } else if (rotationSectionY >= 0) {
            anglesY[rotationSectionY] += angularVelocity;
            if (anglesY[rotationSectionY] % CUBE_STRIP_ROT_STEP == 0) {
                anglesY[rotationSectionY] = 0;
                rubiksCube.applyRotation(new Rotation(Rotation.Axis.Y, direction, rotationSectionY));
                rotationSectionY = -1;
            }

        } else if (rotationSectionZ >= 0) {
            anglesZ[rotationSectionZ] += angularVelocity;
            if (anglesZ[rotationSectionZ] % CUBE_STRIP_ROT_STEP == 0) {
                anglesZ[rotationSectionZ] = 0;
                rubiksCube.applyRotation(new Rotation(Rotation.Axis.Z, direction, rotationSectionZ));
                rotationSectionZ = -1;
            }

        }
        */
    }

    // section is the index of the column/row/face that is to be rotated.
    // if reverse is true then rotation will be clockwise
    private void rotateSection(int section, int axis, boolean reverse) {
        // make sure nothing is currently rotating
        if (!isRotating()) {
            if (axis == 0) rotationSectionX = section;
            if (axis == 1) rotationSectionY = section;
            if (axis == 2) rotationSectionZ = section;
            angularVelocity = reverse ? -Math.abs(angularVelocity) : Math.abs(angularVelocity);
        }
    }

    @Override
    public void keyPressed(KeyEvent e) {
        //keyAlt1(e);
        keyAlt2(e);
        //keyAlt3(e);
    }

    private void selectCube(int number) {
        updateView();

        if (number == 1 || number == 4 || number == 7) {
            selectedCubePos[0] = 0;
        } else if (number == 2 || number == 5 || number == 8) {
            selectedCubePos[0] = 1;
        } else {
            selectedCubePos[0] = 2;
        }

        if (number < 4) {
            selectedCubePos[1] = 0;
        } else if (number < 7) {
            selectedCubePos[1] = 1;
        } else {
            selectedCubePos[1] = 2;
        }

        switch (colorSide) {
            case 0: // x, y, 2  front/red
                selectedCubePos[2] = 2; // z = 2
                break;

            case 1: // x, y, 0  back/orange
                selectedCubePos[2] = 0; // z = 0
                break;

            case 2: // x, 2, z  top/blue
                selectedCubePos[1] = 2; // y = 2
                break;

            case 3: // x, 0, z  bot/green
                selectedCubePos[1] = 0; // y = 0
                break;

            case 4: // 2, y, z  right/yellow
                selectedCubePos[0] = 2; // x = 2
                break;

            case 5: // 0, y, z  left/white
                selectedCubePos[0] = 0; // x = 0
                break;
        }

        if (colorSide == 2 || colorSide == 3) {
            //  set z
            if (number < 4) {
                selectedCubePos[2] = 2;
            } else if (number < 7) {
                selectedCubePos[2] = 1;
            } else {
                selectedCubePos[2] = 0;
            }

        } else if (colorSide > 3) {
            //  set y, z
            if (number == 1 || number == 4 || number == 7) {
                selectedCubePos[2] = 2;
            } else if (number == 2 || number == 5 || number == 8) {
                selectedCubePos[2] = 1;
            } else {
                selectedCubePos[2] = 0;
            }
        }

        System.out.println("Selected x: " + selectedCubePos[0] + ", y: " + selectedCubePos[1] + ", z: " + selectedCubePos[2]);
    }


    private void keyAlt1(KeyEvent e) {
        if (e.isShiftDown()) {
            switch (e.getKeyCode()) {
                case KeyEvent.VK_UP:

                    viewX -= VIEW_ROT_STEP;
                    if (viewX < 0) viewX += 360;
                    System.out.println("x: " + viewX);
                    break;

                case KeyEvent.VK_DOWN:
                    viewX += VIEW_ROT_STEP;
                    if (viewX > 360) viewX -= 360;
                    System.out.println("x: " + viewX);
                    break;

                case KeyEvent.VK_LEFT:
                    viewY -= VIEW_ROT_STEP;
                    if (viewY < 0) viewY += 360;
                    System.out.println("y: " + viewY);
                    break;

                case KeyEvent.VK_RIGHT:
                    viewY += VIEW_ROT_STEP;
                    if (viewY > 360) viewY -= 360;
                    System.out.println("y: " + viewY);
                    break;

                case KeyEvent.VK_SPACE:
                    viewZ -= VIEW_ZOOM_STEP;
                    if (viewZ < 0) viewZ += 360;
                    System.out.println("z: " + viewZ);
                    break;
            }
            updateView();
        } else if(e.isControlDown()){
            if (e.getKeyCode() == KeyEvent.VK_SPACE) {
                viewZ += VIEW_ZOOM_STEP;
                if (viewZ > 360) viewZ -= 360;
                System.out.println("z: " + viewZ);
            }
            updateView();

        } else {
            if (isSelected) {
                System.out.println("Rotate");
                switch (e.getKeyCode()) {
                    case KeyEvent.VK_UP:
                        selectedDirection = 2;

                        rubiksCube.rotateByAxis(0, selectedCubePos, false);

                        //rotateSection(selectedCubePos[1], int 1, false);
                        break;

                    case KeyEvent.VK_DOWN:
                        selectedDirection = 3;

                        rubiksCube.rotateByAxis(0, selectedCubePos, true);
                        //rotateSection(selectedCubePos[1], Rotation.Axis.Y, true);
                        break;

                    case KeyEvent.VK_LEFT:
                        selectedDirection = 1;

                        rubiksCube.rotateByAxis(1, selectedCubePos, true);
                        //rotateSection(selectedCubePos[0], Rotation.Axis.X, true);
                        break;

                    case KeyEvent.VK_RIGHT:
                        selectedDirection = 0;

                        rubiksCube.rotateByAxis(1, selectedCubePos, false);
                        //rotateSection(selectedCubePos[0], Rotation.Axis.X, false);
                        break;

                    case KeyEvent.VK_PLUS:
                        selectedDirection = 4; // +z

                        rubiksCube.rotateByAxis(2, selectedCubePos, false);
                        //rotateSection(selectedCubePos[2], Rotation.Axis.Z, false);
                        break;

                    case KeyEvent.VK_MINUS:
                        selectedDirection = 5; // -z

                        rubiksCube.rotateByAxis(2, selectedCubePos, true);
                        //rotateSection(selectedCubePos[2], Rotation.Axis.Z, true);
                        break;
                }

                selectedCube = -1;
                isSelected = false;
                selectedDirection = -1;
                selectedCubePos = null;


            }

            switch (e.getKeyCode()) {
                case KeyEvent.VK_Q:
                    //e.setAttachment(KeyEvent.VK_Q);
                    selectedCube = 7;
                    isSelected = true;

                    break;

                case KeyEvent.VK_W:
                    //e.setAttachment(KeyEvent.VK_W);
                    selectedCube = 8;
                    isSelected = true;
                    break;

                case KeyEvent.VK_E:
                    //e.setAttachment(KeyEvent.VK_E);
                    selectedCube = 9;
                    isSelected = true;
                    break;

                case KeyEvent.VK_A:
                    //e.setAttachment(KeyEvent.VK_A);
                    selectedCube = 4;
                    isSelected = true;

                    break;

                case KeyEvent.VK_S:
                    //e.setAttachment(KeyEvent.VK_S);
                    selectedCube = 5;
                    isSelected = true;

                    break;

                case KeyEvent.VK_D:
                    //e.setAttachment(KeyEvent.VK_D);
                    selectedCube = 6;   //  2, 1, z
                    isSelected = true;

                    break;

                case KeyEvent.VK_Z:
                    //e.setAttachment(KeyEvent.VK_Z);
                    selectedCube = 1;   // 0, 0, z
                    isSelected = true;

                    break;

                case KeyEvent.VK_X:
                    //e.setAttachment(KeyEvent.VK_X);
                    selectedCube = 2;
                    isSelected = true;

                    break;

                case KeyEvent.VK_C:
                    //e.setAttachment(KeyEvent.VK_C);
                    selectedCube = 3;
                    isSelected = true;
                    break;
            }
            selectCube(selectedCube);
        }
    }

    private void keyAlt2(KeyEvent e) {
        if (e.isShiftDown()) {
            keyAlt2view(e);
        } else {
            keyAlt2select(e);
        }
    }

    private void keyAlt2view(KeyEvent e) {
        switch (e.getKeyCode()) {
            case KeyEvent.VK_UP:

                viewX -= VIEW_ROT_STEP;
                if (viewX < 0) viewX += 360;
                break;

            case KeyEvent.VK_DOWN:
                viewX += VIEW_ROT_STEP;
                if (viewX > 360) viewX -= 360;
                break;

            case KeyEvent.VK_LEFT:
                viewY -= VIEW_ROT_STEP;
                if (viewY < 0) viewY += 360;
                break;

            case KeyEvent.VK_RIGHT:
                viewY += VIEW_ROT_STEP;
                if (viewY > 360) viewY -= 360;
                break;

            case KeyEvent.VK_SPACE:
                viewZ -= VIEW_ZOOM_STEP;
                if (viewZ < 0) viewZ += 360;
                break;
        }
    }

    private void keyAlt2select(KeyEvent e) {
        switch (e.getKeyCode()) {
            case KeyEvent.VK_Q:
                selectedCube = 7;
                isSelected = true;
                selectCube(selectedCube);
                //rotateSection(RubiksCube.X_LEFT, 0, e.isShiftDown());
                break;
            case KeyEvent.VK_W:
                selectedCube = 8;
                isSelected = true;
                selectCube(selectedCube);
                //rotateSection(RubiksCube.X_MID, 0, e.isShiftDown());
                break;
            case KeyEvent.VK_E:
                selectedCube = 9;
                isSelected = true;
                //rotateSection(RubiksCube.X_RIGHT, 0, e.isShiftDown());
                break;
            case KeyEvent.VK_A:
                selectedCube = 4;
                isSelected = true;
                selectCube(selectedCube);
                //rotateSection(RubiksCube.Y_BOT, 1, e.isShiftDown());
                break;
            case KeyEvent.VK_S:
                selectedCube = 5;
                isSelected = true;
                selectCube(selectedCube);
                //rotateSection(RubiksCube.Y_MID, 1, e.isShiftDown());
                break;
            case KeyEvent.VK_D:
                selectedCube = 6;
                isSelected = true;
                selectCube(selectedCube);
                //rotateSection(RubiksCube.Y_TOP, 1, e.isShiftDown());
                break;
            case KeyEvent.VK_Z:
                selectedCube = 1;
                isSelected = true;
                selectCube(selectedCube);
                //rotateSection(RubiksCube.Z_FRONT, 2, e.isShiftDown());
                break;
            case KeyEvent.VK_X:
                selectedCube = 2;
                isSelected = true;
                selectCube(selectedCube);
                //rotateSection(RubiksCube.Z_MID, 2, e.isShiftDown());
                break;
            case KeyEvent.VK_C:
                selectedCube = 3;
                isSelected = true;
                selectCube(selectedCube);
                //rotateSection(RubiksCube.Z_BACK, 2, e.isShiftDown());
                break;

            case KeyEvent.VK_UP:

                rubiksCube.rotateByAxis(0, selectedCubePos, false);

                //rotateSection(selectedCubePos[1], int 1, false);
                keyAlt2rotate();
                break;

            case KeyEvent.VK_DOWN:
                rubiksCube.rotateByAxis(0, selectedCubePos, true);
                //rotateSection(selectedCubePos[1], Rotation.Axis.Y, true);
                keyAlt2rotate();
                break;

            case KeyEvent.VK_LEFT:
                rubiksCube.rotateByAxis(1, selectedCubePos, true);
                //rotateSection(selectedCubePos[0], Rotation.Axis.X, true);
                keyAlt2rotate();
                break;

            case KeyEvent.VK_RIGHT:

                rubiksCube.rotateByAxis(1, selectedCubePos, false);
                //rotateSection(selectedCubePos[0], Rotation.Axis.X, false);
                keyAlt2rotate();
                break;

            case KeyEvent.VK_PLUS:

                rubiksCube.rotateByAxis(2, selectedCubePos, false);
                //rotateSection(selectedCubePos[2], Rotation.Axis.Z, false);
                keyAlt2rotate();
                break;

            case KeyEvent.VK_MINUS:

                rubiksCube.rotateByAxis(2, selectedCubePos, true);
                //rotateSection(selectedCubePos[2], Rotation.Axis.Z, true);
                keyAlt2rotate();
                break;
        }
    }

    private void keyAlt2rotate() {

        selectedCube = -1;
        isSelected = false;
        selectedDirection = -1;
        selectedCubePos = null;
    }

    private void keyAlt3(KeyEvent e) {
        switch (e.getKeyCode()) {
            case KeyEvent.VK_UP:
                viewX -= VIEW_ROT_STEP;
                break;
            case KeyEvent.VK_DOWN:
                viewX += VIEW_ROT_STEP;
                break;
            case KeyEvent.VK_LEFT:
                if (e.isShiftDown()) viewZ += VIEW_ROT_STEP;
                else viewY -= VIEW_ROT_STEP;
                break;
            case KeyEvent.VK_RIGHT:
                if (e.isShiftDown()) viewZ -= VIEW_ROT_STEP;
                else viewY += VIEW_ROT_STEP;
                break;
            case KeyEvent.VK_Q:
                rotateSection(RubiksCube.X_LEFT, 0, e.isShiftDown()); break;
            case KeyEvent.VK_W:
                rotateSection(RubiksCube.X_MID, 0, e.isShiftDown()); break;
            case KeyEvent.VK_E:
                rotateSection(RubiksCube.X_RIGHT, 0, e.isShiftDown()); break;
            case KeyEvent.VK_A:
                rotateSection(RubiksCube.Y_BOT, 1, e.isShiftDown()); break;
            case KeyEvent.VK_S:
                rotateSection(RubiksCube.Y_MID, 1, e.isShiftDown()); break;
            case KeyEvent.VK_D:
                rotateSection(RubiksCube.Y_TOP, 1, e.isShiftDown()); break;
            case KeyEvent.VK_Z:
                rotateSection(RubiksCube.Z_FRONT, 2, e.isShiftDown()); break;
            case KeyEvent.VK_X:
                rotateSection(RubiksCube.Z_MID, 2, e.isShiftDown()); break;
            case KeyEvent.VK_C:
                rotateSection(RubiksCube.Z_BACK, 2, e.isShiftDown()); break;
        }
    }


    private void findSelectedSection() {
        //if (selectedCube == 0)
    }


    public void dispose(GLAutoDrawable drawable) {}

    public void keyReleased(KeyEvent e) {}

    public void mouseClicked(MouseEvent e){}
    public void mouseEntered(MouseEvent e) {}
    public void mouseExited(MouseEvent e) {}
    public void mousePressed(MouseEvent e) {}
    public void mouseReleased(MouseEvent e) {}
    public void mouseMoved(MouseEvent e) {}
    public void mouseDragged(MouseEvent e) {}
    public void mouseWheelMoved(MouseEvent e) {}

    private abstract class RotationAnimatorThread extends Thread {
        private boolean isTerminated = false;

        public void terminate() { isTerminated = true; }

        protected abstract int getSection(int i);
        protected abstract int getAxis(int i);
        protected abstract boolean isReverse(int i);
        protected abstract boolean isComplete(int i);

        @Override
        public void run() {
            int i = 0;
            while (!isTerminated && !isComplete(i)) {
                while (isRotating()) {
                    try { Thread.sleep(10); }
                    catch (InterruptedException e) { }
                }
                rotateSection(getSection(i), getAxis(i), isReverse(i));
                i++;
            }
        }
    }


}
