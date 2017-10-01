package tr.xip.scd.tensuu.ui.lists

import android.annotation.SuppressLint
import android.content.Context
import android.support.v7.widget.PopupMenu
import android.support.v7.widget.RecyclerView
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import io.realm.OrderedRealmCollection
import io.realm.Realm
import io.realm.RealmRecyclerViewAdapter
import kotlinx.android.synthetic.main.item_list.view.*
import tr.xip.scd.tensuu.R
import tr.xip.scd.tensuu.realm.model.StudentList
import tr.xip.scd.tensuu.local.Credentials
import tr.xip.scd.tensuu.ui.lists.detail.ListDetailActivity
import tr.xip.scd.tensuu.util.ext.getLayoutInflater
import tr.xip.scd.tensuu.util.ext.toVisibility

class ListsAdapter(
        val context: Context,
        val realm: Realm,
        data: OrderedRealmCollection<StudentList>
) : RealmRecyclerViewAdapter<StudentList, ListsAdapter.ViewHolder>(data, true) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = parent.context.getLayoutInflater().inflate(R.layout.item_list, parent, false)
        return ViewHolder(v)
    }

    @SuppressLint("SimpleDateFormat")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = data?.get(position) ?: return

        holder.itemView.setOnClickListener {
            context.startActivity(ListDetailActivity.getLaunchIntent(item.name, item.owner?.email))
        }

        holder.itemView.name.text = item.name ?: "?"
        holder.itemView.studentsCount.text = context.resources.getString(R.string.x_students, item.students.size)
        holder.itemView.privateTag.visibility = (item.public != true).toVisibility()

        /*
         * Options
         */
        val menu = PopupMenu(holder.itemView.context, holder.itemView.more, Gravity.BOTTOM)
        menu.menuInflater.inflate(R.menu.list, menu.menu)

        /*
         * - Owners SHOULD be able to edit IF they (still) have modification rights
         * - Admins SHOULD be able to edit no matter what
         */
        val canEdit =
                (item.owner?.email == Credentials.email && Credentials.canModify)
                        || Credentials.isAdmin

        val deleteItem = menu.menu.findItem(R.id.delete)
        deleteItem.isVisible = canEdit
        deleteItem.isEnabled = canEdit

        menu.setOnMenuItemClickListener {
            when (it.itemId) {
                R.id.delete -> {
                    realm.executeTransaction {
                        item.deleteFromRealm()
                    }
                }
            }
            true
        }
        holder.itemView.more.setOnClickListener {
            menu.show()
        }
    }

    class ViewHolder(v: View) : RecyclerView.ViewHolder(v)
}