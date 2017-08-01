package com.gmail.sintinium.ludumdare39.ecs.system

import com.artemis.Aspect
import com.artemis.ComponentMapper
import com.artemis.systems.IteratingSystem
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.math.MathUtils
import com.gmail.sintinium.ludumdare39.ecs.component.*

class WaddleSystem : IteratingSystem(Aspect.all(WaddleComponent::class.java).one(PlayerComponent::class.java, EnemyComponent::class.java)) {

    private lateinit var mWaddle: ComponentMapper<WaddleComponent>
    private lateinit var mVelocity: ComponentMapper<VelocityComponent>

    override fun process(entityId: Int) {
        val cWaddle = mWaddle[entityId]
        val cVelocity = if (mVelocity.has(entityId)) mVelocity[entityId] else null
        val moving = if (cVelocity == null) true else !cVelocity.velocity.isZero(.1f)
        if (!moving) {
            cWaddle.rotation = 0f
        }

        if (cWaddle.goingUp) {
            cWaddle.rotation += cWaddle.speed * Gdx.graphics.deltaTime
        } else {
            cWaddle.rotation -= cWaddle.speed * Gdx.graphics.deltaTime
        }
        if (Math.abs(cWaddle.rotation) > cWaddle.max) {
            cWaddle.goingUp = !cWaddle.goingUp
            cWaddle.rotation = MathUtils.clamp(cWaddle.rotation, -cWaddle.max, cWaddle.max)
        }
    }

}