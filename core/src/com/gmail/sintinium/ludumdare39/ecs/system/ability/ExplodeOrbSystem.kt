package com.gmail.sintinium.ludumdare39.ecs.system.ability

import com.artemis.Aspect
import com.artemis.ComponentMapper
import com.artemis.systems.IteratingSystem
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.math.Vector2
import com.gmail.sintinium.ludumdare39.ecs.component.EnemyComponent
import com.gmail.sintinium.ludumdare39.ecs.component.HealthComponent
import com.gmail.sintinium.ludumdare39.ecs.component.PositionComponent
import com.gmail.sintinium.ludumdare39.ecs.component.ability.ExplodeOrbComponent
import com.gmail.sintinium.ludumdare39.screen.gameScreen

class ExplodeOrbSystem : IteratingSystem(Aspect.all(ExplodeOrbComponent::class.java, PositionComponent::class.java)) {

    private lateinit var mPosition: ComponentMapper<PositionComponent>
    private lateinit var mOrb: ComponentMapper<ExplodeOrbComponent>

    private lateinit var mHealth: ComponentMapper<HealthComponent>

    override fun process(entityId: Int) {
        val cOrb = mOrb[entityId]
        cOrb.timeAlive += Gdx.graphics.deltaTime
        if (cOrb.timeAlive >= cOrb.duration) {
            cOrb.shouldExplode = true
        }
        if (cOrb.shouldExplode) {
            damageEnemies(entityId)
            world.delete(entityId)
            return
        }
    }

    fun damageEnemies(entityId: Int) {
        val cOrb = mOrb[entityId]
        val cPosition = mPosition[entityId]
        val enemies = world.aspectSubscriptionManager[Aspect.all(EnemyComponent::class.java, PositionComponent::class.java, HealthComponent::class.java)].entities
        for (i in 0..(enemies.size() - 1)) {
            val enemyId = enemies[i]
            val ePosition = mPosition[enemyId]
            val eHealth = mHealth[enemyId]
            if (ePosition.position.dst(cPosition.position) <= cOrb.explodeRadius) {
                eHealth.currentHealth -= cOrb.damage
                println(cOrb.damage)
            }
        }
    }

    override fun removed(entityId: Int) {
        gameScreen.playerInput.orbFired = false
        gameScreen.playerInput.orbId = -1
    }
}