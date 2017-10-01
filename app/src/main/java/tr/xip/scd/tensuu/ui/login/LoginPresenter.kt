package tr.xip.scd.tensuu.ui.login

import android.text.Editable
import io.realm.ObjectServerError
import io.realm.RealmAsyncTask
import io.realm.SyncCredentials
import io.realm.SyncUser
import tr.xip.scd.tensuu.App.Companion.context
import tr.xip.scd.tensuu.R
import tr.xip.scd.tensuu.realm.model.User
import tr.xip.scd.tensuu.realm.model.UserFields
import tr.xip.scd.tensuu.local.Credentials
import tr.xip.scd.tensuu.realm.RealmConfig
import tr.xip.scd.tensuu.ui.common.mvp.SafeViewMvpPresenter
import tr.xip.scd.tensuu.realm.util.RealmUtils

class LoginPresenter : SafeViewMvpPresenter<LoginView>() {
    private var realmLoginTask: RealmAsyncTask? = null

    override fun detachView(retainInstance: Boolean) {
        if (realmLoginTask != null) {
            realmLoginTask!!.cancel()
        }
        super.detachView(retainInstance)
    }

    fun init() {
        loginToRealm()
    }

    private fun loginToRealm() {
        if (SyncUser.currentUser() == null) {
            view?.showProgress()
            enableInputs(false)

            val email = RealmConfig.EMAIL
            val password = RealmConfig.PASSWORD
            val credentials = SyncCredentials.usernamePassword(email, password, false)

            view?.showSnackbar(context.getString(R.string.info_connecting_to_server))
            realmLoginTask = SyncUser.loginAsync(credentials, RealmConfig.URL_AUTH, object : SyncUser.Callback {
                override fun onSuccess(user: SyncUser?) {
                    onLoggedInToRealm()
                }

                override fun onError(error: ObjectServerError?) {
                    val text = context.getString(R.string.error_connecting_to_server_x, error?.toString())
                    view?.showSnackbar(
                            text,
                            context.getString(R.string.retry),
                            this@LoginPresenter::loginToRealm
                    )
                    view?.showProgress(false)
                }
            })
        } else {
            onLoggedInToRealm()
        }
    }

    private fun onLoggedInToRealm() {
        view?.showProgress(false)
        view?.showSnackbar(context.getString(R.string.info_connected_to_server))
        enableInputs()
    }

    private fun enableInputs(enable: Boolean = true) {
        view?.enableEmail(enable)
        view?.enablePassword(enable)
        view?.enableSignInButton(enable)
    }

    fun onEmailChanged(s: Editable?) {
        if (s?.trim()?.matches("^\\S+@\\S+\\.\\S+$".toRegex()) ?: false) {
            view?.setEmailError(null)
            view?.enableSignInButton()
        } else {
            view?.setEmailError(context.getString(R.string.error_invalid_email))
            view?.enableSignInButton(false)
        }
    }

    fun onPasswordChanged(s: Editable?) {
        if (s?.trim()?.isNotEmpty() ?: false) {
            view?.setPasswordError(null)
            view?.enableSignInButton()
        } else {
            view?.setPasswordError(context.getString(R.string.error_invalid_password))
            view?.enableSignInButton(false)
        }
    }

    fun onSignInClicked(email: String, password: String) {
        view?.showProgress()
        enableInputs(false)

        val realm = RealmUtils.syncedRealm()

        val user = realm.where(User::class.java)
                .equalTo(UserFields.EMAIL, email)
                .equalTo(UserFields.PASSWORD, password)
                .findFirst()

        if (user != null) {
            Credentials.loadFrom(user)
            realm.close()
            view?.startMainActivity()
            view?.die()
        } else {
            view?.showSnackbar(context.getString(R.string.error_signing_in))
            enableInputs()
        }
        view?.showProgress(false)
    }
}