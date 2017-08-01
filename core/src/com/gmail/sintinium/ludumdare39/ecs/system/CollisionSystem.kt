package com.gmail.sintinium.ludumdare39.ecs.system

import com.artemis.Aspect
import com.artemis.ComponentMapper
import com.artemis.systems.IteratingSystem
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.math.GridPoint2
import com.badlogic.gdx.math.MathUtils
import com.badlogic.gdx.math.Rectangle
import com.badlogic.gdx.math.Vector2
import com.gmail.sintinium.ludumdare39.ecs.component.CollisionComponent
import com.gmail.sintinium.ludumdare39.ecs.component.ManaComponent
import com.gmail.sintinium.ludumdare39.ecs.component.PositionComponent
import com.gmail.sintinium.ludumdare39.ecs.component.VelocityComponent
import com.gmail.sintinium.ludumdare39.ecs.component.ability.DashComponent
import com.gmail.sintinium.ludumdare39.screen.GameScreen
import com.gmail.sintinium.ludumdare39.screen.gameScreen
import ktx.math.vec2

class CollisionSystem : IteratingSystem(Aspect.all(PositionComponent::class.java, VelocityComponent::class.java, CollisionComponent::class.java)) {

    private lateinit var mPosition: ComponentMapper<PositionComponent>
    private lateinit var mVelocity: ComponentMapper<VelocityComponent>
    private lateinit var mCollision: ComponentMapper<CollisionComponent>

    private lateinit var mDash: ComponentMapper<DashComponent>

    private val tileSize = GameScreen.TILESIZE
    private val tiles by lazy { gameScreen.tilesLayer }

    private val tempEntityRect = Rectangle()
    private val tempTileRect = Rectangle(0f, 0f, tileSize, tileSize)

    override fun process(entityId: Int) {
        val cVelocity = mVelocity[entityId]
        val cCollision = mCollision[entityId]
        val xCollision = checkCollision(entityId, vec2(cVelocity.velocity.x, 0f))
        val yCollision = checkCollision(entityId, vec2(0f, cVelocity.velocity.y))

        if (cCollision.shouldAdjust) {
            if (xCollision != null) {
                adjustCollision(entityId, xCollision, true)
            }
            if (yCollision != null) {
                adjustCollision(entityId, yCollision, false)
            }
        }

        if (xCollision != null || yCollision != null) {
            cCollision.collidable?.onCollide(entityId)
            if (mDash.has(entityId)) {
                val cPosition = mPosition[entityId]
                val cDash = mDash[entityId]
                if (xCollision != null) {
                    cDash.target.x = cPosition.position.x
                }
                if (yCollision != null) {
                    cDash.target.y = cPosition.position.y
                }
            }
        }
        if (xCollision != null && yCollision != null) {
            if (mDash.has(entityId)) {
                mDash[entityId].hitWall = true
            }
        }
    }

    private fun adjustCollision(entityId: Int, collideTile: GridPoint2, xDir: Boolean) {
        val cVelocity = mVelocity[entityId]
        val cCollision = mCollision[entityId]
        val cPosition = mPosition[entityId]
        val velocity = cVelocity.velocity

        if (xDir) {
            if (velocity.x > 0) {
                cPosition.position.x = (collideTile.x * GameScreen.TILESIZE) - (cCollision.bounds.x / 2f)
            } else if (velocity.x < 0) {
                cPosition.position.x = (collideTile.x * GameScreen.TILESIZE) + GameScreen.TILESIZE + (cCollision.bounds.x / 2f)
            }
            velocity.x = 0f
        } else {
            if (velocity.y > 0) {
                cPosition.position.y = (collideTile.y * GameScreen.TILESIZE) - (cCollision.bounds.y)
            } else if (velocity.y < 0) {
                cPosition.position.y = (collideTile.y * GameScreen.TILESIZE) + GameScreen.TILESIZE
            }
            velocity.y = 0f
        }
    }

    private fun checkCollision(entityId: Int, velocity: Vector2): GridPoint2? {
        val cCollision = mCollision[entityId]
        val cPosition = mPosition[entityId]

        val scaledVelocity = Vector2(velocity).scl(Gdx.graphics.deltaTime)
        val newPosition = Vector2(cPosition.position).add(scaledVelocity)

        val left = MathUtils.floor((newPosition.x - cCollision.bounds.x / 2f) / tileSize) - 1
        val right = MathUtils.floor((newPosition.x + cCollision.bounds.x / 2f) / tileSize) + 1
        val bottom = MathUtils.floor(newPosition.y / tileSize) - 1
        val top = MathUtils.floor((newPosition.y + cCollision.bounds.y) / tileSize) + 1

        tempEntityRect.set(newPosition.x - cCollision.bounds.x / 2f, newPosition.y, cCollision.bounds.x, cCollision.bounds.y)
        for (x in left..right) {
            for (y in bottom..top) {
                val tile = tiles.getCell(x, y)
                tempTileRect.setPosition(x * tileSize, y * tileSize)
                if (tile == null) continue
                if (tempTileRect.overlaps(tempEntityRect)) {
                    if (tile.tile.properties.containsKey("solid") && tile.tile.properties["solid", Boolean::class.java]) {
                        return GridPoint2(x, y)
                    }
                }
            }
        }
        return null
    }

}