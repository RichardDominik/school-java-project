import javafx.scene.Camera;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.Sphere;

public class Projectile extends Sphere {
//    private double dx;
//    private double dy;
     int lenght = 10;
     double angle;
     Camera camera;
    PhongMaterial material;

    Projectile(PhongMaterial material, Camera camera, double angle){
        super();
        this.angle = angle;
        this.camera = camera;
        this.material = material;


        double dx = lenght * Math.sin(Math.toRadians(angle));
        double dy = lenght * Math.cos(Math.toRadians(angle));
        double x = camera.getTranslateX();
        double y = camera.getTranslateY();
        double z = camera.getTranslateZ();

        this.setRadius(0.5);
        this.setMaterial(material);
        this.setTranslateX(x + dx);
        this.setTranslateY(y);
        this.setTranslateZ(z + dy);
//        this.dx = dx;
//        this.dy = dy;
    }

    public void recompute(){
        double dx = lenght * Math.sin(Math.toRadians(this.angle));
        double dy = lenght * Math.cos(Math.toRadians(this.angle));
        double x = this.camera.getTranslateX();
        double y = this.camera.getTranslateY();
        double z = this.camera.getTranslateZ();

        this.setRadius(0.5);
        this.setMaterial(material);
        this.setTranslateX(x + dx);
        this.setTranslateY(y);
        this.setTranslateZ(z + dy);
    }
}
