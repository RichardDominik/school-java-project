import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.Box;

public class EnemyBox extends Box {
    Enemy enemy;

    EnemyBox(int X, int Z, PhongMaterial enemyMaterial, Enemy enemy){
        super();
        this.enemy = enemy;
        setWidth(12);
        setHeight(5);
        setDepth(12);
        setTranslateX(X);
        setTranslateZ(Z);
        setTranslateY(-1);
        setMaterial(enemyMaterial);
    }
}