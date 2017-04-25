package tr.xip.scd.tensuu.ui.login

import android.content.Intent
import android.os.Bundle
import android.support.design.widget.Snackbar
import com.hannesdorfmann.mosby3.mvp.MvpActivity
import kotlinx.android.synthetic.main.activity_login.*
import tr.xip.scd.tensuu.R
import tr.xip.scd.tensuu.ui.main.MainActivity
import tr.xip.scd.tensuu.util.ext.toVisibility
import tr.xip.scd.tensuu.util.ext.watchForChange

class LoginActivity : MvpActivity<LoginView, LoginPresenter>(), LoginView {
    override fun createPresenter(): LoginPresenter = LoginPresenter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        email.watchForChange {
            presenter.onEmailChanged(it)
        }

        password.watchForChange {
            presenter.onPasswordChanged(it)
        }

        signIn.setOnClickListener {
            presenter.onSignInClicked(email.text.toString(), password.text.toString())
        }

        presenter.init()
    }

    override fun setEmailError(error: String?) {
        emailLayout?.error = error
    }

    override fun setPasswordError(error: String?) {
        passwordLayout?.error = error
    }

    override fun enableEmail(enable: Boolean) {
        email?.isEnabled = enable
    }

    override fun enablePassword(enable: Boolean) {
        password?.isEnabled = enable
    }

    override fun enableSignInButton(enable: Boolean) {
        signIn?.isEnabled = enable
    }

    override fun showProgress(show: Boolean) {
        progress?.visibility = show.toVisibility()
    }

    override fun showSnackbar(text: String, actionText: String?, actionListener: (() -> Unit)?) {
        val snackbar = Snackbar.make(coordinatorLayout, text, Snackbar.LENGTH_LONG)
        if (actionText != null && actionListener != null) {
            snackbar.setAction(actionText, {
                actionListener.invoke()
            })
        }
        snackbar.show()
    }

    override fun startMainActivity() {
        startActivity(Intent(this, MainActivity::class.java))
    }

    override fun die() {
        finish()
    }
}