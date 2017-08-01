package com.gmail.sintinium.ludumdare39.ecs.system.render

import com.artemis.Aspect
import com.artemis.ComponentMapper
import com.artemis.systems.IteratingSystem
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.math.MathUtils
import com.gmail.sintinium.ludumdare39.ecs.component.LevelComponent
import com.gmail.sintinium.ludumdare39.ecs.component.PositionComponent
import com.gmail.sintinium.ludumdare39.ecs.component.SpawnerComponent
import com.gmail.sintinium.ludumdare39.io.ResourceManager
import com.gmail.sintinium.ludumdare39.screen.GameScreen
import com.gmail.sintinium.ludumdare39.screen.gameScreen

class SpawnerLevelRenderSystem : IteratingSystem(Aspect.all(PositionComponent::class.java, SpawnerComponent::class.java, LevelComponent::class.java)) {

    private lateinit var mPosition: ComponentMapper<PositionComponent>
    private lateinit var mSpawner: ComponentMapper<SpawnerComponent>
    private lateinit var mLevel: ComponentMapper<LevelComponent>

    private val flameColor = Color(1f, 1f, 1f, .75f)

    override fun process(entityId: Int) {
        val cPosition = mPosition[entityId]
        val cSpawner = mSpawner[entityId]
        val cLevel = mLevel[entityId]
        cSpawner.flameDelta += Gdx.graphics.deltaTime
        if (cLevel.level == 1) {
            cSpawner.flameRotation += 100f * Gdx.graphics.deltaTime
        } else {
            cSpawner.flameRotation += (100f / (cLevel.level / 2)) * Gdx.graphics.deltaTime
        }
        while (cSpawner.flameRotation > 360) {
            cSpawner.flameRotation -= 360
        }
        gameScreen.batch.color = flameColor
        val texture = ResourceManager.graveFlameAnimation.getKeyFrame(cSpawner.flameDelta, true)

        for (i in 0..(cLevel.level - 1)) {
            val rad = MathUtils.degreesToRadians * cSpawner.flameRotation
            val t = (2 * Math.PI * i / cLevel.level).toFloat()
            val x = cPosition.position.x + MathUtils.cos(t + rad) * 15f
            val y = cPosition.position.y + MathUtils.sin(t + rad) * 15f
            val width = texture.regionWidth / 1.5f
            val height = texture.regionHeight / 1.5f
            gameScreen.batch.draw(texture, x - width / 2f, y - height / 2f + GameScreen.TILESIZE / 2f,
                    width, height)
        }

        gameScreen.batch.color = Color.WHITE
    }

}