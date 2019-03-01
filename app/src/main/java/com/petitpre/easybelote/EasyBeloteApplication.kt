package com.petitpre.easybelote

import android.app.Application
import android.content.Context
import androidx.room.Room
import com.petitpre.easybelote.model.AppDatabase
import com.petitpre.easybelote.model.GameRepository

class EasyBeloteApplication : Application() {

    private val database: AppDatabase by lazy {
        Room.databaseBuilder(
            applicationContext,
            AppDatabase::class.java, "easy-belote"
        )
            .fallbackToDestructiveMigration()
            .build()
    }

    val gameRepository: GameRepository by lazy { GameRepository(database.gameDao()) }

}

val Context.easyBelote: EasyBeloteApplication
    get() {
        return this.applicationContext as EasyBeloteApplication
    }