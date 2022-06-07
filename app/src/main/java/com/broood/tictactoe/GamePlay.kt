package com.broood.tictactoe

import android.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.view.View
import androidx.appcompat.widget.AppCompatButton
import kotlinx.android.synthetic.main.activity_game_play.*
import kotlin.system.exitProcess

var playerTurn = true
class GamePlay : AppCompatActivity() {
    var player1count = 0
    var player2count = 0
    var player1 = ArrayList<Int>()
    var player2 = ArrayList<Int>()
    var emptyCells = ArrayList<Int>()
    var activeUser = 1
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_game_play)
        reset.setOnClickListener {
            resetBoard()
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
        }
    }

    fun buttonClick(v: View){
        if(playerTurn){
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
        }
    }

    private fun playNow(but: AppCompatButton, cellID: Int) {
        if(activeUser == 1){
            emptyCells.add(cellID)
            but.setText("X")
            player1.add(cellID)
            but.isEnabled = false
            val checkWinner = checkWinner()
            if(checkWinner == 1){
                Handler().postDelayed(Runnable { resetBoard() }, 200)
            }
            else if(singlePlayer){
                Handler().postDelayed(Runnable{robot()}, 500)
            }
            else{
                activeUser = 2
            }
        }
        else{
            but.setText("0")
            emptyCells.add(cellID)
            activeUser = 1
            player2.add(cellID)
            but.isEnabled = false
            val checkWinner = checkWinner()
            if(checkWinner == 1){
                Handler().postDelayed(Runnable { resetBoard() }, 200)
            }
        }
    }

    private fun robot() {
        val rnd = (1..9).random()
        if(emptyCells.contains(rnd))
            robot()
        else {
            val buttonselected : AppCompatButton?
            buttonselected = when(rnd) {
                1 -> btn1
                2 -> btn2
                3 -> btn3
                4 -> btn4
                5 -> btn5
                6 -> btn6
                7 -> btn7
                8 -> btn8
                9 -> btn9
                else -> {btn1}
            }
            emptyCells.add(rnd);
            buttonselected?.text = "O"
            player2.add(rnd)
            if (buttonselected != null) {
                buttonselected.isEnabled = false
            }
            var checkWinner = checkWinner()
            if(checkWinner == 1)
                Handler().postDelayed(Runnable { resetBoard() } , 500)
        }
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
        build.setMessage("PLayer 1 hsa won the game.."+"\n\n"+"Do you want to play again")
        build.setPositiveButton("Ok") { dialog, which ->
            resetBoard()
        }
        build.setNegativeButton("Exit") { dialog, which ->
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
        }
        build.show()
        return 1

    }
        return 0
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
        Handler().postDelayed(Runnable { reset.isEnabled = true }, 500)
    }
}