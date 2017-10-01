package tr.xip.scd.tensuu.common.ui.view

import android.support.v7.widget.RecyclerView

/**
 * [RecyclerView.AdapterDataObserver] implementation which calls [onChanged] for all kinds of changes.
 *
 * WARNING: [onChanged] may be called multiple times.
 */
class RecyclerViewAdapterDataObserver(val body: () -> Unit) : RecyclerView.AdapterDataObserver() {

    override fun onChanged() {
        super.onChanged()
        body.invoke()
    }

    override fun onItemRangeRemoved(positionStart: Int, itemCount: Int) {
        super.onItemRangeRemoved(positionStart, itemCount)
        onChanged()
    }

    override fun onItemRangeMoved(fromPosition: Int, toPosition: Int, itemCount: Int) {
        super.onItemRangeMoved(fromPosition, toPosition, itemCount)
        onChanged()
    }

    override fun onItemRangeInserted(positionStart: Int, itemCount: Int) {
        super.onItemRangeInserted(positionStart, itemCount)
        onChanged()
    }

    override fun onItemRangeChanged(positionStart: Int, itemCount: Int) {
        super.onItemRangeChanged(positionStart, itemCount)
        onChanged()
    }

    override fun onItemRangeChanged(positionStart: Int, itemCount: Int, payload: Any?) {
        super.onItemRangeChanged(positionStart, itemCount, payload)
        onChanged()
    }
}