import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.Box;

public class Ammo extends Box {
    private int sum = 10;
    private final int WIDTH = 12;
    private final int HEIGHT = 5;
    private final int DEPTH = 12;
    private final int Y = -1;

    /**
     * Ammo constructor
     * @param X x position
     * @param Z z position
     * @param ammmoMaterial material
     */
    Ammo(int X, int Z,  PhongMaterial ammmoMaterial){
        super();
        setWidth(WIDTH);
        setHeight(HEIGHT);
        setDepth(DEPTH);
        setTranslateX(X);
        setTranslateZ(Z);
        setTranslateY(Y);
        setMaterial(ammmoMaterial);
    }

    /**
     * getter
     * @return amount of ammo
     */
    public int getSum() {
        return sum;
    }
}