package com.broood.tictactoe

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.view.View
import android.widget.Toast
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.activity_multiplayer_game_selector.*

var isCodeMaker = false
var codee: String? = null
var codefound = false
var checkTemp = true
var keyValue : String? = null
class MultiplayerGameSelector : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_multiplayer_game_selector)
        create.setOnClickListener{
            codee = null
            codefound = false
            checkTemp = true
            keyValue = null
            codee = code.text.toString()
            create.visibility = View.GONE
            join.visibility = View.GONE
            headTV.visibility = View.GONE
            code.visibility = View.GONE
            idPBLoad.visibility = View.VISIBLE
            if(codee!=null && codee!=""){
                isCodeMaker = true
                FirebaseDatabase.getInstance().reference.child("codes").addValueEventListener(object : ValueEventListener{
                    override fun onDataChange(snapshot: DataSnapshot) {
                        var check = isValueAvailable(snapshot, codee!!)
                        Handler().postDelayed({
                            if(check == true){
                                create.visibility = View.VISIBLE
                                join.visibility = View.VISIBLE
                                headTV.visibility = View.VISIBLE
                                code.visibility = View.VISIBLE
                                idPBLoad.visibility = View.GONE
                            }else{
                                FirebaseDatabase.getInstance().reference.child("codes").push().setValue(codee)
                                isValueAvailable(snapshot, codee!!)
                                checkTemp = false
                                Handler().postDelayed({
                                    accepted()
                                    Toast.makeText(this@MultiplayerGameSelector, "Please don't go back", Toast.LENGTH_SHORT).show()
                                }, 300)
                            }
                        }, 2000)
                    }

                    override fun onCancelled(p0: DatabaseError) {

                    }
                })
            }else{
                create.visibility = View.VISIBLE
                join.visibility = View.VISIBLE
                headTV.visibility = View.VISIBLE
                code.visibility = View.VISIBLE
                idPBLoad.visibility = View.GONE
                Toast.makeText(this@MultiplayerGameSelector, "Enter a valid code", Toast.LENGTH_SHORT).show()
            }
        }
        join.setOnClickListener{
            isCodeMaker = false
            codee = null
            codefound = false
            checkTemp = true
            keyValue = null
            codee = code.text.toString()
            if(codee!=null && codee!= ""){
                create.visibility = View.GONE
                join.visibility = View.GONE
                headTV.visibility = View.GONE
                code.visibility = View.GONE
                idPBLoad.visibility = View.VISIBLE
                isCodeMaker = false
                FirebaseDatabase.getInstance().reference.child("codes").addValueEventListener(object : ValueEventListener{
                    override fun onDataChange(snapshot: DataSnapshot) {
                        var data: Boolean = isValueAvailable(snapshot, codee!!)
                        Handler().postDelayed({
                            if(data==true){
                                codefound = true
                                accepted()
                                create.visibility = View.VISIBLE
                                join.visibility = View.VISIBLE
                                headTV.visibility = View.VISIBLE
                                code.visibility = View.VISIBLE
                                idPBLoad.visibility = View.GONE
                            }else{
                                create.visibility = View.VISIBLE
                                join.visibility = View.VISIBLE
                                headTV.visibility = View.VISIBLE
                                code.visibility = View.VISIBLE
                                idPBLoad.visibility = View.GONE
                                Toast.makeText(this@MultiplayerGameSelector, "invalid code", Toast.LENGTH_SHORT).show()
                            }
                        }, 2000)
                    }

                    override fun onCancelled(p0: DatabaseError) {

                    }
                })
            }else{
                Toast.makeText(this, "enter a valid code", Toast.LENGTH_SHORT).show()
            }
        }
    }
    fun accepted(){
        startActivity(Intent(this, OnlineMultiplayerGameActivity::class.java))
        create.visibility = View.VISIBLE
        join.visibility = View.VISIBLE
        headTV.visibility = View.VISIBLE
        code.visibility = View.VISIBLE
        idPBLoad.visibility = View.GONE
    }
    fun isValueAvailable(snapshot: DataSnapshot, code: String): Boolean{
        var data = snapshot.children
        data.forEach {
            var value = it.getValue().toString()
            if(value == code){
                keyValue = it.key.toString()
                return true
            }
        }
        return false
    }
}