package future.code.dark.dungeon.service;

import future.code.dark.dungeon.GameFrame;
import future.code.dark.dungeon.config.Configuration;
import future.code.dark.dungeon.domen.Coin;
import future.code.dark.dungeon.domen.DynamicObject;
import future.code.dark.dungeon.domen.Enemy;
import future.code.dark.dungeon.domen.Exit;
import future.code.dark.dungeon.domen.GameObject;
import future.code.dark.dungeon.domen.Map;
import future.code.dark.dungeon.domen.Player;
import javax.swing.*;
import javax.swing.text.html.ImageView;
import java.awt.*;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.util.stream.Collectors;

import static future.code.dark.dungeon.config.Configuration.*;
import static future.code.dark.dungeon.domen.GameObject.coinCounter;

public class GameMaster {

    private static GameMaster instance;
    public boolean levelComplete = false;
    public static Integer enemyCounter = 0;
    private final Map map;
    private final List<GameObject> gameObjects;
    private Image end = new ImageIcon(END_IMAGE).getImage();

    public static synchronized GameMaster getInstance() {
        if (instance == null) {
            instance = new GameMaster();
        }
        return instance;
    }

    private GameMaster() {
        try {
            this.map = new Map(Configuration.MAP_FILE_PATH);
            this.gameObjects = initGameObjects(map.getMap());
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    private List<GameObject> initGameObjects(char[][] map) {
        List<GameObject> gameObjects = new ArrayList<>();
        Consumer<GameObject> addGameObject = gameObjects::add;
        Consumer<Enemy> addEnemy = enemy -> {if (ENEMIES_ACTIVE) gameObjects.add(enemy);};

        for (int i = 0; i < map.length; i++) {
            for (int j = 0; j < map[i].length; j++) {
                switch (map[i][j]) {
                    case EXIT_CHARACTER -> addGameObject.accept(new Exit(j, i));
                    case COIN_CHARACTER -> addGameObject.accept(new Coin(j, i));
                    case ENEMY_CHARACTER -> addEnemy.accept(new Enemy(j, i));
                    case PLAYER_CHARACTER -> addGameObject.accept(new Player(j, i));
                }
            }
        }

        return gameObjects;
    }

    public void renderFrame(Graphics graphics) {
        if(!endOfGame())
        {
            getMap().render(graphics);
            getStaticObjects().forEach(gameObject -> gameObject.render(graphics));
            getEnemies().forEach(gameObject -> gameObject.render(graphics));
            getPlayer().render(graphics);
            graphics.setColor(Color.WHITE);
            graphics.drawString(getPlayer().toString(), 10, 20);
           //graphics.drawString(enemyCounter.toString(), 100, 20);
            graphics.drawString(getCoinsCount().toString(), 1120, 20);
        } else { graphics.drawImage(end, 180, 0, null);}
    }

    public Boolean endOfGame(){
        if(getExit().getXPosition() == getPlayer().getXPosition()
        && getExit().getYPosition() == getPlayer().getYPosition()) return true;
        else return false;
    }

    public Player getPlayer() {
        return (Player) gameObjects.stream()
                .filter(gameObject -> gameObject instanceof Player)
                .findFirst()
                .orElseThrow();
    }

    private List<GameObject> getStaticObjects() {
        return gameObjects.stream()
                .filter(gameObject -> !(gameObject instanceof DynamicObject))
                .collect(Collectors.toList());
    }

    public List<Enemy> getEnemies() {
        return gameObjects.stream()
                .filter(gameObject -> gameObject instanceof Enemy)
                .map(gameObject -> (Enemy) gameObject)
                .collect(Collectors.toList());
    }

    public List<Coin> getCoin()
    {
        return gameObjects.stream()
                .filter(gameObject -> gameObject instanceof Coin)
                .map(gameObject -> (Coin) gameObject)
                .collect(Collectors.toList());
    }

    public Integer getCoinsCount()
    {
        return coinCounter;
    }

    public Exit getExit()
    {
        return (Exit) gameObjects.stream()
                .filter(gameObject -> gameObject instanceof Exit)
                .findFirst()
                .orElseThrow();
    }

    public Boolean deleteEnemy(int x, int y)
    {
        enemyCounter++;
        return gameObjects.removeIf(object -> object instanceof Enemy
                && object.getXPosition() == x
                && object.getYPosition() ==y);
    }

    public Boolean raiseCoin(int x, int y)
    {
        coinCounter++;
        return gameObjects.removeIf(object -> object instanceof Coin
                && object.getXPosition() == x
                && object.getYPosition() == y);
    }

    public Map getMap() {
        return map;
    }

}
