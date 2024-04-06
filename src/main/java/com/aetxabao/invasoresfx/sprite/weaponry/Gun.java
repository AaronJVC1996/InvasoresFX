package com.aetxabao.invasoresfx.sprite.weaponry;

import com.aetxabao.invasoresfx.sprite.ASprite;

import java.util.ArrayList;

import static com.aetxabao.invasoresfx.game.AppConsts.BALL_SPRITE_IMAGE;

public class Gun extends AWeapon {
    // Esta es la arma equipada y
    @Override
    public ArrayList<AShot> shoot(ASprite sprite) {
        // Aqui llamamos al metodo shoot y es un arraylist el cual requiere de parametros una imagen y dos posiciones,
        // el cual es nuestra localizacion, desde ahi saldra el disparo
        ArrayList<AShot> list = new ArrayList<>();
        // imagen de la bala enemiga
        AShot shot = new Cannonball(BALL_SPRITE_IMAGE);
        // posiciones
        shot.setPos(sprite.getRect().centerX(), sprite.getRect().bottom);
        // Agrega el disparo con sus parametros de imagen y posicion X , Y
        list.add(shot);
        return list;
    }

}
