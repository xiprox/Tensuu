package tr.xip.scd.tensuu.ui.admin.user

import android.os.Bundle
import android.text.Editable
import android.view.MenuItem
import tr.xip.scd.tensuu.App.Companion.context
import tr.xip.scd.tensuu.R
import tr.xip.scd.tensuu.data.model.User
import tr.xip.scd.tensuu.data.model.UserFields
import tr.xip.scd.tensuu.ui.common.mvp.RealmPresenter

class EditUserPresenter : RealmPresenter<EditUserView>() {
    private var user: User? = null

    fun loadWith(extras: Bundle?) {
        if (extras == null) {
            throw IllegalArgumentException("No extra passed to ${EditUserPresenter::class.java.simpleName}")
        }

        user = realm.where(User::class.java)
                .equalTo(UserFields.EMAIL, extras.getString(EditUserActivity.ARG_USER_EMAIL))
                .findFirst()

        if (user != null) {
            view?.setEmail(user?.email)
            view?.setName(user?.name)
            view?.setPassword(user?.password)
            view?.setAdmin(user?.isAdmin ?: false)
            view?.setCanModify(user?.isAdmin ?: false)
        } else {
            view?.showToast(context.getString(R.string.error_couldnt_find_user))
            view?.die()
        }
    }

    fun onEmailChanged(s: Editable?) {
        if (s == null) return
        realm.executeTransaction {
            user?.email = s.toString()
        }
    }

    fun onPasswordChanged(s: Editable?) {
        if (s == null) return
        realm.executeTransaction {
            user?.password = s.toString()
        }
    }

    fun onNameChanged(s: Editable?) {
        if (s == null) return
        realm.executeTransaction {
            user?.name = s.toString()
        }
    }

    fun onAdminChanged(isChecked: Boolean) {
        realm.executeTransaction {
            user?.isAdmin = isChecked
        }
    }

    fun onCanModifyChanged(isChecked: Boolean) {
        realm.executeTransaction {
            user?.canModify = isChecked
        }
    }

    fun onOptionsMenuItemSelected(item: MenuItem) {
        when (item.itemId) {
            R.id.action_delete -> realm.executeTransaction {
                user?.deleteFromRealm()
                if (!(user?.isValid ?: false)) {
                    view?.die()
                }
            }
        }
    }
}