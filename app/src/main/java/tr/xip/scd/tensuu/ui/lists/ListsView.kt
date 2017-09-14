package tr.xip.scd.tensuu.ui.lists

import com.hannesdorfmann.mosby3.mvp.MvpView
import tr.xip.scd.tensuu.ui.feed.FeedAdapter

interface ListsView : MvpView {
    fun setAdapter(value: ListsAdapter)
    fun getAdapter(): ListsAdapter?
}