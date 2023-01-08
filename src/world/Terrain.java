package world;

import danogl.collisions.GameObjectCollection;
import danogl.collisions.Layer;
import danogl.components.GameObjectPhysics;
import danogl.gui.rendering.RectangleRenderable;
import danogl.util.Vector2;
import pepse.util.ColorSupplier;

import java.awt.*;
import java.util.Random;

public class Terrain {
    private final GameObjectCollection gameObjects;
    private final int groundLayer;
    private final int groundHeightAtX0;
    private static final Color BASE_GROUND_COLOR = new Color(212, 123, 74);
    private final Vector2 windowDimensions;

    private Random rand;

    /**
     * The constructor for the terrain class.
     *
     * @param gameObjects      game objects collection
     * @param groundLayer      the layer the terrain applies to.
     * @param windowDimensions the dimensions of the game window.
     * @param seed             used in random module to instantiate constant randomness.
     */
    public Terrain(GameObjectCollection gameObjects,
                   int groundLayer,
                   Vector2 windowDimensions,
                   int seed) {
        this.gameObjects = gameObjects;
        this.groundLayer = groundLayer;
        this.groundHeightAtX0 = 0;
        this.windowDimensions = windowDimensions;
        this.rand = new Random(seed);
    }

    /**
     * This function gets a number and returns its fraction,
     * both for positive and negative.
     *
     * @param input the number to return the fraction from
     * @return the fraction of the number.
     */
    double fraction(float input) {
        if (input < 0) {
            return input - Math.ceil(input);
        } else {
            return input - Math.floor(input);
        }
    }

    /**
     * The perlin noise function, used for the terrain
     * of the game.
     *
     * @param x coordinate of the game window.
     * @return the perlin noise for the current coordinates.
     */
    float perlinNoise(int x) {
        float new_x = x/ (windowDimensions.y());
        float perlinFraction =
                (float)(
                         0.35 *Math.sin(2.75 *(new_x) + 1) -
                        0.2 *Math.sin(6.23 *(new_x + 5))+
                        0.15 *Math.cos(15 *(new_x+4.63)) )*
                        windowDimensions.y()/3 + windowDimensions.y()/3*2;
        return (perlinFraction);
    }

    /**
     * A method that receives a coordinate X,
     * and returns the matching Y for the ground height.
     *
     * @param x the input in x-axis of window.
     * @return matching coordinate Y for the ground height.
     */
    public float groundHeightAt(float x) {
        return perlinNoise((int) x);
    }

    /**
     * A method used to create the terrain blocks in a given range
     * of X values, corresponding the output of the groundHightAt function.
     *
     * @param minX the X range minimum
     * @param maxX the X range maximum
     */
    public void createInRange(int minX, int maxX) {
        var blockImage = new RectangleRenderable(ColorSupplier.approximateColor(BASE_GROUND_COLOR));
        for (int nextBlockX = minX; nextBlockX < maxX; nextBlockX += Block.SIZE) {
            int nextBlockY = (int) windowDimensions.y() - Block.SIZE;
            int maxY = (int) groundHeightAt(nextBlockX);
            // loop through the blocks in the column from the top to the ground height
            for (int i = nextBlockY; i >= maxY; i -= Block.SIZE) {
                // Create a new block image on each iteration of the outer loop
                Vector2 placeBlockAt = new Vector2(nextBlockX, i);
                Block curBlock = new Block(placeBlockAt, blockImage);
                gameObjects.addGameObject(curBlock, groundLayer);
                curBlock.setTag("ground");
            }
        }
    }
}
