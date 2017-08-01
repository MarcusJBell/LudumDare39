package com.gmail.sintinium.ludumdare39.util.level

import com.badlogic.gdx.math.MathUtils

object PlayerHelper {

    fun getNextLevel(playerLevel: Int): Float {
        return (20 * Math.sqrt(750.0 * playerLevel) + 1000).toFloat()
    }

    fun getMaxMana(playerLevel: Int): Float {
        return 400f + 4.5f * playerLevel
    }

    fun getManaDecay(playerLevel: Int): Float {
        return 1f + Math.sqrt(playerLevel / 6.0).toFloat()
    }

    fun getFireballDamage(playerLevel: Int): Float {
        return 50f + .01f * Math.pow(playerLevel.toDouble(), 2.0).toFloat()
    }

    fun getDashDamage(playerLevel: Int): Float {
        return 75f + (.04f * Math.pow(playerLevel.toDouble(), 2.0).toFloat())
    }

    fun getSlowZoneRange(playerLevel: Int): Float {
        return 32f + sqrt(32.0f * playerLevel)
    }

    fun getSlowZoneDuration(playerLevel: Int): Float {
        return 10f + sqrt(15.0f * playerLevel)
    }

    fun getSlowZoneAmount(playerLevel: Int): Float {
        val amount = -sqrt(.02f * playerLevel) + .75f
        if (amount < .3f) {
            return .3f
        }
        return amount
    }

    fun getOrbDuration(playerLevel: Int): Float {
        return sqrt(.05f * playerLevel) + 1
    }

    fun getOrbRadius(playerLevel: Int): Float {
        return 32 + sqrt(30f * playerLevel)
    }

    fun getOrbDamage(playerLevel: Int): Float {
        return 75f + .4f * pow(playerLevel.toFloat(), 2f)
    }

    fun getTimeUntilNextSpawner(playerLevel: Int): Float {
        return 15f
    }

}

fun sqrt(x: Float): Float {
    return Math.sqrt(x.toDouble()).toFloat()
}

fun pow(x: Float, d: Float): Float {
    return Math.pow(x.toDouble(), d.toDouble()).toFloat()
}