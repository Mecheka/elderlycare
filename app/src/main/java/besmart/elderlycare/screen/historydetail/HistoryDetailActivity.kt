package besmart.elderlycare.screen.historydetail

import android.os.Bundle
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import besmart.elderlycare.R
import besmart.elderlycare.model.history.HistoryResponce
import besmart.elderlycare.screen.base.BaseActivity
import besmart.elderlycare.screen.bodymasshistory.BodyMassHistoryAdapter
import besmart.elderlycare.screen.bloodhistory.BloodPressureHistoryAdapter
import besmart.elderlycare.screen.sugarhistory.SugarHistoryAdapter
import besmart.elderlycare.util.BaseDialog
import kotlinx.android.synthetic.main.activity_main.*
import org.koin.android.viewmodel.ext.android.viewModel

class HistoryDetailActivity : BaseActivity() {

    companion object {
        const val HISTORY = "history"
    }

    private val history: HistoryResponce by lazy {
        intent.getParcelableExtra(HISTORY) as HistoryResponce
    }
    private val viewModel: HistoryDetailViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_history_detail)
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

    private fun observerViewModel() {
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

        viewModel.bodymassLiveEvent.observe(this, Observer {
            recyclerView.adapter = BodyMassHistoryAdapter(it.toMutableList())
        })
        viewModel.evaluationLiveEvent.observe(this, Observer {
            recyclerView.adapter = BloodPressureHistoryAdapter(it.toMutableList())
        })
        viewModel.sugarLiveEvent.observe(this, Observer {
            recyclerView.adapter = SugarHistoryAdapter(it)
        })
        viewModel.getHistoryDetail(history)
    }
}
