package tr.xip.scd.tensuu.ui.common.mvp

import com.hannesdorfmann.mosby3.mvp.MvpPresenter
import com.hannesdorfmann.mosby3.mvp.MvpView
import java.lang.ref.WeakReference

/**
 * A Kotlin implementation of [com.hannesdorfmann.mosby3.mvp.MvpBasePresenter] for null safety.
 *
 * Prevents subclasses from doing unsafe calls on [view].
 */
open class SafeViewMvpPresenter<T : MvpView> : MvpPresenter<T> {
    private var viewRef: WeakReference<T>? = null

    internal val view
        get() = if (viewRef == null) null else viewRef?.get()

    override fun attachView(view: T?) {
        viewRef = WeakReference<T>(view)
    }

    fun isAttached() = view != null

    override fun detachView(retainInstance: Boolean) {
        if (viewRef != null) {
            viewRef?.clear()
            viewRef = null
        }
    }
}