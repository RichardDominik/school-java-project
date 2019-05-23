package textures;


import javafx.scene.Camera;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.Sphere;

public class Projectile extends Sphere {
    private double dx;
    private double dy;

    public Projectile(PhongMaterial material, Camera camera, double dx, double dy){
        super();
        this.setRadius(0.5);
        this.setMaterial(material);
        this.setTranslateX(camera.getTranslateX() + dx);
        this.setTranslateY(camera.getTranslateY());
        this.setTranslateZ(camera.getTranslateZ() * dy);
        this.dx = dx;
        this.dy = dy;
    }
}
