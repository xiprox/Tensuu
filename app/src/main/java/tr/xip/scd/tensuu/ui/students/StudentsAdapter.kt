package tr.xip.scd.tensuu.ui.students

import android.annotation.SuppressLint
import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.ViewGroup
import io.realm.OrderedRealmCollection
import io.realm.Realm
import io.realm.RealmRecyclerViewAdapter
import kotlinx.android.synthetic.main.item_student.view.*
import tr.xip.scd.tensuu.App
import tr.xip.scd.tensuu.R
import tr.xip.scd.tensuu.realm.model.Period
import tr.xip.scd.tensuu.realm.model.Point
import tr.xip.scd.tensuu.realm.model.PointFields
import tr.xip.scd.tensuu.realm.model.Student
import tr.xip.scd.tensuu.common.ext.getLayoutInflater

class StudentsAdapter(
        val realm: Realm,
        data: OrderedRealmCollection<Student>,
        val itemClickListener: ((clickedView: View) -> Unit)? = null
) : RealmRecyclerViewAdapter<Student, StudentsAdapter.ViewHolder>(data, true) {

    private val period: Period? by lazy { realm.where(Period::class.java).findFirst() }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = parent.context.getLayoutInflater().inflate(R.layout.item_student, parent, false)
        if (itemClickListener != null) {
            v.setOnClickListener { itemClickListener.invoke(v) }
        }
        return ViewHolder(v)
    }

    @SuppressLint("SimpleDateFormat")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = data?.get(position) ?: return

        holder.itemView.fullName.text = item.fullName ?: "?"
        holder.itemView.grade.text = "?"
        item.grade?.let {
            holder.itemView.grade.text = App.context.getString(R.string.year_x, it)
        }

        val points = 100 + realm.where(Point::class.java)
                .equalTo(PointFields.TO.SSID, item.ssid)
                .greaterThanOrEqualTo(PointFields.TIMESTAMP, period?.start ?: 0)
                .lessThanOrEqualTo(PointFields.TIMESTAMP, period?.end ?: 0)
                .sum(PointFields.AMOUNT).toInt()
        holder.itemView.points.text = points.toString()
    }

    class ViewHolder(v: View) : RecyclerView.ViewHolder(v)
}
