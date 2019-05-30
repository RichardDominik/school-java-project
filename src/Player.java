import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.transform.Rotate;

// some parts are taken from Labyrinth.java Author: Lukas Gajdosech

public class Player extends Thread {
    private final float MOUSE_SENSITIVITY = 0.1f;
    private enum Dir {FORWARD, BACKWARD, STOP}
    private Dir direction = Dir.STOP;
    private Game game;
    private boolean mousePressed = false;
    private boolean alive = true;
    private double angle = 0;
    private double angle1 = 0;
    private double lastMouseX;
    private double lastMouseY;
    private int health = 100;
    private int ammo = 30;

    /**
     * setter
     * @param mousePressed boolean on mouse press
     */
    public void setMousePressed(boolean mousePressed) {
        this.mousePressed = mousePressed;
    }

    /**
     * setter
     * @param alive boolean if player is alive
     */
    public void setAlive(boolean alive) {
        this.alive = alive;
    }

    /**
     * getter
     * @return boolean if player is alive
     */
    public boolean getAlive(){
        return alive;
    }

    /**
     * setter
     * @param lastMouseX mouse x position
     */
    public void setLastMouseX(double lastMouseX) {
        this.lastMouseX = lastMouseX;
    }

    /**
     * setter
     * @param lastMouseY mouse y position
     */
    public void setLastMouseY(double lastMouseY) {
        this.lastMouseY = lastMouseY;
    }

    /**
     * getter
     * @return health in percentage
     */
    public int getHealth() {
        return health;
    }

    /**
     * setter
     * @param health health in percentage
     */
    public void setHealth(int health) {
        this.health = health;
    }

    /**
     * getter
     * @return ammo
     */
    public int getAmmo() {
        return ammo;
    }

    /**
     * setter
     * @param ammo ammo
     */
    public void setAmmo(int ammo) {
        this.ammo = ammo;
    }

    /**
     * Player constructor
     * @param game game object
     */
    Player(Game game) {
        this.game = game;
    }

    /**
     * thread run
     */
    @Override
    public void run() {
        while (alive) {
            try {
                movement();
                Thread.sleep(30);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * function which sets direction by key event
     * @param event KeyEvent
     */
    void setDirection(KeyEvent event) {
        switch (event.getCode()) {
            case W:
                direction = Dir.FORWARD;
                break;
            case S:
                direction = Dir.BACKWARD;
                break;
        }
    }

    /**
     * function which react on mouseDragged
     * @param event mouseEvent
     */
    void mouseDragged(MouseEvent event) {
        if (mousePressed) {
            Rotate rotate = (Rotate) game.getCamera().getTransforms().get(0);
            game.getCamera().getTransforms().clear();
            game.getCylinder().getTransforms().clear();

            rotate.setAngle(rotate.getAngle() + (event.getSceneX() - lastMouseX) * MOUSE_SENSITIVITY);
            Rotate rotate1 = new Rotate();
            rotate1.setAngle(rotate.getAngle() + (event.getSceneX() - lastMouseX) * MOUSE_SENSITIVITY);

            angle = rotate.getAngle();
            angle1 = rotate1.getAngle();
            game.getCamera().getTransforms().add(rotate);
            game.getCylinder().getTransforms().add(rotate);
            game.getCylinder().getTransforms().add(new Rotate(-90, 0, 0, 0, Rotate.X_AXIS));
        }
        lastMouseX = event.getSceneX();
        lastMouseY = event.getSceneY();
    }

    /**
     * function which stop player movement
     */
    void stopMovement() {
        direction = Dir.STOP;
    }

    /**
     * function which provide player movement
     */
    private void movement() {
        if (direction == Dir.STOP) return;
        double dx = Math.sin(Math.toRadians(angle));
        double dz = Math.cos(Math.toRadians(angle));
        double oldZ = game.getCamera().getTranslateZ();
        double oldX = game.getCamera().getTranslateX();
        switch (direction) {
            case FORWARD:
                game.getCamera().translateZProperty().set(game.getCamera().getTranslateZ() + dz);
                game.getCamera().translateXProperty().set(game.getCamera().getTranslateX() + dx);
                game.getCylinder().translateZProperty().set(game.getCylinder().getTranslateZ() + dz);
                game.getCylinder().translateXProperty().set(game.getCylinder().getTranslateX() + dx);
                break;
            case BACKWARD:
                game.getCamera().translateZProperty().set(game.getCamera().getTranslateZ() - dz);
                game.getCamera().translateXProperty().set(game.getCamera().getTranslateX() - dx);
                game.getCylinder().translateZProperty().set(game.getCylinder().getTranslateZ() - dz);
                game.getCylinder().translateXProperty().set(game.getCylinder().getTranslateX() - dx);
                break;
        }
        if (game.checkCameraCollision()) {
            game.getCamera().translateZProperty().set(oldZ);
            game.getCamera().translateXProperty().set(oldX);
            game.getCylinder().translateZProperty().set(oldZ);
            game.getCylinder().translateXProperty().set(oldX);
        }
    }
}
