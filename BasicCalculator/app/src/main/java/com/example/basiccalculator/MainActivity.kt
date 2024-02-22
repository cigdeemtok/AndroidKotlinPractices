package com.example.basiccalculator

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.basiccalculator.databinding.ActivityMainBinding
import java.math.RoundingMode

class MainActivity : AppCompatActivity() {
    private lateinit var binding : ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)


        binding.sumButton.setOnClickListener {
            val numOne = binding.inputOne.text.toString()
            val numTwo = binding.inputTwo.text.toString()
            val res = numOne.toInt() + numTwo.toInt()
            binding.resultText.text = res.toString()
        }
        binding.subtractButton.setOnClickListener {
            val numOne = binding.inputOne.text.toString()
            val numTwo = binding.inputTwo.text.toString()

            val res = numOne.toInt() - numTwo.toInt()
            binding.resultText.text = res.toString()
        }
        binding.multiplyButton.setOnClickListener {
            val numOne = binding.inputOne.text.toString()
            val numTwo = binding.inputTwo.text.toString()

            val res = numOne.toInt() * numTwo.toInt()
            binding.resultText.text = res.toString()
        }
        binding.divideButton.setOnClickListener {
            val numOne = binding.inputOne.text.toString()
            val numTwo = binding.inputTwo.text.toString()


            if(numTwo.toInt()!=0){
                if(numOne.toInt() % numTwo.toInt() != 0){
                    var res = numOne.toDouble() / numTwo.toDouble()
                    res = res.toBigDecimal().setScale(3,RoundingMode.UP).toDouble()
                    binding.resultText.text = res.toString()

                }else{
                    val res = numOne.toInt() / numTwo.toInt()
                    binding.resultText.text = res.toString()
                }

            }else{
                Toast.makeText(this,"Can't divide by 0!",Toast.LENGTH_LONG).show()
                binding.inputTwo.text.clear()
                binding.inputOne.text.clear()
                binding.resultText.text = "Result here"
            }

        }
    }

}