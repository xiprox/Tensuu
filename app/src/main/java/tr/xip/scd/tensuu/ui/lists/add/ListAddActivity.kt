package tr.xip.scd.tensuu.ui.lists.add

import android.content.Intent
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import com.hannesdorfmann.mosby3.mvp.MvpActivity
import kotlinx.android.synthetic.main.activity_list_add.*
import tr.xip.scd.tensuu.App
import tr.xip.scd.tensuu.R
import tr.xip.scd.tensuu.data.model.Student
import tr.xip.scd.tensuu.ui.lists.StudentsAddingAdapter
import tr.xip.scd.tensuu.ui.mypoints.StudentsAutoCompleteAdapter
import tr.xip.scd.tensuu.util.ext.toVisibility
import tr.xip.scd.tensuu.util.ext.watchForChange
import tr.xip.scd.tensuu.util.ext.watchForChangeDebounce

class ListAddActivity : MvpActivity<ListAddView, ListAddPresenter>(), ListAddView {

    override fun createPresenter() = ListAddPresenter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_list_add)

        recycler.layoutManager = LinearLayoutManager(this)

        search.setOnItemClickListener { _, _, position, _ ->
            presenter.onNewStudentSelected(search.adapter.getItem(position) as Student)
            search.setText("")
        }
        search.threshold = 1

        exit.setOnClickListener {
            finish()
        }

        done.setOnClickListener {
            presenter.onDoneClicked()
        }

        name.watchForChangeDebounce {
            presenter.onNameChanged(it.toString())
        }

        search.watchForChange {
            presenter.onSearchTextChangedInstant(it)
        }

        search.watchForChangeDebounce {
            presenter.onSearchTextChanged(it)
        }

        clear.setOnClickListener {
            search.setText("")
        }

        presenter.init()
    }

    override fun setAdapter(value: StudentsAddingAdapter) {
        recycler.adapter = value
    }

    override fun getAdapter(): StudentsAddingAdapter? {
        return recycler.adapter as StudentsAddingAdapter?
    }

    override fun setAutoCompleteAdapter(value: StudentsAutoCompleteAdapter) {
        search.setAdapter(value)
    }

    override fun getAutoCompleteAdapter(): StudentsAutoCompleteAdapter? {
        return search.adapter as StudentsAutoCompleteAdapter?
    }

    override fun setSearchClearVisible(value: Boolean) {
        clear.visibility = value.toVisibility()
    }

    override fun runOnUi(body: () -> Unit) {
        runOnUiThread {
            body()
        }
    }

    override fun die() {
        finish()
    }

    companion object {

        fun getLaunchIntent(): Intent {
            val intent =
            return Intent(App.context, ListAddActivity::class.java)
        }
    }
}