package ru.skillbranch.devintensive

import android.graphics.Color
import android.graphics.PorterDuff
import android.os.Bundle
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*
import ru.skillbranch.devintensive.extensions.hideKeyboard
import ru.skillbranch.devintensive.models.Bender

class MainActivity : AppCompatActivity(), View.OnClickListener {

    private val benderKey = "Bender"

    private lateinit var benderObj: Bender

    private lateinit var sendBtn: ImageView
    private lateinit var messageEt: EditText
    private lateinit var benderImage: ImageView
    private lateinit var textTxt: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        benderImage = iv_bender
        textTxt = tv_text
        messageEt = et_message
        sendBtn = iv_send

        benderObj = if (savedInstanceState != null) {
            savedInstanceState.getSerializable(benderKey) as Bender
        } else {
            Bender()
        }

        textTxt.text = benderObj.askQuestion()
        sendBtn.setOnClickListener(this)

        messageEt.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                checkAnswer()
                hideKeyboard()
                true
            } else false
        }
    }

    override fun onClick(v: View?) {
        if (v?.id == R.id.iv_send) {
            checkAnswer()
            hideKeyboard()
        }
    }

    private fun checkAnswer() {
        val (phrase, color) = benderObj.listenAnswer(messageEt.text.toString().trim())
        textTxt.setText(phrase)
        messageEt.setText("")
        val (r, g, b) = color
        benderImage.setColorFilter(Color.rgb(r, g, b), PorterDuff.Mode.MULTIPLY)
    }

    override fun onSaveInstanceState(outState: Bundle?) {
        super.onSaveInstanceState(outState)
        outState?.putSerializable(benderKey, benderObj)
    }
}
