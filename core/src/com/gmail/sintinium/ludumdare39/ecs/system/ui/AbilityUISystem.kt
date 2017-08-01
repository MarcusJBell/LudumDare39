package com.gmail.sintinium.ludumdare39.ecs.system.ui

import com.artemis.BaseSystem
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.g2d.TextureRegion
import com.gmail.sintinium.ludumdare39.io.ResourceManager
import com.gmail.sintinium.ludumdare39.screen.gameScreen
import ktx.app.color
import java.awt.geom.Line2D

class AbilityUISystem : BaseSystem() {

    private var initTextures = false
    private var sumWidth = 0f
    private lateinit var abilties: Array<TextureRegion>
    private var abilityKeys = arrayOf("Q", "E", "R")
    private var scale = 4f
    private var orbAbilityColor = color(.7f, .5f, .5f, 1f)

    private fun initTextures() {
        initTextures = true
        abilties = arrayOf(ResourceManager.dashAbility, ResourceManager.orbAbility, ResourceManager.slowAbility)
        for (a in abilties) {
            sumWidth += a.regionWidth * scale
        }
    }

    override fun processSystem() {
        if (gameScreen.playerEntity == -1) return
        if (!initTextures) {
            initTextures()
        }
        val playerEntity = gameScreen.playerEntity
        gameScreen.batch.end()

        gameScreen.hudViewport.apply()
        gameScreen.hudBatch.projectionMatrix = gameScreen.hudCamera.combined
        gameScreen.hudBatch.begin()

        val layout = ResourceManager.mainFontLayout
        val font = ResourceManager.mainFont

        var currentX = gameScreen.hudViewport.screenWidth / 2f - sumWidth / 2f

        for ((i, a) in abilties.withIndex()) {
            if (gameScreen.playerInput.orbFired && a === ResourceManager.orbAbility) {
                gameScreen.hudBatch.color = orbAbilityColor
            } else {
                gameScreen.hudBatch.color = Color.WHITE
            }
            gameScreen.hudBatch.draw(a, currentX, 0f, a.regionWidth * scale, a.regionHeight * scale)
            font.color = Color.WHITE
            layout.setText(font, abilityKeys[i])
            font.draw(gameScreen.hudBatch, layout, currentX + 5f, a.regionHeight * scale - 5f)

            currentX += a.regionWidth * scale

        }

        gameScreen.hudBatch.color = Color.WHITE
        gameScreen.hudBatch.end()
        gameScreen.viewport.apply()
        gameScreen.batch.begin()
    }

}