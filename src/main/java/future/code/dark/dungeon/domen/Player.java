package future.code.dark.dungeon.domen;

import future.code.dark.dungeon.config.Configuration;
import future.code.dark.dungeon.service.GameMaster;

public class Player extends DynamicObject {
    private static final int stepSize = 1;

    public Player(int xPosition, int yPosition) {
        super(xPosition, yPosition, Configuration.PLAYER_SPRITE);
    }

    public void move(Direction direction) {
        super.move(direction, stepSize);

        if (GameMaster.getInstance().getEnemies().stream()
                .anyMatch(this::collision)) {
            GameMaster.getInstance().deleteEnemy(this.xPosition, this.yPosition);
        }

        if(GameMaster.getInstance().getCoin().stream()
                .anyMatch(this::collision)) {
            GameMaster.getInstance().raiseCoin(this.xPosition, this.yPosition);
        }
        //если коллизия с выходом, то менять флаг levelCompleted
    }

    @Override
    public String toString() {
        return "Player{[" + xPosition + ":" + yPosition + "]}";
    }
}
