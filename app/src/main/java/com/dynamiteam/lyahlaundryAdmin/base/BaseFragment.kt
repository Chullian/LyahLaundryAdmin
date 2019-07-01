package com.dynamiteam.lyahlaundryAdmin.base

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import com.dynamiteam.lyahlaundryAdmin.tools.bindInterfaceOrThrow

abstract class BaseFragment<T : BaseViewModel> : Fragment(), BaseView {

    override fun createViewModelFactory(): ViewModelProvider.NewInstanceFactory? {
        return null
    }

    override fun isNeedProgress() = true

    private val errorObserver = Observer<Throwable> { error ->
        baseView?.showSnack(error.localizedMessage)
    }
    protected abstract val layoutId: Int

    abstract val viewModelClass: Class<T>

    private var baseView: BaseView? = null

    protected val viewModel: T by lazy {
        createViewModelFactory()?.let {
            ViewModelProviders.of(this, it).get(viewModelClass)
        } ?: ViewModelProviders.of(this).get(viewModelClass)
    }

    abstract fun observeLiveData()

    override fun onAttach(context: Context) {
        super.onAttach(context)
        baseView = bindInterfaceOrThrow<BaseView>(parentFragment, context)
    }

    override fun onDetach() {
        baseView = null
        super.onDetach()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        observeAllLiveData()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(layoutId, container, false)

        return view
    }

    private fun observeAllLiveData() {
        observeLiveData()
        viewModel.errorObserverLD.observe(this, errorObserver)
    }

    override fun showSnack(localizedMessage: String) {
        baseView?.showSnack(localizedMessage)
    }

}