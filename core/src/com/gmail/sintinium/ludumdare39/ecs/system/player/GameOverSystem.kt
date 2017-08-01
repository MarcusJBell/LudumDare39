package com.gmail.sintinium.ludumdare39.ecs.system.player

import com.artemis.BaseSystem
import com.artemis.ComponentMapper
import com.gmail.sintinium.ludumdare39.ecs.component.LevelComponent
import com.gmail.sintinium.ludumdare39.ecs.component.ManaComponent
import com.gmail.sintinium.ludumdare39.game
import com.gmail.sintinium.ludumdare39.screen.GameoverScreen
import com.gmail.sintinium.ludumdare39.screen.gameScreen

class GameOverSystem : BaseSystem() {

    private lateinit var mMana: ComponentMapper<ManaComponent>
    private lateinit var mLevel: ComponentMapper<LevelComponent>

    override fun processSystem() {
        if (gameScreen.playerEntity == -1) return
        val cMana = mMana[gameScreen.playerEntity]
        if (cMana.currentMana <= 0) {
            game.screen = GameoverScreen(gameScreen, mLevel[gameScreen.playerEntity].level)
            gameScreen.playerEntity = -1
        }
    }

}