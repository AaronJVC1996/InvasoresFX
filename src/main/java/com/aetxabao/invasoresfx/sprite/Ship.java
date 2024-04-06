package com.aetxabao.invasoresfx.sprite;

import com.aetxabao.invasoresfx.util.Rect;
import javafx.scene.image.Image;

import static com.aetxabao.invasoresfx.game.AppConsts.*;

public class Ship extends ASprite {

    private Rect gameRect;
    private boolean isAlive;

    public Ship(Rect gameRect, Image img) {
        super(img, SHIP_ROWS, SHIP_COLS);
        this.gameRect = gameRect;
        xSpeed = SHIP_MAX_SPEED;
        ySpeed = SHIP_MAX_SPEED;
        setInitPos();
        isAlive = true;
    }

    public void setInitPos(){
        x = gameRect.centerX()-width/2;
        y = (int)(gameRect.bottom-1.2*height);
    }

    public boolean isAlive() {
        return isAlive;
    }

    public void setAlive(boolean alive) {
        isAlive = alive;
    }
    public void moveRight(){
        xSpeed = SHIP_MAX_SPEED;
        side = SHIP_RIGHT;
        updateFrame();
    }

    public void moveLeft(){
        xSpeed = -SHIP_MAX_SPEED;
        side = SHIP_LEFT;
        updateFrame();
    }

    public void moveUp(){
        ySpeed = -SHIP_MAX_SPEED;
        side = SHIP_UP;
        updateFrame();
    }

    public void moveDown(){
        ySpeed = SHIP_MAX_SPEED;
        side = SHIP_DOWN;
        updateFrame();
    }

    public void waiting(){      //moveNoWhere
        xSpeed = 0;
        ySpeed = 0;
        side = SHIP_UP;
        updateFrame();
    }

    public void updateFrame(){
        currentFrame = ++currentFrame%cols;
    }

    @Override
    public Rect[] getRects(){
        Rect[] rects;
        if (xSpeed==0){
            rects = new Rect[3];
            rects[0] = new Rect((int)(x+(0.47*width)),y,(int)(x+(0.53*width)),y+height);
            rects[1] = new Rect((int)(x+(0.37*width)),(int)(y+(0.22*height)),(int)(x+(0.63*width)),y+height);
            rects[2] = new Rect(x,(int)(y+(0.4*height)),x+width,(int)(y+(0.55*height)));
        }else{
            rects = new Rect[2];
            rects[0] = new Rect(x+width/3,y,x+2*width/3,y+height);
            rects[1] = new Rect((int)(x+(0.3*width)),(int)(y+(0.4*height)),(int)(x+(0.7*width)),y+height);
        }
        return rects;
    }

    @Override
    public void update() {
        if (x <= gameRect.right - width - xSpeed && x + xSpeed >= gameRect.left) {
            x = x + xSpeed;
        }
              // Este codigo es el de base y tiene un bug que no puedes bajar al llegar arriba del todo (gameRect.top)
              // el problema se basa en que se restaba y - yspeed y tiene que sumar para ver si sobrepasa el limite
              if (y < gameRect.bottom - height - ySpeed && y + ySpeed >= gameRect.top){
              y = y + ySpeed;
                  // Esta fue la primera solucion que encontre, pero leyendo y viendo que es cada cosa
                  // pude reducir el codigo y caer en que solo habia que cambiar un simbolo
                        //if (y - 1 > gameRect.top && y + ySpeed <= gameRect.bottom - height) {
                        //  y = y + ySpeed;
                        // } else if (y + ySpeed + height < gameRect.bottom && y + ySpeed >= gameRect.top) {
                        // y = y + ySpeed;
        }
    }

}
