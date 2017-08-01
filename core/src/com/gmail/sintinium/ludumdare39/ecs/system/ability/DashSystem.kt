package com.gmail.sintinium.ludumdare39.ecs.system.ability

import com.artemis.Aspect
import com.artemis.BaseSystem
import com.artemis.ComponentMapper
import com.artemis.utils.IntBag
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.GL20
import com.badlogic.gdx.graphics.glutils.ShapeRenderer
import com.badlogic.gdx.math.Intersector
import com.badlogic.gdx.math.MathUtils
import com.badlogic.gdx.math.Polygon
import com.badlogic.gdx.math.Rectangle
import com.gmail.sintinium.ludumdare39.ecs.component.*
import com.gmail.sintinium.ludumdare39.ecs.component.ability.DashComponent
import com.gmail.sintinium.ludumdare39.screen.GameScreen
import com.gmail.sintinium.ludumdare39.screen.gameScreen
import com.gmail.sintinium.ludumdare39.util.level.PlayerHelper
import ktx.math.times
import ktx.math.vec2

class DashSystem : BaseSystem() {

    private lateinit var mPosition: ComponentMapper<PositionComponent>
    private lateinit var mLevel: ComponentMapper<LevelComponent>
    private lateinit var mVelocity: ComponentMapper<VelocityComponent>
    private lateinit var mDash: ComponentMapper<DashComponent>
    private lateinit var mMana: ComponentMapper<ManaComponent>

    private lateinit var mHealth: ComponentMapper<HealthComponent>
    private lateinit var mCollision: ComponentMapper<CollisionComponent>

    private lateinit var enemies: IntBag
    private var tempRect = Rectangle()
    private var tempPoly = Polygon()
    var polys = arrayListOf<Polygon>()
    private var start = false

    override fun begin() {
        enemies = world.aspectSubscriptionManager[Aspect.all(EnemyComponent::class.java, PositionComponent::class.java)].entities
    }

    override fun processSystem() {
        val playerId = gameScreen.playerEntity
        if (playerId == -1 || !mDash.has(playerId)) return
        val cLevel = mLevel[playerId]
        val cDash = mDash[playerId]
        val cMana = mMana[playerId]
        val cPosition = mPosition[playerId]

        val cVelocity = mVelocity[playerId]
        val dir = vec2(cDash.target.x - cPosition.position.x, cDash.target.y - cPosition.position.y).nor()

        val dashDamage = PlayerHelper.getDashDamage(cLevel.level)
        for (i in 0..enemies.size() - 1) {
            val enemyId = enemies[i]
            val ePosition = mPosition[enemyId]
            val eHealth = mHealth[enemyId]
            val eCollision = mCollision[enemyId]
            if (cDash.entitiesHit.contains(enemyId)) {
                continue
            }
            tempRect.set(ePosition.position.x, ePosition.position.y, eCollision.bounds.x, eCollision.bounds.y)
            val poly = rectToPoly(tempRect)

            val collides = Intersector.intersectSegmentPolygon(cDash.target, cPosition.position, poly)
            if (collides) {
                cDash.entitiesHit.add(enemyId)
                eHealth.tickDamage += dashDamage
            }
        }

        if (cDash.hitWall) {
            mDash.remove(playerId)
            cMana.protected = false
            return
        }
        cMana.protected = true
        cVelocity.velocity.add(dir * cDash.speed)
        if (cDash.target.dst(cPosition.position) < 1f) {
            mDash.remove(playerId)
            cMana.protected = false
            return
        }
    }

    private fun rectToPoly(rectangle: Rectangle): Polygon {
        tempPoly.vertices = arrayOf(
                rectangle.x - rectangle.width / 2f, rectangle.y,
                rectangle.x + rectangle.width / 2f, rectangle.y,
                rectangle.x + rectangle.width / 2f, rectangle.y + rectangle.height,
                rectangle.x - rectangle.width / 2f, rectangle.y + rectangle.height).toFloatArray()
//        tempPoly.setPosition(rectangle.x, rectangle.y)
        start = true
        return tempPoly
    }

}