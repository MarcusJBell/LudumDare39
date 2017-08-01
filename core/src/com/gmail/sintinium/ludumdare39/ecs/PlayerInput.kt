package com.gmail.sintinium.ludumdare39.ecs

import com.artemis.ComponentMapper
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Input
import com.badlogic.gdx.Input.Keys
import com.badlogic.gdx.math.MathUtils
import com.gmail.sintinium.ludumdare39.ecs.component.*
import com.gmail.sintinium.ludumdare39.ecs.component.ability.DashComponent
import com.gmail.sintinium.ludumdare39.ecs.component.ability.ExplodeOrbComponent
import com.gmail.sintinium.ludumdare39.screen.gameScreen
import com.gmail.sintinium.ludumdare39.util.level.PlayerHelper
import ktx.app.KtxInputAdapter
import ktx.math.vec2

class PlayerInput : KtxInputAdapter {

    var orbFired = false
    var orbId = -1

    val world = gameScreen.world
    private lateinit var mPosition: ComponentMapper<PositionComponent>
    private lateinit var mTexture: ComponentMapper<TextureComponent>
    private lateinit var mVelocity: ComponentMapper<VelocityComponent>
    private lateinit var mMana: ComponentMapper<ManaComponent>
    private lateinit var mLevel: ComponentMapper<LevelComponent>
    private lateinit var mDash: ComponentMapper<DashComponent>
    private lateinit var mOrb: ComponentMapper<ExplodeOrbComponent>

    private var selectedAbility: AbilityType? = null

    init {
        world.inject(this)
    }

    override fun touchDown(screenX: Int, screenY: Int, pointer: Int, button: Int): Boolean {
        if (gameScreen.playerEntity == -1) return false
        if (button == Input.Buttons.RIGHT) {
            selectedAbility = null
            resetGhost()
            return false
        }
        if (button != Input.Buttons.LEFT) {
            return false
        }

        val cPosition = mPosition[gameScreen.playerEntity]
        val cMana = mMana[gameScreen.playerEntity]
        val cTexture = mTexture[gameScreen.playerEntity]
        val cLevel = mLevel[gameScreen.playerEntity]
        val clickPosition = gameScreen.getWorldCoords(vec2(screenX.toFloat(), screenY.toFloat()))
        val angle = MathUtils.radiansToDegrees * MathUtils.atan2(clickPosition.y - cPosition.position.y, clickPosition.x - cPosition.position.x)

        if (selectedAbility == null) {
            cMana.currentMana -= 5f
            gameScreen.entityFactory.createBullet(cPosition.position.cpy().add(0f, cTexture.texture!!.regionHeight / 4f), cLevel.level, angle, 100f)
        } else if (selectedAbility!! == AbilityType.DASH) {
            cMana.currentMana -= 25f
            resetGhost()
            gameScreen.entityFactory.createDash(gameScreen.playerEntity, clickPosition, cLevel.level)
        } else if (selectedAbility!! == AbilityType.ORB && !orbFired) {
            cMana.currentMana -= 45f
            orbId = gameScreen.entityFactory.createExplodeOrb(cPosition.position.cpy().add(0f, cTexture.texture!!.regionHeight / 4f), cLevel.level, angle)
            orbFired = true
            resetGhost()
        } else if (selectedAbility!! == AbilityType.SLOWZONE) {
            cMana.currentMana -= 65f
            resetGhost()
            gameScreen.entityFactory.createSlowZone(clickPosition, cLevel.level)
        }
        selectedAbility = null
        return false
    }

    override fun keyDown(keycode: Int): Boolean {
        if (keycode == Keys.Q || keycode == Keys.E || keycode == Keys.R) {
            resetGhost()
        }
        if (keycode == Keys.Q) {
            selectedAbility = AbilityType.DASH
        } else if (keycode == Keys.E) {
            if (!orbFired) {
                selectedAbility = AbilityType.ORB
            } else if (orbId != -1) {
                mOrb[orbId].shouldExplode = true
            }
        } else if (keycode == Keys.R) {
            selectedAbility = AbilityType.SLOWZONE
        }
        return false
    }

    fun resetGhost() {
        gameScreen.slowZoneRenderer.ghostPosition = null
        gameScreen.explodeOrbRender.ghostPosition = null
        gameScreen.dashRender.ghostPosition = null
    }

    fun update(dt: Float) {
        if (gameScreen.playerEntity == -1) return
        val speed = 150f
        val cVelocity = mVelocity[gameScreen.playerEntity]
        if (!mDash.has(gameScreen.playerEntity)) {
            if (Gdx.input.isKeyPressed(Keys.A)) {
                cVelocity.velocity.sub(speed, 0f)
            } else if (Gdx.input.isKeyPressed(Keys.D)) {
                cVelocity.velocity.add(speed, 0f)
            }

            if (Gdx.input.isKeyPressed(Keys.S)) {
                cVelocity.velocity.sub(0f, speed)
            } else if (Gdx.input.isKeyPressed(Keys.W)) {
                cVelocity.velocity.add(0f, speed)
            }
        }

        val mousePosition = gameScreen.getWorldCoords(vec2(Gdx.input.x.toFloat(), Gdx.input.y.toFloat()))
        when (selectedAbility) {
            AbilityType.SLOWZONE -> gameScreen.slowZoneRenderer.ghostPosition = mousePosition
            AbilityType.ORB -> gameScreen.explodeOrbRender.ghostPosition = mousePosition
            AbilityType.DASH -> gameScreen.dashRender.ghostPosition = mousePosition
        }
    }

    enum class AbilityType {
        DASH, ORB, SLOWZONE
    }

}