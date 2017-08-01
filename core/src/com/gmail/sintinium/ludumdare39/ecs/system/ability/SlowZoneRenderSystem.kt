package com.gmail.sintinium.ludumdare39.ecs.system.ability

import com.artemis.Aspect
import com.artemis.ComponentMapper
import com.artemis.systems.IteratingSystem
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.GL20
import com.badlogic.gdx.graphics.glutils.ShapeRenderer
import com.badlogic.gdx.math.Vector2
import com.gmail.sintinium.ludumdare39.ecs.component.LevelComponent
import com.gmail.sintinium.ludumdare39.ecs.component.PositionComponent
import com.gmail.sintinium.ludumdare39.ecs.component.ability.SlowZoneComponent
import com.gmail.sintinium.ludumdare39.screen.gameScreen
import com.gmail.sintinium.ludumdare39.util.level.PlayerHelper

class SlowZoneRenderSystem : IteratingSystem(Aspect.all(SlowZoneComponent::class.java, PositionComponent::class.java)) {

    private lateinit var mPosition: ComponentMapper<PositionComponent>
    private lateinit var mSlowZone: ComponentMapper<SlowZoneComponent>

    private lateinit var mLevel: ComponentMapper<LevelComponent>

    private val color = Color(204f / 255f, 173f / 255f, 109f / 255f, .5f)
    private var lastPlayerLevel = -1
    private var lastRadius = -1f

    var ghostPosition: Vector2? = null

    override fun begin() {
        gameScreen.batch.end()
        Gdx.gl.glEnable(GL20.GL_BLEND)
        Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA)
//        gameScreen.shapeRenderer.setAutoShapeType(true)
        gameScreen.shapeRenderer.begin(ShapeRenderer.ShapeType.Filled)

        gameScreen.shapeRenderer.color = color
    }

    override fun process(entityId: Int) {
        val cPosition = mPosition[entityId]
        val cSlowZone = mSlowZone[entityId]
        gameScreen.shapeRenderer.circle(cPosition.position.x, cPosition.position.y, cSlowZone.radius)
    }

    override fun end() {
        gameScreen.shapeRenderer.end()
        Gdx.gl.glDisable(GL20.GL_BLEND)
        gameScreen.shapeRenderer.begin(ShapeRenderer.ShapeType.Line)
        Gdx.gl.glLineWidth(5f)
        if (gameScreen.playerEntity != -1 && ghostPosition != null) {
            val cLevel = mLevel[gameScreen.playerEntity]
            if (cLevel.level != lastPlayerLevel) {
                lastPlayerLevel = cLevel.level
                lastRadius = PlayerHelper.getSlowZoneRange(lastPlayerLevel)
            }
            gameScreen.shapeRenderer.circle(ghostPosition!!.x, ghostPosition!!.y, lastRadius - 1f)
        }
        gameScreen.shapeRenderer.end()
        gameScreen.batch.begin()
    }
}