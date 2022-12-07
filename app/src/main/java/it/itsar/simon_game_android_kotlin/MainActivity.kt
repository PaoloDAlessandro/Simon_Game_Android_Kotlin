package it.itsar.simon_game_android_kotlin

import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import it.itsar.simon_game_android_kotlin.databinding.ActivityMainBinding
import kotlin.concurrent.thread

private const val alphaDelay: Int = 500
private const val alphaValue: Double = 0.5

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var playButton: Button

    private lateinit var colori: Array<Colore>
    private var userSequence: ArrayList<String> = ArrayList()
    private var sequence: ArrayList<String> = ArrayList()

    private var score: Int = 0
    private var indice: Int = 0
    private var nClicks: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        playButton = binding.playButton
        colori = arrayOf(
            Colore("red", 329, 250, binding.redButton, Color.RED),
            Colore("green", 391, 250, binding.greenButton, Color.GREEN),
            Colore("blue", 195, 250, binding.blueButton, Color.BLUE),
            Colore("yellow", 261, 250, binding.yellowButton, Color.YELLOW))

        setButtonsBackgroundColor()
        addClickListeners()
        disableButtons()

        playButton.setOnClickListener {
            reset()
            updateScore()
            enableButtons()
            playButton.isEnabled = false
            play()
        }
    }

    private fun setButtonsBackgroundColor() {
        for(colore in colori) {
            colore.button.alpha = (alphaValue).toFloat()
        }
    }

    private fun disableButtons() {
        for(colore in colori) {
            colore.button.isEnabled = false
        }
    }

    private fun enableButtons() {
        for(colore in colori) {
            colore.button.isEnabled = true
        }
    }

    private fun addClickListeners() {
        colori.forEachIndexed { index, colore ->
            val finalI: Int = index
            colore.button.setOnClickListener {
                userSequence.add(colori[index].nome)
                nClicks++
                Log.d("userSequence: ", userSequence.toString())
                Log.d("sequence: ", sequence.toString())
                if(userSequence != sequence.subList(0, nClicks)) {
                    gameOver()
                }
                if(userSequence == sequence && sequence.size > 0) {
                    win()
                }
                colore.button.alpha = 1.toFloat()
                colori[finalI].play()
                setTimeOut({ this.runOnUiThread{ colore.button.alpha = alphaValue.toFloat() }}, alphaDelay)
                val finalnClicks = nClicks
                setTimeOut({checkClick(finalnClicks)}, 1500)
            }
        }
    }

    private fun gameOver() {
        this.runOnUiThread {
            disableButtons()
            binding.score.text = "Game over"
            playButton.isEnabled = true
        }
        reset()
    }

    private fun win() {
        nClicks = 0
        indice = 0
        score++
        updateScore()
        setTimeOut({play()}, 500)
    }

    private fun updateScore() {
        this.binding.score.text = "Your score: " + score
    }

    private fun setTimeOut(runnable: Runnable, delay: Int) {
        thread(start = true) {
            Thread.sleep(delay.toLong())
            runnable.run()
        }
    }

    private fun play() {
        this.runOnUiThread(this::enableButtons)
        userSequence.clear()
        val index: Int = (Math.random() * colori.size).toInt()
        sequence.add(colori[index].nome)
        setTimeOut({playSound(colori[findIndex(sequence[indice])])}, 500)
    }

    private fun findIndex(nome: String): Int {
        colori.forEachIndexed { index, colore ->
            if(colore.nome == nome) {
                return index
            }
        }
        return 0
    }

    private fun reset() {
        indice = 0
        nClicks = 0
        score = 0
        sequence.clear()
        userSequence.clear()
    }

    private fun checkClick(preClick: Int) {
        if(nClicks < preClick + 1 && nClicks != 0) {
            gameOver()
        }
    }

    private fun changeButtonAlpha(color: Colore) {
        color.button.alpha = 1.toFloat()
        setTimeOut({this.runOnUiThread { color.button.alpha = alphaValue.toFloat() } }, alphaDelay)
    }

    private fun playSound(color: Colore) {
        color.play()
        changeButtonAlpha(color)
        indice++
        if(indice < sequence.size) {
            setTimeOut({playSound(colori[findIndex(sequence[indice])])}, alphaDelay)
        }
        else {
            this.runOnUiThread(this::enableButtons)
        }
    }
}