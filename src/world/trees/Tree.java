package world.trees;

import danogl.collisions.GameObjectCollection;
import danogl.gui.rendering.RectangleRenderable;
import danogl.util.Vector2;
import world.Block;
import world.Terrain;

import java.awt.*;
import java.util.Random;


public class Tree {
    private final int foliageRadius;
    private GameObjectCollection gameObjects;
    private int treeLayer;
    private Terrain terrain;
    private int seed;
    private Vector2 windowDimensions;
    private Random random;
    public Tree(GameObjectCollection gameObjects,
                int treeLayer,
                Terrain terrain,
                int seed,
                Vector2 windowDimensions){
        this.gameObjects = gameObjects;
        this.treeLayer = treeLayer;
        this.terrain = terrain;
        this.seed = seed;
        this.windowDimensions = windowDimensions;
        this.random = new Random(seed);
        this.foliageRadius = 50;
    }

    public void createLeaves(Vector2 treeTopCoords) {
        var leafRenderable = new RectangleRenderable(new Color(50, 200, 30));
        for (int nextLeafX = (int) (treeTopCoords.x() - foliageRadius);
                        nextLeafX < (int) treeTopCoords.x() + foliageRadius; nextLeafX += Block.SIZE) {
            for (int nextLeafY = (int) treeTopCoords.y() - foliageRadius;
                        nextLeafY < (int) treeTopCoords.y() + foliageRadius; nextLeafY += Block.SIZE) {
                Vector2 leafCoords = new Vector2(nextLeafX, nextLeafY);
                Leaf curLeaf = new Leaf(gameObjects, leafCoords, treeLayer-2,
                               new Vector2(Block.SIZE, Block.SIZE), leafRenderable);
                gameObjects.addGameObject(curLeaf, treeLayer-2);
                curLeaf.setTag("leaf");
            }
        }
    }

    public void createInRange(int minX, int maxX) {
        var treeRenderable = new RectangleRenderable(new Color(100, 50, 20));
        for (int nextBlockX = minX; nextBlockX < maxX; nextBlockX += Block.SIZE) {
            // prob. of choosing where to place the tree at
            if (random.nextFloat()> 0.1) {
                continue;
            }
            int nextBlockY = (int) terrain.groundHeightAt(nextBlockX);
            int treeTop = random.nextInt((int) windowDimensions.y() - nextBlockY);
            Vector2 treeTopCoords = new Vector2(nextBlockX, treeTop);
            createLeaves(treeTopCoords);
            for (nextBlockY = (int) terrain.groundHeightAt(nextBlockX);
                            nextBlockY >= treeTop; nextBlockY -= Block.SIZE) {
                Block curBlock = new Block(new Vector2(nextBlockX, nextBlockY), treeRenderable);
                gameObjects.addGameObject(curBlock);
                curBlock.setTag("tree");
            }
        }
    }
}
