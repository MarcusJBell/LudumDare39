package com.gmail.sintinium.ludumdare39.ecs.system.player

import com.artemis.BaseSystem
import com.artemis.ComponentMapper
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.math.MathUtils
import com.badlogic.gdx.math.Vector2
import com.gmail.sintinium.ludumdare39.ecs.component.PositionComponent
import com.gmail.sintinium.ludumdare39.screen.gameScreen
import ktx.math.vec2

class CameraFollowSystem : BaseSystem() {

    private lateinit var mPosition: ComponentMapper<PositionComponent>
    private val tweenPosition: Vector2 by lazy { Vector2(gameScreen.camera.position.x, gameScreen.camera.position.y) }

    override fun processSystem() {
        if (gameScreen.playerEntity == -1) return
        val cPosition = mPosition[gameScreen.playerEntity]
//        gameScreen.camera.lookAt(cPosition.position.x, cPosition.position.y, 0f)
        tweenPosition.lerp(cPosition.position, 20f * Gdx.graphics.deltaTime)
        gameScreen.camera.position.set(tweenPosition.x, tweenPosition.y, 0f)
        gameScreen.camera.update()
    }

}