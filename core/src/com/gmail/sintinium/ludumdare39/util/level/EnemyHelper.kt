package com.gmail.sintinium.ludumdare39.util.level

object EnemyHelper {

    fun getHealth(enemyLevel: Int): Float {
        return 50 + (1f * Math.pow(enemyLevel.toDouble(), 2.0).toFloat()) + (3 * enemyLevel)
    }

    fun getDamage(enemyLevel: Int): Float {
        return 25f + 3f * enemyLevel
    }

    fun getSpeed(enemyLevel: Int): Float {
        return 100f
    }

    fun getManaDropAmount(enemyLevel: Int): Float {
        return 10f + enemyLevel
    }

    fun getXpDropAmount(enemyLevel: Int): Float {
        return enemyLevel * 10f + 100f
    }

}