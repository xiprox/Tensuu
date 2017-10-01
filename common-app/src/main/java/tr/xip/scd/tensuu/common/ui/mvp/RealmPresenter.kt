package tr.xip.scd.tensuu.common.ui.mvp



import com.hannesdorfmann.mosby3.mvp.MvpView
import io.realm.Realm
import tr.xip.scd.tensuu.realm.util.RealmUtils.syncedRealm

open class RealmPresenter<T : MvpView> : SafeViewMvpPresenter<T>() {
    val realm: Realm by lazy {
        syncedRealm()
    }

    override fun detachView(retainInstance: Boolean) {
        super.detachView(retainInstance)
        realm.close()
    }
}