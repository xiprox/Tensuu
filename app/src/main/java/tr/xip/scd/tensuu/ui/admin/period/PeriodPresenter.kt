package tr.xip.scd.tensuu.ui.admin.period

import tr.xip.scd.tensuu.data.model.Period
import tr.xip.scd.tensuu.ui.common.mvp.RealmPresenter
import tr.xip.scd.tensuu.util.ext.setTimeInMillisAndReturn
import tr.xip.scd.tensuu.util.ext.strippedTimestamp
import java.util.*

class PeriodPresenter : RealmPresenter<PeriodView>() {
    var period: Period? = null

    fun init() {
        period = realm.where(Period::class.java).findFirst()
        if (period == null) {
            realm.executeTransaction {
                period = it.createObject(Period::class.java)
            }
        }

        view?.setStartDate(period?.start ?: 0)
        view?.setEndDate(period?.end ?: 0)
    }

    fun onStartDateClicked() {
        val cal = GregorianCalendar().setTimeInMillisAndReturn(period?.start ?: 0)
        view?.showStartDateDialog(cal) {
            view?.setStartDate(it.strippedTimestamp())
            val newCal = it
            realm.executeTransaction {
                period?.start = newCal.strippedTimestamp()
            }
        }
    }

    fun onEndDateClicked() {
        val cal = GregorianCalendar().setTimeInMillisAndReturn(period?.end ?: 0)
        view?.showStartDateDialog(cal) {
            view?.setEndDate(it.strippedTimestamp())
            val newCal = it
            realm.executeTransaction {
                period?.end = newCal.strippedTimestamp()
            }
        }
    }
}