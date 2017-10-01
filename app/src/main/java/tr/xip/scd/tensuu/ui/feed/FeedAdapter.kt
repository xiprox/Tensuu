package tr.xip.scd.tensuu.ui.feed

import android.annotation.SuppressLint
import android.content.Context
import android.support.v7.widget.RecyclerView
import android.text.Html
import android.view.View
import android.view.ViewGroup
import io.realm.OrderedRealmCollection
import io.realm.Realm
import io.realm.RealmRecyclerViewAdapter
import kotlinx.android.synthetic.main.item_feed.view.*
import tr.xip.scd.tensuu.R
import tr.xip.scd.tensuu.realm.model.Point
import tr.xip.scd.tensuu.util.ext.getLayoutInflater
import java.text.SimpleDateFormat

class FeedAdapter(
        val context: Context,
        val realm: Realm, data: OrderedRealmCollection<Point>
) : RealmRecyclerViewAdapter<Point, FeedAdapter.ViewHolder>(data, true) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = parent.context.getLayoutInflater().inflate(R.layout.item_feed, parent, false)
        return ViewHolder(v)
    }

    @SuppressLint("SimpleDateFormat")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = data?.get(position) ?: return

        holder.itemView.oval.setBackgroundResource(
                if (item.amount ?: -1 < 0) R.drawable.oval_minus else R.drawable.oval_plus
        )
        holder.itemView.text.text = Html.fromHtml(context.getString(
                R.string.x_added_y_points_to_z,
                item.from?.name ?: "?",
                item.amount.toString(),
                item.to?.fullName ?: "?"
        ))
        holder.itemView.date.text = SimpleDateFormat("EEE, MMM d, yyyy, h:mm a").format(item.timestamp)
    }

    class ViewHolder(v: View) : RecyclerView.ViewHolder(v)
}