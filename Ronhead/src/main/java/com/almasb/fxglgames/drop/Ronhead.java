/**
 * Imports
 */
package com.almasb.fxglgames.drop;
import com.almasb.fxgl.app.GameApplication;
import com.almasb.fxgl.app.GameSettings;
import com.almasb.fxgl.core.math.FXGLMath;
import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.entity.components.CollidableComponent;
import javafx.scene.input.KeyCode;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.util.Duration;
import java.util.Map;

import static com.almasb.fxgl.dsl.FXGL.*;


/**
 * My class Ronhead which extends Almas Class GameApplication ( to implement his methods )
 */

public class Ronhead extends GameApplication {

    /**
     * Types of entities in this game.
     */
    public enum EntityType {
        RONHEAD, BALL , FIELD
    }

    /**
     * Here i define my entities
     */
    private Entity ronhead;
    private Entity background;


    /**
     * Here i use the method initSettings to define the gamescene, with the mentioned values
     * @param settings
     */
    @Override
    protected void initSettings(GameSettings settings) {
        settings.setTitle("Ronhead");
        settings.setVersion("1.0");
        settings.setWidth(480);
        settings.setHeight(800);
        settings.setMenuEnabled(true);
    }

    /**
     * Here is another method called initInput which allows me to move my entity with a chosen key, here i choose the key
     * LEFT(Arrow) which will move the entity in X-direction -200 pixels, which is to the left on the screen
     * and RIGHT (Arrow) which will move the entity in X-direction 200 pixels, which is to the right on the screen
     */
    @Override
    protected void initInput() {
        onKey(KeyCode.LEFT, "Move Left", () -> ronhead.translateX(-200 * tpf()));
        onKey(KeyCode.RIGHT, "Move Right", () -> ronhead.translateX(200 * tpf()));
    }

    /**
     * Here i call the methods which are defined longer down in the code, so it spawns my background, and the player, and the spawnBall method each 1 seconds.
     * It also loops the background music that i put in my assets
     */
    @Override
    protected void initGame() {
        background = backGround();
        ronhead = spawnRonhead();

        run(() -> spawnBall(), Duration.seconds(1));

        loopBGM("bgm.wav");
    }

    /**
     * The physics method , which says that when the 2 entity types (ronhead, and ball) is colliding, the ball must be removed from the world.
     * Also on the collision, the asset mentioned will be played.
     */
    @Override
    protected void initPhysics() {
        onCollisionBegin(EntityType.RONHEAD, EntityType.BALL, (ronhead, ball) -> {
            ball.removeFromWorld();
            play("siii.wav");


        });
    }

    /**
     *This method onUpdate, makes each ball drop (direction Y) 200 * tpf (time per frame)
     * @param tpf
     */
    @Override
    protected void onUpdate(double tpf) {
        getGameWorld().getEntitiesByType(EntityType.BALL).forEach(ball -> ball.translateY(200 * tpf));
    }

    /**
     * the entity which is a method, with the defined values, that returns the method entityBuilder
     * So this is what happens when i call the method up in the initGame() spawnRonhead() it will be builded to the gamescene.
     * For example this is of the EntityType RONHEAD, it will spawn at the middle of the screen (app-width / 2)
     * and in y position appheight -200 pixels.
     * the .viewWithBox reads the asset "Ronhead.png", and it is a collidableComponent which makes it collide with the footballs, and the last
     * builds it and attaches it to the gameworld.
     * @return entityBuilder()
     */
    private Entity spawnRonhead() {
        return entityBuilder()
                .type(EntityType.RONHEAD)
                .at(getAppWidth() / 2, getAppHeight() - 200)
                .viewWithBBox("Ronhead.png")
                .with(new CollidableComponent(true))
                .buildAndAttach();
    }

    /**
     * A method like before to create objects / this is the ball (it spawns at random position with the FXGLMath.random but always in the top with y = 0
     * @return entityBuilder()
     */
    private Entity spawnBall() {
        return entityBuilder()
                .type(EntityType.BALL)
                .at(FXGLMath.random(getAppWidth() - 64), 0)
                .viewWithBBox("ball.png")
                .with(new CollidableComponent(true))
                .buildAndAttach();

    }
    /**
     * A method like before to create objects / here is my background
     * @return entityBuilder()
     */
    private Entity backGround() {
        return entityBuilder()
                .type(EntityType.FIELD)
                .at(0,0)
                .viewWithBBox("Field.png")
                .buildAndAttach();
    }

    /**
     * The next to methods are an attempt to make track of scores, but can't make it work.
     * @param vars
     */
    @Override
    protected void initGameVars(Map<String, Object> vars) {
        vars.put("score", 0);

    }

    @Override
    protected void initUI() {
        Text textScore = getUIFactory().newText("", Color.BLACK, 22);

        textScore.setTranslateX(10);
        textScore.setTranslateY(50);

        textScore.textProperty().bind(getGameState().intProperty("score").asString());

        getGameScene().addUINodes(textScore);

    }

    /**
     * This method launchs the game
     * @param args
     */
    public static void main(String[] args) {
        launch(args);
    }
}
