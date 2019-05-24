import javafx.scene.Camera;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.Sphere;

public class Projectile extends Sphere {
    private int lenght = 10;
    private double angle;
    private Camera camera;
    private  PhongMaterial material;

    /**
     * getter
     * @return length from player
     */
    public int getLenght() {
        return lenght;
    }

    /**
     * setter
     * @param lenght length from player
     */
    public void setLenght(int lenght) {
        this.lenght = lenght;
    }

    /**
     * Projectile constructor
     * @param material material
     * @param camera camera object
     * @param angle angle
     */
    Projectile(PhongMaterial material, Camera camera, double angle){
        super();

        double dx = lenght * Math.sin(Math.toRadians(angle));
        double dy = lenght * Math.cos(Math.toRadians(angle));
        double x = camera.getTranslateX();
        double y = camera.getTranslateY();
        double z = camera.getTranslateZ();

        this.angle = angle;
        this.camera = camera;
        this.material = material;
        this.setRadius(0.5);
        this.setMaterial(material);
        this.setTranslateX(x + dx);
        this.setTranslateY(y);
        this.setTranslateZ(z + dy);
    }

    /**
     * function to recompute projectile position
     */
    public void recompute(){
        double dx = lenght * Math.sin(Math.toRadians(this.angle));
        double dy = lenght * Math.cos(Math.toRadians(this.angle));
        double x = this.camera.getTranslateX();
        double y = this.camera.getTranslateY();
        double z = this.camera.getTranslateZ();

        this.setTranslateX(x + dx);
        this.setTranslateY(y);
        this.setTranslateZ(z + dy);
    }
}
