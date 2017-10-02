package tr.xip.scd.tensuu.student.ui.feature.login

import android.text.Editable
import io.realm.ObjectServerError
import io.realm.RealmAsyncTask
import io.realm.SyncCredentials
import io.realm.SyncUser
import tr.xip.scd.tensuu.realm.RealmConfig
import tr.xip.scd.tensuu.common.ui.mvp.SafeViewMvpPresenter
import tr.xip.scd.tensuu.realm.model.Student
import tr.xip.scd.tensuu.realm.model.StudentFields
import tr.xip.scd.tensuu.realm.util.RealmUtils
import tr.xip.scd.tensuu.student.App.Companion.context
import tr.xip.scd.tensuu.student.R
import tr.xip.scd.tensuu.student.ui.feature.local.Credentials
import java.util.*

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
        view?.enableId(enable)
        view?.enablePassword(enable)
        view?.enableSignInButton(enable)
    }

    fun onIdChanged(s: Editable?) {
        if (!s.isNullOrBlank()) {
            view?.setIdError(null)
            view?.enableSignInButton()
        } else {
            view?.setIdError(context.getString(R.string.error_invalid_id))
            view?.enableSignInButton(false)
        }
    }

    fun onPasswordChanged(s: Editable?) {
        if (!s.isNullOrBlank()) {
            view?.setPasswordError(null)
            view?.enableSignInButton()
        } else {
            view?.setPasswordError(context.getString(R.string.error_invalid_password))
            view?.enableSignInButton(false)
        }
    }

    fun onSignInClicked(id: String, password: String) {
        view?.showProgress()
        enableInputs(false)

        val realm = RealmUtils.syncedRealm()

        val student = realm.where(Student::class.java)
                .equalTo(StudentFields.SSID, id)
                .equalTo(StudentFields.PASSWORD, password)
                .findFirst()

        if (student != null) {
            Credentials.loadFrom(student)

            Credentials.notificationsToken?.let { token ->
                realm.executeTransaction {
                    student.notificationToken = token
                }
            }

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