package besmart.elderlycare.screen.elderlyadd

import android.app.Activity
import android.os.Bundle
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import besmart.elderlycare.R
import besmart.elderlycare.model.profile.ProfileResponce
import besmart.elderlycare.screen.SelectType
import besmart.elderlycare.screen.base.BaseActivity
import besmart.elderlycare.util.BaseDialog
import besmart.elderlycare.util.Constance
import com.orhanobut.hawk.Hawk
import kotlinx.android.synthetic.main.activity_add_elderly.*
import kotlinx.android.synthetic.main.activity_calendar.toolbar
import kotlinx.android.synthetic.main.activity_main.recyclerView
import org.koin.android.viewmodel.ext.android.viewModel

class AddElderlyActivity : BaseActivity(), OnSelectItemListenner {

    private val viewModel: AddElderlyViewModel by viewModel()
    private lateinit var elderlyAdapter: AddElderlyAdapter
    private val selectType = Hawk.get<String>(Constance.LOGIN_TYPE)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_elderly)
        initInstance()
        observerViewModel()
    }

    private fun initInstance() {
        refresh.setOnRefreshListener {
            viewModel.getAllProfileWithRefresh()
        }
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        toolbar.setNavigationOnClickListener {
            finish()
        }

        toolbar.title = getStringBySelectType()
        btnAddElderly.text = getStringBySelectType()

        recyclerView.layoutManager = LinearLayoutManager(this, RecyclerView.VERTICAL, false)
        recyclerView.setHasFixedSize(true)
        recyclerView.addItemDecoration(DividerItemDecoration(this, DividerItemDecoration.VERTICAL))

        btnAddElderly.setOnClickListener {
            elderlyAdapter.getItemSelect()
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

        viewModel.profileLiveData.observe(this, Observer {
            refresh.isRefreshing = false
            elderlyAdapter = AddElderlyAdapter(it,this)
            recyclerView.adapter = elderlyAdapter
        })

        viewModel.successLiveData.observe(this, Observer {
            setResult(Activity.RESULT_OK)
            finish()
        })

        viewModel.getAllProfile()
    }

    override fun onSelect(profile: ProfileResponce) {
        viewModel.addElderlty(profile)
    }

    override fun onError() {
        BaseDialog.warningDialog(this, "Please select")
    }

    private fun getStringBySelectType(): String {
        return if (selectType == SelectType.HEALTH) {
            getString(R.string.add_orsomor)
        } else {
            getString(R.string.add_my_elderly)
        }
    }
}
