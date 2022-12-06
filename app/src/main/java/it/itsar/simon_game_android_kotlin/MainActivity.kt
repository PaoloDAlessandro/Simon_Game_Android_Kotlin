package it.itsar.simon_game_android_kotlin

import android.graphics.Color
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.graphics.ColorUtils
import it.itsar.simon_game_android_kotlin.databinding.ActivityMainBinding
import kotlin.concurrent.thread

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    private lateinit var colori: Array<Colore>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        colori = arrayOf(
            Colore("red", 329, 250, binding.redButton, Color.RED),
            Colore("green", 391, 250, binding.greenButton, Color.GREEN),
            Colore("blue", 195, 250, binding.blueButton, Color.BLUE),
            Colore("yellow", 261, 250, binding.yellowButton, Color.YELLOW))


        setButtonsBackgroundColor()
        addClickListeners();
        
        binding.playButton.setOnClickListener {
            colori[0].play()
        }
    }

    fun setButtonsBackgroundColor() {
        for(colore in colori) {
            println(colore.button.toString())
            colore.button.alpha = (0.5).toFloat()
        }
    }

    fun addClickListeners() {
        for(colore in colori) {
            colore.button.setOnClickListener {
                colore.button.alpha = 1.toFloat()
                setTimeOut({colore.button.alpha = 0.5.toFloat()}, 500)
            }
        }
    }

    fun setTimeOut(runnable: Runnable, delay: Int) {
        thread(start = true) {
            Thread.sleep(delay.toLong())
            runnable.run()
        }
    }

}