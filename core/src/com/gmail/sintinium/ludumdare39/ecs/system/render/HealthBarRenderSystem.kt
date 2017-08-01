package com.gmail.sintinium.ludumdare39.ecs.system.render

import com.artemis.Aspect
import com.artemis.ComponentMapper
import com.artemis.systems.IteratingSystem
import com.gmail.sintinium.ludumdare39.ecs.component.HealthComponent
import com.gmail.sintinium.ludumdare39.ecs.component.PositionComponent
import com.gmail.sintinium.ludumdare39.screen.gameScreen

class HealthBarRenderSystem : IteratingSystem(Aspect.all(PositionComponent::class.java, HealthComponent::class.java)) {

    private lateinit var mPosition: ComponentMapper<PositionComponent>
    private lateinit var mHealth: ComponentMapper<HealthComponent>

    private val batch by lazy { gameScreen.batch }

    override fun process(entityId: Int) {
        val cPosition = mPosition[entityId]
        val cHealth = mHealth[entityId]
    }
}