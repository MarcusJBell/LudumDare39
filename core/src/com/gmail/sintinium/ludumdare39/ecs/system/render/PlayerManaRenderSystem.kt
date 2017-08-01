package com.gmail.sintinium.ludumdare39.ecs.system.render

import com.artemis.BaseSystem
import com.artemis.ComponentMapper
import com.badlogic.gdx.graphics.Color
import com.gmail.sintinium.ludumdare39.ecs.component.ManaComponent
import com.gmail.sintinium.ludumdare39.io.ResourceManager
import com.gmail.sintinium.ludumdare39.screen.gameScreen

class PlayerManaRenderSystem : BaseSystem() {

    private lateinit var mMana: ComponentMapper<ManaComponent>

    override fun processSystem() {
//        if (gameScreen.playerEntity == -1) return
//        val cMana = mMana[gameScreen.playerEntity]
//
//        gameScreen.batch.color = Color.CYAN
//        gameScreen.batch.draw(ResourceManager.debug, 0f, gameScreen.hudViewport.screenHeight - 40f, gameScreen.hudViewport.screenWidth * (cMana.currentMana / cMana.maxMana), 40f)
//        val manaString = "${cMana.currentMana.toInt()}/${cMana.maxMana.toInt()}"
//        val layout = ResourceManager.mainFontLayout
//        layout.setText(ResourceManager.mainFont, manaString)
//        ResourceManager.mainFont.color = Color.RED
//        ResourceManager.mainFont.draw(gameScreen.batch, layout, gameScreen.hudViewport.screenWidth / 2f - layout.width / 2f, gameScreen.hudViewport.screenHeight - 20f + layout.height / 2f)
//
////        ResourceManager.mainFont.color = Color.WHITE
//        gameScreen.batch.color = Color.WHITE
    }

}