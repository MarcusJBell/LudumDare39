package com.gmail.sintinium.ludumdare39.ecs.system.render

import com.artemis.Aspect
import com.artemis.ComponentMapper
import com.artemis.systems.IteratingSystem
import com.badlogic.gdx.Gdx
import com.gmail.sintinium.ludumdare39.ecs.component.BulletComponent
import com.gmail.sintinium.ludumdare39.ecs.component.PositionComponent
import com.gmail.sintinium.ludumdare39.ecs.component.TextureComponent
import com.gmail.sintinium.ludumdare39.screen.gameScreen

class BulletRenderSystem : IteratingSystem(Aspect.all(TextureComponent::class.java, PositionComponent::class.java, BulletComponent::class.java)) {

    lateinit var mTexture: ComponentMapper<TextureComponent>
    lateinit var mPosition: ComponentMapper<PositionComponent>
    lateinit var mBullet: ComponentMapper<BulletComponent>

    override fun process(entityId: Int) {
        val cTexture = mTexture[entityId]
        val cPosition = mPosition[entityId]
        val cBullet = mBullet[entityId]
        cTexture.animationDelta += Gdx.graphics.deltaTime
        val frame = cTexture.animation!!.getKeyFrame(cTexture.animationDelta)
        
        gameScreen.batch.draw(frame.texture, cPosition.position.x, cPosition.position.y,
                frame.regionWidth.toFloat() / 2f, frame.regionHeight.toFloat() / 2f,
                frame.regionWidth.toFloat(), frame.regionHeight.toFloat(),
                1f, 1f, cBullet.angle,
                frame.regionX, frame.regionY, frame.regionWidth, frame.regionHeight,
                false, false)
    }

}