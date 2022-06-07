package com.broood.tictactoe

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_main.*

var singlePlayer = false
class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        multiplayer.setOnClickListener{
            singlePlayer = false
            intent = Intent(this, GamePlay::class.java)
            startActivity(intent)
        }
        singleplayer.setOnClickListener {
            singlePlayer = true
            intent = Intent(this, GamePlay::class.java)
            startActivity(intent)
        }
    }
}