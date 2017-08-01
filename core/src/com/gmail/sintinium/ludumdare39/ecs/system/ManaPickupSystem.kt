package com.gmail.sintinium.ludumdare39.ecs.system

import com.artemis.Aspect
import com.artemis.ComponentMapper
import com.artemis.systems.IteratingSystem
import com.badlogic.gdx.math.MathUtils
import com.gmail.sintinium.ludumdare39.ecs.component.ManaComponent
import com.gmail.sintinium.ludumdare39.ecs.component.ManaGlobeComponent
import com.gmail.sintinium.ludumdare39.ecs.component.PositionComponent
import com.gmail.sintinium.ludumdare39.ecs.component.VelocityComponent
import com.gmail.sintinium.ludumdare39.screen.gameScreen
import ktx.math.vec2

class ManaPickupSystem : IteratingSystem(Aspect.all(ManaGlobeComponent::class.java, PositionComponent::class.java, VelocityComponent::class.java)) {

    private lateinit var mPosition: ComponentMapper<PositionComponent>
    private lateinit var mManaGlobe: ComponentMapper<ManaGlobeComponent>
    private lateinit var mVelocity: ComponentMapper<VelocityComponent>

    private lateinit var mMana: ComponentMapper<ManaComponent>

    override fun process(entityId: Int) {
        if (gameScreen.playerEntity == -1) return

        val cPosition = mPosition[entityId]
        val cVelocity = mVelocity[entityId]
        val cManaGlobe = mManaGlobe[entityId]

        val pPosition = mPosition[gameScreen.playerEntity]
        val pMana = mMana[gameScreen.playerEntity]

        if (cPosition.position.dst2(pPosition.position) <= 2) {
            world.delete(entityId)
            pMana.currentMana += cManaGlobe.amount
            pMana.currentMana = MathUtils.clamp(pMana.currentMana, 0f, pMana.maxMana)
            return
        }

        val dir = vec2(pPosition.position.x -  cPosition.position.x, pPosition.position.y - cPosition.position.y).nor()
        cVelocity.velocity.add(dir.scl(500f))
    }
}