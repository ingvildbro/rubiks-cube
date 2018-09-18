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
import static com.jogamp.opengl.GL2.GL_QUAD_STRIP;
import static com.jogamp.opengl.GL2ES1.GL_PERSPECTIVE_CORRECTION_HINT;
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

public class Rubiks_Canvas extends GLCanvas implements GLEventListener, KeyListener, MouseListener {
    private static final String TITLE = "Rubik's Cube";
    private static final int CANVAS_WIDTH = 640;
    private static final int CANVAS_HEIGHT = 480;
    private static final int FPS = 60;

    // Default view
    private static final float DEFAULT_VIEW_X = 25.0f;
    private static final float DEFAULT_VIEW_Y = -25.0f;
    private static final float DEFAULT_ZOOM = -18.0f;

    // Rotate degrees
    private static final int VIEW_ROT_STEP = 5;
    private static final int CUBE_STRIP_ROT_STEP = 90;


    // START VALUES - VIEW
    private float viewX = DEFAULT_VIEW_X;
    private float viewY = DEFAULT_VIEW_Y;
    private float viewZ = 0.0f;
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
    private int[] selectedCubePos = new int[3];
    private boolean isSelected;

    private int colorSide;

    public Rubiks_Canvas() {
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
                new Thread() {
                    @Override
                    public void run() {
                        animator.stop();
                        System.exit(0);
                    }
                }.start();
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

                    //gl.glRotatef(anglesX[x], 1.0f, 0.0f, 0.0f);
                    //gl.glRotatef(anglesY[y], 0.0f, 1.0f, 0.0f);
                    //gl.glRotatef(anglesZ[z], 0.0f, 0.0f, 1.0f);

                    gl.glTranslatef((x-1)*2.5f, (y-1)*2.5f, (z-1)*2.5f);

                    drawOutline(gl, x, y, z);
                    drawCube(gl, x, y, z);

                    gl.glPopMatrix();
                }
            }
        }
    }

    private void drawBackgroundCell(GL2 gl, int x, int y, int z) {
        // Front face cells

        float fSize = 0.0f;
        float fZ = 1.0f;

        gl.glBegin(GL_QUADS);

        gl.glColor3f(0.5f, 0.5f, 0.5f);     // Grey background

        // x: -1, y: -1 (bottom row, left column)   # 1
        if(selectedCube == 1) {
            gl.glVertex3f(-3.75f, -3.75f, fZ);      // LEFT  -  BOT  |  ( ++ , ++ )
            gl.glVertex3f(-1.25f, -3.75f, fZ);      // RIGHT -  BOT  |  ( ++ , ++ )
            gl.glVertex3f(-1.25f, -1.25f, fZ);      // RIGHT -  TOP  |  ( ++ , ++ )
            gl.glVertex3f(-3.75f, -1.25f, fZ);      // LEFT  -  TOP  |  ( ++ , ++ )
        }


        //  x: 0, y: -1     (bottom row, mid column)     # 2
        if(selectedCube == 2) {
            gl.glVertex3f(-1.25f, -3.75f, fZ);      // LEFT  -  BOT  |  ( ++ , ++ )
            gl.glVertex3f(-1.25f, -3.75f, fZ);      // RIGHT -  BOT  |  ( ++ , ++ )
            gl.glVertex3f(-1.25f, -1.25f, fZ);      // RIGHT -  TOP  |  ( ++ , ++ )
            gl.glVertex3f(-3.75f, -1.25f, fZ);      // LEFT  -  TOP  |  ( ++ , ++ )
        }

        //  x: 1, y: -1      (RIGHT col, BOT row)           # 3
        if(selectedCube == 3) {
            gl.glVertex3f(-3.75f, -3.75f, fZ);      // LEFT  -  BOT  |  ( -  -
            gl.glVertex3f(-1.25f, -3.75f, fZ);      // RIGHT -  BOT  |  ( ++ , ++ )
            gl.glVertex3f(-1.25f, -1.25f, fZ);      // RIGHT -  TOP  |  ( ++ , ++ )
            gl.glVertex3f(-3.75f, -1.25f, fZ);      // LEFT  -  TOP  |  ( ++ , ++ )
        }

        //  x: 1, y: 0      (LEFT col, MID row)          # 4
        if(selectedCube == 4) {
            gl.glVertex3f(3.75f, 3.75f, fZ);      // LEFT  -  BOT  |  ( ++ ,  - )
            gl.glVertex3f(1.25f, 3.75f, fZ);      // RIGHT -  BOT  |  ( ++ ,  - )
            gl.glVertex3f(3.75f, 3.75f, fZ);      // RIGHT -  TOP  |  ( ++ ,  + )
            gl.glVertex3f(3.75f, 1.25f, fZ);      // LEFT  -  TOP  |  ( ++ ,  + )
        }


        //  x: -1, y: 0     (MID col, MID row)           # 5
        if(selectedCube == 5) {
            gl.glVertex3f(3.75f, 3.75f, fZ);      // LEFT  -  BOT  |  (  - ,  - )
            gl.glVertex3f(1.25f, 3.75f, fZ);      // RIGHT -  BOT  |  (  + ,  - )
            gl.glVertex3f(3.75f, 3.75f, fZ);      // RIGHT -  TOP  |  (  + ,  + )
            gl.glVertex3f(3.75f, 1.25f, fZ);      // LEFT  -  TOP  |  (  - ,  + )
        }


        //  x: 1, y: 0      (RIGHT col, MID row)         # 6
        if(selectedCube == 6) {
            gl.glVertex3f(3.75f, 3.75f, fZ);      // LEFT  -  BOT  |  (  + ,  - )
            gl.glVertex3f(1.25f, 3.75f, fZ);      // RIGHT -  BOT  |  ( ++ ,  - )
            gl.glVertex3f(3.75f, 3.75f, fZ);      // RIGHT -  TOP  |  ( ++ ,  + )
            gl.glVertex3f(3.75f, 1.25f, fZ);      // LEFT  -  TOP  |  (  + ,  + )
        }

        //  x: -1, y: 1     (LEFT col, TOP row)          # 7
        if(selectedCube == 7) {
            gl.glVertex3f(-3.75f, 1.25f, fZ);      // LEFT  -  BOT  |  ( -- ,  + )
            gl.glVertex3f(-1.25f, 1.25f, fZ);      // RIGHT -  BOT  |  (  - ,  + )
            gl.glVertex3f(-1.25f, 3.75f, fZ);      // RIGHT -  TOP  |  (  - , ++ )
            gl.glVertex3f(-3.75f, 3.75f, fZ);      // LEFT  -  TOP  |  ( -- , ++ )
        }

        //  x: 0, y: 1      (MID col, TOP row)              # 8
        if(selectedCube == 8) {
            gl.glVertex3f(-1.25f, 1.25f, fZ);      // LEFT  -  BOT  |  (  - ,  + )
            gl.glVertex3f(1.25f, 1.25f, fZ);       // RIGHT -  BOT  |  (  + ,  + )
            gl.glVertex3f(1.25f, 3.75f, fZ);       // RIGHT -  TOP  |  (  + , ++ )
            gl.glVertex3f(-1.25f, 3.75f, fZ);      // LEFT  -  TOP  |  (  - , ++ )
        }

        //  x: 1, y: 1      (RIGHT col, TOP row)         # 9
        if(selectedCube == 9) {
            gl.glVertex3f(1.25f, 1.25f, fZ);      // LEFT  -  BOT  |  (  + ,  + )
            gl.glVertex3f(3.75f, 1.25f, fZ);      // RIGHT -  BOT  |  ( ++ ,  + )
            gl.glVertex3f(3.75f, 3.75f, fZ);      // RIGHT -  TOP  |  ( ++ , ++ )
            gl.glVertex3f(1.25f, 3.75f, fZ);      // LEFT  -  TOP  |  (  + , ++ )
        }

        gl.glEnd();

    }

    private void drawOutline(GL2 gl, int x, int y, int z) {

        float[] width = new float[2];

        // TOP
        gl.glBegin(GL_LINE_LOOP);

        gl.glColor3f(0.5f, 0.5f, 0.5f);             // inside black

        gl.glVertex3f(1.05f, 1.05f, 1.05f);           // bot/front right
        gl.glVertex3f(-1.05f,1.05f, 1.05f);            // bot/front left
        gl.glVertex3f(-1.05f,1.05f, -1.05f);             // top/back left
        gl.glVertex3f(1.05f, 1.05f, -1.05f);            // top/back right

        gl.glEnd();



        // BOTTOM - green
        gl.glBegin(GL_LINE_LOOP);
        gl.glColor3f(0.5f, 0.5f, 0.5f);             // inside black

        gl.glVertex3f(1.05f, -1.05f, 1.05f);           // bot/front right
        gl.glVertex3f(-1.05f,-1.05f, 1.05f);           // bot/front left
        gl.glVertex3f(-1.05f,-1.05f, -1.05f);          // top/back left
        gl.glVertex3f(1.05f, -1.05f, -1.05f);          // top/back right

        gl.glEnd();



        // FRONT - red
        gl.glBegin(GL_LINE_LOOP);
        gl.glColor3f(0.5f, 0.5f, 0.5f);             // inside black

        gl.glVertex3f(1.05f, -1.05f, 1.05f);         // bot left
        gl.glVertex3f(-1.05f,-1.05f, 1.05f);          // bot right
        gl.glVertex3f(-1.05f,1.05f,1.05f);           // top right
        gl.glVertex3f(1.05f, 1.05f,1.05f);          // top left

        gl.glEnd();



        // BACK - orange
        gl.glBegin(GL_LINE_LOOP);
        gl.glColor3f(0.5f, 0.5f, 0.5f);             // inside black

        gl.glVertex3f(1.05f, -1.05f,-1.05f);             //
        gl.glVertex3f(-1.05f,-1.05f,-1.05f);             //
        gl.glVertex3f(-1.05f,1.05f, -1.05f);           //
        gl.glVertex3f(1.05f, 1.05f, -1.05f);           //

        gl.glEnd();



        // RIGHT - yellow
        gl.glBegin(GL_LINE_LOOP);
        gl.glColor3f(0.5f, 0.5f, 0.5f);             // inside black

        gl.glVertex3f(1.05f, -1.05f,-1.05f);   // top left
        gl.glVertex3f(1.05f, -1.05f,1.05f);    // top right
        gl.glVertex3f(1.05f, 1.05f, 1.05f);   //
        gl.glVertex3f(1.05f, 1.05f, -1.05f);  //

        gl.glEnd();



        // LEFT - white
        gl.glBegin(GL_LINE_LOOP);
        gl.glColor3f(0.5f, 0.5f, 0.5f);             // inside black

        gl.glVertex3f(-1.05f, -1.05f,-1.05f);
        gl.glVertex3f(-1.05f, -1.05f,1.05f);
        gl.glVertex3f(-1.05f, 1.05f, 1.05f);
        gl.glVertex3f(-1.05f, 1.05f, -1.05f);

        gl.glEnd();
    }

    private void drawCube(GL2 gl, int x, int y, int z) {
        // Start drawing quads to form a single cube
        gl.glBegin(GL_QUADS);

        // TOP - blue
        gl.glColor3f(0.0f, 0.0f, 0.0f);             // inside black
        if(y == 2) {
            //if (disp1 == 1) System.out.println("Blue - top");
            gl.glColor3f(0.0f, 0.0f, 1.0f);  // change to blue
        }
        gl.glVertex3f(1.0f, 1.0f, 1.0f);           // bot/front right
        gl.glVertex3f(-1.0f,1.0f, 1.0f);            // bot/front left
        gl.glVertex3f(-1.0f,1.0f, -1.0f);             // top/back left
        gl.glVertex3f(1.0f, 1.0f, -1.0f);            // top/back right

        // BOTTOM - green
        gl.glColor3f(0.0f, 0.0f, 0.0f);             // inside black
        if(y == 0) {
            //if (disp1 == 1) System.out.println("Green - bot");
            gl.glColor3f(0.0f, 1.0f, 0.0f);  // change to green
        }
        gl.glVertex3f(1.0f, -1.0f, 1.0f);           // bot/front right
        gl.glVertex3f(-1.0f,-1.0f, 1.0f);           // bot/front left
        gl.glVertex3f(-1.0f,-1.0f, -1.0f);          // top/back left
        gl.glVertex3f(1.0f, -1.0f, -1.0f);          // top/back right

        // FRONT - red
        gl.glColor3f(0.0f, 0.0f, 0.0f);             // inside black
        if(z == 2) {
            //if (disp1 == 1) System.out.println("Red - front");
            gl.glColor3f(1.0f, 0.0f, 0.0f);  // change to red
        }
        gl.glVertex3f(1.0f, -1.0f, 1.0f);         // bot left
        gl.glVertex3f(-1.0f,-1.0f, 1.0f);          // bot right
        gl.glVertex3f(-1.0f,1.0f,1.0f);           // top right
        gl.glVertex3f(1.0f, 1.0f,1.0f);          // top left

        // BACK - orange
        gl.glColor3f(0.0f, 0.0f, 0.0f);             // inside black
        if(z == 0) {
            //if (disp1==1) System.out.println("Orange - back");
            gl.glColor3f(1.0f, 0.5f, 0.0f);  // change to orange
        }
        gl.glVertex3f(1.0f, -1.0f,-1.0f);             //
        gl.glVertex3f(-1.0f,-1.0f,-1.0f);             //
        gl.glVertex3f(-1.0f,1.0f, -1.0f);           //
        gl.glVertex3f(1.0f, 1.0f, -1.0f);           //

        // RIGHT - yellow
        gl.glColor3f(0.0f, 0.0f, 0.0f);             // inside black
        if(x == 2) {
            //if (disp1==1)System.out.println("Yellow - right");
            gl.glColor3f(1.0f, 1.0f, 0.0f);  // change to yellow
        }
        gl.glVertex3f(1.0f, -1.0f,-1.0f);   // top left
        gl.glVertex3f(1.0f, -1.0f,1.0f);    // top right
        gl.glVertex3f(1.0f, 1.0f, 1.0f);   //
        gl.glVertex3f(1.0f, 1.0f, -1.0f);  //

        // LEFT - white
        gl.glColor3f(0.0f, 0.0f, 0.0f);             // inside black
        if(x == 0) {
            //if (disp1==1) System.out.println("White - left");
            gl.glColor3f(1.0f, 1.0f, 1.0f);  // change to white
        }
        gl.glVertex3f(-1.0f, -1.0f,-1.0f);
        gl.glVertex3f(-1.0f, -1.0f,1.0f);
        gl.glVertex3f(-1.0f, 1.0f, 1.0f);
        gl.glVertex3f(-1.0f, 1.0f, -1.0f);

        gl.glEnd();

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
        switch (e.getKeyCode()){
            //  VIEW ROTATION
            case KeyEvent.VK_UP:
                if (e.isControlDown()){
                    viewX -= VIEW_ROT_STEP;
                    System.out.println("x: " + viewX);
                }

                break;

            case KeyEvent.VK_DOWN:
                if (e.isControlDown()){
                    viewX += VIEW_ROT_STEP;
                    System.out.println("x: " + viewX);
                }

                break;

            case KeyEvent.VK_LEFT:
                if (e.isControlDown()){
                    viewY -= VIEW_ROT_STEP;
                    System.out.println("y: " + viewY);
                }

                break;

            case KeyEvent.VK_RIGHT:
                if (e.isControlDown()){
                    viewY += VIEW_ROT_STEP;
                    System.out.println("y: " + viewY);
                }

                break;

            case KeyEvent.VK_PLUS:
                cubes ++;
                break;


            //  CUBE SECTION ROTATION
            case KeyEvent.VK_Q:
                e.setAttachment(KeyEvent.VK_Q);
                break;


            case KeyEvent.VK_W:
                break;

            case KeyEvent.VK_E:
                break;

            case KeyEvent.VK_A:
                break;

            case KeyEvent.VK_S:
                break;

            case KeyEvent.VK_D:
                break;

            case KeyEvent.VK_Z:
                break;

            case KeyEvent.VK_X:
                break;

            case KeyEvent.VK_C:
                break;

        }
        */


        if (e.isControlDown()) {
            switch (e.getKeyCode()) {
                case KeyEvent.VK_UP:
                    viewX -= VIEW_ROT_STEP;
                    System.out.println("x: " + viewX);
                    break;

                case KeyEvent.VK_DOWN:
                    viewX += VIEW_ROT_STEP;
                    System.out.println("x: " + viewX);
                    break;

                case KeyEvent.VK_LEFT:
                    viewY -= VIEW_ROT_STEP;
                    System.out.println("y: " + viewY);
                    break;

                case KeyEvent.VK_RIGHT:
                    viewY += VIEW_ROT_STEP;
                    System.out.println("y: " + viewY);
                    break;
            }
        } else if (e.getAttachment() != null) {
            System.out.println(e.getAttachment());



        } else {

            if (viewX <= DEFAULT_VIEW_X && viewX >= -DEFAULT_VIEW_X ) {
                System.out.println("Red");
            }

            switch (e.getKeyCode()) {
                case KeyEvent.VK_Q:
                    e.setAttachment(KeyEvent.VK_Q);

                    break;

                case KeyEvent.VK_W:
                    e.setAttachment(KeyEvent.VK_W);
                    break;

                case KeyEvent.VK_E:
                    e.setAttachment(KeyEvent.VK_E);
                    break;

                case KeyEvent.VK_A:
                    e.setAttachment(KeyEvent.VK_A);
                    break;

                case KeyEvent.VK_S:
                    e.setAttachment(KeyEvent.VK_S);
                    break;

                case KeyEvent.VK_D:
                    e.setAttachment(KeyEvent.VK_D);
                    break;

                case KeyEvent.VK_Z:
                    e.setAttachment(KeyEvent.VK_Z);
                    break;

                case KeyEvent.VK_X:
                    e.setAttachment(KeyEvent.VK_X);
                    break;

                case KeyEvent.VK_C:
                    e.setAttachment(KeyEvent.VK_C);
                    break;
            }
        }
    }

    private void selectCube(int[] position) {


    }

    private void rotateSliceX(boolean direction) {

    }

    private void rotateSliceY() {

    }

    private void rotateSliceZ() {

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