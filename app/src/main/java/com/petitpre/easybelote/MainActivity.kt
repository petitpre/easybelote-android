package com.petitpre.easybelote

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.NavHostFragment

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val finalHost = NavHostFragment.create(R.navigation.easy_belote)
        supportFragmentManager.beginTransaction()
            .replace(R.id.content, finalHost)
            .setPrimaryNavigationFragment(finalHost)
            .commit()
    }


}
