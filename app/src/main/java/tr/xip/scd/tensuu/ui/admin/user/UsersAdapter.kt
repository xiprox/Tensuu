package tr.xip.scd.tensuu.ui.admin.user

import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.View.GONE
import android.view.ViewGroup
import io.realm.OrderedRealmCollection
import io.realm.RealmRecyclerViewAdapter
import kotlinx.android.synthetic.main.item_user.view.*
import tr.xip.scd.tensuu.R
import tr.xip.scd.tensuu.realm.model.User
import tr.xip.scd.tensuu.util.ext.getLayoutInflater
import tr.xip.scd.tensuu.util.ext.toVisibility

class UsersAdapter(
        data: OrderedRealmCollection<User>,
        val itemClickListener: ((clickedView: View) -> Unit)? = null
) : RealmRecyclerViewAdapter<User, UsersAdapter.ViewHolder>(data, true) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = parent.context.getLayoutInflater().inflate(R.layout.item_user, parent, false)
        if (itemClickListener != null) {
            v.setOnClickListener {
                itemClickListener.invoke(v)
            }
        }
        return ViewHolder(v)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = data?.get(position) ?: return

        holder.itemView.email.text = item.email ?: "?"
        holder.itemView.admin.visibility = item.isAdmin?.toVisibility() ?: GONE
        holder.itemView.modify.visibility = item.canModify?.toVisibility() ?: GONE
    }

    class ViewHolder(v: View) : RecyclerView.ViewHolder(v)
}