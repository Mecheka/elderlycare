package besmart.elderlycare.screen.history

import android.content.Intent
import android.os.Bundle
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import besmart.elderlycare.R
import besmart.elderlycare.model.history.HistoryResponce
import besmart.elderlycare.model.profile.ProfileResponce
import besmart.elderlycare.screen.base.BaseActivity
import besmart.elderlycare.screen.historydetail.HistoryDetailActivity
import besmart.elderlycare.util.BaseDialog
import besmart.elderlycare.util.SimpleOnItemClick
import kotlinx.android.synthetic.main.activity_main.*
import org.koin.android.viewmodel.ext.android.viewModel

class HistoryActivity : BaseActivity(), SimpleOnItemClick<HistoryResponce> {

    companion object {
        const val PROFILE = "profile"
    }

    private lateinit var profile: ProfileResponce
    private val viewModel: HistoryViewModel by viewModel()
    private lateinit var historyAdapter: HistoryAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_history)
        profile = intent.getParcelableExtra(PROFILE)
        initInstance()
        observerViewModel()
    }

    private fun initInstance() {
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        toolbar.setNavigationOnClickListener {
            finish()
        }
        recyclerView.layoutManager = LinearLayoutManager(this, RecyclerView.VERTICAL, false)
        recyclerView.addItemDecoration(DividerItemDecoration(this, DividerItemDecoration.VERTICAL))
        recyclerView.hasFixedSize()
    }

    private fun observerViewModel(){
        viewModel.errorLiveData.observe(this, Observer {
            BaseDialog.warningDialog(this, it)
        })

        viewModel.loadingLiveData.observe(this, Observer {
            if (it) {
                showLoadingDialog(this)
            } else {
                dismissDialog()
            }
        })

        viewModel.historyLiveData.observe(this, Observer {
            historyAdapter = HistoryAdapter(it,this)
            recyclerView.adapter = historyAdapter
        })

        viewModel.getHistoryByCardId(profile)
    }

    override fun onItemClick(item: HistoryResponce) {
        Intent().apply {
            this.setClass(this@HistoryActivity, HistoryDetailActivity::class.java)
            this.putExtra(HistoryDetailActivity.HISTORY, item)
            startActivity(this)
        }
    }
}
