package tr.xip.scd.tensuu.ui.reports

import android.annotation.SuppressLint
import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.ViewGroup
import io.realm.OrderedRealmCollection
import io.realm.Realm
import io.realm.RealmRecyclerViewAdapter
import kotlinx.android.synthetic.main.item_report_student.view.*
import tr.xip.scd.tensuu.App.Companion.context
import tr.xip.scd.tensuu.R
import tr.xip.scd.tensuu.realm.model.Point
import tr.xip.scd.tensuu.realm.model.PointFields
import tr.xip.scd.tensuu.realm.model.Student
import tr.xip.scd.tensuu.common.ext.getLayoutInflater

class ReportsAdapter(
        val realm: Realm,
        var beginTimestamp: Long,
        var endTimestamp: Long,
        val weekly: Boolean,
        data: OrderedRealmCollection<Student>,
        val itemClickListener: ((clickedView: View) -> Unit)? = null
) : RealmRecyclerViewAdapter<Student, ReportsAdapter.ViewHolder>(data, true) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = parent.context.getLayoutInflater().inflate(R.layout.item_report_student, parent, false)
        if (itemClickListener != null) {
            v.setOnClickListener { itemClickListener.invoke(v) }
        }
        return ViewHolder(v)
    }

    @SuppressLint("SimpleDateFormat")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = data?.get(position) ?: return

        holder.itemView.fullName.text = item.fullName ?: "?"

        holder.itemView.floor.text = when (item.floor ?: -1) {
            2 -> context.getString(R.string.second_floor)
            3 -> context.getString(R.string.third_floor)
            else -> context.getString(R.string.question_floor)
        }

        var points = realm.where(Point::class.java)
                .equalTo(PointFields.TO.SSID, item.ssid)
                .greaterThanOrEqualTo(PointFields.TIMESTAMP, beginTimestamp)
                .lessThanOrEqualTo(PointFields.TIMESTAMP, endTimestamp)
                .sum(PointFields.AMOUNT).toInt()
        if (!weekly) points += 100
        holder.itemView.points.text = points.toString()
    }

    fun updateDates(begin: Long, end: Long) {
        beginTimestamp = begin
        endTimestamp = end
        notifyDataSetChanged()
    }

    class ViewHolder(v: View) : RecyclerView.ViewHolder(v)
}
