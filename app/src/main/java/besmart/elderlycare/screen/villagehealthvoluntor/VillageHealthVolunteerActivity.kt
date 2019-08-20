package besmart.elderlycare.screen.villagehealthvoluntor

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import besmart.elderlycare.R
import besmart.elderlycare.databinding.ActivityVillageHealthVolunteerBinding
import besmart.elderlycare.model.profile.ProfileResponce
import besmart.elderlycare.screen.base.BaseActivity
import besmart.elderlycare.screen.elderlyinfo.ElderlyInfoActivity
import besmart.elderlycare.util.BaseDialog
import besmart.elderlycare.util.SimpleOnItemClick
import kotlinx.android.synthetic.main.activity_main.*
import org.koin.android.viewmodel.ext.android.viewModel

class VillageHealthVolunteerActivity : BaseActivity(), SimpleOnItemClick<ProfileResponce> {

    companion object {
        const val PROFILE = "profile"
    }
    private lateinit var binding: ActivityVillageHealthVolunteerBinding
    private val profile: ProfileResponce by lazy {
        intent.getParcelableExtra(PROFILE) as ProfileResponce
    }
    private val viewModel: VillageHealthVolunteerViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_village_health_volunteer)
        binding.model = profile
        initInstalce()
        observerViewModel()
    }

    private fun initInstalce(){
        setSupportActionBar(binding.toolbar)
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

        viewModel.successLiveData.observe(this, Observer {
            setResult(Activity.RESULT_OK)
            finish()
        })

        viewModel.profileLiveData.observe(this, Observer {
            recyclerView.apply {
                layoutManager = LinearLayoutManager(this@VillageHealthVolunteerActivity, RecyclerView.VERTICAL, false)
                adapter = VillageHealthVolumteerAdapter(it, this@VillageHealthVolunteerActivity)
            }
        })

        viewModel.getMyOrsomor(profile)
    }

    override fun onItemClick(item: ProfileResponce) {
        Intent().apply {
            this.setClass(this@VillageHealthVolunteerActivity, ElderlyInfoActivity::class.java)
            this.putExtra(ElderlyInfoActivity.PROFILE, item)
            startActivity(this)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater: MenuInflater = menuInflater
            inflater.inflate(R.menu.public_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        return when (item?.itemId) {
            R.id.delete_elderly -> {
                viewModel.removeElderly(profile)
                true
            }
            else -> {
                super.onOptionsItemSelected(item)
            }
        }
    }


}
