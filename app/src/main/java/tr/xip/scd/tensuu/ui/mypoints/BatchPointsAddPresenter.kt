package tr.xip.scd.tensuu.ui.mypoints

import android.content.Context
import android.text.Editable
import io.realm.Case
import tr.xip.scd.tensuu.data.model.*
import tr.xip.scd.tensuu.local.Credentials
import tr.xip.scd.tensuu.local.Store
import tr.xip.scd.tensuu.ui.common.DatePickerDialog
import tr.xip.scd.tensuu.ui.common.mvp.RealmPresenter
import tr.xip.scd.tensuu.util.ext.strippedTimestamp
import java.util.Calendar.HOUR_OF_DAY
import java.util.Calendar.MINUTE
import java.util.Calendar.SECOND
import java.util.Calendar.MILLISECOND
import java.util.*

class BatchPointsAddPresenter : RealmPresenter<BatchPointsAddView>() {
    private val studentPoints = arrayListOf<Point>()

    private var mainAmount = 0
    private val date = Calendar.getInstance()
    private var reason: String? = null

    fun init() {
        view?.setAdapter(BatchPointsStudentsAdapter(realm, studentPoints,
                removeClickedListener = {
                    onStudentRemoveClicked(it)
                }
        ))

        view?.setStudentsAdapter(
                StudentsAutoCompleteAdapter(realm, realm.where(Student::class.java).findAll())
        )

        view?.setReasonsAdapter(
                ReasonsAutoCompleteAdapter(realm, realm.where(PointReason::class.java).findAll())
        )

        // Load timestamp from Store if one was persisted less than an hour ago
        if (Store.lastPointTimestampUpdated > System.currentTimeMillis() - 60 * 60 * 1000) {
            date.timeInMillis = Store.lastPointAddTimestamp
        }
        view?.setDate(date)
    }

    fun addStudents(studentIds: List<String>) {
        val query = realm.where(Student::class.java)
        studentIds.forEach {
            query.or().equalTo(StudentFields.SSID, it)
        }
        query.findAll().forEach { onNewStudentSelected(it) }
    }

    fun onDoneClicked() {
        realm.executeTransaction {
            for (point in studentPoints) {
                if (point.amount != 0) it.copyToRealm(point)
            }
            if (reason?.isNotBlank() == true) {
                val pointReason = PointReason()
                pointReason.text = reason
                val existing = realm.where(PointReason::class.java).equalTo(PointReasonFields.TEXT, reason)
                        .findFirst()
                if (existing == null) {
                    realm.copyToRealm(pointReason)
                }
            }
        }
        Store.lastPointAddTimestamp = date.strippedTimestamp()
        Store.lastPointTimestampUpdated = System.currentTimeMillis()
        view?.die()
    }

    fun onMainAmountChanged(amount: Int) {
        mainAmount = amount
        for (point in studentPoints) {
            point.amount = amount
        }
        view?.runOnUi {
            view?.getAdapter()?.notifyDataSetChanged()
        }
    }

    fun onDateClicked(context: Context) {
        DatePickerDialog.new(context, date.get(Calendar.YEAR), date.get(Calendar.MONTH),
                date.get(Calendar.DAY_OF_MONTH),
                { y, m, d ->
                    date.set(y, m, d)
                    val now = Calendar.getInstance()
                    date.set(HOUR_OF_DAY, now.get(HOUR_OF_DAY))
                    date.set(MINUTE, now.get(MINUTE))
                    date.set(SECOND, now.get(SECOND))
                    date.set(MILLISECOND, now.get(MILLISECOND))
                    view?.setDate(date)
                }
        )
    }

    fun onReasonChanged(reason: String) {
        this.reason = if (reason.isBlank()) null else reason
    }

    fun onNewStudentSelected(student: Student) {
        val point = Point()
        point.to = student
        point.from = realm.where(User::class.java).equalTo(UserFields.EMAIL, Credentials.email).findFirst()
        point.amount = mainAmount
        point.reason = reason
        point.timestamp = date.timeInMillis
        studentPoints.add(point)
        view?.getAdapter()?.notifyItemInserted(studentPoints.indexOf(point))
    }

    private fun onStudentRemoveClicked(position: Int) {
        val adapter = view?.getAdapter() ?: return
        val lastIndex = adapter.itemCount - 1
        adapter.data.removeAt(position)
        adapter.notifyItemRemoved(position)
        adapter.notifyItemRangeChanged(position, lastIndex)
    }

    fun onSearchTextChangedInstant(s: Editable?) {
        view?.setSearchClearVisible(s?.toString()?.isNotEmpty() ?: false)
    }

    fun onSearchTextChanged(s: Editable?) {
        val q = s?.toString() ?: return

        view?.runOnUi {
            view?.getStudentsAdapter()?.updateData(
                    realm.where(Student::class.java)
                            .beginGroup()
                            .contains(StudentFields.FULL_NAME, q, Case.INSENSITIVE)
                            .or()
                            .contains(StudentFields.FULL_NAME_SIMPLIFIED, q, Case.INSENSITIVE)
                            .endGroup()
                            .findAll()
            )
        }
    }
}