package world.trees;

import danogl.GameObject;
import danogl.collisions.GameObjectCollection;
import danogl.gui.rendering.RectangleRenderable;
import danogl.util.Vector2;
import pepse.util.ColorSupplier;
import world.Block;
import world.Terrain;
import world.Block;
import world.Terrain;

import java.awt.*;
import java.util.Random;


public class Tree {
    private GameObjectCollection gameObjects;
    private int groundLayer;
    private Terrain terrain;
    private int seed;
    private Vector2 windowDimensions;
    private Random random;
    public Tree(GameObjectCollection gameObjects,
                int groundLayer,
                Terrain terrain,
                int seed,
                Vector2 windowDimensions){
        this.gameObjects = gameObjects;
        this.groundLayer = groundLayer;
        this.terrain = terrain;
        this.seed = seed;
        this.windowDimensions = windowDimensions;
        this.random = new Random(seed);
    }

    public void createInRange(int minX, int maxX) {
        var treeRenderable = new RectangleRenderable(new Color(100, 50, 20));
        //var leafBlock = new RectangleRenderable(new Color(50, 200, 30));

        int nextBlockX = minX;
        while (nextBlockX < maxX) {
            // prob. of choosing where to place the tree at
            if (random.nextFloat()> 0.1) {
                nextBlockX += Block.SIZE;
                continue;
            }
            int nextBlockY = (int) terrain.groundHeightAt(nextBlockX); // the root
            int treeTop = random.nextInt((int) windowDimensions.y() - nextBlockY);
            while (nextBlockY >= treeTop) {
                Vector2 placeBlockAt = new Vector2(nextBlockX, nextBlockY);
                Block curBlock = new Block(placeBlockAt, treeRenderable);
                gameObjects.addGameObject(curBlock);
                nextBlockY -= Block.SIZE;
                curBlock.setTag("tree");
            }
            nextBlockX += Block.SIZE;
        }
    }
}
