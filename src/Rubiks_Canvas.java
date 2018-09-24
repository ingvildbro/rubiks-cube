/*import com.jogamp.newt.event.*;
import com.jogamp.newt.opengl.GLWindow;
import com.jogamp.opengl.*;
import com.jogamp.opengl.awt.GLCanvas;
import com.jogamp.opengl.glu.GLU;
import com.jogamp.opengl.util.FPSAnimator;

import static com.jogamp.opengl.GL.*;
import static com.jogamp.opengl.GL2ES1.GL_PERSPECTIVE_CORRECTION_HINT;
import static com.jogamp.opengl.GL2ES3.GL_QUADS;
import static com.jogamp.opengl.fixedfunc.GLLightingFunc.GL_SMOOTH;
import static com.jogamp.opengl.fixedfunc.GLMatrixFunc.GL_MODELVIEW;
import static com.jogamp.opengl.fixedfunc.GLMatrixFunc.GL_PROJECTION;
*/

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

import java.awt.*;
import java.util.Arrays;

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


    // Color views

    //RED   -   -45 < x < 45    -   -45 < y < 45
    private static final float RED_VIEW_X_MAX = 22.5f;      //44.9f         #1.9
    private static final float RED_VIEW_X = 0.0f;                       //  #1      0       360
    private static final float RED_VIEW_X_MIN = -22.5f;     //-44.9f        #1.1

    private static final float RED_VIEW_Y_MAX = 22.5f;      //44.9f         #
    private static final float RED_VIEW_Y = 0.0f;                       //  #x      0       360
    private static final float RED_VIEW_Y_MIN = -22.5f;     //-44.9f        #

    //ORANGE
    private static final float ORANGE_VIEW_X_MAX = 202.5f;  //224.9f        #3.9
    private static final float ORANGE_VIEW_X = 180.0f;                  //  #3      -180    180
    private static final float ORANGE_VIEW_X_MIN = 157.5f;  //135.1f        #3.1

    private static final float ORANGE_VIEW_Y_MAX = 22.5f;   //
    private static final float ORANGE_VIEW_Y = 22.5f;                   //
    private static final float ORANGE_VIEW_Y_MIN = 22.5f;   //

    //BLUE
    private static final float BLUE_VIEW_X_MAX = 112.5f;    //134.9f        #2.9
    private static final float BLUE_VIEW_X = 90.0f;                     //  #2      -270    90
    private static final float BLUE_VIEW_X_MIN = 67.5f;     //45.1f         #2.1

    private static final float BLUE_VIEW_Y_MAX = 22.5f;     //
    private static final float BLUE_VIEW_Y = 22.5f;                     //
    private static final float BLUE_VIEW_Y_MIN = 22.5f;     //

    //GREEN
    private static final float GREEN_VIEW_X_MAX = 292.5f;    //314.9f       #4.9
    private static final float GREEN_VIEW_X = 270.0f;                   //  #4      -90     270
    private static final float GREEN_VIEW_X_MIN = 247.5f;    //225.1f       #4.1

    private static final float GREEN_VIEW_Y_MAX = 22.5f;    //
    private static final float GREEN_VIEW_Y = 22.5f;                    //
    private static final float GREEN_VIEW_Y_MIN = 22.5f;    //

    //YELLOW
    private static final float YELLOW_VIEW_X_MAX = 22.5f;
    private static final float YELLOW_VIEW_X = 0.0f;
    private static final float YELLOW_VIEW_X_MIN = 22.5f;

    private static final float YELLOW_VIEW_Y_MAX = 22.5f;
    private static final float YELLOW_VIEW_Y = 22.5f;
    private static final float YELLOW_VIEW_Y_MIN = 22.5f;


    //WHITE
    private static final float WHITE_VIEW_X_MAX = 22.5f;
    private static final float WHITE_VIEW_X = 0.0f;
    private static final float WHITE_VIEW_X_MIN = 22.5f;

    private static final float WHITE_VIEW_Y_MAX = 22.5f;
    private static final float WHITE_VIEW_Y = 22.5f;
    private static final float WHITE_VIEW_Y_MIN = 22.5f;


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
    private boolean isSelected;

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
            public void windowDestroyNotify(WindowEvent windowEvent) {
                new Thread(() -> {
                    animator.stop();
                    System.exit(0);
                }).start();
            }
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
        updateRotationAngles();
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

                    gl.glRotatef(anglesX[x], 1.0f, 0.0f, 0.0f);
                    gl.glRotatef(anglesY[y], 0.0f, 1.0f, 0.0f);
                    gl.glRotatef(anglesZ[z], 0.0f, 0.0f, 1.0f);

                    gl.glTranslatef((x-1)*2.251f, (y-1)*2.251f, (z-1)*2.251f);


                    if (!(rubiksCube.findCube(x, y, z))) {
                        // first drawing, create cube
                        Cube cube = rubiksCube.createCube(x, y, z);
                        rubiksCube.addCube(cube);
                        drawCube(gl, x, y, z, cube);
                    } else {
                        // update position
                        Cube cube = rubiksCube.getCube(x, y, z);
                        rubiksCube.updateCube(cube, x, y, z);
                        drawCube(gl, x, y, z, cube);

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
        float fZ = 1.0f;



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

    private void drawCube(GL2 gl, int x, int y, int z, Cube cube) {
        // Start drawing quads to form a single cube

        gl.glEnable(GL_COLOR_MATERIAL);

        gl.glBegin(GL_QUADS);


        // FRONT - red
        gl.glColor3f(0.0f, 0.0f, 0.0f);             // inside black
        //if(z == 2) gl.glColor3f(1.0f, 0.0f, 0.0f);  // change to red
        if (cube.front == 0) {
            findColor(gl, cube.front);
            gl.glColor3f(1.0f, 0.0f, 0.0f);
            cube.createCell(x, y, z, 0);
        }

        gl.glVertex3f(1.0f, -1.0f, 1.125f);           // bot left front
        gl.glVertex3f(-1.0f,-1.0f, 1.125f);           // bot right front
        gl.glVertex3f(-1.0f,1.0f,  1.125f);           // top right front
        gl.glVertex3f(1.0f, 1.0f,  1.125f);           // top left front

        // BACK - orange
        gl.glColor3f(0.0f, 0.0f, 0.0f);             // inside black
        if(z == 0) gl.glColor3f(1.0f, 0.5f, 0.0f);  // change to orange

        gl.glVertex3f(1.0f, -1.0f,-1.125f);           // bot left back
        gl.glVertex3f(-1.0f,-1.0f,-1.125f);           // bot right back
        gl.glVertex3f(-1.0f,1.0f, -1.125f);           // top right back
        gl.glVertex3f(1.0f, 1.0f, -1.125f);           // top left back

        // TOP - blue
        gl.glColor3f(0.0f, 0.0f, 0.0f);             // inside black
        if(y == 2) gl.glColor3f(0.0f, 0.0f, 1.0f);  // change to blue

        gl.glVertex3f(1.0f, 1.125f, 1.0f);            // bot/front right
        gl.glVertex3f(-1.0f,1.125f, 1.0f);            // bot/front left
        gl.glVertex3f(-1.0f,1.125f, -1.0f);           // top/back left
        gl.glVertex3f(1.0f, 1.125f, -1.0f);           // top/back right

        // BOTTOM - green
        gl.glColor3f(0.0f, 0.0f, 0.0f);             // inside black
        if(y == 0) gl.glColor3f(0.0f, 1.0f, 0.0f);  // change to green

        gl.glVertex3f(1.0f, -1.125f, 1.0f);           // bot/front right
        gl.glVertex3f(-1.0f,-1.125f, 1.0f);           // bot/front left
        gl.glVertex3f(-1.0f,-1.125f, -1.0f);          // top/back left
        gl.glVertex3f(1.0f, -1.125f, -1.0f);          // top/back right


        // RIGHT - yellow
        gl.glColor3f(0.0f, 0.0f, 0.0f);             // inside black
        if(x == 2) gl.glColor3f(1.0f, 1.0f, 0.0f);  // change to yellow

        gl.glVertex3f(1.125f, -1.0f,-1.0f);           // top left
        gl.glVertex3f(1.125f, -1.0f,1.0f);            // top right
        gl.glVertex3f(1.125f, 1.0f, 1.0f);            //
        gl.glVertex3f(1.125f, 1.0f, -1.0f);           //

        // LEFT - white
        gl.glColor3f(0.0f, 0.0f, 0.0f);             // inside black
        if(x == 0) gl.glColor3f(1.0f, 1.0f, 1.0f);  // change to white

        gl.glVertex3f(-1.125f, -1.0f,-1.0f);
        gl.glVertex3f(-1.125f, -1.0f,1.0f);
        gl.glVertex3f(-1.125f, 1.0f, 1.0f);
        gl.glVertex3f(-1.125f, 1.0f, -1.0f);

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

        System.out.println(colorSide);
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
    }

    // section is the index of the column/row/face that is to be rotated.
    // if reverse is true then rotation will be clockwise
    private void rotateSection(int section, Rotation.Axis axis, boolean reverse) {
        // make sure nothing is currently rotating
        if (!isRotating()) {
            if (axis == Rotation.Axis.X) rotationSectionX = section;
            if (axis == Rotation.Axis.Y) rotationSectionY = section;
            if (axis == Rotation.Axis.Z) rotationSectionZ = section;
            angularVelocity = reverse ? -Math.abs(angularVelocity) : Math.abs(angularVelocity);
        }
    }

    public void keyPressed(KeyEvent e) {

        /*
        System.out.println("toString: " + e.toString());

        System.out.println("KeyCode: " + e.getKeyCode());
        System.out.println("KeyChar: " + e.getKeyChar());
        System.out.println("KeySymbol: " + e.getKeySymbol());

        System.out.println("Attachment: " + e.getAttachment());
        System.out.println("Attachment string: " + e.getAttachment().toString());

        System.out.println("ActionKey: " + e.isActionKey());
        System.out.println("ModifierKey: " + e.isModifierKey());
        System.out.println("PrintableKey: " + e.isPrintableKey());

        System.out.println("HashCode: " + e.hashCode());
        System.out.println("Source: " + e.getSource());
        System.out.println("EventType: " + e.getEventType());

        System.out.println("ButtonDownCount: " + e.getButtonDownCount());
        System.out.println("ButtonsDown: " + Arrays.toString(e.getButtonsDown()));
        */


        //  VIEW CONTROLS
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
        }else if (e.getAttachment() != null) {
            System.out.println(e.getAttachment());
            updateView();




            switch (e.getKeyCode()) {
                case KeyEvent.VK_UP:
                    selectedDirection = 2;
                    rotateSection(selectedCubePos[1], Rotation.Axis.Y, false);
                    break;

                case KeyEvent.VK_DOWN:
                    selectedDirection = 3;
                    rotateSection(selectedCubePos[1], Rotation.Axis.Y, true);
                    break;

                case KeyEvent.VK_LEFT:
                    selectedDirection = 1;
                    rotateSection(selectedCubePos[0], Rotation.Axis.X, true);
                    break;

                case KeyEvent.VK_RIGHT:
                    selectedDirection = 0;
                    rotateSection(selectedCubePos[0], Rotation.Axis.X, false);
                    break;

                case KeyEvent.VK_PLUS:
                    selectedDirection = 4; // +z
                    rotateSection(selectedCubePos[2], Rotation.Axis.Z, false);
                    break;

                case KeyEvent.VK_MINUS:
                    selectedDirection = 5; // -z
                    rotateSection(selectedCubePos[2], Rotation.Axis.Z, true);
                    break;
            }


        } else {


            switch (e.getKeyCode()) {
                case KeyEvent.VK_Q:
                    e.setAttachment(KeyEvent.VK_Q);
                    selectedCube = 7;

                    break;

                case KeyEvent.VK_W:
                    e.setAttachment(KeyEvent.VK_W);
                    selectedCube = 8;
                    break;

                case KeyEvent.VK_E:
                    e.setAttachment(KeyEvent.VK_E);
                    selectedCube = 9;
                    break;

                case KeyEvent.VK_A:
                    e.setAttachment(KeyEvent.VK_A);
                    selectedCube = 4;

                    break;

                case KeyEvent.VK_S:
                    e.setAttachment(KeyEvent.VK_S);
                    selectedCube = 5;

                    break;

                case KeyEvent.VK_D:
                    e.setAttachment(KeyEvent.VK_D);
                    selectedCube = 6;   //  2, 1, z

                    break;

                case KeyEvent.VK_Z:
                    e.setAttachment(KeyEvent.VK_Z);
                    selectedCube = 1;   // 0, 0, z

                    break;

                case KeyEvent.VK_X:
                    e.setAttachment(KeyEvent.VK_X);
                    selectedCube = 2;

                    break;

                case KeyEvent.VK_C:
                    e.setAttachment(KeyEvent.VK_C);
                    selectedCube = 3;
                    break;
            }
            selectCube(selectedCube);
        }
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
            case 0: // x, y, 2  red

                selectedCubePos[2] = 2; // z = 2
                break;

            case 1: // x, y, 0  orange


                selectedCubePos[2] = 0; // z = 0
                break;

            case 2: // x, 2, z  blue
                //selectedCubePos[2] = selectedCubePos[1];

                selectedCubePos[1] = 2; // y = 2
                break;

            case 3: // x, 0, z  green
                //selectedCubePos[0] = 0;
                selectedCubePos[1] = 0; // y = 0
                //selectedCubePos[2] = 2;
                break;

            case 4: // 2, y, z  yellow
                selectedCubePos[0] = 2; // x = 2
                //selectedCubePos[1] = 0;
                //selectedCubePos[2] = 2;
                break;

            case 5: // 0, y, z  white
                selectedCubePos[0] = 0; // x = 0
                //selectedCubePos[1] = 0;
                //selectedCubePos[2] = 2;
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

        System.out.println("x: " + selectedCubePos[0] + ", y: " + selectedCubePos[1] + ", z: " + selectedCubePos[2]);
    }

    private void rotateSliceX(boolean direction) {

    }

    private void rotateSliceY(boolean direction) {

    }

    private void rotateSliceZ(boolean direction) {

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


}
