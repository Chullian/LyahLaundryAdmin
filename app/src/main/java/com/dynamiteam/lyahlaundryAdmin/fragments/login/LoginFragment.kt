package com.dynamiteam.lyahlaundryAdmin.fragments.login

import android.content.Context
import android.graphics.Typeface
import android.os.Bundle
import android.text.Spannable
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import android.text.style.StyleSpan
import android.view.View
import androidx.lifecycle.Observer
import com.dynamiteam.lyahlaundryAdmin.R
import com.dynamiteam.lyahlaundryAdmin.activities.MainCallback
import com.dynamiteam.lyahlaundryAdmin.base.BaseFragment
import com.dynamiteam.lyahlaundryAdmin.data.model.request.LoginRequest
import com.dynamiteam.lyahlaundryAdmin.data.model.response.LoginResponse
import com.dynamiteam.lyahlaundryAdmin.tools.App
import com.dynamiteam.lyahlaundryAdmin.tools.PrefManager
import com.dynamiteam.lyahlaundryAdmin.tools.bindInterfaceOrThrow
import com.dynamiteam.lyahlaundryAdmin.tools.extentions.setClickListeners
import com.dynamiteam.lyahlaundryAdmin.tools.extentions.text
import kotlinx.android.synthetic.main.fr_login.*


class LoginFragment : BaseFragment<LoginVM>(), View.OnClickListener {


    override val layoutId = R.layout.fr_login
    override val viewModelClass = LoginVM::class.java


    private var passwordValidationObserver = Observer<Boolean> {

        if (it) {
            callback?.hideProgress();
            loginPasswordContainer.error = getString(R.string.password_cannot_be_empty)
        }
    }

    private var emailValidationObserver = Observer<Boolean> {
        if (it) {
            callback?.hideProgress();
            loginEmailContainer.error = getString(R.string.email_cannot_be_empty)
        }
    }

    private var loginResponseObserver = Observer<LoginResponse> {
        callback?.hideProgress()
        callback?.showSnack(it.id.toString())
        PrefManager.saveLoginSession(loginEmailField.text(), it)
        PrefManager.isFirstOpen = false
        callback?.showHomeFragment()
    }

    override fun observeLiveData() {
        viewModel.run {
            passwordValidationLD.observe(this@LoginFragment, passwordValidationObserver)
            emailValidationLD.observe(this@LoginFragment, emailValidationObserver)
            loginResponseLD.observe(this@LoginFragment, loginResponseObserver)
        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        setClickListeners(loginButton,registerButtonLogin)
        initViews()
    }

    private fun initViews() {
        val wordtoSpan =SpannableString(getString(R.string.not_a_member_register))
        wordtoSpan.setSpan(ForegroundColorSpan(App.instance.getColorApp(R.color.colorPrimary)), 14, 22, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        wordtoSpan.setSpan(StyleSpan(Typeface.BOLD), 14, 22, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        registerButtonLogin.text = wordtoSpan
    }

    override fun onClick(v: View?) {
        when (v) {
            loginButton -> {
                callback?.showProgress()
                var loginRequest = LoginRequest(loginEmailField.text(), loginPasswordField.text())
                viewModel.doLogin(loginRequest)
            }
            registerButtonLogin->{
                callback?.showRegistrationFragment()
            }
        }
    }

    var callback: MainCallback? = null
    override fun onAttach(context: Context) {
        super.onAttach(context)
        callback = bindInterfaceOrThrow<MainCallback>(parentFragment, context)
    }
}