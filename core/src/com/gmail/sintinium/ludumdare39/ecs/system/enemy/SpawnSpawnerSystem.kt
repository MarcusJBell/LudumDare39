package com.gmail.sintinium.ludumdare39.ecs.system.enemy

import com.artemis.BaseSystem
import com.artemis.ComponentMapper
import com.badlogic.gdx.Gdx
import com.gmail.sintinium.ludumdare39.ecs.component.LevelComponent
import com.gmail.sintinium.ludumdare39.screen.gameScreen
import com.gmail.sintinium.ludumdare39.util.level.PlayerHelper

class SpawnSpawnerSystem : BaseSystem() {

    private var lastSpawnTime = 0f

    private lateinit var mLevel: ComponentMapper<LevelComponent>

    override fun processSystem() {
        if (gameScreen.playerEntity == -1) return
        val cLevel = mLevel[gameScreen.playerEntity]

        lastSpawnTime += Gdx.graphics.deltaTime
        if (lastSpawnTime > PlayerHelper.getTimeUntilNextSpawner(cLevel.level)) {
            lastSpawnTime = 0f
            gameScreen.entityFactory.createRandomSpawner()
        }
    }

}