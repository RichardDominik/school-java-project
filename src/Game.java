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
    private HBox panel = new HBox();

    Group group = new Group();
    Camera camera = new PerspectiveCamera(true);
    Cylinder cylinder;

    SubScene scene = new SubScene(group, 1280, 720, true, SceneAntialiasing.BALANCED);

    PhongMaterial boxMaterial = new PhongMaterial();
    PhongMaterial floorMaterial = new PhongMaterial();
    PhongMaterial roofMaterial = new PhongMaterial();
    PhongMaterial firstAidMaterial = new PhongMaterial();
    PhongMaterial ammmoMaterial = new PhongMaterial();
    PhongMaterial weaponMarerial = new PhongMaterial();
    double X;
    double XX;

    private BorderPane layout = new BorderPane();
    private Scene root = new Scene(layout, 1280, 770);
    Text text;
    Player player = new Player(this);
    private char[][] map = new char[12][12];
    int countOfEnemies = 0;
    boolean checkEnemyConflict = false;
    boolean first = false;

    @Override
    public void start(Stage primaryStage) {
        preparePanel();

        layout.setCenter(scene);
        layout.setBottom(panel);

        scene.setFill(Color.LIGHTBLUE);
        scene.setCamera(camera);
        camera.setTranslateX(50);
        camera.setTranslateZ(50);
        camera.setTranslateY(0);
        camera.setFarClip(700);
        createWeapon();
        createFloor();
        readMapFromFile();
        loadMap();
        setEvents();
        player.start();

        primaryStage.setTitle("3D game");
        primaryStage.setScene(root);
        primaryStage.show();

        Timeline animation = new Timeline(new KeyFrame(Duration.millis(1000), o -> {
            update();
        }));
        animation.setCycleCount(Timeline.INDEFINITE);
        animation.play();
    }

    private void createWeapon(){
        weaponMarerial.setDiffuseMap(new Image("textures/MP_diff_orange.png"));
        cylinder = new Cylinder(0.025, 2);
        cylinder.setTranslateX(50);
        cylinder.setTranslateZ(50);
        cylinder.setTranslateY(0.1);
        cylinder.getTransforms().add(new Rotate(-90, 0, 0, 0, Rotate.X_AXIS));
        cylinder.setMaterial(weaponMarerial);
        group.getChildren().add(cylinder);
    }


    private void preparePanel() {
        panel.setMinHeight(50);
        panel.setStyle("-fx-background-image: url(\"textures/m-013-015-bg-min.jpg\");");
        panel.setAlignment(Pos.CENTER);
        panel.setSpacing(100);
        text = new Text("HEALTH: "+ player.health + "%" + "      AMMO: " + player.ammo);
        text.setFill(Color.RED);
        text.setStyle("-fx-font: 24 arial;");
        panel.getChildren().add(text);
    }

    private void setEvents() {
        root.setOnKeyPressed(event -> {
            switch (event.getCode()) {
                case R:
                    camera.translateYProperty().set(camera.getTranslateY() - 2);
                    break;
                case F:
                    camera.translateYProperty().set(camera.getTranslateY() + 2);
                    break;
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
            player.mousePressed = true;
            player.lastMouseX = event.getSceneX();
            player.lastMouseY = event.getSceneY();
        });

        root.setOnMouseReleased(event -> player.mousePressed = false);

        root.setOnMouseDragged(event -> player.mouseDragged(event));
    }

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

    private void createBox(int X, int Z) {
        Box box = new Box(50, 50,50);
        box.setTranslateX(X);
        box.setTranslateZ(Z);
        box.setMaterial(boxMaterial);
        group.getChildren().add(box);
    }

    private void createFirstAid(int X, int Z){
        FirstAid firstAid = new FirstAid(X, Z, firstAidMaterial);
        group.getChildren().add(firstAid);
    }

    private void createAmmo(int X, int Z){
        Ammo ammo = new Ammo(X, Z, ammmoMaterial);
        group.getChildren().add(ammo);
    }

    private void createFloor() {
        boxMaterial.setDiffuseMap(new Image("textures/m-035-min.jpg"));
        floorMaterial.setDiffuseMap(new Image("textures/m-001-min.jpg"));
        roofMaterial.setDiffuseMap(new Image("textures/m-024-min.jpg"));
        firstAidMaterial.setDiffuseMap(new Image("textures/firstaid.jpg"));
        ammmoMaterial.setDiffuseMap(new Image("textures/bullet.jpg"));
        int floorX = 0;
        int floorZ = 0;
        for(int i = 0; i < 11; i++) {
            for (int j = 0; j < 12; j++) {
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

    synchronized boolean checkCameraCollision () {
        CopyOnWriteArrayList<Node> c = new CopyOnWriteArrayList(group.getChildren());
            for (Node n : c) {
                if (n instanceof Box) {
                    Box b = (Box) n;
                    if (b.getBoundsInParent().intersects(camera.getBoundsInParent()) && b instanceof Ammo) {
                        player.ammo += ((Ammo) b).getSum();
                        Platform.runLater(() -> {
                            group.getChildren().remove(n);
                            text.setText("HEALTH: " + player.health + "%" + "      AMMO: " + player.ammo);
                        });
                    } else if (b.getBoundsInParent().intersects(camera.getBoundsInParent()) && b instanceof FirstAid) {
                        if (player.health + ((FirstAid) b).getSum() > 100) {
                            player.health = 100;
                        } else {
                            player.health += ((FirstAid) b).getSum();
                        }
                        Platform.runLater(() -> {
                            group.getChildren().remove(n);
                            text.setText("HEALTH: " + player.health + "%" + "      AMMO: " + player.ammo);
                        });
                    } else if (b.getBoundsInParent().intersects(camera.getBoundsInParent()) && b instanceof EnemyBox) {
                        player.health -= ((EnemyBox) b).enemy.healthFactor;

                        Platform.runLater(() -> {
                            text.setText("HEALTH: " + player.health + "%" + "      AMMO: " + player.ammo);

                            if (player.health <= 0) {
                                player.alive = false;
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


    private synchronized void shoot() {
        if(player.ammo > 0 && player.alive){
            Rotate rotate = (Rotate) cylinder.getTransforms().get(0);

            double angle = rotate.getAngle();

            if(!first){
                //pretoze je default camery z nejakeho dovodu -90 stupnov ?!
                angle = 0;
                first = true;
            }




            Projectile projectile = new Projectile(new PhongMaterial(Color.RED), camera, angle);


//            System.out.println(rotate.getAngle());
//            Sphere projectile = new Sphere(0.5);
//            projectile.setMaterial(new PhongMaterial(Color.RED));
//            projectile.setTranslateX(camera.getTranslateX() + dx);
//            projectile.setTranslateY(camera.getTranslateY());
//            projectile.setTranslateZ(camera.getTranslateZ() + dy);
//            Rotate sranda = (Rotate) cylinder.getTransforms().get(0);
//
//            projectile.getTransforms().add(sranda);
//            projectile.getTransforms().add(new Rotate(-90, 0, 0, 0, Rotate.X_AXIS));
//
//            System.out.println(cylinder.getTransforms());

//            projectile.setRotationAxis(Rotate.X_AXIS);
//            projectile.setRotate(400);

//            System.out.println(rotate.getAngle());

//            Translate translate = new Translate();
//            //translate.
//
//            translate.setX(20);
//            translate.setY(20);
//            translate.setZ(20);
//
//            projectile.getTransforms().add(translate);
//            projectile.setRotationAxis(Rotate.X_AXIS);



//            Rotate a = new Rotate();
//            a.setAngle(rotate.getAngle());
//            a.setPivotX(rotate.getMxx());
//            a.setAxis(Rotate.X_AXIS);





           // Rotate rotate = (Rotate) camera.getTransforms().get(0);
            //Rotate a = new Rotate(-rotate.getAngle(), camera.getTranslateX(), camera.getTranslateY() , camera.getTranslateZ(), Rotate.Y_AXIS);
            //projectile.getTransforms().add(a);

           // projectile.getTransforms().add(new Rotate(rotate.getAngle(), 0, 0, 0, Rotate.Z_AXIS));



            //System.out.println(cylinder.getLayoutY());

            //System.out.println(projectile.getTransforms().get(0));


            group.getChildren().add(projectile);
            player.ammo -= 1;
            text.setText("HEALTH: "+ player.health + "%" + "      AMMO: " + player.ammo);
        }
    }

    private synchronized void update() {
//        Platform.runLater(() -> {
//            List<Node> list = Collections.synchronizedList(group.getChildren());
//            synchronized (list) {
        CopyOnWriteArrayList<Node> c = new CopyOnWriteArrayList(group.getChildren());
                for (Node n : c) {
                    if (n instanceof Sphere && n instanceof Projectile) {
                        Sphere s = (Sphere) n;
//                        s.setTranslateX(s.getTranslateX() * 1.25);
//                        s.setTranslateZ(s.getTranslateZ() * 1.25);
                        ((Projectile) n).lenght += 10;
                        ((Projectile) n).recompute();
                        checkProjectileConflicts(s);
                    }
                }
//            }
//        });
    }

    private synchronized void checkProjectileConflicts(Sphere projectile){
//        Platform.runLater(() -> {
//            List<Node> list = Collections.synchronizedList(group.getChildren());
            Set<Node> toRemove = new HashSet<>();
            CopyOnWriteArrayList<Node> c = new CopyOnWriteArrayList(group.getChildren());
//            synchronized (list) {
                for (Node n : c) {
                    if (n instanceof Box) {
                        Box b = (Box) n;
                        if (b.getBoundsInParent().intersects(projectile.getBoundsInParent()) && b instanceof EnemyBox) {
                            if (checkEnemyConflict) {
                                ((EnemyBox) b).enemy.health -= 20;
                                checkEnemyConflict = false;
                            }
                            if (((EnemyBox) b).enemy.health <= 0) {
                                countOfEnemies--;
                                ((EnemyBox) b).enemy.alive = false;
                                toRemove.add(n);
                            }
                            Platform.runLater(() -> {
                                if (countOfEnemies == 0) {
                                    player.alive = false;
                                    text.setText("You win !");
                                }
                            });
                        } else if (b.getBoundsInParent().intersects(projectile.getBoundsInParent())) {
                            Platform.runLater(() -> {
                                toRemove.add(projectile);
                            });

                        }
                    }
                }
//            }
            group.getChildren().removeAll(toRemove);
//        });
    }

    public static void main(String[] args) {
        launch(args);
    }
}
