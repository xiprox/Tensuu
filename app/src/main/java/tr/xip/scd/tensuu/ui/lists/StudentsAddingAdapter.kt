package tr.xip.scd.tensuu.ui.lists

import android.annotation.SuppressLint
import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.View.INVISIBLE
import android.view.View.VISIBLE
import android.view.ViewGroup
import io.realm.OrderedRealmCollection
import kotlinx.android.synthetic.main.item_student_list_add.view.*
import tr.xip.scd.tensuu.R
import tr.xip.scd.tensuu.data.model.Student
import tr.xip.scd.tensuu.util.ext.getLayoutInflater

class StudentsAddingAdapter(
        val data: OrderedRealmCollection<Student>?,
        private val showDeleteButton: Boolean = true,
        private val removeClickedListener: ((position: Int) -> Unit)? = null
) : RecyclerView.Adapter<StudentsAddingAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = parent.context.getLayoutInflater().inflate(R.layout.item_student_list_add, parent, false)
        return ViewHolder(v)
    }

    @SuppressLint("SimpleDateFormat")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = data?.get(position)
        val view = holder.itemView

        view.student.text = item?.fullName ?: "?"
        view.remove.visibility = if (showDeleteButton) VISIBLE else INVISIBLE
        view.remove.setOnClickListener {
            removeClickedListener?.invoke(position)
        }
    }

    override fun getItemCount(): Int = data?.size ?: 0

    class ViewHolder(v: View) : RecyclerView.ViewHolder(v)
}