/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package co.com.nebulae.course.world.labs;

import co.com.nebulae.course.world.*;
import co.com.nebulae.course.entity.Xform;
import javafx.scene.input.KeyEvent;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.Cylinder;
import javafx.scene.shape.Sphere;

/**
 *
 * @author Sebastian Molano - nebulae.com.co
 */
public class LaunchableBall implements WorldShape {

    private Xform world;
    final Xform elementsGroup = new Xform();

    //<editor-fold defaultstate="collapsed" desc="ARROW">
    private Xform arrowform;

    private Cylinder cylinder;

    private Double arrowRadius = 20d;

    private Double arrowHeight = 400d;

    private Double angleYArrow;

    private Double angleXArrow;

    //</editor-fold>
    //<editor-fold defaultstate="collapsed" desc="LAUNCHBALL">
    private Xform ballform;
    private Sphere oxygenSphere;
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="PROPERTIES">
    private Double directionX = 1d;
    private Double directionY = 1d;
    private Double directionZ = 1d;
    private Double gravity = -9.8 / 1000;
    private Double angle = 70d;
    private Double angleX = 60d;
    private Double angleY = 20d;
    private Double angleZ = 70d;
    private Double speed = 2d;
    private Double speedX = 0d;
    private Double speedY = 0d;
    private Double speedZ = 0d;
    private Double traveledDistanceX = 0d;
    private Double traveledDistanceY = 0d;
    private Double traveledDistanceZ = 0d;
    private Boolean stopY = false;
    private Boolean go = false;
    private Boolean finish = false;
    private Double radius = 25d;
    private Double frictionCoefficient = 0.005d;
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="GETTER AND SETTER">
    /**
     * @return the gravity
     */
    public Double getGravity() {
        return gravity;
    }

    /**
     * @param gravity the gravity to set
     */
    public void setGravity(Double gravity) {
        this.gravity = gravity;
    }

    /**
     * @return the speedX
     */
    public Double getSpeedX() {
        return speedX;
    }

    /**
     * @param speedX the speedX to set
     */
    public void setSpeedX(Double speedX) {
        this.speedX = speedX;
    }

    /**
     * @return the speedY
     */
    public Double getSpeedY() {
        return speedY;
    }

    /**
     * @param speedY the speedY to set
     */
    public void setSpeedY(Double speedY) {
        this.speedY = speedY;
    }

    public Xform getElementsGroup() {
        return elementsGroup;
    }

    public Xform getOxygenform() {
        return ballform;
    }

    public Sphere getOxygenSphere() {
        return oxygenSphere;
    }

//</editor-fold>
    public void buildElements(Xform world) {
        this.world = world;
        ballform = new Xform();
        Log.print("build ball ... ");

        final PhongMaterial greyMaterial = new PhongMaterial();
        greyMaterial.setDiffuseColor(Color.DARKGREY);
        greyMaterial.setSpecularColor(Color.GREY);

        arrowform = new Xform();
        cylinder = new Cylinder(arrowRadius, arrowHeight);
        //cylinder.heightProperty().
        cylinder.setMaterial(greyMaterial);
        arrowform.getChildren().add(cylinder);

        final PhongMaterial redMaterial = new PhongMaterial();
        redMaterial.setDiffuseColor(Color.DARKORANGE);
        redMaterial.setSpecularColor(Color.ORANGE);

        oxygenSphere = new Sphere(radius);
        oxygenSphere.setMaterial(redMaterial);
        ballform.getChildren().add(oxygenSphere);

        elementsGroup.getChildren().add(ballform);
        elementsGroup.getChildren().add(arrowform);

        ballform.t.setY(150);
        ballform.t.setX(-500);
        ballform.t.setZ(500);

        arrowform.t.setY(200);
        arrowform.t.setX(0);
        arrowform.t.setZ(0);
        arrowform.setTranslateX(arrowRadius / 2);
        arrowform.setTranslateY(arrowHeight / 2);
        arrowform.setTranslateZ(arrowRadius / 2);

        world.getChildren().addAll(elementsGroup);
    }

    public void go() {
        go = false;
        angle = 70d;
        angleX = 60d;
        angleY = 20d;
        angleZ = 70d;
        speed = 3d;
        speedX = speed * Math.cos(Math.toRadians(angleX));
        speedY = speed * Math.cos(Math.toRadians(angleY));
        speedZ = speed * Math.cos(Math.toRadians(angleZ));
        directionX = 1d;
        directionY = 1d;
        directionZ = 1d;
        traveledDistanceX = 0d;
        traveledDistanceY = 0d;
        traveledDistanceZ = 0d;
        ballform.t.setY(150);
        ballform.t.setX(-500);
        ballform.t.setZ(500);
        //go = true;
        finish = false;
    }

    public void move(Long time) {
        if (!go) {
            return;
        }

        if (ballform.t.getY() <= radius) {
            speedX = speedX * (1 - frictionCoefficient);
            speedZ = speedZ * (1 - frictionCoefficient);

            if (speedX <= 0.01) {
                speedX = 0d;
            }

            if (speedZ <= 0.01) {
                speedZ = 0d;
            }

            speedY += gravity * time;
            speedY = speedY * 0.80 * -1;

            stopY = speedY < 0.6;

        } else {
            speedY += gravity * time;
        }

        if (stopY) {
            speedY = 0d;
        }

        if (ballform.t.getX() <= -1000 || ballform.t.getX() >= 1000) {
            directionX = directionX * -1;
        }

        if (ballform.t.getZ() <= -1000 || ballform.t.getZ() >= 1000) {
            directionZ = directionZ * -1;
        }

        traveledDistanceY = (speedY * time) + (0.5 * gravity * Math.pow(time, 2));
        traveledDistanceX = (speedX * time) * directionX;
        traveledDistanceZ = (speedZ * time) * directionZ;
        double z = ballform.t.getZ() + traveledDistanceZ;
        double y = ballform.t.getY() + traveledDistanceY;
        double x = ballform.t.getX() + traveledDistanceX;

        if (y <= radius) {
            y = radius;
        }

        if (x <= -1000) {
            x = -1000;
        }

        if (x >= 1000) {
            x = 1000;
        }

        if (z <= -1000) {
            z = -1000;
        }

        if (z >= 1000) {
            z = 1000;
        }

        ballform.t.setZ(z);
        ballform.t.setY(y);
        ballform.t.setX(x);

        if (stopY && speedX == 0 && speedZ == 0) {
            world.getChildren().remove(elementsGroup);
            go = false;
            finish = true;
        }
    }

    @Override
    public boolean collide(WorldShape worldShape) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void redraw(Long time) {
        move(time);
    }

    @Override
    public void handleInput(KeyEvent keyEvent) {
        switch (keyEvent.getCode()) {
            case UP:
                arrowform.rx.setAngle(arrowform.rx.getAngle() + 1);
                break;
            case DOWN:
                arrowform.rx.setAngle(arrowform.rx.getAngle() - 1);
                break;
            case RIGHT:
                arrowform.ry.setAngle(arrowform.ry.getAngle() + 1);
                break;
            case LEFT:
                arrowform.ry.setAngle(arrowform.ry.getAngle() - 1);
                break;

        }
    }

    @Override
    public boolean isValid() {
        return !finish;
    }

}
