package com.gmail.sintinium.ludumdare39.screen

import com.badlogic.gdx.Screen
import com.badlogic.gdx.graphics.Color
import com.gmail.sintinium.ludumdare39.game
import com.gmail.sintinium.ludumdare39.io.ResourceManager
import ktx.app.clearScreen

class GameoverScreen(val gameScreen: GameScreen, val level: Int) : Screen {

    var time = System.currentTimeMillis()
    var ended = false

    override fun hide() {
    }

    override fun show() {
    }

    override fun render(delta: Float) {
        if (ended) return
        clearScreen(0f, 0f, 0f)
        gameScreen.hudViewport.apply()
        gameScreen.hudBatch.projectionMatrix = com.gmail.sintinium.ludumdare39.screen.gameScreen.hudCamera.combined
        gameScreen.hudBatch.begin()

        val font = ResourceManager.mainFont
        val layout = ResourceManager.mainFontLayout
        font.color = Color.RED
        layout.setText(font, "GAME OVER")
        font.draw(gameScreen.hudBatch, layout, gameScreen.hudViewport.screenWidth / 2f - layout.width / 2f, gameScreen.hudViewport.screenHeight / 2f - layout.height / 2f)
        font.color = Color.WHITE
        layout.setText(font, "You made it to level $level!")
        font.draw(gameScreen.hudBatch, layout, gameScreen.hudViewport.screenWidth / 2f - layout.width / 2f, gameScreen.hudViewport.screenHeight / 2f - layout.height / 2f - layout.height - 5f)

        gameScreen.hudBatch.color = Color.WHITE
        gameScreen.hudBatch.end()
        gameScreen.viewport.apply()

        if (System.currentTimeMillis() - time >= 3000L) {
            gameScreen.dispose()
            game.gameScreen = GameScreen()
            game.screen = game.gameScreen
            ended = true
        }
    }

    override fun pause() {
    }

    override fun resume() {
    }

    override fun resize(width: Int, height: Int) {
        gameScreen.hudCamera.position.set(width / 2f, height / 2f, 0f)
        gameScreen.hudViewport.update(width, height)
    }

    override fun dispose() {
    }

}