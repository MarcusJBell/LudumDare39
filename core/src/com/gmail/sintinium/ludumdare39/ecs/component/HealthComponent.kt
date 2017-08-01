package com.gmail.sintinium.ludumdare39.ecs.component

import com.artemis.Component

class HealthComponent : Component() {

    var currentHealth = -1f
    var tickDamage = 0f

    var maxHealth = currentHealth
    var rendered = true

}