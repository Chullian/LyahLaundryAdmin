package com.dynamiteam.lyahlaundryAdmin.fragments.registration

import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.lifecycle.Observer
import com.dynamiteam.lyahlaundryAdmin.R
import com.dynamiteam.lyahlaundryAdmin.activities.MainCallback
import com.dynamiteam.lyahlaundryAdmin.base.BaseFragment
import com.dynamiteam.lyahlaundryAdmin.data.model.request.RegsitrationRequest
import com.dynamiteam.lyahlaundryAdmin.tools.bindInterfaceOrThrow
import com.dynamiteam.lyahlaundryAdmin.tools.extentions.setClickListeners
import com.dynamiteam.lyahlaundryAdmin.tools.extentions.text
import kotlinx.android.synthetic.main.fr_register.*


class RegistrationFragment : BaseFragment<RegistrationVM>(), View.OnClickListener {
    private var registrationResponseObserver = Observer<Any> {
        callback?.showLoginFragment()
    }
    private var emptyFieldObserver = Observer<String> {
        when (it) {
            "phone" -> phoneContainer.error = getString(R.string.phone_number_cannot_be_empty)
            "address" -> addressContainer.error = getString(R.string.address_cannt_be_empty)
            "name" -> nameContainer.error = getString(R.string.name_cannot_be_empty)
            "password" -> passwordContainer.error =
                getString(R.string.password_cannot_be_empty)
            "weak password" -> {
                passwordContainer.helperText = "Password must have at least 8 characters"
            }
        }
    }
    override val layoutId = R.layout.fr_register
    override val viewModelClass = RegistrationVM::class.java


    override fun observeLiveData() {
        viewModel.run {
            emptyFieldLD.observe(this@RegistrationFragment, emptyFieldObserver)
            registrationResponseLD.observe(this@RegistrationFragment, registrationResponseObserver)
        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        setClickListeners(registerButton)
        initView()
    }

    private fun initView() {
//        val tm = context?.getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager
//        val countryISO = tm.networkCountryIso.toUpperCase()
//        val countries = resources.openRawResource(R.raw.countries)
//        viewModel.parseCountry(countryISO, countries)
    }

    override fun onClick(v: View?) {
        when (v) {
            registerButton -> {
                if (isPasswordMatch()) {
                    viewModel.registerUser(wrapFields())
                } else {
                    passwordContainer.error = getString(R.string.password_not_matching)
                    confirmPasswordContainer.error = getString(R.string.password_not_matching)
                }
            }
        }
    }


    var callback: MainCallback? = null
    override fun onAttach(context: Context) {
        super.onAttach(context)
        callback = bindInterfaceOrThrow<MainCallback>(parentFragment, context)
    }

    private fun isPasswordMatch() = passwordRegsiter.text() == confirmPasswordRegsiter.text()

    fun wrapFields() = RegsitrationRequest(
        nameRegister.text(),
        phoneRegsiter.text(),
        addressRegsiter.text(),
        passwordRegsiter.text()
    )

}