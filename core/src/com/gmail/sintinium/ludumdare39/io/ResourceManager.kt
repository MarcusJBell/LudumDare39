package com.gmail.sintinium.ludumdare39.io

import com.badlogic.gdx.assets.AssetManager
import com.badlogic.gdx.assets.loaders.resolvers.InternalFileHandleResolver
import com.badlogic.gdx.audio.Music
import com.badlogic.gdx.audio.Sound
import com.badlogic.gdx.graphics.g2d.*
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGeneratorLoader
import com.badlogic.gdx.graphics.g2d.freetype.FreetypeFontLoader
import javax.xml.soap.Text

object ResourceManager {

    lateinit var assetManager: AssetManager

    lateinit var textureAtlas: TextureAtlas

    lateinit var player: TextureRegion
    lateinit var manaEater: Animation<TextureRegion>
    lateinit var debug: TextureRegion
    lateinit var mana: TextureRegion
    lateinit var graveSpawner: TextureRegion
    lateinit var graveFlameAnimation: Animation<TextureRegion>

    lateinit var fireballAnimation: Animation<TextureRegion>
    lateinit var explodeOrb: TextureRegion

    lateinit var manaUI: TextureRegion
    lateinit var manaFiller: TextureRegion
    lateinit var xpBar: TextureRegion
    lateinit var xpBarFiller: TextureRegion

    lateinit var dashAbility: TextureRegion
    lateinit var orbAbility: TextureRegion
    lateinit var slowAbility: TextureRegion

    lateinit var mainFont: BitmapFont
    lateinit var mainFontLayout: GlyphLayout

    lateinit var music: Music
    lateinit var ow: Sound

    var loaded = false

    fun init() {
        assetManager = AssetManager()
        initResolvers()
        initFonts()
        assetManager.load("packed/textures.atlas", TextureAtlas::class.java)
        assetManager.load("Epicmusicedited.mp3", Music::class.java)
        assetManager.load("OwSound.mp3", Sound::class.java)
        assetManager.finishLoading()
        afterLoaded()
    }

    fun initFonts() {
        val mainFont = FreetypeFontLoader.FreeTypeFontLoaderParameter()
        mainFont.fontFileName = "Ubuntu-Bold.ttf"
        mainFont.fontParameters.size = 14
        assetManager.load(mainFont.fontFileName, BitmapFont::class.java, mainFont)
    }

    fun initResolvers() {
        val ftfResolver = InternalFileHandleResolver()
        assetManager.setLoader(FreeTypeFontGenerator::class.java, FreeTypeFontGeneratorLoader(ftfResolver))
        assetManager.setLoader(BitmapFont::class.java, "Ubuntu-Bold.ttf", FreetypeFontLoader(ftfResolver))
    }

    fun afterLoaded() {
        loaded = true
        textureAtlas = assetManager["packed/textures.atlas", TextureAtlas::class.java]
        music = assetManager["Epicmusicedited.mp3", Music::class.java]
        ow = assetManager["OwSound.mp3", Sound::class.java]

        mainFont = assetManager["Ubuntu-Bold.ttf", BitmapFont::class.java]
        mainFontLayout = GlyphLayout()

        player = textureAtlas.findRegion("wizardman")
        manaEater = Animation(.2f, textureAtlas.findRegions("Manaeater"), Animation.PlayMode.LOOP)
        debug = textureAtlas.findRegion("player")
        mana = textureAtlas.findRegion("mana")
        graveSpawner = textureAtlas.findRegion("Holespawner")
        graveFlameAnimation = Animation(0.5f, textureAtlas.findRegions("Graveflame"))

        fireballAnimation = Animation(0.2f, textureAtlas.findRegions("fireball"), Animation.PlayMode.LOOP)
        explodeOrb = textureAtlas.findRegion("orbthing")

        dashAbility = textureAtlas.findRegion("Dashability")
        orbAbility = textureAtlas.findRegion("Orbability")
        slowAbility = textureAtlas.findRegion("Slowability")

        manaUI = textureAtlas.findRegion("ManaUIreal")
        manaFiller = textureAtlas.findRegion("ManaUIfiller")
        xpBar = textureAtlas.findRegion("xpbar")
        xpBarFiller = textureAtlas.findRegion("xpbarfiller")

        music.volume = .6f
        music.isLooping = true
        music.play()
    }

    fun dispose() {
        assetManager.dispose()
    }

}