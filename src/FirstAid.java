import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.Box;

public class FirstAid extends Box {
    private int sum = 40;

    FirstAid(int X, int Z,  PhongMaterial firstAidMaterial){
        super();
        setWidth(12);
        setHeight(5);
        setDepth(12);
        setTranslateX(X);
        setTranslateZ(Z);
        setTranslateY(-1);
        setMaterial(firstAidMaterial);
    }

    public int getSum() {
        return sum;
    }
}