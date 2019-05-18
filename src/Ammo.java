import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.Box;

public class Ammo extends Box {
    private int sum = 10;

    Ammo(int X, int Z,  PhongMaterial ammmoMaterial){
        super();
        setWidth(12);
        setHeight(5);
        setDepth(12);
        setTranslateX(X);
        setTranslateZ(Z);
        setTranslateY(-1);
        setMaterial(ammmoMaterial);
    }

    public int getSum() {
        return sum;
    }
}