package com.gmail.sintinium.ludumdare39.util.level

object SpawnerHelper {

    fun getHealth(spawnerLevel: Int): Float {
        return 50 + 2.5f * pow(spawnerLevel.toFloat(), 2f) + 4 * spawnerLevel.toFloat()
    }

    fun getCooldown(spawnerLevel: Int): Float {
        return Math.max(1.5f, -sqrt(.2f * spawnerLevel) + 3f)
    }

    fun getEnemiesToNextLevel(spawnerLevel: Int): Int {
        return (sqrt(25f * spawnerLevel) + 10f).toInt()
    }

    fun getManaDropAmount(spawnerLevel: Int): Float {
        return spawnerLevel * 15f + 100f
    }

}