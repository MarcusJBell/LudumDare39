package com.gmail.sintinium.ludumdare39.ecs.factory

import com.artemis.ComponentMapper
import com.artemis.World
import com.badlogic.gdx.math.GridPoint2
import com.badlogic.gdx.math.MathUtils
import com.badlogic.gdx.math.Vector2
import com.gmail.sintinium.ludumdare39.ecs.component.*
import com.gmail.sintinium.ludumdare39.ecs.component.ability.DashComponent
import com.gmail.sintinium.ludumdare39.ecs.component.ability.ExplodeOrbComponent
import com.gmail.sintinium.ludumdare39.ecs.component.ability.SlowZoneComponent
import com.gmail.sintinium.ludumdare39.io.ResourceManager
import com.gmail.sintinium.ludumdare39.screen.GameScreen
import com.gmail.sintinium.ludumdare39.screen.gameScreen
import com.gmail.sintinium.ludumdare39.util.level.EnemyHelper
import com.gmail.sintinium.ludumdare39.util.level.PlayerHelper
import com.gmail.sintinium.ludumdare39.util.level.SpawnerHelper
import ktx.math.vec2
import java.util.*

class EntityFactory(var world: World) {

    init {
        world.inject(this)
    }

    private lateinit var mPosition: ComponentMapper<PositionComponent>
    private lateinit var mTexture: ComponentMapper<TextureComponent>
    private lateinit var mVelocity: ComponentMapper<VelocityComponent>
    private lateinit var mCollision: ComponentMapper<CollisionComponent>
    private lateinit var mSpawner: ComponentMapper<SpawnerComponent>
    private lateinit var mHealth: ComponentMapper<HealthComponent>
    private lateinit var mEnemy: ComponentMapper<EnemyComponent>
    private lateinit var mEnemyAI: ComponentMapper<EnemyAIComponent>
    private lateinit var mBullet: ComponentMapper<BulletComponent>
    private lateinit var mMana: ComponentMapper<ManaComponent>
    private lateinit var mPlayer: ComponentMapper<PlayerComponent>
    private lateinit var mLevel: ComponentMapper<LevelComponent>
    private lateinit var mManaGlobe: ComponentMapper<ManaGlobeComponent>
    private lateinit var mWaddle: ComponentMapper<WaddleComponent>

    private lateinit var mOrb: ComponentMapper<ExplodeOrbComponent>
    private lateinit var mSlowZone: ComponentMapper<SlowZoneComponent>
    private lateinit var mDash: ComponentMapper<DashComponent>

    var currentSpawners = arrayListOf<GridPoint2>()

    private val random = Random()

    private val bulletCollider = object : Collidable {
        override fun onCollide(entityId: Int) {
            world.delete(entityId)
        }
    }

    private val orbCollider = object : Collidable {
        override fun onCollide(entityId: Int) {
            mOrb[entityId].shouldExplode = true
        }
    }

    fun createPlayer(position: Vector2): Int {
        val e = world.create()
        val texture = ResourceManager.player
        mPlayer.create(e).also {
            it.nextLevel = PlayerHelper.getNextLevel(1)
        }
        mPosition.create(e).also {
            it.position = position
        }
        mWaddle.create(e)
        mTexture.create(e).also {
            it.texture = texture
        }
        mVelocity.create(e)
        mCollision.create(e).also {
            it.bounds = vec2(texture.regionWidth.toFloat() - 3, texture.regionHeight.toFloat() / 2f)
        }
        mMana.create(e).also {
            it.currentMana = PlayerHelper.getMaxMana(1)
            it.maxMana = it.currentMana
        }
        mLevel.create(e)
        return e
    }

    fun createBullet(startPosition: Vector2, playerLevel: Int, angle: Float, speed: Float): Int {
        val e = world.create()
        val animation = ResourceManager.fireballAnimation
        mTexture.create(e).also {
            it.animation = animation
        }
        mPosition.create(e).also {
            it.position = startPosition.cpy().sub(animation.keyFrames.first().regionWidth / 2f, 0f)
        }
        mVelocity.create(e).also {
            it.velocity = vec2(MathUtils.cos(MathUtils.degreesToRadians * angle) * speed, MathUtils.sin(MathUtils.degreesToRadians * angle) * speed)
            it.resetNextFrame = false
        }
        mBullet.create(e).also {
            it.angle = angle
            it.speed = speed
            it.damage = PlayerHelper.getFireballDamage(playerLevel)
        }
        mCollision.create(e).also {
            it.bounds = vec2(animation.keyFrames.first().regionWidth.toFloat(), animation.keyFrames.first().regionHeight.toFloat())
            it.shouldAdjust = false
            it.collidable = bulletCollider
        }
        return e
    }

    fun createRandomSpawner(): Int {
        val e = world.create()
        val texture = ResourceManager.graveSpawner
        var spawnPoint = GridPoint2()
        mPosition.create(e).also {
            val spawnTile: Vector2
            while (true) {
                val x = random.nextInt(gameScreen.tilesLayer.width)
                val y = random.nextInt(gameScreen.tilesLayer.height)
                val cell = gameScreen.tilesLayer.getCell(x, y)
                if ((cell.tile.properties.containsKey("solid") && !cell.tile.properties["solid", Boolean::class.java]) || !cell.tile.properties.containsKey("solid")) {
                    var nearWall = false
                    wallLoop@ for (tx in -1..1) {
                        for (ty in -1..1) {
                            val tCell = gameScreen.tilesLayer.getCell(x + tx, y + ty)
                            if (tCell.tile.properties.containsKey("solid") && tCell.tile.properties["solid", Boolean::class.java]) {
                                nearWall = true
                                break@wallLoop
                            }
                        }
                    }
                    if (nearWall) {
                        continue
                    }
                    if (containsSpawner(x, y)) {
                        continue
                    }
                    spawnPoint.set(x, y)
                    currentSpawners.add(spawnPoint)
                    spawnTile = vec2(x.toFloat(), y.toFloat())
                    break
                }
            }
            it.position = vec2((spawnTile.x * GameScreen.TILESIZE) + texture.regionWidth / 2f, spawnTile.y * GameScreen.TILESIZE)
        }
        mTexture.create(e).also {
            it.texture = texture
        }
        mCollision.create(e).also {
            it.bounds = vec2(texture.regionWidth.toFloat(), texture.regionHeight.toFloat())
        }
        mHealth.create(e).also {
            it.currentHealth = SpawnerHelper.getHealth(1)
            it.maxHealth = it.currentHealth
        }
        mSpawner.create(e).also {
            it.spawnPoint = spawnPoint
        }
        mLevel.create(e).also {
            it.level = 1
        }
        mEnemy.create(e)
        return e
    }

    fun createZombie(level: Int, spawnerPosition: Vector2): Int {
        val e = world.create()
        val texture = ResourceManager.manaEater.keyFrames.first()
        mPosition.create(e).also {
            it.position = spawnerPosition.cpy()
        }
        mVelocity.create(e)
        mTexture.create(e).also {
            it.animation = ResourceManager.manaEater
        }
        mCollision.create(e).also {
            it.bounds = vec2(texture.regionWidth.toFloat(), texture.regionHeight.toFloat())
        }
        mHealth.create(e).also {
            it.currentHealth = EnemyHelper.getHealth(level)
            it.maxHealth = it.currentHealth
        }
        mEnemy.create(e).also {
            it.damage = EnemyHelper.getDamage(level)
        }
        mEnemyAI.create(e).also {
            it.speed = EnemyHelper.getSpeed(level)
        }
        mLevel.create(e).also {
            it.level = level
        }
        return e
    }

    fun createManaGlobe(position: Vector2, amount: Float): Int {
        val e = world.create()
        val texture = ResourceManager.mana
        mPosition.create(e).also {
            it.position = position.cpy()
        }
        mManaGlobe.create(e).also {
            it.amount = amount
        }
        mVelocity.create(e)
        mTexture.create(e).also {
            it.texture = texture
        }
        return e
    }

    fun createSlowZone(position: Vector2, playerLevel: Int): Int {
        val e = world.create()
        mPosition.create(e).also {
            it.position = position.cpy()
        }
        mSlowZone.create(e).also {
            it.radius = PlayerHelper.getSlowZoneRange(playerLevel)
            it.duration = PlayerHelper.getSlowZoneDuration(playerLevel)
            it.slowAmount = PlayerHelper.getSlowZoneAmount(playerLevel)
        }
        return e
    }

    fun createExplodeOrb(startPosition: Vector2, playerLevel: Int, angle: Float): Int {
        val e = world.create()
        val texture = ResourceManager.explodeOrb
        val speed = 100f
        mOrb.create(e).also {
            it.explodeRadius = PlayerHelper.getOrbRadius(playerLevel)
            it.duration = PlayerHelper.getOrbDuration(playerLevel)
            it.damage = PlayerHelper.getOrbDamage(playerLevel)
        }
        mTexture.create(e).also {
            it.texture = texture
            it.scale = 4f
            it.yOffset = (-texture.regionHeight * it.scale) / 2f
        }
        mPosition.create(e).also {
            it.position = startPosition.cpy()
        }
        mVelocity.create(e).also {
            it.velocity = vec2(MathUtils.cos(MathUtils.degreesToRadians * angle) * speed, MathUtils.sin(MathUtils.degreesToRadians * angle) * speed)
            it.resetNextFrame = false
        }
        mCollision.create(e).also {
            it.bounds = vec2(texture.regionWidth.toFloat(), texture.regionHeight.toFloat())
            it.shouldAdjust = false
            it.collidable = orbCollider
        }
        return e
    }

    fun createDash(playerId: Int, target: Vector2, playerLevel: Int) {
        mDash.create(playerId).also {
            it.damage = PlayerHelper.getDashDamage(playerLevel)
            it.speed = 750f
            it.target = target.cpy()
        }
    }

    fun containsSpawner(x: Int, y: Int) = currentSpawners.any { it.x == x && it.y == y}

}