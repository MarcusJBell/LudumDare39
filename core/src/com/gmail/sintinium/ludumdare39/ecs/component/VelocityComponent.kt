package com.gmail.sintinium.ludumdare39.ecs.component

import com.artemis.Component
import com.badlogic.gdx.math.MathUtils
import ktx.math.vec2

class VelocityComponent : Component() {

    var velocity = vec2()
    var resetNextFrame = true
    var slowAmount = 1f // Percent slowed 0=stop 1=none
    get
    set(value) {
        field = MathUtils.clamp(value, 0f, 1f)
    }

}