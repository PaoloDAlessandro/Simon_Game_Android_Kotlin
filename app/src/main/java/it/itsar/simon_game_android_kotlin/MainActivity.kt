package it.itsar.simon_game_android_kotlin

import android.graphics.Color
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.graphics.ColorUtils
import it.itsar.simon_game_android_kotlin.databinding.ActivityMainBinding
import kotlin.concurrent.thread

private const val alphaDelay: Int = 500
private const val alphaValue: Double = 0.5

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

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
        colori = arrayOf(
            Colore("red", 329, 250, binding.redButton, Color.RED),
            Colore("green", 391, 250, binding.greenButton, Color.GREEN),
            Colore("blue", 195, 250, binding.blueButton, Color.BLUE),
            Colore("yellow", 261, 250, binding.yellowButton, Color.YELLOW))


        setButtonsBackgroundColor()
        addClickListeners()
        disableButtons()

        binding.playButton.setOnClickListener {
            reset()
            updateScore()
            enableButtons()
            play()
        }
    }

    fun setButtonsBackgroundColor() {
        for(colore in colori) {
            colore.button.alpha = (alphaValue).toFloat()
        }
    }

    fun disableButtons() {
        for(colore in colori) {
            colore.button.isEnabled = false
        }
    }

    fun enableButtons() {
        for(colore in colori) {
            colore.button.isEnabled = true
        }
    }

    fun addClickListeners() {
        colori.forEachIndexed { index, colore ->
            val finalI: Int = index
            colore.button.setOnClickListener {
                userSequence.add(colori[index].nome)
                nClicks++
                if(!(userSequence.equals(sequence.subList(0, nClicks)))) {
                    gameOver()
                }
                if(userSequence.equals(sequence) && sequence.size > 0) {
                    win()
                }
                colore.button.alpha = 1.toFloat()
                colori[finalI].play()
                setTimeOut({ this.runOnUiThread{ colore.button.alpha = alphaValue.toFloat() }}, alphaDelay)
                var finalnClicks = nClicks
                setTimeOut({checkClick(finalnClicks)}, 1500)
            }
        }
    }

    fun gameOver() {
        this.runOnUiThread {
            binding.score.text = "Game over"
            binding.playButton.isEnabled = true
        }
        reset()
    }

    fun win() {
        nClicks = 0
        indice = 0
        score++
        updateScore()
        setTimeOut({play()}, 500)
    }

    fun updateScore() {
        this.binding.score.text = "Your score: " + score
    }

    fun setTimeOut(runnable: Runnable, delay: Int) {
        thread(start = true) {
            Thread.sleep(delay.toLong())
            runnable.run()
        }
    }

    fun play() {
        userSequence.clear()
        val index: Int = (Math.random() * colori.size).toInt()
        sequence.add(colori[index].nome)
        setTimeOut({playSound(colori[findIndex(sequence[indice])])}, 500)
    }

    fun findIndex(nome: String): Int {
        colori.forEachIndexed { index, colore ->
            if(colore.nome.equals(nome)) {
                return index
            }
        }
        return 0
    }

    fun reset() {
        indice = 0
        nClicks = 0
        score = 0
        sequence.clear()
        userSequence.clear()
    }

    fun checkClick(preClick: Int) {
        if(nClicks < preClick + 1 && nClicks != 0) {
            gameOver()
        }
    }

    fun changeButtonAlpha(color: Colore) {
        color.button.alpha = 1.toFloat()
        setTimeOut({this.runOnUiThread({color.button.alpha = alphaValue.toFloat()})}, alphaDelay)
    }

    fun playSound(color: Colore) {
        color.play()
        changeButtonAlpha(color)
        indice++
        if(indice < sequence.size) {
            setTimeOut({playSound(color)}, alphaDelay)
        }
    }
}