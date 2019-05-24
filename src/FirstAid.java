import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.Box;

public class FirstAid extends Box {
    private int sum = 40;
    private final int WIDTH = 12;
    private final int HEIGHT = 5;
    private final int DEPTH = 12;
    private final int Y = -1;

    /**
     * FirstAid constructor
     * @param X x position
     * @param Z z position
     * @param firstAidMaterial material
     */
    FirstAid(int X, int Z,  PhongMaterial firstAidMaterial){
        super();
        setWidth(WIDTH);
        setHeight(HEIGHT);
        setDepth(DEPTH);
        setTranslateX(X);
        setTranslateZ(Z);
        setTranslateY(Y);
        setMaterial(firstAidMaterial);
    }

    /**
     * function which return value for increasing player health
     * @return sum
     */
    public int getSum() {
        return sum;
    }
}