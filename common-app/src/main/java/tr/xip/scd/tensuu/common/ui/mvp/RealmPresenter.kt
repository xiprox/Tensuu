package tr.xip.scd.tensuu.common.ui.mvp

import com.hannesdorfmann.mosby3.mvp.MvpView
import io.realm.Realm
import tr.xip.scd.tensuu.realm.util.RealmUtils.syncedRealm

open class RealmPresenter<T : MvpView> : SafeViewMvpPresenter<T>() {
    lateinit var realm: Realm

    override fun attachView(view: T?) {
        super.attachView(view)
        realm = syncedRealm()
    }

    override fun detachView(retainInstance: Boolean) {
        super.detachView(retainInstance)
        realm.close()
    }
}