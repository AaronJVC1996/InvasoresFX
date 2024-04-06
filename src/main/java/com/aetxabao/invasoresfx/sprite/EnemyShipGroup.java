package com.aetxabao.invasoresfx.sprite;

import com.aetxabao.invasoresfx.util.Rect;
import javafx.scene.canvas.GraphicsContext;
import org.apache.log4j.Logger;

import java.util.List;

/**
 * Los enemigos se desplazan como un conjunto
 */
public class EnemyShipGroup extends EnemyShip {

    List<EnemyShip> group;
    public EnemyShipGroup(Rect gameRect, List<EnemyShip> enemies) {
        super(gameRect, null, 1);
        group = enemies;
        setXY();
        setWidth();
        setHeight();
    }

    public void setXY(){
        x = gameRect.width();
        y = gameRect.height();
        for(EnemyShip e : group){
            if (e.x < x) x = e.x;
            if (e.y < y) y = e.y;
        }
    }

    public void setWidth(){
        int x1 = gameRect.width();
        int x2 = 0;
        for(EnemyShip e : group){
            if (e.x < x1) x1 = e.x;
            if (e.x + e.width > x2) x2 = e.x + e.width;
        }
        this.width = x2 - x1;
    }

    public void setHeight(){
        int y1 = gameRect.height();
        int y2 = 0;
        for(EnemyShip e : group){
            if (e.y < y1) y1 = e.y;
            if (e.y + e.height > y2) y2 = e.y + e.height;
        }
        this.height = y2 - y1;
    }

    public List<EnemyShip> getEnemyList(){
        return group;
    }

    @Override
    public Rect[] getRects(){
        int i = 0;
        Rect[] rects = new Rect[group.size()];
        for(EnemyShip e : group){
            rects[i++] = e.getRect();
        }
        return rects;
    }

    @Override
    public void updateFrame(){
        for(EnemyShip e : group){
            e.updateFrame();
        }
    }

    @Override
    public void update() {
        setWidth();
        setHeight();
        if (x > gameRect.right - width - xSpeed || x + xSpeed < gameRect.left) {
            xSpeed = -xSpeed;
            y = y + group.get(0).height;
            for(EnemyShip e : group){
                e.y = e.y + e.height;
            }
        }
        x = x + xSpeed;
        for(EnemyShip e : group){
            e.x = e.x + xSpeed;
        }
        updateFrame();
    }

    @Override
    public void draw(GraphicsContext gc) {
        for(EnemyShip e : group){
            e.draw(gc);
        }
    }

}
