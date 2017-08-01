package com.gmail.sintinium.ludumdare39.ecs.component

import com.artemis.Component
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.math.collision.BoundingBox
import ktx.math.vec2

class CollisionComponent : Component() {

    var bounds = vec2()
    var collidable: Collidable? = null
    var shouldAdjust = true

}

interface Collidable {
    fun onCollide(entityId: Int)
}