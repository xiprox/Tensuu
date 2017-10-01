package tr.xip.scd.tensuu.ui.admin.user

import android.annotation.SuppressLint
import android.content.Context
import android.support.v7.app.AlertDialog
import android.view.View
import io.realm.Realm
import kotlinx.android.synthetic.main.dialog_add_user.view.*
import tr.xip.scd.tensuu.App
import tr.xip.scd.tensuu.R
import tr.xip.scd.tensuu.realm.model.User
import tr.xip.scd.tensuu.common.ext.getLayoutInflater

object AddUserDialog {

    fun new(realm: Realm, context: Context): AlertDialog? {
        @SuppressLint("InflateParams")
        val v = context.getLayoutInflater().inflate(R.layout.dialog_add_user, null, false)

        val dialog = AlertDialog.Builder(context)
                .setView(v)
                .setTitle(R.string.title_add_user)
                .setPositiveButton(R.string.action_add, null)
                .setNegativeButton(R.string.action_cancel, null)
                .create()

        dialog.setOnShowListener {
            val positive = dialog.getButton(AlertDialog.BUTTON_POSITIVE)

            positive.setOnClickListener {
                val email = v.email.text.toString()
                val password = v.password.text.toString()
                val passwordConfirm = v.passwordConfirm.text.toString()
                val name = v.name.text.toString()
                val admin = v.adminSwitch.isChecked
                val modify = v.modifySwitch.isChecked

                if (validateFields(v, email, password, passwordConfirm, name)) {
                    val user = User()
                    user.email = email
                    user.password = password
                    user.name = name
                    user.isAdmin = admin
                    user.canModify = modify

                    realm.executeTransaction {
                        it.copyToRealm(user)
                    }

                    dialog.dismiss()
                }
            }
        }

        return dialog
    }

    private fun validateFields(
            v: View,
            email: String?,
            password: String?,
            passwordConfirm: String?,
            name: String?
    ): Boolean {
        /* Email */
        if (email == null || !email.trim().matches("^\\S+@\\S+\\.\\S+$".toRegex())) {
            v.emailLayout.error = App.context.getString(R.string.error_invalid_email)
            return false
        } else {
            v.emailLayout.error = null
        }

        /* Password */
        if (password == null || password.trim().isEmpty()) {
            v.passwordLayout.error = App.context.getString(R.string.error_invalid_password)
            return false
        } else {
            v.passwordLayout.error = null
        }

        /* Password Confirm */
        if (passwordConfirm == null || passwordConfirm.trim().isEmpty() || passwordConfirm != password) {
            v.passwordConfirmLayout.error = App.context.getString(R.string.error_invalid_password_not_matched)
            return false
        } else {
            v.passwordConfirmLayout.error = null
        }

        /* Name */
        if (name == null || name.trim().isEmpty()) {
            v.nameLayout.error = App.context.getString(R.string.error_invalid_name)
            return false
        } else {
            v.nameLayout.error = null
        }

        /* All iz wel */
        return true
    }
}