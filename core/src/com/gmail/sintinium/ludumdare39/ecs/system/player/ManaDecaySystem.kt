package com.gmail.sintinium.ludumdare39.ecs.system.player

import com.artemis.Aspect
import com.artemis.ComponentMapper
import com.artemis.systems.IteratingSystem
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.math.MathUtils
import com.gmail.sintinium.ludumdare39.ecs.component.LevelComponent
import com.gmail.sintinium.ludumdare39.ecs.component.ManaComponent
import com.gmail.sintinium.ludumdare39.ecs.component.PlayerComponent
import com.gmail.sintinium.ludumdare39.util.level.PlayerHelper

class ManaDecaySystem : IteratingSystem(Aspect.all(PlayerComponent::class.java, LevelComponent::class.java, ManaComponent::class.java)) {

    private lateinit var mPlayer: ComponentMapper<PlayerComponent>
    private lateinit var mLevel: ComponentMapper<LevelComponent>
    private lateinit var mMana: ComponentMapper<ManaComponent>

    override fun process(entityId: Int) {
        val cMana = mMana[entityId]
        val cLevel = mLevel[entityId]
        cMana.currentMana -= PlayerHelper.getManaDecay(cLevel.level) * Gdx.graphics.deltaTime
        if (cMana.protected) {
            cMana.protectedTime += Gdx.graphics.deltaTime
        }
        if (cMana.protected && cMana.protectedTime > 1f) {
            cMana.protected = false
            cMana.protectedTime = 0f
        }
        if (cMana.currentMana < 0) {
            cMana.currentMana = 0f
        }
    }
}