package com.katyrin.thundergram.view.main

interface ToolBarMotionListener {
    fun onChangeSceneTransitionSwipe(dragDirection: Int)
    fun onSetToolBarText(text: String)
}