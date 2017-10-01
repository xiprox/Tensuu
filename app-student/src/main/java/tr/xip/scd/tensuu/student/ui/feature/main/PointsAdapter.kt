package tr.xip.scd.tensuu.student.ui.feature.main

import android.annotation.SuppressLint
import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.ViewGroup
import android.view.animation.DecelerateInterpolator
import io.realm.OrderedRealmCollection
import io.realm.RealmRecyclerViewAdapter
import kotlinx.android.synthetic.main.item_point.view.*
import tr.xip.scd.tensuu.common.ext.getLayoutInflater
import tr.xip.scd.tensuu.common.ext.isToday
import tr.xip.scd.tensuu.common.ext.isYesterday
import tr.xip.scd.tensuu.realm.model.Point
import tr.xip.scd.tensuu.student.App.Companion.context
import tr.xip.scd.tensuu.student.R
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
        holder.itemView.amount.setBackgroundResource(
                if ((item.amount ?: 0) < 0) R.drawable.oval_minus else R.drawable.oval_plus
        )

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
            holder.itemView.time.visibility = View.VISIBLE
        } else {
            holder.itemView.time.visibility = View.GONE
        }

        /* Reason */
        if (item.reason != null) {
            holder.itemView.reason.text = item.reason
            holder.itemView.root.setOnClickListener(reasonClickListener)
            holder.itemView.expandIndicator.visibility = View.VISIBLE
        } else {
            holder.itemView.root.setOnClickListener(null)
            holder.itemView.expandIndicator.visibility = View.GONE
        }
    }

    class ViewHolder(v: View) : RecyclerView.ViewHolder(v)

    private object reasonClickListener : View.OnClickListener {
        override fun onClick(v: View) {
            val expand = v.reasonContainer.visibility == View.GONE
            v.reasonContainer.visibility = if (expand) View.VISIBLE else View.GONE
            v.expandIndicator.animate()
                    .rotation(if (expand) 180f else 0f)
                    .setInterpolator(DecelerateInterpolator())
                    .setDuration(200)
                    .start()
        }
    }
}