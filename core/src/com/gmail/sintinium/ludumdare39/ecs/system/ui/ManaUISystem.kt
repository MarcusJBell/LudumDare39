package com.gmail.sintinium.ludumdare39.ecs.system.ui

import com.artemis.BaseSystem
import com.artemis.ComponentMapper
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.GL20
import com.gmail.sintinium.ludumdare39.ecs.component.ManaComponent
import com.gmail.sintinium.ludumdare39.io.ResourceManager
import com.gmail.sintinium.ludumdare39.screen.gameScreen
import com.sun.xml.internal.ws.util.StringUtils
import ktx.math.vec2

class ManaUISystem : BaseSystem() {

    private lateinit var mMana: ComponentMapper<ManaComponent>

    override fun processSystem() {
        if (gameScreen.playerEntity == -1) return
        val playerEntity = gameScreen.playerEntity
        gameScreen.batch.end()

        gameScreen.hudViewport.apply()
        gameScreen.hudBatch.projectionMatrix = gameScreen.hudCamera.combined
        gameScreen.hudBatch.begin()

        val cMana = mMana[playerEntity]
        val scale = 4f
        gameScreen.hudBatch.draw(ResourceManager.manaUI, 0f, 0f, ResourceManager.manaUI.regionWidth * scale, ResourceManager.manaUI.regionHeight * scale)
        gameScreen.hudBatch.flush()

        val x = 6f * scale
        val y = 3f * scale
        val width = ResourceManager.manaFiller.regionWidth * scale
        val height = ResourceManager.manaFiller.regionHeight * scale
        Gdx.gl.glEnable(GL20.GL_SCISSOR_TEST)
        Gdx.gl.glScissor(x.toInt(), y.toInt(), width.toInt(), ((cMana.currentMana / cMana.maxMana) * height).toInt())
        gameScreen.hudBatch.draw(ResourceManager.manaFiller, x, y, width, height)
        gameScreen.hudBatch.flush()
        Gdx.gl.glDisable(GL20.GL_SCISSOR_TEST)

        if (cMana.currentMana / cMana.maxMana <= .1) {
            ResourceManager.mainFont.setColor(.9f, 0f, 0f, 1f)
        } else {
            ResourceManager.mainFont.color = Color.WHITE
        }

        val layout = ResourceManager.mainFontLayout
        layout.setText(ResourceManager.mainFont,"${cMana.currentMana.toInt()}")
        ResourceManager.mainFont.draw(gameScreen.hudBatch, layout, x + width / 2f - layout.width / 2f, y + height / 2f + layout.height / 2f + layout.height - 1)
        layout.setText(ResourceManager.mainFont, "_".repeat(cMana.maxMana.toString().length))
        ResourceManager.mainFont.draw(gameScreen.hudBatch, layout, x + width / 2f - layout.width / 2f, y + height / 2f + layout.height / 2f + 7)
        layout.setText(ResourceManager.mainFont,"${cMana.maxMana.toInt()}")
        ResourceManager.mainFont.draw(gameScreen.hudBatch, layout, x + width / 2f - layout.width / 2f, y + height / 2f + layout.height / 2f - layout.height + 1)

        gameScreen.hudBatch.end()

        gameScreen.viewport.apply()
        gameScreen.batch.begin()
    }

}