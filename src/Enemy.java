import javafx.scene.image.Image;
import javafx.scene.paint.PhongMaterial;

public class Enemy extends Thread {
    private Game game;
    private double enemySpeed = 1.01;
    private boolean alive = true;
    private int health = 100;
    private int healthFactor = 5;
    private PhongMaterial enemyMaterial = new PhongMaterial();

    /**
     * setter
     * @param alive alive
     */
    public void setAlive(boolean alive) {
        this.alive = alive;
    }

    /**
     * setter
     * @param health enemy health
     */
    public void setHealth(int health) {
        this.health = health;
    }

    /**
     * getter
     * @return enemy speed
     */
    public double getEnemySpeed() {
        return enemySpeed;
    }


    /**
     * getter
     * @return health
     */
    public int getHealth() {
        return health;
    }

    /**
     * getter
     * @return health factor for conflicts with player
     */
    public int getHealthFactor() {
        return healthFactor;
    }

    /**
     * Enemy constructor
     * @param game game object
     * @param X x position
     * @param Z z position
     */
    Enemy(Game game, int X, int Z) {
        this.game = game;
        enemyMaterial.setDiffuseMap(new Image("textures/enemy.png"));
        EnemyBox enemy = new EnemyBox(X, Z, enemyMaterial, this);
        game.getGroup().getChildren().add(enemy);
    }

    /**
     * thread run
     */
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