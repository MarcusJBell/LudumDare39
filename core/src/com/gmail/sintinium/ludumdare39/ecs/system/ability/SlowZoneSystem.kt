package com.gmail.sintinium.ludumdare39.ecs.system.ability

import com.artemis.Aspect
import com.artemis.ComponentMapper
import com.artemis.systems.IteratingSystem
import com.artemis.utils.IntBag
import com.badlogic.gdx.Gdx
import com.gmail.sintinium.ludumdare39.ecs.component.EnemyComponent
import com.gmail.sintinium.ludumdare39.ecs.component.PositionComponent
import com.gmail.sintinium.ludumdare39.ecs.component.SpawnerComponent
import com.gmail.sintinium.ludumdare39.ecs.component.VelocityComponent
import com.gmail.sintinium.ludumdare39.ecs.component.ability.SlowZoneComponent

class SlowZoneSystem : IteratingSystem(Aspect.all(PositionComponent::class.java, SlowZoneComponent::class.java)) {

    private lateinit var mPosition: ComponentMapper<PositionComponent>
    private lateinit var mSlowZone: ComponentMapper<SlowZoneComponent>

    private lateinit var mVelocity: ComponentMapper<VelocityComponent>

    private lateinit var enemies: IntBag

    override fun begin() {
        enemies = world.aspectSubscriptionManager[Aspect.all(EnemyComponent::class.java, PositionComponent::class.java, VelocityComponent::class.java).exclude(SpawnerComponent::class.java)].entities
    }

    override fun process(entityId: Int) {
        val cPosition = mPosition[entityId]
        val cSlowZone = mSlowZone[entityId]
        cSlowZone.timeAlive += Gdx.graphics.deltaTime
        if (cSlowZone.timeAlive > cSlowZone.duration) {
            world.delete(entityId)
            return
        }
        for (i in 0..enemies.size()-1) {
            val enemyId = enemies[i]
            val ePosition = mPosition[enemyId]
            val eVelocity = mVelocity[enemyId]

            if (cPosition.position.dst2(ePosition.position) <= cSlowZone.radius * cSlowZone.radius) {
                eVelocity.slowAmount = cSlowZone.slowAmount
            }
        }
    }
}