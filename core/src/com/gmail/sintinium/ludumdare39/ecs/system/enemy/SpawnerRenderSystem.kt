package com.gmail.sintinium.ludumdare39.ecs.system.enemy

import com.artemis.Aspect
import com.artemis.ComponentMapper
import com.artemis.systems.IteratingSystem
import com.gmail.sintinium.ludumdare39.ecs.component.PositionComponent
import com.gmail.sintinium.ludumdare39.ecs.component.SpawnerComponent
import com.gmail.sintinium.ludumdare39.ecs.component.TextureComponent
import com.gmail.sintinium.ludumdare39.screen.gameScreen

class SpawnerRenderSystem : IteratingSystem(Aspect.all(TextureComponent::class.java, SpawnerComponent::class.java, PositionComponent::class.java)) {

    lateinit var mTexture: ComponentMapper<TextureComponent>
    lateinit var mPosition: ComponentMapper<PositionComponent>

    override fun process(entityId: Int) {
        val cTexture = mTexture[entityId]
        val cPosition = mPosition[entityId]
        gameScreen.batch.draw(cTexture.texture, cPosition.position.x - (cTexture.texture!!.regionWidth / 2f), cPosition.position.y)
    }

}