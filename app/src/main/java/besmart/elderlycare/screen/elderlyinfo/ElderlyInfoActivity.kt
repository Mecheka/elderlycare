package besmart.elderlycare.screen.elderlyinfo

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import besmart.elderlycare.R
import besmart.elderlycare.databinding.ActivityElderlyInfoBinding
import besmart.elderlycare.model.profile.ProfileResponce
import besmart.elderlycare.screen.SelectType
import besmart.elderlycare.screen.base.BaseActivity
import besmart.elderlycare.screen.blood.BloodPressureActivity
import besmart.elderlycare.screen.bodymass.BodyMassActivity
import besmart.elderlycare.screen.editprofile.EditProfileActivity
import besmart.elderlycare.screen.evaluation.EvaluationActivity
import besmart.elderlycare.screen.history.HistoryActivity
import besmart.elderlycare.screen.sugar.SugarActivity
import besmart.elderlycare.screen.vaccine.VaccineActivity
import besmart.elderlycare.util.BaseDialog
import besmart.elderlycare.util.Constance
import besmart.elderlycare.util.loadImageUrlCircle
import com.orhanobut.hawk.Hawk
import kotlinx.android.synthetic.main.activity_main.*
import org.koin.android.viewmodel.ext.android.viewModel

@Suppress("NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")
class ElderlyInfoActivity : BaseActivity() {

    companion object {
        const val PROFILE = "profile"
    }

    private lateinit var binding: ActivityElderlyInfoBinding
    private lateinit var profile: ProfileResponce
    private val viewModel: ElderlyinfoViewModel by viewModel()
    private val selectType = Hawk.get<String>(Constance.LOGIN_TYPE)
    private var isEditSuccess = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_elderly_info)
        profile = intent.getParcelableExtra(PROFILE)
        binding.model = profile
        initInstance()
        observerViewModel()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(resultCode == Activity.RESULT_OK){
            viewModel.getProfileByCardId(profile)
            isEditSuccess = true
        }
    }

    private fun initInstance() {
        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        toolbar.setNavigationOnClickListener {
            onBackPressed()
        }

        binding.imageProfile.loadImageUrlCircle(Constance.BASE_URL + "/" + profile.imagePath)

        binding.cardHistory.setOnClickListener {
            Intent().apply {
                this.setClass(this@ElderlyInfoActivity, HistoryActivity::class.java)
                this.putExtra(HistoryActivity.PROFILE, profile)
                startActivity(this)
            }
        }
        binding.cardBodyMass.setOnClickListener {
            Intent().apply {
                this.setClass(this@ElderlyInfoActivity, BodyMassActivity::class.java)
                this.putExtra(BodyMassActivity.PROFILE, profile)
                startActivity(this)
            }
        }
        binding.cardPressure.setOnClickListener {
            Intent().apply {
                this.setClass(this@ElderlyInfoActivity, BloodPressureActivity::class.java)
                this.putExtra(BodyMassActivity.PROFILE, profile)
                startActivity(this)
            }
        }
        binding.cardBlood.setOnClickListener {
            Intent().apply {
                this.setClass(this@ElderlyInfoActivity, SugarActivity::class.java)
                this.putExtra(SugarActivity.PROFILE, profile)
                startActivity(this)
            }
        }
        binding.cardVaccine.setOnClickListener {
            startActivity(Intent(this, VaccineActivity::class.java))
        }
        binding.cardEvaluation.setOnClickListener {
            Intent().apply {
                setClass(this@ElderlyInfoActivity,EvaluationActivity::class.java)
                putExtra(EvaluationActivity.PROFILE, profile)
                startActivity(this)
            }
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

        viewModel.profileLiveEvent.observe(this, Observer {
            if (selectType == SelectType.PERSON){
                Hawk.put(Constance.USER, it)
            }
            profile = it
            binding.model = it
        })

        viewModel.getProfileByCardId(profile)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater: MenuInflater = menuInflater
        if (selectType == SelectType.ORSOMO) {
            inflater.inflate(R.menu.elderly_menu, menu)
        } else {
            inflater.inflate(R.menu.profile_menu, menu)
        }
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        return when (item?.itemId) {
            R.id.edit_profile -> {
                Intent().apply {
                    this.setClass(this@ElderlyInfoActivity, EditProfileActivity::class.java)
                    this.putExtra(EditProfileActivity.USER, profile)
                    startActivityForResult(this, 101)
                }
                true
            }
            R.id.delete_elderly -> {
                viewModel.removeElderly(profile)
                true
            }
            else -> {
                super.onOptionsItemSelected(item)
            }
        }
    }

    override fun onBackPressed() {
        if (isEditSuccess){
            setResult(Activity.RESULT_OK)
        }
        super.onBackPressed()
    }
}
