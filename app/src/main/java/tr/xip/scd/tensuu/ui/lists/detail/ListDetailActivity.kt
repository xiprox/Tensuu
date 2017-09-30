package tr.xip.scd.tensuu.ui.lists.detail

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.view.Menu
import android.view.MenuItem
import android.widget.EditText
import android.widget.Toast
import com.hannesdorfmann.mosby3.mvp.MvpActivity
import kotlinx.android.synthetic.main.activity_list_details.*
import tr.xip.scd.tensuu.App
import tr.xip.scd.tensuu.R
import tr.xip.scd.tensuu.ui.common.view.RecyclerViewAdapterDataObserver
import tr.xip.scd.tensuu.ui.lists.StudentsAddingAdapter
import tr.xip.scd.tensuu.ui.lists.add.ListAddActivity
import tr.xip.scd.tensuu.util.ext.setDisplayedChildSafe
import tr.xip.scd.tensuu.util.ext.toVisibility

class ListDetailActivity : MvpActivity<ListDetailView, ListDetailPresenter>(), ListDetailView {

    private val dataObserver = RecyclerViewAdapterDataObserver {
        flipper?.setDisplayedChildSafe(
                if (recycler?.adapter?.itemCount ?: 0 == 0) FLIPPER_NO_POINTS else FLIPPER_CONTENT
        )
    }

    override fun createPresenter(): ListDetailPresenter = ListDetailPresenter()

    override fun onResume() {
        super.onResume()
        recycler?.adapter?.notifyDataSetChanged()
    }

    @SuppressLint("SimpleDateFormat")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_list_details)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        recycler.layoutManager = LinearLayoutManager(this)

        edit.setOnClickListener {
            presenter.onEditClicked()
        }

        val extras = intent.extras
        if (extras != null) {
            presenter.loadWith(extras.getString(ARG_LIST_NAME), extras.getString(ARG_OWNER_EMAIL))
        } else {
            finish()
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        super.onBackPressed()
        return true
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.list_detail, menu)
        return super.onCreateOptionsMenu(menu)
    }

    @SuppressLint("SimpleDateFormat")
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        presenter.onOptionsItemSelected(item)
        return super.onOptionsItemSelected(item)
    }

    override fun setName(value: String) {
        title = value
    }

    override fun setEditButtonShown(show: Boolean) {
        edit.visibility = show.toVisibility()
    }

    override fun setAdapter(value: StudentsAddingAdapter) {
        recycler.adapter = value
        recycler.adapter.registerAdapterDataObserver(dataObserver)
        dataObserver.onChanged()
    }

    override fun getAdapter(): StudentsAddingAdapter {
        return recycler.adapter as StudentsAddingAdapter
    }

    override fun showRenameDialog() {
        val input = EditText(this)
        AlertDialog.Builder(this)
                .setView(input)
                .setPositiveButton(R.string.ok, { _, _ ->
                    val text = input.text.toString()
                    if (text.isNotBlank()) {
                        presenter.onRenameOkClicked(input.text.toString())
                    }
                })
                .setNegativeButton(R.string.action_cancel, null)
                .show()
    }

    override fun startListEditActivity(listName: String) {
        startActivity(ListAddActivity.getLaunchIntent(listName))
    }

    override fun die() {
        finish()
    }

    companion object {
        private val FLIPPER_CONTENT = 0
        private val FLIPPER_NO_POINTS = 1

        private val ARG_LIST_NAME = "list name"
        private val ARG_OWNER_EMAIL = "owner email"

        fun getLaunchIntent(listName: String?, ownerEmail: String?): Intent {
            val intent = Intent(App.context, ListDetailActivity::class.java)
            intent.putExtra(ARG_LIST_NAME, listName)
            intent.putExtra(ARG_OWNER_EMAIL, ownerEmail)
            return intent
        }
    }
}