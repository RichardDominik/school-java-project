import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;

public class Enemy extends Thread {
    private Game game;

    boolean alive = true;
    int health = 100;
    int healthFactor = 10;

    PhongMaterial enemyMaterial = new PhongMaterial();

    Enemy(Game game, int X, int Z) {
        this.game = game;
        enemyMaterial.setDiffuseColor(Color.RED);
        EnemyBox enemy = new EnemyBox(X, Z, enemyMaterial, this);
        game.group.getChildren().add(enemy);
    }

    @Override
    public void run() {
        while (alive) {
            try {
                Thread.sleep(30);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}