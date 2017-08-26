package tr.xip.scd.tensuu.ui.mypoints

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.support.v7.app.AlertDialog
import android.view.View
import android.widget.EditText
import io.realm.Realm
import kotlinx.android.synthetic.main.dialog_add_point.view.*
import tr.xip.scd.tensuu.App.Companion.context
import tr.xip.scd.tensuu.R
import tr.xip.scd.tensuu.data.model.*
import tr.xip.scd.tensuu.local.Credentials
import tr.xip.scd.tensuu.local.Store
import tr.xip.scd.tensuu.ui.common.DatePickerDialog
import tr.xip.scd.tensuu.util.ext.getLayoutInflater
import tr.xip.scd.tensuu.util.ext.strippedTimestamp
import java.text.SimpleDateFormat
import java.util.*

object AddPointDialog {

    @SuppressLint("InflateParams")
    fun new(realm: Realm, context: Context): AlertDialog? {
        val v = context.getLayoutInflater().inflate(R.layout.dialog_add_point, null, false)

        val studentsAdapter = StudentsAutoCompleteAdapter(realm, realm.where(Student::class.java).findAll())
        var selectedStudent: Student? = null
        v.to.setOnItemClickListener { _, _, position, _ ->
            selectedStudent = studentsAdapter.getItem(position)
        }
        v.to.threshold = 1
        v.to.setAdapter(studentsAdapter)
        v.to.setSelection(0)

        val reasonsAdapter = ReasonsAutoCompleteAdapter(realm, realm.where(PointReason::class.java).findAll())
        v.reason.threshold = 1
        v.reason.setAdapter(reasonsAdapter)

        val date = Calendar.getInstance()

        // Load timestamp from Store if one was persisted less than an hour ago
        if (Store.lastPointTimestampUpdated > System.currentTimeMillis() - 60 * 60 * 1000) {
            date.timeInMillis = Store.lastPointAddTimestamp
        }

        updateDate(v.date, date) // Initial

        v.date.setOnClickListener {
            DatePickerDialog.new(context, date.get(Calendar.YEAR), date.get(Calendar.MONTH), date.get(Calendar.DAY_OF_MONTH),
                    { y, m, d ->
                        date.set(y, m, d)
                        updateDate(v.date, date)
                    }
            )
        }

        val dialog = AlertDialog.Builder(context)
                .setView(v)
                .setTitle(R.string.title_add_point)
                .setPositiveButton(R.string.action_add, null)
                .setNegativeButton(R.string.action_cancel, null)
                .create()

        dialog.setOnShowListener {
            val positive = dialog.getButton(AlertDialog.BUTTON_POSITIVE)

            positive.setOnClickListener {
                val amountText = v.amount.text.toString()
                var reason: String? = v.reason.text?.toString()
                if (reason?.trim()?.isEmpty() == true) reason = null

                if (validateFields(v, amountText, selectedStudent, reason)) {
                    val point = Point()
                    point.amount = amountText.toInt()
                    point.to = selectedStudent
                    point.from = realm.where(User::class.java)
                            .equalTo(UserFields.EMAIL, Credentials.email)
                            .findFirst()
                    point.timestamp = date.strippedTimestamp()
                    point.reason = reason

                    realm.executeTransaction {
                        it.copyToRealm(point)
                    }

                    persistData(realm, date, reason)

                    dialog.dismiss()
                }
            }
        }

        v.addMultiple.setOnClickListener {
            context.startActivity(Intent(context, BatchPointsAddActivity::class.java))
            dialog.dismiss()
        }

        return dialog
    }

    @SuppressLint("SimpleDateFormat")
    private fun updateDate(view: EditText, cal: Calendar) {
        view.setText(SimpleDateFormat("dd/MM/yyyy").format(cal.timeInMillis))
    }

    private fun validateFields(v: View, amount: String?, to: Student?, reason: String?): Boolean {
        if (amount == null || amount.replace("-", "").trim().isEmpty()) {
            v.amountLayout.error = context.getString(R.string.error_invalid_amount)
            return false
        } else {
            v.amountLayout.error = null
        }
        if (to == null) {
            v.toLayout.error = context.getString(R.string.error_invalid_student)
            return false
        } else {
            v.toLayout.error = null
        }
        return true
    }

    private fun persistData(realm: Realm, date: Calendar, reason: String?) {
        Store.lastPointAddTimestamp = date.strippedTimestamp()
        Store.lastPointTimestampUpdated = System.currentTimeMillis()

        reason?.let {
            val pointReason = PointReason()
            pointReason.text = reason
            val existingRecord = realm.where(PointReason::class.java)
                    .equalTo(PointReasonFields.TEXT, reason).findFirst()
            if (existingRecord == null) {
                realm.executeTransaction {
                    realm.copyToRealm(pointReason)
                }
            }
        }
    }
}