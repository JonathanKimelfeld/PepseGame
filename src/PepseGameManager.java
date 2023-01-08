import danogl.GameManager;
import danogl.GameObject;
import danogl.collisions.GameObjectCollection;
import danogl.collisions.Layer;
import danogl.components.GameObjectPhysics;
import danogl.gui.ImageReader;
import danogl.gui.SoundReader;
import danogl.gui.UserInputListener;
import danogl.gui.WindowController;
import danogl.gui.rendering.Camera;
import danogl.util.Vector2;
import world.Avatar;
import world.Sky;
import world.Terrain;
import world.daynight.Night;
import world.daynight.Sun;
import world.daynight.SunHalo;
import world.trees.Tree;
import world.trees.Tree;

import java.awt.*;
import java.util.ArrayList;

public class PepseGameManager extends GameManager {
    private final static Color HALO_COLOR = new Color(255, 255, 0, 20);
    private final static float DAY_CYCLE = 30f;

    private final static int groundLayer = Layer.STATIC_OBJECTS;
    private final static int nightLayer = Layer.FOREGROUND;
    private final static int skyLayer = Layer.BACKGROUND;
    private final static int sunLayer = Layer.BACKGROUND + 1;
    private final static int treeLayer = Layer.STATIC_OBJECTS + 1;
    private final static int leafLayer = Layer.STATIC_OBJECTS - 1;
    private final static int haloLayer = Layer.BACKGROUND + 2;
    private final static int avatarLayer = Layer.DEFAULT;


    public PepseGameManager(String windowTitle) {
        super(windowTitle, null);
    }

    @Override
    public void initializeGame(ImageReader imageReader, SoundReader soundReader,
                               UserInputListener inputListener,
                               WindowController windowController) {
        super.initializeGame(imageReader, soundReader, inputListener, windowController);
        Vector2 windowDimensions = windowController.getWindowDimensions();
        // create terrain
        Terrain terrain = new Terrain(gameObjects(), groundLayer,
                windowDimensions, 0);
        terrain.createInRange(0, (int) windowDimensions.x());
        // create sky
        Sky.create(gameObjects(), windowDimensions, skyLayer);
        // create the night feature
        Night.create(gameObjects(), nightLayer, windowDimensions, DAY_CYCLE);
        // create sun and halo
        GameObject sun = Sun.create(gameObjects(), sunLayer, windowDimensions, DAY_CYCLE);
        SunHalo.create(gameObjects(), haloLayer, sun, HALO_COLOR);
        // create trees (with leaves)
        Tree trees = new Tree(gameObjects(), treeLayer, terrain, 0, windowDimensions);
        trees.createInRange(0, (int) windowDimensions.y());
        gameObjects().layers().shouldLayersCollide(leafLayer, groundLayer, true);
        // create avatar
        int initialAvatarXPos = (int) windowDimensions.x() / 2;
        Vector2 avatarPosition = new Vector2(initialAvatarXPos, (int) windowDimensions.y() / 2);
        Avatar avatar = Avatar.create(gameObjects(), avatarLayer, avatarPosition, inputListener, imageReader);
        // todo: find out what's this
        avatar.physics().preventIntersectionsFromDirection(Vector2.ZERO);
        // fix camera on avatar
        setCamera(new Camera(avatar, Vector2.ZERO,
                windowController.getWindowDimensions(), windowController.getWindowDimensions()));

    }

    public static void main(String[] args) {
        new PepseGameManager("Pepse").run();
    }

}

