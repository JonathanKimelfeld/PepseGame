package world.daynight;

import danogl.GameObject;
import danogl.collisions.GameObjectCollection;
import danogl.components.CoordinateSpace;
import danogl.components.Transition;
import danogl.gui.rendering.OvalRenderable;
import danogl.util.Vector2;

import java.awt.*;

public class Sun {
    private static float SUN_SIZE_COEF = 6;

    public static GameObject create(GameObjectCollection gameObjects,
                                    int layer,
                                    Vector2 windowDimensions,
                                    float cycleLength) {
        OvalRenderable sunRenderable = new OvalRenderable(Color.YELLOW);
        GameObject sun = new GameObject(Vector2.ZERO, Vector2.ZERO, sunRenderable);
        sun.setCoordinateSpace(CoordinateSpace.CAMERA_COORDINATES);
        float sunSize = Math.min(windowDimensions.x(),windowDimensions.y())/SUN_SIZE_COEF;
        sun.setDimensions(new Vector2(sunSize, sunSize));
        gameObjects.addGameObject(sun, layer);
        sun.setTag("Sun");

        // Create a transition to move the sun in a circular path
        new Transition<Float>(
                sun, // the game object being changed
                angleInSky -> {
                    // Calculate the center of the sun as a function of the angle
                    float xRadius = (float) (windowDimensions.x() /1.8);
                    float yRadius = windowDimensions.y() /2;
                    float x = windowDimensions.x() / 2 + xRadius * (float) Math.cos(angleInSky);
                    float y = windowDimensions.y() / 2 + yRadius * (float) Math.sin(angleInSky)+ yRadius/3;
                    Vector2 center = new Vector2(x, y);
                    sun.setCenter(center);
                }, // the method to call`
                (float) (-0.5 * Math.PI), // initial transition value
                (float) (1.5 * Math.PI), // final transition value
                Transition.LINEAR_INTERPOLATOR_FLOAT, // use a linear interpolator
                cycleLength, // transition over the given cycle length
                Transition.TransitionType.TRANSITION_LOOP, // loop the transition
                null // nothing further to execute upon reaching final value
        );

        return sun;
    }
}
