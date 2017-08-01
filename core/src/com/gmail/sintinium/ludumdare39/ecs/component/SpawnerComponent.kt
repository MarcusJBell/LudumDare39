package com.gmail.sintinium.ludumdare39.ecs.component

import com.artemis.Component
import com.badlogic.gdx.math.GridPoint2

class SpawnerComponent : Component() {

    var enemiesSpawned = 0
    var timeSinceLastSpawn = 0f
    var flameDelta = 0f
    var flameRotation = 0f

    lateinit var spawnPoint: GridPoint2

}