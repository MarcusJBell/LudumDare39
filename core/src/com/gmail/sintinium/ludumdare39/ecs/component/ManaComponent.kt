package com.gmail.sintinium.ludumdare39.ecs.component

import com.artemis.Component

class ManaComponent : Component() {

    var currentMana = 0f
    var maxMana = currentMana

    /* If (player) is protected from taking mana damage (using dash ability) */
    var protected = false
    var protectedTime = 0f

}