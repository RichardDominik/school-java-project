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
import java.util.HashSet;
import java.util.Scanner;
import java.util.Set;

public class Game extends Application {
    // 2D UI Panel
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

    // Layout aplikácie
    private BorderPane layout = new BorderPane();
    private Scene root = new Scene(layout, 1280, 770);
    Text text;
    Player player = new Player(this);
    private char[][] map = new char[12][12];

    @Override
    public void start(Stage primaryStage) {
        preparePanel();

        layout.setCenter(scene); // hore bude 3D scéna
        layout.setBottom(panel); // dole 2D UI Panel

        scene.setFill(Color.LIGHTBLUE);
        scene.setCamera(camera);
        camera.setTranslateX(50);
        camera.setTranslateZ(50);
        camera.setTranslateY(0);
        camera.setFarClip(500);
        createWeapon();
        createFloor();
        readMapFromFile();
        loadMap();
        setEvents();
        player.start();


        primaryStage.setTitle("3D game");
        primaryStage.setScene(root);
        primaryStage.show();


        Timeline animation = new Timeline(new KeyFrame(Duration.millis(20), o -> {
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
        for (Node n: group.getChildren()) {
            if (n instanceof Box) {
                Box b = (Box) n;
                if (b.getBoundsInParent().intersects(camera.getBoundsInParent()) && b instanceof Ammo) {
                    player.ammo += ((Ammo) b).getSum();
                    System.out.println();
                    Platform.runLater(() -> {
                        group.getChildren().remove(n);
                        text.setText("HEALTH: "+ player.health + "%" + "      AMMO: " + player.ammo);
                    });
                } else if(b.getBoundsInParent().intersects(camera.getBoundsInParent()) && b instanceof FirstAid){
                    if(player.health + ((FirstAid) b).getSum() > 100){
                        player.health = 100;
                    } else {
                        player.health += ((FirstAid) b).getSum();
                    }
                    Platform.runLater(() -> {
                        group.getChildren().remove(n);
                        text.setText("HEALTH: "+ player.health + "%" + "      AMMO: " + player.ammo);
                    });
                } else if (b.getBoundsInParent().intersects(camera.getBoundsInParent()) && b instanceof EnemyBox){
                    player.health -= ((EnemyBox) b).enemy.healthFactor;

                    Platform.runLater(() -> {
                        text.setText("HEALTH: "+ player.health + "%" + "      AMMO: " + player.ammo);

                        if(player.health <= 0){
                            player.alive = false;
                            text.setText("You was killed by enemy !");
                        }
                    });

                } else if(b.getBoundsInParent().intersects(camera.getBoundsInParent()) ){
                    return true;
                }
            }
        }

        return false;
    }

    private void shoot() {
        if(player.ammo > 0){
            Sphere projectile = new Sphere(0.5);
            projectile.setTranslateX(camera.getTranslateX());
            projectile.setTranslateY(camera.getTranslateY());
            projectile.setTranslateZ(camera.getTranslateZ() + 10);
            projectile.setMaterial(new PhongMaterial(Color.RED));
            group.getChildren().add(projectile);
            player.ammo -= 1;
            text.setText("HEALTH: "+ player.health + "%" + "      AMMO: " + player.ammo);
        }
    }

    private synchronized void update() {
        Set<Node> toRemove = new HashSet<>();
        for (Node n : group.getChildren()) {
            if (n instanceof Sphere) {
                Sphere s = (Sphere) n;
                s.setTranslateZ(s.getTranslateZ() + 5);
//                if (s.getTranslateY() < -20) {
//                    toRemove.add(n);
//                } else {

//                }
            }
        }
        group.getChildren().removeAll(toRemove);
    }

    public static void main(String[] args) {
        launch(args);
    }
}
