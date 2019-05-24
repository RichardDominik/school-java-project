import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Pos;
import javafx.scene.*;
import javafx.scene.image.Image;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.Box;
import javafx.scene.shape.Cylinder;
import javafx.scene.shape.Sphere;
import javafx.scene.text.Text;
import javafx.scene.transform.Rotate;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;
import java.util.concurrent.CopyOnWriteArrayList;

public class Game extends Application {
    private final int START_X = 50;
    private final int START_Z = 50;
    private HBox panel = new HBox();
    private Group group = new Group();
    private Camera camera = new PerspectiveCamera(true);
    private Cylinder cylinder;
    private SubScene scene = new SubScene(group, 1280, 720, true, SceneAntialiasing.BALANCED);
    private PhongMaterial boxMaterial = new PhongMaterial();
    private PhongMaterial floorMaterial = new PhongMaterial();
    private PhongMaterial roofMaterial = new PhongMaterial();
    private PhongMaterial firstAidMaterial = new PhongMaterial();
    private PhongMaterial ammmoMaterial = new PhongMaterial();
    private PhongMaterial weaponMarerial = new PhongMaterial();
    private PhongMaterial projectileMaterial = new PhongMaterial();
    private BorderPane layout = new BorderPane();
    private Scene root = new Scene(layout, 1280, 770);
    private Text text;
    private Player player = new Player(this);
    private char[][] map = new char[12][12];
    private int countOfEnemies = 0;
    private boolean checkEnemyConflict = false;
    private boolean first = false;

    /**
     * getter
     * @return group
     */
    public Group getGroup() {
        return group;
    }

    /**
     * getter
     * @return camera
     */
    public Camera getCamera() {
        return camera;
    }

    /**
     * getter
     * @return cylinder
     */
    public Cylinder getCylinder() {
        return cylinder;
    }

    /**
     * @param primaryStage stage
     */
    @Override
    public void start(Stage primaryStage) {
        preparePanel();

        layout.setCenter(scene);
        layout.setBottom(panel);

        scene.setFill(Color.LIGHTBLUE);
        scene.setCamera(camera);
        camera.setTranslateX(START_X);
        camera.setTranslateZ(START_Z);
        camera.setTranslateY(0);
        camera.setFarClip(700);
        createWeapon();
        readMapFromFile();
        loadMap();
        createFloorAndRoof();
        setEvents();
        player.start();

        primaryStage.setTitle("3D game");
        primaryStage.setScene(root);
        primaryStage.show();

        Timeline animation = new Timeline(new KeyFrame(Duration.millis(40), o -> {
            update();
        }));
        animation.setCycleCount(Timeline.INDEFINITE);
        animation.play();
    }

    /**
     * function which create weapon for player
     */
    private void createWeapon(){
        weaponMarerial.setDiffuseMap(new Image("textures/MP_diff_orange.png"));
        cylinder = new Cylinder(0.025, 2);
        cylinder.setTranslateX(START_X);
        cylinder.setTranslateZ(START_Z);
        cylinder.setTranslateY(0.1);
        cylinder.getTransforms().add(new Rotate(-90, 0, 0, 0, Rotate.X_AXIS));
        cylinder.setMaterial(weaponMarerial);
        group.getChildren().add(cylinder);
    }

    /**
     * function which prepare panel with informations about health and ammo for player
     */
    private void preparePanel() {
        panel.setMinHeight(50);
        panel.setStyle("-fx-background-image: url(\"textures/m-013-015-bg-min.jpg\");");
        panel.setAlignment(Pos.CENTER);
        panel.setSpacing(100);
        text = new Text("HEALTH: "+ player.getHealth() + "%" + "      AMMO: " + player.getAmmo());
        text.setFill(Color.RED);
        text.setStyle("-fx-font: 24 arial;");
        panel.getChildren().add(text);
    }

    /**
     * function which sets events
     */
    private void setEvents() {
        root.setOnKeyPressed(event -> {
            switch (event.getCode()) {
                case SPACE:
                    checkEnemyConflict = true;
                    shoot();
                    break;
                default:
                    player.setDirection(event);
                    break;
            }

        });

        root.setOnKeyReleased(event -> player.stopMovement());
        camera.getTransforms().add(new Rotate(0, Rotate.Y_AXIS));

        root.setOnMousePressed(event -> {
            player.setMousePressed(true);
            player.setLastMouseX(event.getSceneX());
            player.setLastMouseY(event.getSceneY());
        });

        root.setOnMouseReleased(event -> player.setMousePressed(false));
        root.setOnMouseDragged(event -> player.mouseDragged(event));
    }

    /**
     * function which read map from input file
     */
    private void readMapFromFile(){
        try{
            Scanner input = new Scanner(new File("src/level.txt"));
            int row = 0;
            int column = 0;

            while(input.hasNext()){
                String c = input.next();
                if(!c.equals(" ") && !c.equals(",")){
                    map[row][column] = c.charAt(0);
                    column++;
                } else {
                    column = 0;
                    row++;
                }
            }
            input.close();
        } catch (FileNotFoundException e) {
            System.out.println("File not found");
        }
    }

    /**
     * function which provide loading map from map array
     */
    private void loadMap() {
        int Z = 0;
        for (int i = map.length - 1; i >= 0; i--) {
            int X = 0;
            for (int j = 0; j < map[0].length; j++) {
                if (map[i][j] == '#') {
                    createBox(X, Z);
                } else if (map[i][j] == 'h'){
                    createFirstAid(X, Z);
                } else if(map[i][j] == 'a'){
                    createAmmo(X, Z);
                } else if (map[i][j] == 'e'){
                    new Enemy(this, X, Z);
                    countOfEnemies++;
                }
                X += 50;
            }
            Z += 50;
        }
    }

    /**
     * function which create BOX
     * @param X x position
     * @param Z z position
     */
    private void createBox(int X, int Z) {
        Box box = new Box(50, 50,50);
        box.setTranslateX(X);
        box.setTranslateZ(Z);
        box.setMaterial(boxMaterial);
        group.getChildren().add(box);
    }

    /**
     * function which create FIRST AID BOX
     * @param X x position
     * @param Z y position
     */
    private void createFirstAid(int X, int Z){
        FirstAid firstAid = new FirstAid(X, Z, firstAidMaterial);
        group.getChildren().add(firstAid);
    }

    /**
     * function which create AMMO BOX
     * @param X x position
     * @param Z z position
     */
    private void createAmmo(int X, int Z){
        Ammo ammo = new Ammo(X, Z, ammmoMaterial);
        group.getChildren().add(ammo);
    }

    /**
     * function which create floor and roof for loaded level
     */
    private void createFloorAndRoof() {
        boxMaterial.setDiffuseMap(new Image("textures/m-035-min.jpg"));
        floorMaterial.setDiffuseMap(new Image("textures/m-001-min.jpg"));
        roofMaterial.setDiffuseMap(new Image("textures/m-024-min.jpg"));
        firstAidMaterial.setDiffuseMap(new Image("textures/firstaid.jpg"));
        ammmoMaterial.setDiffuseMap(new Image("textures/bullet.jpg"));
        int floorX = 0;
        int floorZ = 0;

        for(int i = 0; i < map.length; i++) {
            for (int j = 0; j < map[0].length; j++) {
                Box box = new Box(50, 1, 50);
                Box box2 = new Box(50, 1, 50);
                box.setTranslateX(floorX);
                box.setTranslateY(15);
                box.setTranslateZ(floorZ);
                box2.setTranslateX(floorX);
                box2.setTranslateY(-25);
                box2.setTranslateZ(floorZ);
                floorX += 50;
                box.setMaterial(floorMaterial);
                box2.setMaterial(roofMaterial);
                group.getChildren().add(box);
                group.getChildren().add(box2);
            }
            floorX = 0;
            floorZ += 50;
        }
    }

    /**
     *  function which checks all possible conflicts for camera
     * @return boolean
     */
    synchronized boolean checkCameraCollision () {
        CopyOnWriteArrayList<Node> c = new CopyOnWriteArrayList(group.getChildren());
            for (Node n : c) {
                if (n instanceof Box) {
                    Box b = (Box) n;
                    if (b.getBoundsInParent().intersects(camera.getBoundsInParent()) && b instanceof Ammo) {
                        player.setAmmo(player.getAmmo() + ((Ammo) b).getSum());
                        Platform.runLater(() -> {
                            group.getChildren().remove(n);
                            text.setText("HEALTH: " + player.getHealth() + "%" + "      AMMO: " + player.getAmmo());
                        });
                    } else if (b.getBoundsInParent().intersects(camera.getBoundsInParent()) && b instanceof FirstAid) {
                        if (player.getHealth() + ((FirstAid) b).getSum() > 100) {
                            player.setHealth(100);
                        } else {
                            player.setHealth(player.getHealth() + ((FirstAid) b).getSum());
                        }
                        Platform.runLater(() -> {
                            group.getChildren().remove(n);
                            text.setText("HEALTH: " + player.getHealth() + "%" + "      AMMO: " + player.getAmmo());
                        });
                    } else if (b.getBoundsInParent().intersects(camera.getBoundsInParent()) && b instanceof EnemyBox) {
                        player.setHealth(player.getHealth() - ((EnemyBox) b).getEnemy().getHealthFactor());

                        Platform.runLater(() -> {
                            text.setText("HEALTH: " + player.getHealth() + "%" + "      AMMO: " + player.getAmmo());

                            if (player.getHealth() <= 0) {
                                player.setAlive(false);
                                text.setText("You was killed by enemy !");
                            }
                        });
                    } else if (b.getBoundsInParent().intersects(camera.getBoundsInParent())) {
                        return true;
                    }
                }
            }
        return false;
    }

    /**
     * function which provides shooting
     */
    private synchronized void shoot() {
        if(player.getAmmo() > 0 && player.getAlive()){
            Rotate rotate = (Rotate) cylinder.getTransforms().get(0);

            double angle = rotate.getAngle();

            if(!first && angle == -90){
                //deault camera position is -90 deg ?!
                angle = 0;
                first = true;
            }

            projectileMaterial.setDiffuseMap(new Image("textures/flaretest.jpg"));
            Projectile projectile = new Projectile(projectileMaterial, camera, angle);

            group.getChildren().add(projectile);
            player.setAmmo(player.getAmmo() - 1);
            text.setText("HEALTH: "+ player.getHealth() + "%" + "      AMMO: " + player.getAmmo());
        }
    }

    /**
     * function for updating enemy and projectile positions
     */
    private synchronized void update() {
        CopyOnWriteArrayList<Node> c = new CopyOnWriteArrayList(group.getChildren());
        for (Node n : c) {
            if (n instanceof Projectile) {
                Sphere s = (Sphere) n;
                ((Projectile) n).setLenght(((Projectile) n).getLenght() + 10);
                ((Projectile) n).recompute();
                checkProjectileConflicts(s);
            } else if (n instanceof EnemyBox){
                if(player.getAlive()){
                    double diffX = camera.getTranslateX() - ((EnemyBox) n).getX();
                    double diffZ = camera.getTranslateZ() - ((EnemyBox) n).getZ();
                    float angle = (float)Math.atan2(diffZ, diffX);
                    ((EnemyBox) n).setX(((EnemyBox) n).getX() + ((EnemyBox) n).getEnemy().getEnemySpeed() * Math.cos(angle));
                    ((EnemyBox) n).setZ(((EnemyBox) n).getZ() + ((EnemyBox) n).getEnemy().getEnemySpeed() * Math.sin(angle));
                    ((EnemyBox) n).recompute();
                }
            }
        }
    }

    /**
     * function which checks all possible conflicts for projectile
     * @param projectile current projectile
     */
    private synchronized void checkProjectileConflicts(Sphere projectile){
        Set<Node> toRemove = new HashSet<>();
        CopyOnWriteArrayList<Node> c = new CopyOnWriteArrayList(group.getChildren());
        for (Node n : c) {
            if (n instanceof Box) {
                Box b = (Box) n;
                if (b.getBoundsInParent().intersects(projectile.getBoundsInParent()) && b instanceof EnemyBox) {
                    if (checkEnemyConflict) {
                        ((EnemyBox) b).getEnemy().setHealth(((EnemyBox) b).getEnemy().getHealth() - 20);
                        checkEnemyConflict = false;
                    }
                    if (((EnemyBox) b).getEnemy().getHealth() <= 0) {
                        countOfEnemies--;
                        ((EnemyBox) b).getEnemy().setAlive(false);
                        toRemove.add(n);
                    }
                    Platform.runLater(() -> {
                        if (countOfEnemies == 0) {
                            player.setAlive(false);
                            text.setText("You win !");
                        }
                    });
                } else if (b.getBoundsInParent().intersects(projectile.getBoundsInParent())) {
                        toRemove.add(projectile);
                }
            }
        }
        group.getChildren().removeAll(toRemove);
    }

    /**
     * function for launching application
     * @param args args
     */
    public static void main(String[] args) {
        launch(args);
    }
}
