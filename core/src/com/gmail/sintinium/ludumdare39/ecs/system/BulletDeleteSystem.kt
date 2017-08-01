package com.gmail.sintinium.ludumdare39.ecs.system

import com.artemis.Aspect
import com.artemis.ComponentMapper
import com.artemis.systems.IteratingSystem
import com.badlogic.gdx.math.Rectangle
import com.gmail.sintinium.ludumdare39.ecs.component.BulletComponent
import com.gmail.sintinium.ludumdare39.ecs.component.PositionComponent
import com.gmail.sintinium.ludumdare39.ecs.component.ability.ExplodeOrbComponent
import com.gmail.sintinium.ludumdare39.screen.GameScreen
import com.gmail.sintinium.ludumdare39.screen.gameScreen

class BulletDeleteSystem : IteratingSystem(Aspect.all(BulletComponent::class.java, PositionComponent::class.java).one(BulletComponent::class.java, ExplodeOrbComponent::class.java)) {

    private lateinit var mPosition: ComponentMapper<PositionComponent>
    private lateinit var mBullet: ComponentMapper<BulletComponent>
    private lateinit var mOrb: ComponentMapper<ExplodeOrbComponent>
    private val worldRect by lazy { Rectangle(0f, 0f, gameScreen.tilesLayer.width * GameScreen.TILESIZE, gameScreen.tilesLayer.height * GameScreen.TILESIZE) }

    override fun process(entityId: Int) {
        val cPosition = mPosition[entityId]
        val cBullet = if (mBullet.has(entityId)) mBullet[entityId] else null

        if (!worldRect.contains(cPosition.position) || cBullet?.hit ?: false) {
            world.delete(entityId)
        }
    }
}