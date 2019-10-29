package besmart.elderlycare.screen.elderlyadd

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import besmart.elderlycare.R
import besmart.elderlycare.model.profile.ProfileResponce
import besmart.elderlycare.screen.SelectType
import besmart.elderlycare.screen.base.BaseActivity
import besmart.elderlycare.screen.editprofile.EditProfileActivity
import besmart.elderlycare.util.BaseDialog
import besmart.elderlycare.util.Constance
import com.orhanobut.hawk.Hawk
import kotlinx.android.synthetic.main.activity_add_my_elderly.*
import kotlinx.android.synthetic.main.activity_calendar.toolbar
import kotlinx.android.synthetic.main.activity_main.recyclerView
import org.koin.android.viewmodel.ext.android.viewModel

class AddMyElderlyActivity : BaseActivity(), OnSelectItemListenner {

    private val viewModel: AddMyElderlyViewModel by viewModel()
    private lateinit var elderlyAdapter: AddElderlyAdapter
    private val selectType = Hawk.get<String>(Constance.LOGIN_TYPE)
    private val ADD_ELDERLY = 401
    private var isAddSuccess = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_my_elderly)
        initInstance()
        observerViewModel()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            viewModel.getAllProfile(selectType)
            isAddSuccess = true
        }
    }

    private fun initInstance() {
        refresh.setOnRefreshListener {
            viewModel.getAllProfile(selectType)
        }
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        toolbar.setNavigationOnClickListener {
            onBackPressed()
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

        viewModel.getAllProfile(selectType)
    }

    override fun onSelect(profileResponce: ProfileResponce) {
        viewModel.addElderlty(profileResponce)
    }

    override fun onError() {
        BaseDialog.warningDialog(this, "Please select")
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater: MenuInflater = menuInflater
        if (selectType == SelectType.ORSOMO) {
            inflater.inflate(R.menu.add_alderly_menu, menu)
            return true
        }
        return false
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        return when (item?.itemId) {
            R.id.add_elderly -> {
                Intent().apply {
                    setClass(this@AddMyElderlyActivity, EditProfileActivity::class.java)
                    putExtra(EditProfileActivity.IS_EDIT, false)
                    startActivityForResult(this, ADD_ELDERLY)
                }
                true
            }
            else -> {
                super.onOptionsItemSelected(item)
            }
        }
    }

    override fun onBackPressed() {
        if (isAddSuccess) {
            setResult(Activity.RESULT_OK)
        }
        super.onBackPressed()
    }

    private fun getStringBySelectType(): String {
        return if (selectType == SelectType.HEALTH) {
            getString(R.string.add_orsomor)
        } else {
            getString(R.string.add_my_elderly)
        }
    }
}
