package tr.xip.scd.tensuu.ui.mypoints

import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import io.realm.Case
import io.realm.OrderedRealmCollection
import io.realm.Realm
import io.realm.RealmBaseAdapter
import kotlinx.android.synthetic.main.item_simple_list.view.*
import tr.xip.scd.tensuu.App
import tr.xip.scd.tensuu.R
import tr.xip.scd.tensuu.realm.model.PointReason
import tr.xip.scd.tensuu.realm.model.PointReasonFields
import tr.xip.scd.tensuu.common.ext.getLayoutInflater

class ReasonsAutoCompleteAdapter(val realm: Realm, var data: OrderedRealmCollection<PointReason>)
    : RealmBaseAdapter<PointReason>(data), Filterable {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        var v = convertView
        if (v == null) {
            v = App.context.getLayoutInflater().inflate(R.layout.item_simple_list, parent, false)
        }
        v!!.text.text = data[position].text
        return v
    }

    override fun getCount(): Int = data.size

    override fun getItem(position: Int): PointReason? = data[position]

    override fun getFilter(): Filter {
        return object : Filter() {
            var hasResults = false

            override fun convertResultToString(resultValue: Any?): CharSequence {
                return (resultValue as PointReason).text ?: "?"
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
                            .where(PointReason::class.java)
                            .contains(PointReasonFields.TEXT, q, Case.INSENSITIVE)
                            .findAll()
                    hasResults = data.size > 0
                }
                notifyDataSetChanged()
            }
        }
    }
}