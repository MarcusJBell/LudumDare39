package com.gmail.sintinium.ludumdare39.screen

import com.artemis.World
import com.artemis.WorldConfigurationBuilder
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Screen
import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.graphics.glutils.ShapeRenderer
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer
import com.badlogic.gdx.maps.tiled.TmxMapLoader
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.utils.viewport.ExtendViewport
import com.badlogic.gdx.utils.viewport.ScreenViewport
import com.gmail.sintinium.ludumdare39.ecs.PlayerInput
import com.gmail.sintinium.ludumdare39.ecs.factory.EntityFactory
import com.gmail.sintinium.ludumdare39.ecs.system.*
import com.gmail.sintinium.ludumdare39.ecs.system.ability.*
import com.gmail.sintinium.ludumdare39.ecs.system.enemy.*
import com.gmail.sintinium.ludumdare39.ecs.system.player.CameraFollowSystem
import com.gmail.sintinium.ludumdare39.ecs.system.player.GameOverSystem
import com.gmail.sintinium.ludumdare39.ecs.system.player.LevelUpSystem
import com.gmail.sintinium.ludumdare39.ecs.system.player.ManaDecaySystem
import com.gmail.sintinium.ludumdare39.ecs.system.render.PlayerManaRenderSystem
import com.gmail.sintinium.ludumdare39.ecs.system.render.BulletRenderSystem
import com.gmail.sintinium.ludumdare39.ecs.system.render.RenderSystem
import com.gmail.sintinium.ludumdare39.ecs.system.render.SpawnerLevelRenderSystem
import com.gmail.sintinium.ludumdare39.ecs.system.ui.AbilityUISystem
import com.gmail.sintinium.ludumdare39.ecs.system.ui.ManaUISystem
import com.gmail.sintinium.ludumdare39.ecs.system.ui.XpUISystem
import com.gmail.sintinium.ludumdare39.io.ResourceManager
import ktx.app.clearScreen
import ktx.math.vec2
import ktx.math.vec3
import kotlin.properties.Delegates

var gameScreen by Delegates.notNull<GameScreen>()

class GameScreen : Screen {

    companion object {
        const val TILESIZE = 16f
    }

    private val tempScreenPos = vec3()

    val batch = SpriteBatch()
    val hudBatch = SpriteBatch()

    val shapeRenderer = ShapeRenderer()
    var camera = OrthographicCamera(Gdx.graphics.width.toFloat(), Gdx.graphics.height.toFloat()).apply { setToOrtho(false, Gdx.graphics.width / 2f, Gdx.graphics.height / 2f) }
    var viewport = ExtendViewport(1280f, 720f, camera)

    var hudCamera = OrthographicCamera(Gdx.graphics.width.toFloat(), Gdx.graphics.height.toFloat()).apply { setToOrtho(false, Gdx.graphics.width.toFloat(), Gdx.graphics.height.toFloat()) }
    var hudViewport = ScreenViewport(hudCamera)

    val slowZoneRenderer = SlowZoneRenderSystem()
    val explodeOrbRender = ExplodeOrbRenderSystem()
    val dashRender = DashRenderSystem()
    var world = World(WorldConfigurationBuilder().with(
            SpawnSpawnerSystem(),
            SpawnerSpawnSystem(),
            SpawnerLevelSystem(),
            EnemyDamagePlayerSystem(),
            ManaDecaySystem(),
            EnemyAISystem(),
            ManaPickupSystem(),
            WaddleSystem(),

            SlowZoneSystem(),
            DashSystem(),
            ExplodeOrbSystem(),

            CollisionSystem(),
            VelocitySystem(),
            BulletHitSystem(),
            BulletDeleteSystem(),
            EnemyDeathSystem(),
            CameraFollowSystem(),
            LevelUpSystem(),
            HealthDamageSystem(),
            GameOverSystem(),

            SpawnerRenderSystem(),
            slowZoneRenderer,
            explodeOrbRender,
            dashRender,
            RenderSystem(),
            BulletRenderSystem(),
            SpawnerLevelRenderSystem(),
            PlayerManaRenderSystem(),

            XpUISystem(),
            AbilityUISystem(),
            ManaUISystem()
    ).build())
    var entityFactory = EntityFactory(world)
    var playerInput: PlayerInput

    var playerEntity = -1

    var tilemap = TmxMapLoader().load("Tilemap/untitled.tmx")
    var playerLayer = tilemap.layers["PlayerLayer"] as TiledMapTileLayer
    var tilesLayer = tilemap.layers.first() as TiledMapTileLayer
    var tileRenderer = OrthogonalTiledMapRenderer(tilemap, batch)

    init {
        ResourceManager.init()

        camera.zoom = .5f
        camera.update()
        gameScreen = this
        world.inject(this)
        playerInput = PlayerInput()
        Gdx.input.inputProcessor = playerInput

        spawnLoop@ for (x in 0..playerLayer.width) {
            for (y in 0..playerLayer.height) {
                val cell = playerLayer.getCell(x, y)
                if (cell != null && cell.tile.properties.containsKey("spawn") && cell.tile.properties["spawn", Boolean::class.java]) {
                    playerEntity = entityFactory.createPlayer(vec2(x * TILESIZE + 8, y * TILESIZE))
                    break@spawnLoop
                }
            }
        }

        tilemap.layers.remove(playerLayer)
        entityFactory.createRandomSpawner()
        entityFactory.createRandomSpawner()

//        entityFactory.createRandomSpawner(5, 10f)
//        entityFactory.createRandomSpawner(2, 10f)
//        entityFactory.createRandomSpawner(10, 10f)
    }

    override fun render(dt: Float) {
        clearScreen(0f, 0f, 0f)
        playerInput.update(dt)
        tileRenderer.setView(camera)
        tileRenderer.render()

        viewport.apply()
        batch.projectionMatrix = camera.combined
        shapeRenderer.projectionMatrix = camera.combined
        batch.begin()
        world.process()
        batch.end()
    }

    override fun resize(width: Int, height: Int) {
        viewport.update(width, height)
        hudCamera.position.set(width / 2f, height / 2f, 0f)
        hudViewport.update(width, height)
    }

    override fun dispose() {
        tilemap.dispose()
        batch.dispose()
        world.dispose()
        ResourceManager.dispose()
    }

    override fun hide() {
    }

    override fun show() {
    }

    override fun pause() {
    }

    override fun resume() {
    }

    fun getWorldCoords(screenPos: Vector2): Vector2 {
        tempScreenPos.set(screenPos, 0f)
        val worldPos = camera.unproject(tempScreenPos)
        return vec2(worldPos.x, worldPos.y)
    }

    fun getScreenPosHud(screenPos: Vector2): Vector2 {
        tempScreenPos.set(screenPos, 0f)
        val worldPos = hudCamera.project(tempScreenPos)
        return vec2(worldPos.x, worldPos.y)
    }

}
