package tr.xip.scd.tensuu.ui.mypoints

import android.annotation.SuppressLint
import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.ViewGroup
import io.realm.Realm
import kotlinx.android.synthetic.main.item_student_batch_point_add.view.*
import tr.xip.scd.tensuu.R
import tr.xip.scd.tensuu.realm.model.Point
import tr.xip.scd.tensuu.util.ext.getLayoutInflater
import tr.xip.scd.tensuu.util.ext.watchForChange

class BatchPointsStudentsAdapter(
        private val realm: Realm,
        val data: MutableList<Point>,
        private val removeClickedListener: ((position: Int) -> Unit)? = null
) : RecyclerView.Adapter<BatchPointsStudentsAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = parent.context.getLayoutInflater().inflate(R.layout.item_student_batch_point_add, parent, false)
        return ViewHolder(v)
    }

    @SuppressLint("SimpleDateFormat")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = data[position]
        val view = holder.itemView

        view.remove.setOnClickListener {
            removeClickedListener?.invoke(position)
        }

        view.student.text = item.to?.fullName

        view.amount.setText("${item.amount}")
        view.amount.watchForChange {
            val text = it.toString()
            if (text != "-" && text.isNotBlank()) {
                realm.executeTransaction {
                    item.amount = text.toInt()
                }
            }
        }
    }

    override fun getItemCount(): Int = data.size

    class ViewHolder(v: View) : RecyclerView.ViewHolder(v)
}