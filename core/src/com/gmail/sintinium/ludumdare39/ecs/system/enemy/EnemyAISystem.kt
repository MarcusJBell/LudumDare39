package com.gmail.sintinium.ludumdare39.ecs.system.enemy

import com.artemis.Aspect
import com.artemis.ComponentMapper
import com.artemis.systems.IteratingSystem
import com.gmail.sintinium.ludumdare39.ecs.component.EnemyAIComponent
import com.gmail.sintinium.ludumdare39.ecs.component.EnemyComponent
import com.gmail.sintinium.ludumdare39.ecs.component.PositionComponent
import com.gmail.sintinium.ludumdare39.ecs.component.VelocityComponent
import com.gmail.sintinium.ludumdare39.screen.gameScreen
import ktx.math.vec2

class EnemyAISystem : IteratingSystem(Aspect.all(EnemyComponent::class.java, EnemyAIComponent::class.java, PositionComponent::class.java, VelocityComponent::class.java)) {

    private lateinit var mVelocity: ComponentMapper<VelocityComponent>
    private lateinit var mEnemyAI: ComponentMapper<EnemyAIComponent>
    private lateinit var mPosition: ComponentMapper<PositionComponent>

    private val playerPosition = vec2(0f, 0f)

    override fun begin() {
        if (gameScreen.playerEntity != -1) {
            val cPosition = mPosition[gameScreen.playerEntity]
            playerPosition.set(cPosition.position.cpy())
        }
    }

    override fun process(entityId: Int) {
        val cPosition = mPosition[entityId]
        val cVelocity = mVelocity[entityId]
        val cEnemyAI = mEnemyAI[entityId]

        val dir = vec2(playerPosition.x -  cPosition.position.x, playerPosition.y - cPosition.position.y).nor()
        cVelocity.velocity.add(dir.scl(cEnemyAI.speed))
    }

}