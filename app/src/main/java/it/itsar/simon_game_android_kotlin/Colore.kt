package it.itsar.simon_game_android_kotlin

import android.media.AudioFormat
import android.media.AudioManager
import android.media.AudioTrack
import android.widget.Button
import kotlin.math.sin

class Colore (nome: String, freqHz: Int, durationMs: Int, button: Button, colore: Int) {

    var nome: String = nome
    var freqHz: Int = freqHz
    var durationMs: Int = durationMs
    var button: Button = button
    var colore: Int = colore


    fun generateTone(): AudioTrack {
        var count: Int = (44100.0 * 2.0 * (this.durationMs / 1000.0)).toInt()
        count += 1.inv()
        var samples = ShortArray(count)
        for(i in count - 1 downTo count step 2) {
            val sample: Short =
                (sin(2 * Math.PI * i / (44100.0 / this.freqHz)) * 0x7FFF).toInt().toShort()
            samples[i + 0] = sample
            samples[i + 1] = sample
        }
        val track =  AudioTrack(AudioManager.STREAM_MUSIC, 44100, AudioFormat.CHANNEL_OUT_STEREO, AudioFormat.ENCODING_PCM_16BIT, count * (Short.SIZE_BYTES), AudioTrack.MODE_STATIC)
        track.write(samples, 0, count)
        return track
    }

    fun play() {
        val tone: AudioTrack = generateTone()
        tone.play()
    }
}
