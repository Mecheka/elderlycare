package besmart.elderlycare.screen.myelderlyprofile

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import besmart.elderlycare.R
import besmart.elderlycare.model.profile.ProfileResponce
import besmart.elderlycare.screen.SelectType
import besmart.elderlycare.screen.base.BaseActivity
import besmart.elderlycare.screen.elderlyadd.AddElderlyActivity
import besmart.elderlycare.screen.elderlyinfo.ElderlyInfoActivity
import besmart.elderlycare.screen.villagehealthvoluntor.VillageHealthVolunteerActivity
import besmart.elderlycare.util.BaseDialog
import besmart.elderlycare.util.Constance
import besmart.elderlycare.util.SimpleOnItemClick
import com.orhanobut.hawk.Hawk
import kotlinx.android.synthetic.main.activity_calendar.toolbar
import kotlinx.android.synthetic.main.activity_main.*
import org.koin.android.viewmodel.ext.android.viewModel

class MyElderlyProfileActivity : BaseActivity(), SimpleOnItemClick<ProfileResponce> {

    companion object{
        const val ADD_ELDERLY = 101
        const val DELETE_ELDERLY = 102
    }

    private val viewModelMy: MyElderlyProfileViewModel by viewModel()
    private lateinit var elderlyProfileAdapter: MyElderlyProfileAdapter
    private val selectType = Hawk.get<String>(Constance.LOGIN_TYPE)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_elderly_profile)
        initInstance()
        observerViewModel()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            viewModelMy.getMyElderly()
        }
    }

    private fun initInstance() {
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        toolbar.setNavigationOnClickListener {
            finish()
        }
        toolbar.title = getString(getElderlyTitleMenuBySelectType())
        recyclerView.layoutManager = GridLayoutManager(this, 3)
    }

    private fun observerViewModel() {
        viewModelMy.errorLiveData.observe(this, Observer {
            BaseDialog.warningDialog(this, it)
        })

        viewModelMy.loadingLiveData.observe(this, Observer {
            if (it) {
                showLoadingDialog(this)
            } else {
                dismissDialog()
            }
        })

        viewModelMy.elderlyLiveData.observe(this, Observer {
            elderlyProfileAdapter = MyElderlyProfileAdapter(it, this)
            recyclerView.adapter = elderlyProfileAdapter
        })

        viewModelMy.getMyElderly()
    }

    fun onAddClick(view: View) {
        Intent().apply {
            this.setClass(this@MyElderlyProfileActivity, AddElderlyActivity::class.java)
            startActivityForResult(this, ADD_ELDERLY)
        }
    }

    override fun onItemClick(item: ProfileResponce) {
        if (selectType == SelectType.ORSOMO) {
            Intent().apply {
                this.setClass(this@MyElderlyProfileActivity, ElderlyInfoActivity::class.java)
                this.putExtra(ElderlyInfoActivity.PROFILE, item)
                startActivityForResult(this, DELETE_ELDERLY)
            }
        } else {
            Intent().apply {
                this.setClass(
                    this@MyElderlyProfileActivity,
                    VillageHealthVolunteerActivity::class.java
                )
                this.putExtra(VillageHealthVolunteerActivity.PROFILE, item)
                startActivityForResult(this, DELETE_ELDERLY)
            }
        }
    }

    private fun getElderlyTitleMenuBySelectType(): Int {
        return when (selectType) {
            SelectType.ORSOMO -> R.string.list_elderly
            else -> R.string.list_orsomor
        }
    }
}
