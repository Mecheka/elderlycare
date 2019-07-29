package besmart.elderlycare.screen.bodymasshistory

import android.os.Bundle
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import besmart.elderlycare.R
import besmart.elderlycare.model.profile.ProfileResponce
import besmart.elderlycare.screen.base.BaseActivity
import besmart.elderlycare.util.BaseDialog
import kotlinx.android.synthetic.main.activity_main.*
import org.koin.android.viewmodel.ext.android.viewModel

class BodyMassHistoryActivity : BaseActivity() {

    companion object {
        const val PROFILE = "profile"
    }

    private val viewModel: BodyMassHistoryViewModel by viewModel()
    private lateinit var profile: ProfileResponce

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_body_mass_history)
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

        viewModel.bodyMassLiveData.observe(this, Observer {
            recyclerView.apply {
                this.layoutManager =
                    LinearLayoutManager(this@BodyMassHistoryActivity, RecyclerView.VERTICAL, false)
                this.hasFixedSize()
                this.adapter = BodyMassHistoryAdapter(it)
            }
        })

        viewModel.getBodyMassHistory(profile.cardID!!)
    }
}
