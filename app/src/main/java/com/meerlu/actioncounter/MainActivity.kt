package com.meerlu.actioncounter

import android.os.Build
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.content.ContextCompat
import kotlinx.android.synthetic.main.activity_main.*
import android.view.animation.AnimationUtils
import android.support.v7.app.AlertDialog
import android.view.Menu
import android.view.MenuItem
import android.widget.*
import com.meerlu.actioncounter.R.id.*
import kotlinx.android.synthetic.main.activity_main.view.*
import java.io.File

/**
 * done save count
 * TODO ads
 * done change of themes
 */


class MainActivity : AppCompatActivity() {

    var count = 0
    val filename = "appData.txt"
    var darkTheme = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //click-listeners for the counter
        btnPlus.setOnClickListener {
            plusOne()
            btnAnimation(btnPlus)
        }

        btnMinus.setOnClickListener {
            minusOne()
            btnAnimation(btnMinus)
        }

        //toolbar (the title is removed to replace it with a custom font)
        setSupportActionBar(toolbar)
        supportActionBar!!.setDisplayShowTitleEnabled(false)
    }

    //**********************************************************************************************
    /**
     * The count  and the theme-choice will be written to a .txt file as the app is closed to save the users counting.
     * This data is read when the app gets resumed (and therefore also when it's started).
     */

    override fun onResume() {
        readDataFromFile()
        super.onResume()
    }

    override fun onStop() {
        saveDataToFile()
        super.onStop()
    }

    fun saveDataToFile() {
        val file = File(this.filesDir, filename)
        file.writeText(count.toString())
        file.appendText("\n" + darkTheme.toString())
    }

    fun readDataFromFile() {
        val directory = this.filesDir
        val file = File(directory, filename)

        if(file.exists()){
            count = file.readLines()[0].toInt()
            setTxtCount()
            darkTheme = file.readLines()[1].toBoolean()
            setTheme()
        }
    }

    //**********************************************************************************************
    /**
     * The custom toolbar gives the user the option to
     *  -reset his count
     *  -change the theme/colors of the app
     *
     *  Both option-items open a dialog to ensure the users action.
     */

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        if (item!!.itemId == menu_reset) {
            resetDialog()
        }else if (item.itemId == menu_theme) {
            colorDialog()
        }

        return super.onOptionsItemSelected(item)
    }

    fun resetDialog() {
        val builder = AlertDialog.Builder(this)
        builder.setMessage("Reset your count?")
                .setPositiveButton("YES", { dialog, id ->
                    //reset count
                    count = 0
                    setTxtCount()
                })
                .setNegativeButton("NO", { dialog, id ->
                    // dialog canceled
                })
        // Create the AlertDialog object and return it
        val dialog: AlertDialog = builder.create()

        dialog.show()
    }

    fun colorDialog() {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Change Theme")
                .setItems(R.array.theme_items, { dialog, which ->
                    // The 'which' argument contains the index position
                    // of the selected item

                    when (which) {
                        0 -> {  //light theme
                            darkTheme = false
                        }
                        1 -> {   //dark theme
                            darkTheme = true
                        }
                    }
                    setTheme()
                })
        val dialog: AlertDialog = builder.create()
        dialog.show()
    }

    //changes the background and toolbar colors
    fun setTheme() {
        if (darkTheme) {
            layout.setBackgroundResource(R.color.colorPrimaryDark)

            toolbar.setBackgroundResource(R.color.colorPrimaryLight)
            toolbar.txtAppName.setTextColor(ContextCompat.getColor(this, R.color.colorPrimaryDark)) //ah yes the android feeling
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                toolbar.overflowIcon!!.setTint(ContextCompat.getColor(this, R.color.colorPrimaryDark))
            } else if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP){
                //this is an hopeless case, android literally didn't allow you to change this variable programmatically until lollipop. no. way.
            }
        } else {
            layout.setBackgroundResource(R.color.colorPrimaryLight)

            toolbar.setBackgroundResource(R.color.colorPrimaryDark)
            toolbar.setTitleTextColor(ContextCompat.getColor(this, R.color.colorPrimaryLight))
            toolbar.txtAppName.setTextColor(ContextCompat.getColor(this, R.color.colorPrimaryLight))
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                toolbar.overflowIcon!!.setTint(ContextCompat.getColor(this, R.color.colorPrimaryLight))
            } else if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP){
                //this is an hopeless case, android literally didn't allow you to change this variable programmatically until lollipop. no. way.
            }
        }
    }

    //**********************************************************************************************
    /**
     * More or less one-liner methods to change the count
     */

    fun plusOne() {
        count++
        setTxtCount()
    }

    fun minusOne() {
        if (count > 0) {
            count--
        }
        setTxtCount()
    }

    fun setTxtCount() {
        txtCount.text = count.toString()
    }

    //**********************************************************************************************
    /**
     * A simple shaky button-animation just for the  a e s t h e t i c s.
     */

    fun btnAnimation(btn: Button) {
        val shake = AnimationUtils.loadAnimation(this, R.anim.shake)
        btn.startAnimation(shake)
    }
}

