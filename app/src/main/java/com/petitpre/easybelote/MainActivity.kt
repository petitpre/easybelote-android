package com.petitpre.easybelote

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.fragment.NavHostFragment
import com.petitpre.easybelote.ui.StartFragment

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
