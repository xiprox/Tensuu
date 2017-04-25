package tr.xip.scd.tensuu.ui.login

import com.hannesdorfmann.mosby3.mvp.MvpView

interface LoginView : MvpView {
    fun setEmailError(error: String?)
    fun setPasswordError(error: String?)
    fun enableEmail(enable: Boolean = true)
    fun enablePassword(enable: Boolean = true)
    fun enableSignInButton(enable: Boolean = true)
    fun showProgress(show: Boolean = true)

    fun showSnackbar(text: String, actionText: String? = null, actionListener: (() -> Unit)? = null)
    fun startMainActivity()
    fun die()
}