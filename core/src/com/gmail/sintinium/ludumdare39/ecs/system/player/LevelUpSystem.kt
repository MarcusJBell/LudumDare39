package com.gmail.sintinium.ludumdare39.ecs.system.player

import com.artemis.Aspect
import com.artemis.ComponentMapper
import com.artemis.systems.IteratingSystem
import com.gmail.sintinium.ludumdare39.ecs.component.LevelComponent
import com.gmail.sintinium.ludumdare39.ecs.component.ManaComponent
import com.gmail.sintinium.ludumdare39.ecs.component.PlayerComponent
import com.gmail.sintinium.ludumdare39.util.level.PlayerHelper

class LevelUpSystem : IteratingSystem(Aspect.all(PlayerComponent::class.java, LevelComponent::class.java, ManaComponent::class.java)) {

    private lateinit var mPlayer: ComponentMapper<PlayerComponent>
    private lateinit var mLevel: ComponentMapper<LevelComponent>
    private lateinit var mMana: ComponentMapper<ManaComponent>

    override fun process(entityId: Int) {
        val cPlayer = mPlayer[entityId]
        val cLevel = mLevel[entityId]
        val cMana = mMana[entityId]

        if (cPlayer.xp >= cPlayer.nextLevel) {
            cLevel.level++
            cPlayer.nextLevel = PlayerHelper.getNextLevel(cLevel.level)
            cMana.maxMana = PlayerHelper.getMaxMana(cLevel.level)
            cPlayer.xp = 0f
            //TODO: Add sound effect/text to notify
        }
    }

}