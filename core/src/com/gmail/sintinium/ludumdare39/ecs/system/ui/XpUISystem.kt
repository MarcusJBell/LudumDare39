package com.gmail.sintinium.ludumdare39.ecs.system.ui

import com.artemis.BaseSystem
import com.artemis.ComponentMapper
import com.badlogic.gdx.graphics.Color
import com.gmail.sintinium.ludumdare39.ecs.component.LevelComponent
import com.gmail.sintinium.ludumdare39.ecs.component.PlayerComponent
import com.gmail.sintinium.ludumdare39.io.ResourceManager
import com.gmail.sintinium.ludumdare39.screen.gameScreen

class XpUISystem : BaseSystem() {

    private lateinit var mPlayer: ComponentMapper<PlayerComponent>
    private lateinit var mLevel: ComponentMapper<LevelComponent>

    private var scale = 6f
    private var barColor = Color(1f, 1f, 1f, .7f)

    override fun processSystem() {
        if (gameScreen.playerEntity == -1) return
        gameScreen.batch.end()

        gameScreen.hudViewport.apply()
        gameScreen.hudBatch.projectionMatrix = gameScreen.hudCamera.combined
        gameScreen.hudBatch.begin()
        val playerId = gameScreen.playerEntity
        val cPlayer = mPlayer[playerId]
        val cLevel = mLevel[playerId]

        val xpBar = ResourceManager.xpBar
        val xpBarFiller = ResourceManager.xpBarFiller
        gameScreen.hudBatch.draw(xpBar, gameScreen.hudViewport.screenWidth - xpBar.regionWidth.toFloat() * scale, 0f, xpBar.regionWidth * scale, xpBar.regionHeight * scale)
        gameScreen.hudBatch.color = barColor
        gameScreen.hudBatch.draw(xpBarFiller, gameScreen.hudViewport.screenWidth - xpBarFiller.regionWidth.toFloat() * scale - 6f, 6f, (xpBarFiller.regionWidth * scale)* (cPlayer.xp / cPlayer.nextLevel), (xpBarFiller.regionHeight * scale))
        gameScreen.hudBatch.color = Color.WHITE

        val font = ResourceManager.mainFont
        val layout = ResourceManager.mainFontLayout
        layout.setText(font, cLevel.level.toString())
        font.draw(gameScreen.hudBatch, layout, gameScreen.hudViewport.screenWidth - (xpBar.regionWidth.toFloat() / 2f) * scale - layout.width / 2f, xpBar.regionHeight * scale - layout.height)

        gameScreen.hudBatch.color = Color.WHITE
        gameScreen.hudBatch.end()
        gameScreen.viewport.apply()
        gameScreen.batch.begin()
    }

}