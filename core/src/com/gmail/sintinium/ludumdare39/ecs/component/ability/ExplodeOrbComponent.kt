package com.gmail.sintinium.ludumdare39.ecs.component.ability

import com.artemis.Component

class ExplodeOrbComponent : Component() {

    var damage = 0f
    var timeAlive = 0f
    var duration = 0f
    var explodeRadius = 0f

    var shouldExplode = false
}