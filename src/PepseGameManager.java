import danogl.GameManager;
import danogl.GameObject;
import danogl.collisions.GameObjectCollection;
import danogl.collisions.Layer;
import danogl.gui.ImageReader;
import danogl.gui.SoundReader;
import danogl.gui.UserInputListener;
import danogl.gui.WindowController;
import danogl.util.Vector2;
import world.Sky;
import world.Terrain;
import pepse.world.daynight.Night;
import pepse.world.daynight.Sun;
import pepse.world.daynight.SunHalo;
import world.trees.Tree;
import world.trees.Tree;

import java.awt.*;
import java.util.ArrayList;

public class PepseGameManager extends GameManager {
    private final static Color HALO_COLOR= new Color(255, 255, 0, 20);
    private final static float DAY_CYCLE = 30f;


    public PepseGameManager(String windowTitle) {
        super(windowTitle, null);
    }

    @Override
    public void initializeGame(ImageReader imageReader, SoundReader soundReader,
                               UserInputListener inputListener,
                               WindowController windowController) {
        super.initializeGame(imageReader, soundReader, inputListener, windowController);
        Vector2 windowDimensions = windowController.getWindowDimensions();
        Terrain terrain = new Terrain(gameObjects(), Layer.STATIC_OBJECTS,
                windowDimensions, 0);
        terrain.createInRange(0, (int) windowDimensions.x());
        Sky.create(gameObjects(),windowDimensions,Layer.BACKGROUND);
        Night.create(gameObjects(), Layer.FOREGROUND, windowDimensions, DAY_CYCLE);
        GameObject sun = Sun.create(gameObjects(), Layer.BACKGROUND+1, windowDimensions, DAY_CYCLE);
        SunHalo.create(gameObjects(),Layer.BACKGROUND+2,sun,HALO_COLOR);
        Tree trees = new Tree(gameObjects(),Layer.STATIC_OBJECTS+1,terrain,0,windowDimensions);
        trees.createInRange(0,(int) windowDimensions.y());

    }

    public static void main(String[] args) {
        new PepseGameManager("Pepse").run();
    }

}

