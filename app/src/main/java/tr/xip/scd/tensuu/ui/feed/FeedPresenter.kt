package tr.xip.scd.tensuu.ui.feed

import io.realm.Sort
import tr.xip.scd.tensuu.App
import tr.xip.scd.tensuu.realm.model.Point
import tr.xip.scd.tensuu.realm.model.PointFields
import tr.xip.scd.tensuu.common.ui.mvp.RealmPresenter

class FeedPresenter : RealmPresenter<FeedView>() {

    override fun attachView(view: FeedView?) {
        super.attachView(view)
        init()
    }

    private fun init() {
        view?.setAdapter(
                FeedAdapter(
                        App.context, realm,
                        realm.where(Point::class.java)
                                .findAllSorted(PointFields.TIMESTAMP, Sort.DESCENDING)
                )
        )
    }
}