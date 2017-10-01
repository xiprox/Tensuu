package tr.xip.scd.tensuu.ui.common.adapter

import android.annotation.SuppressLint
import android.support.v7.widget.PopupMenu
import android.support.v7.widget.RecyclerView
import android.view.Gravity
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import android.view.animation.DecelerateInterpolator
import io.realm.OrderedRealmCollection
import io.realm.RealmRecyclerViewAdapter
import kotlinx.android.synthetic.main.item_point.view.*
import tr.xip.scd.tensuu.App.Companion.context
import tr.xip.scd.tensuu.R
import tr.xip.scd.tensuu.realm.model.Point
import tr.xip.scd.tensuu.local.Credentials
import tr.xip.scd.tensuu.ui.student.StudentActivity
import tr.xip.scd.tensuu.realm.util.RealmUtils.syncedRealm
import tr.xip.scd.tensuu.common.ext.getLayoutInflater
import tr.xip.scd.tensuu.common.ext.isToday
import tr.xip.scd.tensuu.common.ext.isYesterday
import java.text.SimpleDateFormat

class PointsAdapter(data: OrderedRealmCollection<Point>) : RealmRecyclerViewAdapter<Point, PointsAdapter.ViewHolder>(data, true) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = parent.context.getLayoutInflater().inflate(R.layout.item_point, parent, false)
        return ViewHolder(v)
    }

    @SuppressLint("SimpleDateFormat")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = data?.get(position) ?: return

        /* 60% alpha if my point and older than a week */
        val now = System.currentTimeMillis()
        val weekInMillis: Long = 7 * 24 * 60 * 60 * 1000
        val olderThanWeek = item.from?.email == Credentials.email &&
                (now - (item.timestamp ?: 0)) > weekInMillis
        holder.itemView.alpha = if (olderThanWeek) 0.6f else 1f

        /* Amount */
        holder.itemView.amount.text = "${item.amount ?: ""}"
        if ((item.amount ?: 0) < 0) {
            holder.itemView.amount.setBackgroundResource(R.drawable.oval_minus)
        } else {
            holder.itemView.amount.setBackgroundResource(R.drawable.oval_plus)
        }

        /* To */
        holder.itemView.to.text = item.to?.fullName ?: "?"

        /* From */
        holder.itemView.from.text = item.from?.name ?: "?"

        /* Time */
        val timestamp = item.timestamp ?: 0
        var timeText: String? = null
        if (timestamp != 0.toLong()) {
            if (timestamp.isToday()) {
                timeText = context.getString(R.string.today)
            } else if (timestamp.isYesterday()) {
                timeText = context.getString(R.string.yesterday)
            } else {
                timeText = SimpleDateFormat("MMM d, yyyy").format(timestamp)
            }
        }
        if (timeText != null) {
            holder.itemView.time.text = timeText
            holder.itemView.time.visibility = VISIBLE
        } else {
            holder.itemView.time.visibility = GONE
        }

        /* Reason */
        if (item.reason != null) {
            holder.itemView.reason.text = item.reason
            holder.itemView.root.setOnClickListener(reasonClickListener)
            holder.itemView.expandIndicator.visibility = VISIBLE
        } else {
            holder.itemView.root.setOnClickListener(null)
            holder.itemView.expandIndicator.visibility = GONE
        }

        /*
         * Options
         */
        val menu = PopupMenu(holder.itemView.context, holder.itemView.more, Gravity.BOTTOM)
        menu.menuInflater.inflate(R.menu.point, menu.menu)

        /*
         * - Owners SHOULD be able to edit IF they (still) have modification rights
         * - Admins SHOULD be able to edit no matter what
         */
        val canEdit =
                (item.from?.email == Credentials.email && Credentials.canModify)
                        || Credentials.isAdmin

        val deleteItem = menu.menu.findItem(R.id.delete)
        deleteItem.isVisible = canEdit
        deleteItem.isEnabled = canEdit

        menu.setOnMenuItemClickListener {
            when (it.itemId) {
                R.id.go_to_student -> {
                    StudentActivity.start(context, item.to?.ssid)
                }
                R.id.delete -> {
                    syncedRealm().use {
                        it.executeTransaction {
                            item.deleteFromRealm()
                        }
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

    private object reasonClickListener : View.OnClickListener {
        override fun onClick(v: View) {
            val expand = v.reasonContainer.visibility == GONE
            v.reasonContainer.visibility = if (expand) VISIBLE else GONE
            v.expandIndicator.animate()
                    .rotation(if (expand) 180f else 0f)
                    .setInterpolator(DecelerateInterpolator())
                    .setDuration(200)
                    .start()
        }
    }
}