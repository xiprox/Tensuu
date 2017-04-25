package tr.xip.scd.tensuu.ui.students

import android.annotation.SuppressLint
import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.ViewGroup
import io.realm.OrderedRealmCollection
import io.realm.Realm
import io.realm.RealmRecyclerViewAdapter
import kotlinx.android.synthetic.main.item_student.view.*
import tr.xip.scd.tensuu.R
import tr.xip.scd.tensuu.data.model.Point
import tr.xip.scd.tensuu.data.model.PointFields
import tr.xip.scd.tensuu.data.model.Student
import tr.xip.scd.tensuu.util.ext.getLayoutInflater

class StudentsAdapter(
        val realm: Realm,
        data: OrderedRealmCollection<Student>,
        val itemClickListener: ((clickedView: View) -> Unit)? = null
) : RealmRecyclerViewAdapter<Student, StudentsAdapter.ViewHolder>(data, true) {

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
        holder.itemView.grade.text = item.grade ?: "?"

        val points = 100 + realm.where(Point::class.java)
                .equalTo(PointFields.TO.SSID, item.ssid)
                .sum(PointFields.AMOUNT).toInt()
        holder.itemView.points.text = points.toString()
    }

    override fun getItemId(index: Int): Long {
        return (data?.get(index)?.id ?: 0).toLong()
    }

    class ViewHolder(v: View) : RecyclerView.ViewHolder(v)
}
