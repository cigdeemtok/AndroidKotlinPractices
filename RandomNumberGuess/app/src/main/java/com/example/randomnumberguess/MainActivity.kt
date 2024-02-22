package com.example.randomnumberguess

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.example.randomnumberguess.R
import kotlin.random.Random

class MainActivity : AppCompatActivity() {
    private lateinit var inputEditText : EditText
    private lateinit var guessBtn : Button
    private lateinit var msgTextView : TextView
    private lateinit var tryAgainBtn : Button
    private lateinit var hakSayisi : TextView

    var randomNum = 0
    var sayac = 5


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //view lari tanimla
        inputEditText = findViewById(R.id.inputEditText)
        guessBtn = findViewById(R.id.guessBtn)
        msgTextView = findViewById(R.id.msgTextView)


        tryAgainBtn = findViewById(R.id.tryAgainBtn)
        hakSayisi = findViewById(R.id.hakSayisi)

        randomNum = randomUret()

        hakSayisi.text = "Hak Sayisi: $sayac"


        guessBtn.setOnClickListener {

            val tahmin = inputEditText.text.toString().toInt()

            sayac -=1

            hakSayisi.text = "Hak Sayisi: $sayac"

            //kac deneme kaldi kontrolu
            if(sayac == 0){
                msgTextView.text = "Tahmin edemediniz, tekrar deneyiniz!"
                tryAgainBtn.visibility = View.VISIBLE
                guessBtn.setClickable(false)
                sayac = 5
                Toast.makeText(this, "Random sayı : $randomNum", Toast.LENGTH_LONG).show()
            }
            //sayi tahmin kontrolu
            else if (tahmin == randomNum) {
                msgTextView.text = "Tebrikler!"
                tryAgainBtn.visibility = View.VISIBLE
            }
            else if (tahmin < randomNum) {
                msgTextView.text = "Daha büyük bir sayı girin."

            }
            else{
                msgTextView.text = "Daha küçük bir sayi girin."
            }

            inputEditText.getText().clear()
        }

        tryAgainBtn.setOnClickListener {
            tryAgainBtn.visibility = View.INVISIBLE
            clear()
            randomNum = randomUret()

        }
    }
    fun clear(){
        guessBtn.setClickable(true)
        sayac = 5
        inputEditText.getText().clear()
        msgTextView.text = ""
        hakSayisi.text = "Hak Sayisi: $sayac"
    }

    fun randomUret() : Int{
        var sayi = Random.nextInt(101) //random sayi uret
        Log.i("Sayi", sayi.toString()) // tahmin kontrolu icin log da goster
        return sayi
    }

}