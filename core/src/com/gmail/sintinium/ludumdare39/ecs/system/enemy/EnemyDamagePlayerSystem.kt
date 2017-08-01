package com.gmail.sintinium.ludumdare39.ecs.system.enemy

import com.artemis.Aspect
import com.artemis.ComponentMapper
import com.artemis.systems.IteratingSystem
import com.badlogic.gdx.math.Rectangle
import com.gmail.sintinium.ludumdare39.ecs.component.*
import com.gmail.sintinium.ludumdare39.io.ResourceManager
import com.gmail.sintinium.ludumdare39.screen.gameScreen

class EnemyDamagePlayerSystem : IteratingSystem(Aspect.all(EnemyComponent::class.java, PositionComponent::class.java, CollisionComponent::class.java).exclude(SpawnerComponent::class.java)) {

    private lateinit var mPosition: ComponentMapper<PositionComponent>
    private lateinit var mEnemy: ComponentMapper<EnemyComponent>
    private lateinit var mCollision: ComponentMapper<CollisionComponent>
    private lateinit var mMana: ComponentMapper<ManaComponent>

    private val tempPlayerRect = Rectangle()
    private val tempEnemyRect = Rectangle()

    override fun process(entityId: Int) {
        if (gameScreen.playerEntity == -1) return
        val playerId = gameScreen.playerEntity
        val cPosision = mPosition[entityId]
        val cEnemy = mEnemy[entityId]
        val cCollision = mCollision[entityId]
        val pPosition = mPosition[playerId]
        val pCollision = mCollision[playerId]
        val pMana = mMana[playerId]

        tempPlayerRect.set(pPosition.position.x, pPosition.position.y, pCollision.bounds.x, pCollision.bounds.y)
        tempEnemyRect.set(cPosision.position.x, cPosision.position.y, cCollision.bounds.x, cCollision.bounds.y)

        if (tempEnemyRect.overlaps(tempPlayerRect) && !pMana.protected) {
            ResourceManager.ow.play(.2f)
            pMana.currentMana -= cEnemy.damage
            pMana.protected = true
            pMana.protectedTime = 0f
        }
    }

}