package ru.skillbranch.devintensive.ui.profile

import android.graphics.ColorFilter
import android.graphics.PorterDuff
import android.graphics.PorterDuffColorFilter
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.EditText
import android.widget.TextView
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import kotlinx.android.synthetic.main.activity_profile.*
import ru.skillbranch.devintensive.R
import ru.skillbranch.devintensive.models.Profile
import ru.skillbranch.devintensive.utils.Utils
import ru.skillbranch.devintensive.viewmodels.ProfileViewModel

class ProfileActivity : AppCompatActivity() {

    companion object {
        const val IS_EDIT_MODE = "IS_EDIT_MODE"
    }

    private lateinit var viewModel: ProfileViewModel
    var isEditMode = false
    lateinit var viewFields: Map<String, TextView>


    override fun onCreate(savedInstanceState: Bundle?) {
        setTheme(R.style.AppTheme)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)
        initViews(savedInstanceState)
        initViewModel()
    }

    override fun onSaveInstanceState(outState: Bundle?) {
        super.onSaveInstanceState(outState)
        outState?.putBoolean(IS_EDIT_MODE, isEditMode)
    }

    private fun initViews(savedInstanceState: Bundle?) {
        isEditMode = savedInstanceState?.getBoolean(IS_EDIT_MODE, false) ?: false

        viewFields = mapOf(
                "nickname" to tv_nick_name,
                "rank" to tv_rank,
                "firstName" to et_first_name,
                "lastName" to et_last_name,
                "about" to et_about,
                "repository" to et_repository,
                "rating" to tv_rating,
                "respect" to tv_respect
        )
        showCurrentMode(isEditMode)

        btn_edit.setOnClickListener {
            if (isEditMode) {
                saveProfileInfo()
            }
            isEditMode = !isEditMode
            showCurrentMode(isEditMode)
        }

        btn_switch_theme.setOnClickListener {
            viewModel.switchTheme()
        }

        et_repository.addTextChangedListener(object : TextWatcher {
            override fun onTextChanged(text: CharSequence, p1: Int, p2: Int, p3: Int) {
                viewModel.validateRepo(text.toString())
            }
            override fun afterTextChanged(p0: Editable?) {}
            override fun beforeTextChanged(text: CharSequence?, p1: Int, p2: Int, p3: Int) {}
        })

    }

    private fun checkRepoState(isValidate: Boolean) {
        if (isValidate) {
            wr_repository.error = null
            nested_scroll.scrollY = et_repository.bottom
        } else {
            wr_repository.error = "Невалидный адрес репозитория"
            nested_scroll.scrollY = wr_repository.bottom
            et_repository.requestFocus()
        }
    }

    private fun setInitials(initials: String) {
        iv_avatar.setImageBitmap(iv_avatar.drawDefaultAvatar(initials))
    }

    private fun initViewModel() {
        viewModel = ViewModelProviders.of(this).get(ProfileViewModel::class.java)
        viewModel.getProfileData().observe(this, Observer { updateUi(it) })
        viewModel.getTheme().observe(this, Observer { swithTheme(it) })
        viewModel.getRepoValid().observe(this, Observer { checkRepoState(it) })
    }

    private fun swithTheme(mode: Int) {
        delegate.setLocalNightMode(mode)
    }

    private fun updateUi(profile: Profile) {
        profile.toMap().also {
            for ((k, v) in viewFields) {
                v.text = it[k].toString()
            }
        }

        val initials = Utils.toInitials(profile.firstName, profile.lastName)
        setInitials(initials ?: "")
    }

    private fun showCurrentMode(editMode: Boolean) {
        val info = viewFields.filter { setOf("firstName", "lastName", "about", "repository").contains(it.key) }
        for ((_, v) in info) {
            v as EditText
            v.isFocusable = isEditMode
            v.isFocusableInTouchMode = isEditMode
            v.isEnabled = isEditMode
            v.background.alpha = if (isEditMode) 255 else 0
        }

        ic_eye.visibility = if (editMode) View.GONE else View.VISIBLE
        wr_about.isCounterEnabled = isEditMode

        with(btn_edit) {
            val filter: ColorFilter? = if (isEditMode) {
                PorterDuffColorFilter(
                        resources.getColor(R.color.color_accent, theme),
                        PorterDuff.Mode.SRC_IN
                )
            } else {
                null
            }

            val icon = if (isEditMode) {
                resources.getDrawable(R.drawable.ic_save_black_24dp, theme)
            } else {
                resources.getDrawable(R.drawable.ic_edit_black_24dp, theme)
            }

            background.colorFilter = filter
            setImageDrawable(icon)
        }
    }

    private fun saveProfileInfo() {
        Profile(
                firstName = et_first_name.text.toString(),
                lastName = et_last_name.text.toString(),
                about = et_about.text.toString(),
                repository = et_repository.text.toString()
        ).apply {
            viewModel.saveProfileData(this)
        }
    }

}
