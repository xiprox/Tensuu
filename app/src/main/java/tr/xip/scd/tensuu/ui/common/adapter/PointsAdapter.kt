package tr.xip.scd.tensuu.ui.common.adapter

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
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
import tr.xip.scd.tensuu.data.model.Point
import tr.xip.scd.tensuu.local.Credentials
import tr.xip.scd.tensuu.ui.student.StudentActivity
import tr.xip.scd.tensuu.util.RealmUtils.syncedRealm
import tr.xip.scd.tensuu.util.ext.getLayoutInflater
import tr.xip.scd.tensuu.util.ext.isToday
import tr.xip.scd.tensuu.util.ext.isYesterday
import tr.xip.scd.tensuu.util.ext.toMonthDayYearString
import java.text.SimpleDateFormat

class PointsAdapter(data: OrderedRealmCollection<Point>) : RealmRecyclerViewAdapter<Point, PointsAdapter.ViewHolder>(data, true) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = parent.context.getLayoutInflater().inflate(R.layout.item_point, parent, false)
        return ViewHolder(v)
    }

    @SuppressLint("SimpleDateFormat")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = data?.get(position) ?: return

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
                val timeFormatted = SimpleDateFormat("h:mm a").format(timestamp)
                timeText = context.getString(R.string.today_x, timeFormatted)
            } else if (timestamp.isYesterday()) {
                val timeFormatted = SimpleDateFormat("h:mm a").format(timestamp)
                timeText = context.getString(R.string.yesterday_x, timeFormatted)
            } else {
                timeText = SimpleDateFormat("MMM d, yyyy, h:mm a").format(timestamp)
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

    override fun getItemId(index: Int): Long {
        return (data?.get(index)?.id ?: 0).toLong()
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