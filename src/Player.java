import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.transform.Rotate;

public class Player extends Thread {
    private final float MOUSE_SENSITIVITY = 0.1f;
    private enum Dir {FORWARD, BACKWARD, STOP}
    private Dir direction = Dir.STOP;
    private Game game;

    boolean mousePressed = false;
    boolean alive = true;
    double angle = 0;
    double angle1 = 0;
    double lastMouseX;
    double lastMouseY;
    int health = 60;
    int ammo = 30;

    Player(Game game) {
        this.game = game;
    }

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

    void mouseDragged(MouseEvent event) {
        if (mousePressed) {
            Rotate rotate = (Rotate) game.camera.getTransforms().get(0);

            game.camera.getTransforms().clear();
            game.cylinder.getTransforms().clear();

            rotate.setAngle(rotate.getAngle() + (event.getSceneX() - lastMouseX) * MOUSE_SENSITIVITY);
            //rotate.setAngle(rotate.getAngle() + (event.getSceneY() - lastMouseY) * MOUSE_SENSITIVITY);
            Rotate rotate1 = new Rotate();
            rotate1.setAngle(rotate.getAngle() + (event.getSceneX() - lastMouseX) * MOUSE_SENSITIVITY);


            angle = rotate.getAngle();
            angle1 = rotate1.getAngle();
            game.camera.getTransforms().add(rotate);
            game.cylinder.getTransforms().add(rotate);
            game.cylinder.getTransforms().add(new Rotate(-90, 0, 0, 0, Rotate.X_AXIS));



            /* Alternative without Rotate matrix
            lab.camera.setRotationAxis(Rotate.Y_AXIS);
            lab.camera.setRotate(lab.camera.getRotate() + (event.getSceneX() - lastMouseX) * MOUSE_SENSITIVITY);
            angle = lab.camera.getRotate();
            */
        }
        lastMouseX = event.getSceneX();
        lastMouseY = event.getSceneY();
    }

    void stopMovement() {
        direction = Dir.STOP;
    }

    private void movement() {
        if (direction == Dir.STOP) return;
        double dx = Math.sin(Math.toRadians(angle));
        double dz = Math.cos(Math.toRadians(angle));
        double oldZ = game.camera.getTranslateZ();
        double oldX = game.camera.getTranslateX();
        switch (direction) {
            case FORWARD:
                game.camera.translateZProperty().set(game.camera.getTranslateZ() + dz);
                game.camera.translateXProperty().set(game.camera.getTranslateX() + dx);
                game.cylinder.translateZProperty().set(game.cylinder.getTranslateZ() + dz);
                game.cylinder.translateXProperty().set(game.cylinder.getTranslateX() + dx);
                break;
            case BACKWARD:
                game.camera.translateZProperty().set(game.camera.getTranslateZ() - dz);
                game.camera.translateXProperty().set(game.camera.getTranslateX() - dx);
                game.cylinder.translateZProperty().set(game.cylinder.getTranslateZ() - dz);
                game.cylinder.translateXProperty().set(game.cylinder.getTranslateX() - dx);
                break;
        }
        if (game.checkCameraCollision()) {
            game.camera.translateZProperty().set(oldZ);
            game.camera.translateXProperty().set(oldX);
            game.cylinder.translateZProperty().set(oldZ);
            game.cylinder.translateXProperty().set(oldX);
        }
    }
}
