package tr.xip.scd.tensuu.ui.admin.user

import android.content.Intent
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.hannesdorfmann.mosby3.mvp.MvpFragment
import kotlinx.android.synthetic.main.fragment_users.*
import tr.xip.scd.tensuu.R
import tr.xip.scd.tensuu.realm.model.User

class UsersFragment : MvpFragment<UsersView, UsersPresenter>(), UsersView {
    override fun createPresenter(): UsersPresenter = UsersPresenter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        retainInstance = true
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_users, container, false)
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        recycler.layoutManager = LinearLayoutManager(context)
        presenter.init()
    }

    override fun setAdapter(value: UsersAdapter) {
        recycler?.adapter = value
    }

    override fun getAdapter(): UsersAdapter? {
        return recycler?.adapter as UsersAdapter?
    }

    override fun onUserClicked(view: View) {
        val position = recycler.getChildAdapterPosition(view)
        val item = (recycler.adapter as UsersAdapter).data?.get(position)
        if (item != null) presenter.onUserClicked(item)
    }

    override fun startUserEditActivity(user: User) {
        val intent = Intent(context, EditUserActivity::class.java)
        intent.putExtra(EditUserActivity.ARG_USER_EMAIL, user.email)
        context.startActivity(intent)
    }

    companion object {
        fun new(): UsersFragment {
            return UsersFragment()
        }
    }
}