package world.daynight;

import danogl.GameObject;
import danogl.collisions.GameObjectCollection;
import danogl.components.CoordinateSpace;
import danogl.gui.rendering.OvalRenderable;
import danogl.util.Vector2;

import java.awt.*;

public class SunHalo {

    private static float HALO_COEF =(float) 2.5;
    public static GameObject create(
            GameObjectCollection gameObjects,
            int layer,
            GameObject sun,
            Color color) {
        OvalRenderable haloRenderable = new OvalRenderable(color);
        GameObject halo = new GameObject(Vector2.ZERO, Vector2.ZERO, haloRenderable);
        halo.setCoordinateSpace(CoordinateSpace.CAMERA_COORDINATES);
        Vector2 haloSize = sun.getDimensions().mult(HALO_COEF);
        halo.setDimensions(haloSize);
        gameObjects.addGameObject(halo, layer);
        sun.setTag("Halo");
        halo.addComponent(deltaTime -> halo.setCenter(sun.getCenter()));
        return halo;
    }
}
