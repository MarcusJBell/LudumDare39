package com.gmail.sintinium.ludumdare39.ecs.system

import com.artemis.Aspect
import com.artemis.ComponentMapper
import com.artemis.systems.IteratingSystem
import com.gmail.sintinium.ludumdare39.ecs.component.*
import com.gmail.sintinium.ludumdare39.screen.gameScreen
import com.gmail.sintinium.ludumdare39.util.level.EnemyHelper
import com.gmail.sintinium.ludumdare39.util.level.SpawnerHelper

class EnemyDeathSystem : IteratingSystem(Aspect.all(EnemyComponent::class.java, HealthComponent::class.java)) {

    private lateinit var mEnemy: ComponentMapper<EnemyComponent>
    private lateinit var mHealth: ComponentMapper<HealthComponent>
    private lateinit var mPosition: ComponentMapper<PositionComponent>
    private lateinit var mLevel: ComponentMapper<LevelComponent>
    private lateinit var mSpawner: ComponentMapper<SpawnerComponent>

    private lateinit var mPlayer: ComponentMapper<PlayerComponent>

    override fun process(entityId: Int) {
        val cEnemy = mEnemy[entityId]
        val cHealth = mHealth[entityId]

        if (cHealth.currentHealth <= 0) {
            if (mPosition.has(entityId) && mLevel.has(entityId)) {
                val cPosition = mPosition[entityId]
                val cLevel = mLevel[entityId]
                val manaDropAmount = if (mSpawner.has(entityId)) SpawnerHelper.getManaDropAmount(cLevel.level) else EnemyHelper.getManaDropAmount(cLevel.level)
                gameScreen.entityFactory.createManaGlobe(cPosition.position, manaDropAmount)

                if (gameScreen.playerEntity != -1) {
                    val cPlayer = mPlayer[gameScreen.playerEntity]
                    cPlayer.xp += EnemyHelper.getXpDropAmount(cLevel.level)
                }
            }

            if (mSpawner.has(entityId)) {
                gameScreen.entityFactory.currentSpawners.remove(mSpawner[entityId].spawnPoint)
            }
            world.delete(entityId)
        }
    }

}