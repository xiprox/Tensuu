package tr.xip.scd.tensuu.ui.lists

import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.DecelerateInterpolator
import com.hannesdorfmann.mosby3.mvp.MvpFragment
import kotlinx.android.synthetic.main.fragment_lists.*
import tr.xip.scd.tensuu.R
import tr.xip.scd.tensuu.ui.common.AnimateableFragment
import tr.xip.scd.tensuu.ui.common.view.RecyclerViewAdapterDataObserver
import tr.xip.scd.tensuu.util.ext.setDisplayedChildSafe
import tr.xip.scd.tensuu.util.ext.toPx

open class ListsFragment : MvpFragment<ListsView, ListsPresenter>(), ListsView, AnimateableFragment {

    val dataObserver = RecyclerViewAdapterDataObserver {
        flipper?.setDisplayedChildSafe(
                if (recycler?.adapter?.itemCount ?: 0 == 0) FLIPPER_NO_POINTS else FLIPPER_CONTENT
        )
    }

    override fun createPresenter(): ListsPresenter = ListsPresenter()

    override fun onResume() {
        super.onResume()
        recycler?.adapter?.notifyDataSetChanged()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        retainInstance = true
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_lists, container, false)
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        recycler.layoutManager = LinearLayoutManager(context)
        presenter.init()
    }

    override fun setAdapter(value: ListsAdapter) {
        recycler.adapter = value
        recycler.adapter.registerAdapterDataObserver(dataObserver)
        dataObserver.onChanged()
        animateRecycler()
    }

    override fun getAdapter(): ListsAdapter? {
        return recycler?.adapter as ListsAdapter?
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

    companion object {
        private val FLIPPER_CONTENT = 0
        private val FLIPPER_NO_POINTS = 1
    }
}