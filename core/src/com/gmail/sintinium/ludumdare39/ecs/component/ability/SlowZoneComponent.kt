package com.gmail.sintinium.ludumdare39.ecs.component.ability

import com.artemis.Component

class SlowZoneComponent : Component() {

    /*
    Duration is in seconds
     */
    var timeAlive = 0f
    var duration = 0f
    var radius = 0f
    var slowAmount = 1f /*0=stop 1=no-slow*/

}