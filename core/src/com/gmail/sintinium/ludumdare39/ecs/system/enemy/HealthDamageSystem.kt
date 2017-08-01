package com.gmail.sintinium.ludumdare39.ecs.system.enemy

import com.artemis.Aspect
import com.artemis.ComponentMapper
import com.artemis.systems.IteratingSystem
import com.badlogic.gdx.Gdx
import com.gmail.sintinium.ludumdare39.ecs.component.HealthComponent

class HealthDamageSystem : IteratingSystem(Aspect.all(HealthComponent::class.java)){

    private lateinit var mHealth: ComponentMapper<HealthComponent>

    override fun process(entityId: Int) {
        val cHealth = mHealth[entityId]
        cHealth.currentHealth -= cHealth.tickDamage
        cHealth.tickDamage = 0f
    }

}