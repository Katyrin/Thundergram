package com.katyrin.thundergram.model.storage

interface Storage {
    fun getLogged(): Boolean
    fun setLogged(isLogged: Boolean)
}