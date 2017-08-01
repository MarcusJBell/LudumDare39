package com.gmail.sintinium.ludumdare39

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Input
import com.badlogic.gdx.Screen
import com.badlogic.gdx.graphics.Pixmap
import com.badlogic.gdx.graphics.PixmapIO
import com.badlogic.gdx.utils.BufferUtils
import com.badlogic.gdx.utils.ScreenUtils
import com.gmail.sintinium.ludumdare39.screen.GameScreen
import com.gmail.sintinium.ludumdare39.screen.gameScreen
import kotlin.properties.Delegates
import com.badlogic.gdx.Game as GdxGame

var game by Delegates.notNull<Game>()

class Game : GdxGame() {

    lateinit var gameScreen: GameScreen

    init {
        game = this
    }

    override fun create() {
        gameScreen = GameScreen()
        setScreen(gameScreen)
    }

    override fun getScreen(): Screen {
        return super.getScreen()
    }

    override fun setScreen(screen: Screen?) {
        super.setScreen(screen)
    }

    override fun render() {
        super.render()
        if (Gdx.input.isKeyJustPressed(Input.Keys.Z)) {
            val pixels = ScreenUtils.getFrameBufferPixels(0, 0, Gdx.graphics.backBufferWidth, Gdx.graphics.backBufferHeight, true)
            val pixmap = Pixmap(Gdx.graphics.backBufferWidth, Gdx.graphics.backBufferHeight, Pixmap.Format.RGBA8888)
            BufferUtils.copy(pixels, 0, pixmap.pixels, pixels.size)
            PixmapIO.writePNG(Gdx.files.absolute("/home/sintinium/Pictures/ManaAddict.png"), pixmap)
            pixmap.dispose()
        }
    }

    override fun pause() {
        super.pause()
    }

    override fun resume() {
        super.resume()
    }

    override fun resize(width: Int, height: Int) {
        super.resize(width, height)
    }

    override fun dispose() {
        super.dispose()
    }

}