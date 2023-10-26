package org.n9ne.h2ohealthy

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import org.n9ne.h2ohealthy.databinding.ActivityMainBinding


class MainActivity : AppCompatActivity() {
    private lateinit var b: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        b = ActivityMainBinding.inflate(layoutInflater)
        setContentView(b.root)
    }
}