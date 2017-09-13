package tr.xip.scd.tensuu.ui.feed

import com.hannesdorfmann.mosby3.mvp.MvpView

interface FeedView : MvpView {
    fun setAdapter(value: FeedAdapter)
    fun getAdapter(): FeedAdapter?
}