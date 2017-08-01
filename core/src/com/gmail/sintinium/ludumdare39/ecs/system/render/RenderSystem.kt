package com.gmail.sintinium.ludumdare39.ecs.system.render

import com.artemis.Aspect
import com.artemis.ComponentMapper
import com.artemis.systems.IteratingSystem
import com.badlogic.gdx.Gdx
import com.gmail.sintinium.ludumdare39.ecs.component.*
import com.gmail.sintinium.ludumdare39.screen.gameScreen

class RenderSystem : IteratingSystem(Aspect.all(TextureComponent::class.java, PositionComponent::class.java).exclude(BulletComponent::class.java, SpawnerComponent::class.java)) {

    lateinit var mTexture: ComponentMapper<TextureComponent>
    lateinit var mPosition: ComponentMapper<PositionComponent>
    lateinit var mWaddle: ComponentMapper<WaddleComponent>

    override fun process(entityId: Int) {
        val cTexture = mTexture[entityId]
        val cPosition = mPosition[entityId]
        val cWaddle = if (mWaddle.has(entityId)) mWaddle[entityId] else null
        val waddle = cWaddle?.rotation ?: 0f

        if (cTexture.texture != null) {
            gameScreen.batch.draw(cTexture.texture!!.texture, cPosition.position.x - (cTexture.texture!!.regionWidth / 2f) + cTexture.xOffset, cPosition.position.y + cTexture.yOffset,
                    cTexture.texture!!.regionWidth.toFloat() / 2f, 0f,
                    cTexture.texture!!.regionWidth.toFloat(), cTexture.texture!!.regionHeight.toFloat(),
                    cTexture.scale, cTexture.scale, waddle,
                    cTexture.texture!!.regionX, cTexture.texture!!.regionY, cTexture.texture!!.regionWidth, cTexture.texture!!.regionHeight,
                    false, false)
//            gameScreen.batch.draw(cTexture.texture, cPosition.position.x - (cTexture.texture!!.regionWidth / 2f), cPosition.position.y)
        } else if (cTexture.animation != null) {
            cTexture.animationDelta += Gdx.graphics.deltaTime
            val frame = cTexture.animation!!.getKeyFrame(cTexture.animationDelta)
            gameScreen.batch.draw(frame, cPosition.position.x - (frame.regionWidth / 2f), cPosition.position.y)
        }
    }

}