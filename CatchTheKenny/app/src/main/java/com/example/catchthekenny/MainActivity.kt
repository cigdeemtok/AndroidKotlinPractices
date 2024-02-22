package com.example.catchthekenny

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.CountDownTimer
import androidx.appcompat.app.AlertDialog
import com.example.catchthekenny.databinding.ActivityMainBinding
import kotlin.random.Random

class MainActivity : AppCompatActivity() {

    private lateinit var binding : ActivityMainBinding
    private var score : Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        showAlertDialog()

    }

    fun playGame(){
        //count-down timer
        score = 0
        binding.scoreText.text = "Score: ${score}"
        object : CountDownTimer(15000,400){
            override fun onTick(millisUntilFinished: Long) {
                binding.timeText.text = "Time: ${millisUntilFinished/1000}"

                //move kenny randomly
                val random = Random
                val width = binding.imageLinear.width
                val height = binding.imageLinear.height

                binding.apply {
                    imageKenny.x = random.nextInt(width-binding.imageKenny.width).toFloat()
                    imageKenny.y = random.nextInt(height-binding.imageKenny.height).toFloat()

                }

                binding.imageKenny.setOnClickListener {
                    //increase score every time kenny get clicked
                    score +=1
                    binding.scoreText.text = "Score: ${score}"


                }
            }

            override fun onFinish() {
                showAlertDialog()
            }

        }.start()
    }

    fun showAlertDialog(){

        //alert dialog to restart
        val builder : AlertDialog.Builder = AlertDialog.Builder(this@MainActivity)
        builder.setTitle("Do you want to restart?")
            .setPositiveButton("YES") { dialog, which ->
                //restart the game
                playGame()
            }
            .setNegativeButton("NO") { dialog, which ->
                //cancel
                finish()
            }
            .setCancelable(false)
        val alertDialog : AlertDialog = builder.create()
        alertDialog.show()
    }
}