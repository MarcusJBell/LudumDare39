package com.gmail.sintinium.ludumdare39.ecs.system

import com.artemis.Aspect
import com.artemis.ComponentMapper
import com.artemis.systems.IteratingSystem
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.math.Vector2
import com.gmail.sintinium.ludumdare39.ecs.component.PositionComponent
import com.gmail.sintinium.ludumdare39.ecs.component.VelocityComponent

class VelocitySystem : IteratingSystem(Aspect.all(PositionComponent::class.java, VelocityComponent::class.java)) {

    private lateinit var mPosition: ComponentMapper<PositionComponent>
    private lateinit var mVelocity: ComponentMapper<VelocityComponent>

    override fun process(entityId: Int) {
        val cPosition = mPosition[entityId]
        val cVelocity = mVelocity[entityId]

        cPosition.position.add(Vector2(cVelocity.velocity).scl(Gdx.graphics.deltaTime).scl(cVelocity.slowAmount))
        cVelocity.slowAmount = 1f
        if (cVelocity.resetNextFrame)
            cVelocity.velocity.setZero()
    }
}