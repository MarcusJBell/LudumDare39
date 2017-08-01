package com.gmail.sintinium.ludumdare39.ecs.system.ability

import com.artemis.Aspect
import com.artemis.BaseSystem
import com.artemis.ComponentMapper
import com.artemis.systems.IteratingSystem
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.GL20
import com.badlogic.gdx.graphics.glutils.ShapeRenderer
import com.badlogic.gdx.math.MathUtils
import com.badlogic.gdx.math.Vector2
import com.gmail.sintinium.ludumdare39.ecs.component.PositionComponent
import com.gmail.sintinium.ludumdare39.ecs.component.ability.ExplodeOrbComponent
import com.gmail.sintinium.ludumdare39.screen.gameScreen
import ktx.math.vec2

class ExplodeOrbRenderSystem : IteratingSystem(Aspect.all(ExplodeOrbComponent::class.java, PositionComponent::class.java)) {

    private lateinit var mPosition: ComponentMapper<PositionComponent>
    private lateinit var mOrb: ComponentMapper<ExplodeOrbComponent>

    private val color = Color(204f / 255f, 173f / 255f, 109f / 255f, .3f)
    private val color1 = Color(204f / 255f, 173f / 255f, 109f / 255f, .75f)
    var ghostPosition: Vector2? = null

    private val tempVec = vec2()

    override fun begin() {
        gameScreen.batch.end()
        Gdx.gl.glEnable(GL20.GL_BLEND)
        Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA)
//        gameScreen.shapeRenderer.setAutoShapeType(true)
        gameScreen.shapeRenderer.begin(ShapeRenderer.ShapeType.Line)

        gameScreen.shapeRenderer.color = color
    }

    override fun process(entityId: Int) {
        val cPosition = mPosition[entityId]
        val cOrb = mOrb[entityId]
        gameScreen.shapeRenderer.circle(cPosition.position.x, cPosition.position.y, cOrb.explodeRadius)
    }

    override fun end() {
        gameScreen.shapeRenderer.color = color1
        if (gameScreen.playerEntity != -1 && ghostPosition != null) {
            val cPosition = mPosition[gameScreen.playerEntity]
            val angle = MathUtils.radiansToDegrees * MathUtils.atan2(ghostPosition!!.y - cPosition.position.y, ghostPosition!!.x - cPosition.position.x)
            tempVec.x = cPosition.position.x + MathUtils.cosDeg(angle) * 150
            tempVec.y = cPosition.position.y + MathUtils.sinDeg(angle) * 150
            gameScreen.shapeRenderer.rectLine(cPosition.position.x, cPosition.position.y, tempVec.x, tempVec.y, 15f)
        }
        gameScreen.shapeRenderer.end()
        Gdx.gl.glDisable(GL20.GL_BLEND)
        gameScreen.batch.begin()
    }

}