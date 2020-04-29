package com.box2d.game.models;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.box2d.game.Box2dContactListener;
import com.box2d.game.controllers.KeyboardController;
import com.box2d.game.factories.BodyFactory;

import static com.box2d.game.factories.BodyFactory.*;

public class Box2dModel {
    public World world;

    private Body bodyd, bodys, bodyk;
    private Body player;
    private KeyboardController controller;

    public boolean isSwimming = false;

    public Box2dModel(KeyboardController cont){
        controller = cont;
        this.world = new World(new Vector2(0, -10f), true);
        world.setContactListener(new Box2dContactListener(this));
        createFloor();
//        createObject();
//        createMovingObject();

        BodyFactory bodyFactory = BodyFactory.getInstance(world);

        player = bodyFactory.makeBoxPolyBody(1, 1,
                2, 2, BodyFactory.RUBBER, BodyDef.BodyType.DynamicBody, false);

        Body water = bodyFactory.makeBoxPolyBody(1, -8,
                40, 4, RUBBER, BodyDef.BodyType.StaticBody);
        water.setUserData("IAMTHESEA");

        bodyFactory.makeAllFixturesSensors(water);
    }

    public void logicStep(float delta){
        if(isSwimming){
            player.applyForceToCenter(0, 50, true);
        }
        world.step(delta, 3, 3);
    }

    private void createObject(){

        // Dynamic Bodies - affected by gravity and other bodies.
        // used for player/enemies
        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.DynamicBody;
        bodyDef.position.set(0, 0);

        bodyd = world.createBody(bodyDef);

        PolygonShape shape = new PolygonShape();
        shape.setAsBox(1,1);

        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = shape;
        fixtureDef.density = 1f;

        //align our body with a physical object
        bodyd.createFixture(shape, 0.0f);

        shape.dispose();
    }

    private void createFloor(){
        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.StaticBody;
        bodyDef.position.set(0, -10);

        bodys = world.createBody(bodyDef);

        PolygonShape shape = new PolygonShape();
        shape.setAsBox(50, 1);

        bodys.createFixture(shape, 0.0f);
        shape.dispose();
    }

    private void createMovingObject(){
        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.KinematicBody;
        bodyDef.position.set(0, -12);

        bodyk = world.createBody(bodyDef);

        PolygonShape shape = new PolygonShape();
        shape.setAsBox(1,1);

        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = shape;
        fixtureDef.density = 1f;

        bodyk.createFixture(shape, 0.0f);

        shape.dispose();

        bodyk.setLinearVelocity(0, 0.75f);
    }
}
