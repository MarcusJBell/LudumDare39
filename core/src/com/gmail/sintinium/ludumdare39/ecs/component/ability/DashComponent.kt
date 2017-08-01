package com.gmail.sintinium.ludumdare39.ecs.component.ability

import com.artemis.Component
import com.badlogic.gdx.math.Vector2

class DashComponent : Component() {

    var entitiesHit = arrayListOf<Int>()
    var damage = 1f
    var speed = 500f
    var hitWall = false
    lateinit var target: Vector2

}