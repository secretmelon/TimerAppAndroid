package com.example.timerapp

import android.graphics.Color
import android.os.Bundle
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import android.widget.Button
import android.widget.TextView

import kotlinx.android.synthetic.main.activity_main.*
import java.util.*

class MainActivity : AppCompatActivity() {

    internal lateinit var   startButton: Button
    internal lateinit var   restartButton: Button
    internal lateinit var   timerView: TextView
    private var     currentMinutes: Int = 0
    private var     currentSeconds: Int = 0
    private var     currentMilliS: Int = 0
    private var     timerExists: Boolean = false
    private var     timer = Timer()
    private var     currState: currentState = currentState.notStarted

    enum class currentState {
        paused,
        running,
        notStarted
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)
        startButton     = findViewById(R.id.startbutton)
        restartButton   = findViewById(R.id.restartbutton)
        timerView       = findViewById(R.id.textView)

        fab.setOnClickListener { view ->
            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show()
        }

        startButton.setOnClickListener{ view ->

            //Timer has already started and it's currently paused
            if (timerExists && currState == currentState.paused){
                currState = currentState.running
                startTimer()
            }
            //Timer has already started and it's currently running
            else if (timerExists && currState == currentState.running){
                currState = currentState.paused
                this.timer.cancel()
                timerExists = false
            }
            //Timer hasn't started
            else if(!timerExists){
                currState = currentState.running
                startTimer()
            }
            classifyButton()
        }

        restartButton.setOnClickListener{ view ->
            restartTimer()
            classifyButton()
        }
    }

    private fun classifyButton() {
        when (this.currState){
            //Running -> Paused
            currentState.paused -> {
                startButton.setText("Start")
                startButton.setBackgroundColor(Color.parseColor("#D9F0FF")) //Baby Blue
            }

            //Paused -> Running
            currentState.running -> {
                startButton.setText("Pause")
                startButton.setBackgroundColor(Color.parseColor("#FF993F")) //Saffron Orange
            }

            //First click of start
            //Restarted
            currentState.notStarted -> {

                //classifyButton()
            }
        }
    }



    private fun restartTimer() {
        currState = currentState.paused
        timerExists = false
        this.timer.cancel()
        this.currentMinutes = 0
        this.currentSeconds = 0
        this.currentMilliS  = 0

        timerView.setText(
            String.format(
                "%02d:%02d.%d",
                currentMinutes,
                currentSeconds,
                currentMilliS
            )
        )



    }

    private fun startTimer() {
        if(!timerExists) {
            timerExists = true
            this.timer = Timer()
            println("timer now started")
            this.timer.schedule(object : TimerTask() {

                override fun run() {
                    incrementTime()
                    timerView.setText(
                        String.format(
                            "%02d:%02d.%d",
                            currentMinutes,
                            currentSeconds,
                            currentMilliS
                        )
                    )
                }

            }, 0, 100L)
        }
    }


    private fun incrementTime() {
        this.currentMilliS += 1
        if(this.currentMilliS % 10 == 0) {
            this.currentSeconds += 1
            this.currentMilliS = 0

            if (this.currentSeconds % 60 == 0) {
                this.currentMinutes += 1
                this.currentSeconds = 0
                this.currentMilliS = 0
            }
        }

    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        return when (item.itemId) {
            R.id.action_settings -> true
            else -> super.onOptionsItemSelected(item)
        }
    }




}
