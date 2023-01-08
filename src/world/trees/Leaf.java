package world.trees;

import danogl.GameObject;
import danogl.collisions.Collision;
import danogl.collisions.GameObjectCollection;
import danogl.components.ScheduledTask;
import danogl.components.Transition;
import danogl.gui.rendering.Renderable;
import danogl.util.Vector2;
import world.Block;

import java.util.Random;

public class Leaf extends GameObject {
    private final int FADEOUT_TIME = 10;
    private final int FALLING_SPEED = 60;
    private final Vector2 leafCoords;
    private final Renderable leafRenderable;
    private Transition horizontalTransition;
    private GameObjectCollection gameObjects;
    private int leafLayer;
    private Vector2 dimensions;


    public Leaf(GameObjectCollection gameObjects,
                Vector2 leafCoords,
                int leafLayer, Vector2 dimensions,
                Renderable leafRenderable) {
        super(leafCoords, dimensions, leafRenderable);
        this.gameObjects = gameObjects;
        this.leafLayer = leafLayer;
        this.dimensions = dimensions;
        this.leafCoords = leafCoords;
        this.leafRenderable = leafRenderable;
        this.horizontalTransition = null;
        scheduleLeafToMove();
        scheduleLeafToFall();

    }

    @Override
    public void onCollisionEnter(GameObject other, Collision collision) { // todo: leaves are not exactly
                                                                        // todo: staying on the ground
        super.onCollisionEnter(other, collision);
        transform().setVelocity(Vector2.ZERO);
        this.removeComponent(horizontalTransition);
    }


    private void scheduleLeafToMove() {
        Random random = new Random();
        new ScheduledTask(
                this,
                random.nextInt(5),
                false,
                this::leafMovingAttributes);
    }

    private void scheduleLeafToFall() {
        Random random = new Random();
        new ScheduledTask(
                this,
                random.nextInt(10),
                true,
                this::implementFallingLeaf);
    }

    private void leafMovingAttributes() {

        new Transition<Float>( // change angles
                this,
                this.renderer()::setRenderableAngle,
                -15f,
                15f,
                Transition.CUBIC_INTERPOLATOR_FLOAT,
                5f,
                Transition.TransitionType.TRANSITION_BACK_AND_FORTH,
                null);

        new Transition<Vector2>( // dimension change
                this,
                this::setDimensions,
                Vector2.ONES.mult(Block.SIZE * 0.9f),
                Vector2.ONES.mult(Block.SIZE * 1.1f),
                Transition.CUBIC_INTERPOLATOR_VECTOR,
                5f,
                Transition.TransitionType.TRANSITION_BACK_AND_FORTH,
                null);
    }

    private void implementFallingLeaf() {
        Random random = new Random();
        if (random.nextFloat() > 0.1) {
            return;
        }
        gameObjects.removeGameObject(this);
        gameObjects.addGameObject(this, leafLayer);
        this.transform().setVelocityY(FALLING_SPEED);
        this.renderer().fadeOut(FADEOUT_TIME, this::respawnLeafBack);
        // falling sideways attribution:
        this.horizontalTransition = new Transition<Float>(
                this,
                this.transform()::setVelocityX,
                -30f,
                30f,
                Transition.LINEAR_INTERPOLATOR_FLOAT,
                1f,
                Transition.TransitionType.TRANSITION_BACK_AND_FORTH,
                null);

    }

    private void respawnLeafBack() {
        Random random = new Random();
        new ScheduledTask(
                this,
                random.nextInt(5) + 5,
                false, () -> { //Leaves appearing again on the tree
            this.renderer().fadeIn(1);
            this.setTopLeftCorner(leafCoords);
            this.transform().setVelocity(Vector2.ZERO);
        });
    }

}
