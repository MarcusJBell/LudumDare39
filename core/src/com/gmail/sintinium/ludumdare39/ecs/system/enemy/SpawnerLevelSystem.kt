package com.gmail.sintinium.ludumdare39.ecs.system.enemy

import com.artemis.Aspect
import com.artemis.ComponentMapper
import com.artemis.systems.IteratingSystem
import com.gmail.sintinium.ludumdare39.ecs.component.LevelComponent
import com.gmail.sintinium.ludumdare39.ecs.component.SpawnerComponent
import com.gmail.sintinium.ludumdare39.util.level.SpawnerHelper

class SpawnerLevelSystem : IteratingSystem(Aspect.all(SpawnerComponent::class.java, LevelComponent::class.java)) {

    private lateinit var mSpawner: ComponentMapper<SpawnerComponent>
    private lateinit var mLevel: ComponentMapper<LevelComponent>

    override fun process(entityId: Int) {
        val cSpawner = mSpawner[entityId]
        val cLevel = mLevel[entityId]
        if (cSpawner.enemiesSpawned >= SpawnerHelper.getEnemiesToNextLevel(cLevel.level)) {
            cLevel.level++
            cSpawner.enemiesSpawned = 0
        }
    }
}