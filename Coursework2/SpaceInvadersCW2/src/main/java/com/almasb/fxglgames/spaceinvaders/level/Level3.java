package com.almasb.fxglgames.spaceinvaders.level;

import com.almasb.fxgl.core.math.FXGLMath;
import com.almasb.fxgl.entity.GameEntity;
import com.almasb.fxgl.entity.control.RandomMoveControl;
import com.almasb.fxglgames.spaceinvaders.Config;
import javafx.geometry.Rectangle2D;

import static com.almasb.fxgl.app.DSLKt.geti;
import static com.almasb.fxglgames.spaceinvaders.Config.ENEMIES_PER_ROW;
import static com.almasb.fxglgames.spaceinvaders.Config.ENEMY_ROWS;


public class Level3 extends SpaceLevel {

    @Override
    public void init() {
        for (int y = 0; y < 1; y++) {                                       // only one row
            for (int x = 0; x < ENEMIES_PER_ROW * 4; x++) {                 // the number of enemies in one row
                GameEntity enemy = spawnEnemy(x*40, 800*2/3);               // the position of enemies    
            }
        }
    }

    @Override
    public void destroy() {

    }
}
