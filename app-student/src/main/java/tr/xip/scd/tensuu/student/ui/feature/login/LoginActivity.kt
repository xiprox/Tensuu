package tr.xip.scd.tensuu.student.ui.feature.login

import android.content.Intent
import android.os.Bundle
import android.support.design.widget.Snackbar
import com.hannesdorfmann.mosby3.mvp.MvpActivity
import kotlinx.android.synthetic.main.activity_login.*
import tr.xip.scd.tensuu.common.ext.toVisibility
import tr.xip.scd.tensuu.common.ext.watchForChange
import tr.xip.scd.tensuu.student.R
import tr.xip.scd.tensuu.student.ui.feature.main.StudentActivity

class LoginActivity : MvpActivity<LoginView, LoginPresenter>(), LoginView {
    override fun createPresenter(): LoginPresenter = LoginPresenter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        id.watchForChange {
            presenter.onIdChanged(it)
        }

        password.watchForChange {
            presenter.onPasswordChanged(it)
        }

        signIn.setOnClickListener {
            presenter.onSignInClicked(id.text.toString(), password.text.toString())
        }

        presenter.init()
    }

    override fun setIdError(error: String?) {
        emailLayout?.error = error
    }

    override fun setPasswordError(error: String?) {
        passwordLayout?.error = error
    }

    override fun enableId(enable: Boolean) {
        id?.isEnabled = enable
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
        startActivity(Intent(this, StudentActivity::class.java))
    }

    override fun die() {
        finish()
    }
}