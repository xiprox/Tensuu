package tr.xip.scd.tensuu.ui.main

import android.os.Bundle
import com.hannesdorfmann.mosby3.mvp.viewstate.RestorableViewState

class MainViewState : RestorableViewState<MainView> {
    var isFabShown: Boolean? = null

    override fun saveInstanceState(out: Bundle) {
        out.putBoolean(KEY_IS_FAB_SHOWN, isFabShown ?: false)
    }

    override fun restoreInstanceState(bundle: Bundle?): RestorableViewState<MainView> {
        isFabShown = bundle?.getBoolean(KEY_IS_FAB_SHOWN)
        return this
    }

    override fun apply(view: MainView?, retained: Boolean) {
        view?.showFab(isFabShown ?: false)
    }

    companion object {
        private val KEY_IS_FAB_SHOWN = "is_fab_shown"
    }
}