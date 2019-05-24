import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.Box;

public class EnemyBox extends Box {
    private Enemy enemy;
    private final int WIDTH = 12;
    private final int HEIGHT = 5;
    private final int DEPTH = 12;
    private final int Y = -1;
    private double x;
    private double z;

    /**
     * getter
     * @return enemy object
     */
    public Enemy getEnemy() {
        return enemy;
    }

    /**
     * getter
     * @return x position
     */
    public double getX() {
        return x;
    }

    /**
     * setter
     * @param x set x position
     */
    public void setX(double x) {
        this.x = x;
    }

    /**
     * getter
     * @return z posistion
     */
    public double getZ() {
        return z;
    }

    /**
     * setter
     * @param z set z position
     */
    public void setZ(double z) {
        this.z = z;
    }

    /**
     * EnemyBox constructor
     * @param X x position
     * @param Z z position
     * @param enemyMaterial material
     * @param enemy enemy object
     */
    EnemyBox(int X, int Z, PhongMaterial enemyMaterial, Enemy enemy){
        super();
        this.x = X;
        this.z = Z;
        this.enemy = enemy;
        setWidth(WIDTH);
        setHeight(HEIGHT);
        setDepth(DEPTH);
        setTranslateX(X);
        setTranslateZ(Z);
        setTranslateY(Y);
        setMaterial(enemyMaterial);
    }

    /**
     * function for recompute enemy actual position
     */
    public void recompute(){
        setTranslateX(this.x);
        setTranslateZ(this.z);
    }
}