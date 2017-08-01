package com.gmail.sintinium.ludumdare39.ecs.system.enemy

import com.artemis.Aspect
import com.artemis.ComponentMapper
import com.artemis.systems.IteratingSystem
import com.badlogic.gdx.Gdx
import com.gmail.sintinium.ludumdare39.ecs.component.LevelComponent
import com.gmail.sintinium.ludumdare39.ecs.component.PositionComponent
import com.gmail.sintinium.ludumdare39.ecs.component.SpawnerComponent
import com.gmail.sintinium.ludumdare39.screen.gameScreen
import com.gmail.sintinium.ludumdare39.util.level.SpawnerHelper

class SpawnerSpawnSystem : IteratingSystem(Aspect.all(SpawnerComponent::class.java, PositionComponent::class.java, LevelComponent::class.java)) {

    private lateinit var mPosition: ComponentMapper<PositionComponent>
    private lateinit var mSpawner: ComponentMapper<SpawnerComponent>
    private lateinit var mLevel: ComponentMapper<LevelComponent>

    override fun process(entityId: Int) {
        val cPosition = mPosition[entityId]
        val cSpawner = mSpawner[entityId]
        val cLevel = mLevel[entityId]
        cSpawner.timeSinceLastSpawn += Gdx.graphics.deltaTime
        if (cSpawner.timeSinceLastSpawn > SpawnerHelper.getCooldown(cLevel.level)) {
            gameScreen.entityFactory.createZombie(cLevel.level, cPosition.position)
            cSpawner.timeSinceLastSpawn = 0f
            cSpawner.enemiesSpawned++
        }
    }

}