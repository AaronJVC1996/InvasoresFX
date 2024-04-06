package com.aetxabao.invasoresfx.game;

import com.aetxabao.invasoresfx.sprite.*;
import com.aetxabao.invasoresfx.sprite.AEnemy;
import com.aetxabao.invasoresfx.sprite.EnemyShip;
import com.aetxabao.invasoresfx.sprite.EnemyShipGroup;
import com.aetxabao.invasoresfx.sprite.IHaveShield;
import com.aetxabao.invasoresfx.sprite.weaponry.AShot;
import com.aetxabao.invasoresfx.sprite.weaponry.Laserbeam;
import com.aetxabao.invasoresfx.util.Rect;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import static com.aetxabao.invasoresfx.game.AppConsts.*;
import static com.aetxabao.invasoresfx.game.enums.EAppStatus.*;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

public class GameManager {
    // Este es la clase de mi nave y sus cosas.

    // region attributes
    private Rect gameRect;

    private Ship ship;
    protected LifesSprite lifesSprite;
    protected List<AShot> shotsUp = new ArrayList<>();
    protected List<AShot> shotsDown = new ArrayList<>();
    protected List<AEnemy> enemies = new ArrayList<>();
    protected List<SpriteTemp> temps = new ArrayList<>();

    public int score = 0;
    private AppStatus appStatus;

    private static Logger log = Logger.getLogger(GameManager.class);

    // endregion

    public GameManager(Rect gameRect, AppStatus appStatus) {
        PropertyConfigurator.configure(LOG4J_PROPERTIES);

        this.gameRect = gameRect;
        this.appStatus = appStatus;
        ship = new Ship(gameRect, SHIP_SPRITE_IMAGE);
        lifesSprite = new LifesSprite(gameRect, LIFES_SPRITE_IMAGE, appStatus);
    }

    public void start(){
        appStatus.start();
        log.debug("start level=" + appStatus.getLevel());
        score = 0;
        clearLevel();
        ship.setAlive(true);
        lifesSprite.update();
        enemies = EnemySpawner.createEnemies(gameRect, appStatus.getLevel());
    }

    public void nextLevel(){
        appStatus.nextLevel();
        log.debug("nextLevel level=" + appStatus.getLevel());
        clearLevel();
        ship.setAlive(true);
        enemies = EnemySpawner.createEnemies(gameRect, appStatus.getLevel());
    }

    public void sameLevel(){
        appStatus.sameLevel();
        log.debug("sameLevel level=" + appStatus.getLevel());
        clearLevel();
        lifesSprite.update();
        ship.setAlive(true);
    }

    private void clearLevel(){
        temps.clear();
        shotsUp.clear();
        shotsDown.clear();
        ship.setInitPos();
    }

    public void finish(){
        appStatus.finish();
    }

    public int getWidth() {
        return gameRect.width();
    }

    public double getHeight() {
        return gameRect.height();
    }

    public int getLevel() {
        return appStatus.getLevel();
    }

    public Ship getShip(){
        return ship;
    }

    public void shot(){
        // Disparo de mi nave
        AShot shot = new Laserbeam(LASER_SPRITE_IMAGE);
        shot.setPos(ship.getRect().centerX(), ship.getRect().top - shot.getRect().height());
        shotsUp.add(shot);
    }

    public void updateGame(){

        //Detección de colisión entre balas
         for (Iterator<AShot> itShotUp = shotsUp.iterator(); itShotUp.hasNext(); ) {
            AShot AShotUp = itShotUp.next();
            for (Iterator<AShot> itShotDown = shotsDown.iterator(); itShotDown.hasNext(); ) {
                AShot AShotDown = itShotDown.next();
                if (AShotUp.collides(AShotDown)) {
                    itShotUp.remove();
                    itShotDown.remove();
                    temps.add(new SpriteTemp(temps, AShotDown.getRect().centerX(), AShotDown.getRect().centerY(),
                            EXPLOSION_12_SPRITE_IMAGE, 12));
                    break;
                }
            }
        }

        //Detección de colisiones de los enemigos con los disparos del protagonista
        for (Iterator<AShot> itBullet = shotsUp.iterator(); itBullet.hasNext(); ) {
            AShot AShot = itBullet.next();
            for (Iterator<AEnemy> itSprite = enemies.iterator(); itSprite.hasNext(); ) {
                ASprite sprite = itSprite.next();
                if (AShot.collides(sprite)){
                    if (sprite instanceof EnemyShipGroup){
                        List<EnemyShip> enemyShipList = ((EnemyShipGroup) sprite).getEnemyList();
                        for (Iterator<EnemyShip> itEnemy = enemyShipList.iterator(); itEnemy.hasNext(); ) {
                            EnemyShip enemyShip = itEnemy.next();
                            if (AShot.collides(enemyShip)){
                                temps.add(new SpriteTemp(temps, enemyShip.getRect().centerX(), enemyShip.getRect().centerY(),
                                                         EXPLOSION_9_SPRITE_IMAGE, 9));
                                itEnemy.remove();
                                if (enemyShipList.size()==0){
                                    itSprite.remove();
                                }else{
                                    ((EnemyShipGroup) sprite).setXY();
                                    ((EnemyShipGroup) sprite).setWidth();
                                    ((EnemyShipGroup) sprite).setHeight();
                                }
                                break;
                            }
                        }
                    }else if (sprite instanceof IHaveShield){
                        temps.add(new SpriteTemp(temps, sprite.getRect().centerX(), sprite.getRect().centerY(),
                                                 EXPLOSION_9_SPRITE_IMAGE, 9));
//                        if (((EnemyBarrier) sprite).impact()){
//                            itSprite.remove();
//                        }
                    }else{
                        temps.add(new SpriteTemp(temps, sprite.getRect().centerX(), sprite.getRect().centerY(),
                                                 EXPLOSION_9_SPRITE_IMAGE, 9));
                        itSprite.remove();
                    }
                    score += AppConsts.PTS_ENEMYSHIP;
                    itBullet.remove();
                    break;
                }
            }
            if (gameRect.contains(AShot.getRect())){
                AShot.update();
            }else{
                itBullet.remove();
            }
        }

        //Actualización del protagonista
        if (ship.isAlive()) {
            ship.update();
        }
        //Actualización de los sprites temporales
        for (int i=temps.size()-1;i>=0;i--) {
            temps.get(i).update();
        }
        //Actualización de los enemigos
        for (ASprite enemy : enemies) {
            enemy.update();
        }
        //Generación de nuevos enemigos
        List<AEnemy> newList = new ArrayList<>();
        for (ASprite enemy : enemies) {
            if (enemy instanceof ICanSpawn){
                newList.addAll(
                    ((ICanSpawn) enemy).spawn()
                );
            }
        }
        enemies.addAll(newList);
        //Comprobación del estado de la partida
        if (!ship.isAlive()&&(temps.size()==0)){
            appStatus.oneLessLife();
            if (appStatus.getValue() == E_APP_LOST){
                log.debug("updateGame E_APP_LOST enemies=" + enemies.size() + " shotsDown=" + shotsDown.size() + " shotsUp=" + shotsUp.size());
            }else{
                log.debug("updateGame E_APP_ONELESSLIFE enemies=" + enemies.size() + " shotsDown=" + shotsDown.size() + " shotsUp=" + shotsUp.size());
            }
            return;
        }
        //Comprobación del estado de la partida
        if ((enemies.size()==0)&&(temps.size()==0)){
            appStatus.newLevel();
            score += PTS_NEWLEVEL;
            return;
        }
        //Detección si los disparos de los enemigos han impactado con el protagonista
        //o se han salido del área se juego
        for (Iterator<AShot> itBullet = shotsDown.iterator(); itBullet.hasNext(); ) {
            AShot AShot = itBullet.next();
            if (ship.isAlive() && AShot.collides(ship)) {
                ship.setAlive(false);
                temps.add(new SpriteTemp(temps, ship.getRect().centerX(), ship.getRect().centerY(),
                        EXPLOSION_12_SPRITE_IMAGE, 12));
            }else {
                if (gameRect.contains(AShot.getRect())) {
                    AShot.update();
                } else {
                    itBullet.remove();
                }
            }
        }
        //Detección si los enemigos han impactado con el protagonista
        //o se han salido del área se juego
        for (Iterator<AEnemy> itSprite = enemies.iterator(); itSprite.hasNext(); ) {
            ASprite sprite = itSprite.next();
            if(ship.isAlive() && sprite.collides(ship)){
                ship.setAlive(false);
                temps.add(new SpriteTemp(temps, ship.getRect().centerX(), ship.getRect().centerY(),
                        EXPLOSION_12_SPRITE_IMAGE, 12));
            }
            if (gameRect.bottom < sprite.getRect().top - sprite.getRect().height() ) {
                itSprite.remove();
            }
        }

        //Generación de disparos por parte de los enemigos
        int n = shotsUp.size() - shotsDown.size();
        ArrayList<AEnemy> shooterEnemies = new ArrayList<>();
        for (AEnemy enemy : enemies) {
            if (enemy.hasWeapon()) {
                shooterEnemies.add(enemy);
            }
        }
        if (shooterEnemies.size()>0){
            for (int i = 0; i < n; i++) {
                int j = (int)(Math.random()*shooterEnemies.size());
                ArrayList<AShot> shots = shooterEnemies.get(j).shoot();
                shotsDown.addAll(shots);
            }
        }
    }

}
