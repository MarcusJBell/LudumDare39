package com.gmail.sintinium.ludumdare39.ecs.component

import com.artemis.Component
import com.badlogic.gdx.graphics.g2d.Animation
import com.badlogic.gdx.graphics.g2d.TextureRegion

class TextureComponent : Component() {
    var texture: TextureRegion? = null
    var animation: Animation<TextureRegion>? = null
    var animationDelta = 0f
    var xOffset = 0f
    var yOffset = 0f
    var rotation = 0f
    var scale = 1f
}