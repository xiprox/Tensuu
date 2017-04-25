package tr.xip.scd.tensuu.ui.mypoints

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.DecelerateInterpolator
import com.hannesdorfmann.mosby3.mvp.MvpFragment
import io.realm.Realm
import io.realm.Sort.DESCENDING
import kotlinx.android.synthetic.main.fragment_my_points.*
import tr.xip.scd.tensuu.R
import tr.xip.scd.tensuu.data.model.Point
import tr.xip.scd.tensuu.data.model.PointFields
import tr.xip.scd.tensuu.local.Credentials
import tr.xip.scd.tensuu.ui.common.AnimateableFragment
import tr.xip.scd.tensuu.ui.common.adapter.PointsAdapter
import tr.xip.scd.tensuu.ui.common.view.RecyclerViewAdapterDataObserver
import tr.xip.scd.tensuu.util.RealmUtils.syncedRealm
import tr.xip.scd.tensuu.util.ext.setDisplayedChildSafe
import tr.xip.scd.tensuu.util.ext.toPx

class MyPointsFragment : MvpFragment<MyPointsView, MyPointsPresenter>(), MyPointsView, AnimateableFragment {
    override fun createPresenter(): MyPointsPresenter = MyPointsPresenter()

    val dataObserver = RecyclerViewAdapterDataObserver {
        flipper?.setDisplayedChildSafe(
                if (recycler?.adapter?.itemCount ?: 0 == 0) {
                    FLIPPER_NO_POINTS
                } else {
                    FLIPPER_CONTENT
                }
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        retainInstance = true
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_my_points, container, false)
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        recycler.layoutManager = LinearLayoutManager(context)
        presenter.init()
    }

    override fun setAdapter(value: PointsAdapter) {
        recycler.adapter = value
        recycler.adapter.registerAdapterDataObserver(dataObserver)
        dataObserver.onChanged()
        animateRecycler()
    }

    override fun getAdapter(): PointsAdapter? {
        return recycler.adapter as PointsAdapter?
    }

    override fun animateEnter() {
        /* nothing here */
    }

    override fun animateExit() {
        /* nothing here */
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