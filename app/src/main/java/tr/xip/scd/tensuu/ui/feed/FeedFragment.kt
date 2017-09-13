package tr.xip.scd.tensuu.ui.feed

import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.DecelerateInterpolator
import com.hannesdorfmann.mosby3.mvp.MvpFragment
import kotlinx.android.synthetic.main.fragment_feed.*
import tr.xip.scd.tensuu.R
import tr.xip.scd.tensuu.ui.common.AnimateableFragment
import tr.xip.scd.tensuu.util.ext.toPx

open class FeedFragment : MvpFragment<FeedView, FeedPresenter>(), FeedView, AnimateableFragment {

    override fun createPresenter(): FeedPresenter = FeedPresenter()

    override fun onResume() {
        super.onResume()
        recycler?.adapter?.notifyDataSetChanged()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        retainInstance = true
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_feed, container, false)
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        recycler.layoutManager = LinearLayoutManager(context)
    }

    override fun setAdapter(value: FeedAdapter) {
        recycler?.adapter = value
        animateRecycler()
    }

    override fun getAdapter(): FeedAdapter? {
        return recycler?.adapter as FeedAdapter?
    }

    override fun animateEnter() {
        /* no animations here */
    }

    override fun animateExit() {
        /* no animations here */
    }

    override fun getEnterDuration(): Long = 0

    override fun getExitDuration(): Long = 0

    private fun animateRecycler() {
        val amount = 24.toPx(context).toFloat()
        recycler.alpha = 0f
        recycler.translationY = recycler.translationY + amount
        recycler.animate()
                .alpha(1f)
                .translationYBy(-amount)
                .setInterpolator(DecelerateInterpolator())
                .setDuration(200)
                .start()
    }
}