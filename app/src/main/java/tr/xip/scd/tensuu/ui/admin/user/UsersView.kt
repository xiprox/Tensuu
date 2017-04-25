package tr.xip.scd.tensuu.ui.admin.user

import android.view.View
import com.hannesdorfmann.mosby3.mvp.MvpView
import tr.xip.scd.tensuu.data.model.User

interface UsersView : MvpView {
    fun setAdapter(value: UsersAdapter)
    fun getAdapter(): UsersAdapter?

    fun onUserClicked(view: View)

    fun startUserEditActivity(user: User)
}