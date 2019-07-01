package com.dynamiteam.lyahlaundryAdmin.base

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import com.dynamiteam.lyahlaundryAdmin.tools.extentions.hideKeyboard

abstract class BaseActivity<T : BaseViewModel> : AppCompatActivity(), BaseView {

    protected abstract val layoutId: Int

    protected abstract val containerId: Int

    protected abstract val viewModelClass: Class<T>

    protected val viewModel: T by lazy(LazyThreadSafetyMode.NONE) { ViewModelProviders.of(this).get(viewModelClass) }

    abstract fun observeLiveData()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(layoutId)
        observeAllLiveData()
    }

    private fun observeAllLiveData() {
        observeLiveData()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        supportFragmentManager.findFragmentById(containerId)?.onActivityResult(requestCode, resultCode, data)
    }

    protected open fun replaceFragment(fragment: Fragment, name: String, needToAddToBackStack: Boolean = true) {
        hideKeyboard()
        with(supportFragmentManager.beginTransaction()) {
            replace(containerId, fragment, name)
            if (needToAddToBackStack) {
                addToBackStack(name)
            }
            commit()
        }
    }

    protected open fun addFragment(fragment: Fragment, name: String, needToAddToBackStack: Boolean = true) {
        hideKeyboard()
        with(supportFragmentManager.beginTransaction()) {
            add(containerId, fragment, name)
            if (needToAddToBackStack) {
                addToBackStack(name)
            }
            commit()
        }
    }

    override fun createViewModelFactory(): ViewModelProvider.NewInstanceFactory? = null

    override fun isNeedProgress(): Boolean = true
}