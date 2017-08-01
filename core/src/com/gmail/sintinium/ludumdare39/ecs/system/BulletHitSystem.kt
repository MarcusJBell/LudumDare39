package com.gmail.sintinium.ludumdare39.ecs.system

import com.artemis.Aspect
import com.artemis.ComponentMapper
import com.artemis.systems.IteratingSystem
import com.artemis.utils.IntBag
import com.badlogic.gdx.math.Rectangle
import com.gmail.sintinium.ludumdare39.ecs.component.*

class BulletHitSystem : IteratingSystem(Aspect.all(BulletComponent::class.java, PositionComponent::class.java, CollisionComponent::class.java)) {

    private lateinit var mPosition: ComponentMapper<PositionComponent>
    private lateinit var mBullet: ComponentMapper<BulletComponent>
    private lateinit var mCollision: ComponentMapper<CollisionComponent>

    private lateinit var mHealth: ComponentMapper<HealthComponent>

    private lateinit var enemies: IntBag
    private val tempBulletRect = Rectangle()
    private val tempEnemyRect = Rectangle()

    override fun begin() {
        enemies = world.aspectSubscriptionManager[Aspect.all(EnemyComponent::class.java, HealthComponent::class.java, PositionComponent::class.java, CollisionComponent::class.java)].entities
    }

    override fun process(entityId: Int) {
        val cPosition = mPosition[entityId]
        val cBullet = mBullet[entityId]
        val cCollision = mCollision[entityId]
        tempBulletRect.set(cPosition.position.x, cPosition.position.y, cCollision.bounds.x, cCollision.bounds.y)
        for (i in 0..enemies.size() - 1) {
            val enemyId = enemies[i]
            val eCollision = mCollision[enemyId]
            val ePosition = mPosition[enemyId]
            val eHealth = mHealth[enemyId]
            tempEnemyRect.set(ePosition.position.x, ePosition.position.y, eCollision.bounds.x, eCollision.bounds.y)
            if (tempBulletRect.overlaps(tempEnemyRect)) {
                cBullet.hit = true
                eHealth.tickDamage += cBullet.damage
            }
        }
    }

}