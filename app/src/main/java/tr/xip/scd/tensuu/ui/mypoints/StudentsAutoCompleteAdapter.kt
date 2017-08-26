package tr.xip.scd.tensuu.ui.mypoints

import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import android.widget.TextView
import io.realm.Case
import io.realm.OrderedRealmCollection
import io.realm.Realm
import io.realm.RealmBaseAdapter
import kotlinx.android.synthetic.main.item_simple_list.view.*
import tr.xip.scd.tensuu.App.Companion.context
import tr.xip.scd.tensuu.R
import tr.xip.scd.tensuu.data.model.Student
import tr.xip.scd.tensuu.data.model.StudentFields
import tr.xip.scd.tensuu.util.ext.getLayoutInflater

class StudentsAutoCompleteAdapter(val realm: Realm, var data: OrderedRealmCollection<Student>)
    : RealmBaseAdapter<Student>(data), Filterable {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        var v = convertView
        if (v == null) {
            v = context.getLayoutInflater().inflate(R.layout.item_simple_list, parent, false)
        }
        v!!.text.text = data[position].fullName
        return v
    }

    override fun getCount(): Int = data.size

    override fun getItem(position: Int): Student? = data[position]

    override fun getFilter(): Filter {
        return object : Filter() {
            var hasResults = false

            override fun convertResultToString(resultValue: Any?): CharSequence {
                return (resultValue as Student).fullName ?: "?"
            }

            override fun performFiltering(constraint: CharSequence?): FilterResults {
                val results = FilterResults()
                results.count = if (hasResults) 1 else 0
                return results
            }

            override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
                if (constraint != null) {
                    val q = constraint.toString()

                    data = realm
                            .where(Student::class.java)
                            .beginGroup()
                            .contains(StudentFields.FULL_NAME, q, Case.INSENSITIVE)
                            .or()
                            .contains(StudentFields.FULL_NAME_SIMPLIFIED, q, Case.INSENSITIVE)
                            .endGroup()
                            .findAll()
                    hasResults = data.size > 0
                }
                notifyDataSetChanged()
            }
        }
    }
}
