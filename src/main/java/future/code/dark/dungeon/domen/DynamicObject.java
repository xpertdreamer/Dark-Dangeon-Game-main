package future.code.dark.dungeon.domen;

import future.code.dark.dungeon.config.Configuration;
import future.code.dark.dungeon.service.GameMaster;

import java.util.Timer;
import static future.code.dark.dungeon.config.Configuration.MILLISECONDS_PER_FRAME;


public abstract class DynamicObject extends GameObject {

    public DynamicObject(int xPosition, int yPosition, String imagePath) {
        super(xPosition, yPosition, imagePath);
    }

    public enum Direction {
        UP, DOWN, LEFT, RIGHT
    }

    protected void enemyMove(){

    }

    protected void move(Direction direction, int distance) {
        int tmpXPosition = getXPosition();
        int tmpYPosition = getYPosition();

        switch (direction) {
            case UP -> tmpYPosition -= distance;
            case DOWN -> tmpYPosition += distance;
            case LEFT -> tmpXPosition -= distance;
            case RIGHT -> tmpXPosition += distance;
        }

        if (isAllowedSurface(tmpXPosition, tmpYPosition) && youCanGo(tmpXPosition, tmpYPosition)) {
            xPosition = tmpXPosition;
            yPosition = tmpYPosition;
        }
    }

    public Boolean youCanGo(int x, int y)
    {
        if(coinCounter == 9) GameMaster.getInstance().levelComplete = true;
        if(!GameMaster.getInstance().levelComplete
                && GameMaster.getInstance().getExit().getXPosition() != x
                && GameMaster.getInstance().getExit().getYPosition() != y) return true;

        if (GameMaster.getInstance().levelComplete == false && x == GameMaster.getInstance().getExit().getXPosition()
        && y == GameMaster.getInstance().getExit().getYPosition()) return false;
        else return true;
    }

    public Boolean collision(GameObject gameObject)
    {
        return this.getXPosition() == gameObject.getXPosition()
            && this.getYPosition() == gameObject.getYPosition();
    }

    private Boolean isAllowedSurface(int x, int y) {
        return GameMaster.getInstance().getMap().getMap()[y][x] != Configuration.WALL_CHARACTER;
    }

}
