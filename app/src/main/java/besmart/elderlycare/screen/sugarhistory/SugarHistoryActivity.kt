package besmart.elderlycare.screen.sugarhistory

import android.os.Bundle
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import besmart.elderlycare.R
import besmart.elderlycare.model.profile.ProfileResponce
import besmart.elderlycare.screen.base.BaseActivity
import besmart.elderlycare.screen.evaluationhistory.EvaluetionHistoryAdapter
import besmart.elderlycare.util.BaseDialog
import kotlinx.android.synthetic.main.activity_main.*
import org.koin.android.viewmodel.ext.android.viewModel

class SugarHistoryActivity : BaseActivity() {

    companion object {
        const val PROFILE = "profile"
    }

    private val viewModel: SugarHistoryViewModel by viewModel()
    private val profile: ProfileResponce by lazy {
        intent.getParcelableExtra(PROFILE) as ProfileResponce
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sugar_history)
        initInstance()
        observerViewModel()
    }

    private fun initInstance() {
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        toolbar.setNavigationOnClickListener {
            finish()
        }
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

        viewModel.sugarLiveData.observe(this, Observer {
            recyclerView.apply {
                this.layoutManager =
                    LinearLayoutManager(
                        this@SugarHistoryActivity,
                        RecyclerView.VERTICAL,
                        false
                    )
                this.hasFixedSize()
                this.adapter = SugarHistoryAdapter(it)
            }
        })

        viewModel.getSugarHistory(profile.cardID!!)
    }
}
