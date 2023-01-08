package world;


import danogl.GameObject;
import danogl.collisions.GameObjectCollection;
import danogl.components.ScheduledTask;
import danogl.gui.ImageReader;
import danogl.gui.UserInputListener;
import danogl.gui.rendering.OvalRenderable;
import danogl.util.Vector2;

import java.awt.*;
import java.awt.event.KeyEvent;

public class Avatar extends GameObject {
    private static final float VELOCITY_X = 300;
    private static final float VELOCITY_Y = -300;
    private static final float GRAVITY = 500;
    private static final Color AVATAR_COLOR = Color.DARK_GRAY;

    private UserInputListener inputListener;

    public Avatar(Vector2 pos, UserInputListener inputListener) {
        super(pos, Vector2.ONES.mult(50), new OvalRenderable(AVATAR_COLOR));
        transform().setAccelerationY(GRAVITY);
        this.inputListener = inputListener;
    }

    public static Avatar create(GameObjectCollection gameObjects,
                         int avatarLayer, Vector2 topLeftCorner,
                         UserInputListener inputListener,
                         ImageReader imageReader) {
        Avatar avatar = new Avatar(topLeftCorner, inputListener);
        gameObjects.addGameObject(avatar, avatarLayer);
        avatar.setTag("avatar");
        return avatar;
    }

    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);
        float xVel = 0;
        if (inputListener.isKeyPressed(KeyEvent.VK_LEFT))
            xVel -= VELOCITY_X;
        if (inputListener.isKeyPressed(KeyEvent.VK_RIGHT))
            xVel += VELOCITY_X;
        transform().setVelocityX(xVel);
        if (inputListener.isKeyPressed(KeyEvent.VK_SPACE) &&
                inputListener.isKeyPressed(KeyEvent.VK_DOWN)) {
            physics().preventIntersectionsFromDirection(null);
            new ScheduledTask(this, .5f, false,
                    () -> physics().preventIntersectionsFromDirection(Vector2.ZERO));
            return;
        }
        if (inputListener.isKeyPressed(KeyEvent.VK_SPACE) && getVelocity().y() == 0)
            transform().setVelocityY(VELOCITY_Y);
    }

}
