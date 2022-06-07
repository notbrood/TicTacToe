package com.broood.tictactoe

import android.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.view.View
import android.widget.Toast
import androidx.appcompat.widget.AppCompatButton
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.activity_game_play.*
import kotlinx.android.synthetic.main.activity_game_play.btn1
import kotlinx.android.synthetic.main.activity_game_play.btn2
import kotlinx.android.synthetic.main.activity_game_play.btn3
import kotlinx.android.synthetic.main.activity_game_play.btn4
import kotlinx.android.synthetic.main.activity_game_play.btn5
import kotlinx.android.synthetic.main.activity_game_play.btn6
import kotlinx.android.synthetic.main.activity_game_play.btn7
import kotlinx.android.synthetic.main.activity_game_play.btn8
import kotlinx.android.synthetic.main.activity_game_play.btn9
import kotlinx.android.synthetic.main.activity_game_play.reset
import kotlinx.android.synthetic.main.activity_game_play.score1
import kotlinx.android.synthetic.main.activity_game_play.score2
import kotlinx.android.synthetic.main.activity_online_multiplayer_game.*
import kotlin.system.exitProcess

var isMyTurn = isCodeMaker
class OnlineMultiplayerGameActivity : AppCompatActivity() {
    var player1count = 0
    var player2count = 0
    var player1 = ArrayList<Int>()
    var player2 = ArrayList<Int>()
    var emptyCells = ArrayList<Int>()
    var activeUser = 1
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_online_multiplayer_game)
        reset.setOnClickListener {
            resetBoard()
        }
        FirebaseDatabase.getInstance().reference.child("data").child(codee!!).addChildEventListener(object:ChildEventListener{
            override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {
                var data = snapshot.value
                if(isMyTurn == true){
                    isMyTurn = false
                    moveOnline(data.toString(), isMyTurn)
                }else{
                    isMyTurn = true
                    moveOnline(data.toString(), isMyTurn)
                }
            }

            override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) {
                TODO("Not yet implemented")
            }

            override fun onChildRemoved(snapshot: DataSnapshot) {
                resetBoard()
                Toast.makeText(this@OnlineMultiplayerGameActivity, "GameReset", Toast.LENGTH_SHORT).show()
            }

            override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) {
                TODO("Not yet implemented")
            }

            override fun onCancelled(snapshot: DatabaseError) {
                TODO("Not yet implemented")
            }

        })
    }

    fun moveOnline(data: String, myTurn: Boolean) {
        if(myTurn){
            var but : AppCompatButton?
            but = when(data.toInt()){
                1 -> btn1
                2 -> btn2
                3 -> btn3
                4 -> btn4
                5 -> btn5
                6 -> btn6
                7 -> btn7
                8 -> btn8
                9 -> btn9
                else -> {
                    btn1
                }
            }
            but.setText("O")
            tvTurn.setText("Turn : Player 1")
            player2.add(data.toInt())
            emptyCells.add(data.toInt())
            Handler().postDelayed(Runnable {  }, 200)
            but.isEnabled = false
            checkWinner()
        }

    }

    private fun resetBoard() {
        player1.clear()
        player2.clear()
        activeUser = 1
        emptyCells.clear()
        for(i in 1..9){
            var buttonSelected : androidx.appcompat.widget.AppCompatButton?
            buttonSelected = when(i){
                1 -> btn1
                2 -> btn2
                3 -> btn3
                4 -> btn4
                5 -> btn5
                6 -> btn6
                7 -> btn7
                8 -> btn8
                9 -> btn9
                else -> {
                    btn1
                }
            }
            buttonSelected.isEnabled = true
            buttonSelected.text = ""
            score1.setText("$player1count")
            score2.setText("$player2count")
            isMyTurn = isCodeMaker
            if(isCodeMaker){
                FirebaseDatabase.getInstance().reference.child("data").child(codee!!).removeValue()
            }
        }
    }

    fun buttonClick(v: View){
        if(isMyTurn){
            val but = v as AppCompatButton
            var cellID = 0
            when(but.id){
                R.id.btn1 -> cellID = 1
                R.id.btn2 -> cellID = 2
                R.id.btn3 -> cellID = 3
                R.id.btn4 -> cellID = 4
                R.id.btn5 -> cellID = 5
                R.id.btn6 -> cellID = 6
                R.id.btn7 -> cellID = 7
                R.id.btn8 -> cellID = 8
                R.id.btn9 -> cellID = 9
            }
            playerTurn = false
            Handler().postDelayed(Runnable { playerTurn = true }, 600)
            playNow(but, cellID)
            updateDatabase(cellID)
        }
    }

    private fun playNow(but: AppCompatButton, cellID: Int) {
            emptyCells.add(cellID)
            but.setText("X")
            tvTurn.setText("Turn: Player 2")
            player1.add(cellID)
            but.isEnabled = false
            Handler().postDelayed(Runnable{}, 200)
            checkWinner()
        }

    private fun checkWinner(): Int {
        if ((player1.contains(1) && player1.contains(2) && player1.contains(3)) || (player1.contains(1)
                    && player1.contains(4) && player1.contains(7)) ||
            (player1.contains(3) && player1.contains(6) && player1.contains(9)) || (player1.contains(
                7
            ) && player1.contains(8) && player1.contains(9)) ||
            (player1.contains(4) && player1.contains(5) && player1.contains(6)) || (player1.contains(
                1
            ) && player1.contains(5) && player1.contains(9)) ||
            player1.contains(3) && player1.contains(5) && player1.contains(7) || (player1.contains(2) && player1.contains(
                5
            ) && player1.contains(8))
        ) {
            player1count += 1
            buttonDisable()
            disableReset()
            val build = AlertDialog.Builder(this)
            build.setTitle("Game Over")
            build.setMessage("Player 1 has won the game.."+"\n\n"+"Do you want to play again")
            build.setPositiveButton("Ok") { dialog, which ->
                resetBoard()
            }
            build.setNegativeButton("Exit") { dialog, which ->
                removeCode()
                exitProcess(1)

            }
            Handler().postDelayed(Runnable { build.show() }, 500)
            return 1


        } else if ((player2.contains(1) && player2.contains(2) && player2.contains(3)) || (player2.contains(
                1
            ) && player2.contains(4) && player2.contains(7)) ||
            (player2.contains(3) && player2.contains(6) && player2.contains(9)) || (player2.contains(
                7
            ) && player2.contains(8) && player2.contains(9)) ||
            (player2.contains(4) && player2.contains(5) && player2.contains(6)) || (player2.contains(
                1
            ) && player2.contains(5) && player2.contains(9)) ||
            player2.contains(3) && player2.contains(5) && player2.contains(7) || (player2.contains(2) && player2.contains(
                5
            ) && player2.contains(8))
        ) {
            player2count += 1
            buttonDisable()
            disableReset()
            val build = AlertDialog.Builder(this)
            build.setTitle("Game Over")
            build.setMessage("Player 2 has won the game"+"\n\n"+"Do you want to play again")
            build.setPositiveButton("Ok") { dialog, which ->
                resetBoard()
            }
            build.setNegativeButton("Exit") { dialog, which ->
                removeCode()
                exitProcess(1)
            }
            Handler().postDelayed(Runnable { build.show() }, 500)
            return 1
        } else if (emptyCells.contains(1) && emptyCells.contains(2) && emptyCells.contains(3) && emptyCells.contains(
                4
            ) && emptyCells.contains(5) && emptyCells.contains(6) && emptyCells.contains(7) &&
            emptyCells.contains(8) && emptyCells.contains(9)
        ) {

            val build = AlertDialog.Builder(this)
            build.setTitle("Game Draw")
            build.setMessage("Nobody Wins" + "\n\n" + "Do you want to play again")
            build.setPositiveButton("Ok") { dialog, which ->
                resetBoard()
            }
            build.setNegativeButton("Exit") { dialog, which ->
                exitProcess(1)
                removeCode()
            }
            build.show()
            return 1

        }
        return 0
    }

    fun removeCode(){
        if(isCodeMaker){
            FirebaseDatabase.getInstance().reference.child("codes").child(keyValue!!).removeValue()
        }
    }

    fun buttonDisable() {
        for (i in 1..9) {
            val buttonSelected = when (i) {
                1 -> btn1
                2 -> btn2
                3 -> btn3
                4 -> btn4
                5 -> btn5
                6 -> btn6
                7 -> btn7
                8 -> btn8
                9 -> btn9
                else -> {
                    btn1
                }

            }
            if (buttonSelected.isEnabled == true)
                buttonSelected.isEnabled = false
        }
    }

    fun disableReset() {
        reset.isEnabled = false
        Handler().postDelayed(Runnable { reset.isEnabled = true }, 2000)
    }

    override fun onBackPressed() {
        removeCode()
        if(isCodeMaker){
            FirebaseDatabase.getInstance().reference.child("data").child(codee!!).removeValue()
        }
        exitProcess(0)
    }

    fun updateDatabase(cellID:Int){
        FirebaseDatabase.getInstance().reference.child("data").child(codee!!).push().setValue(cellID)
    }


}