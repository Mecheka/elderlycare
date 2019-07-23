package besmart.elderlycare.screen.profile

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import besmart.elderlycare.R
import besmart.elderlycare.databinding.ActivityProfileBinding
import besmart.elderlycare.screen.base.BaseActivity
import besmart.elderlycare.screen.editprofile.EditProfileActivity
import besmart.elderlycare.util.BaseDialog
import besmart.elderlycare.util.Constance
import besmart.elderlycare.util.loadImageResourceCircle
import besmart.elderlycare.util.loadImageUrlCircle
import kotlinx.android.synthetic.main.activity_main.*
import org.koin.android.viewmodel.ext.android.viewModel

class ProfileActivity : BaseActivity() {

    private val viewModel: ProfileViewModel by viewModel()
    private lateinit var binding: ActivityProfileBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_profile)
        binding.viewModel = viewModel
        binding.lifecycleOwner = this
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

        viewModel.profileLiveData.observe(this, Observer {
            it.imagePath?.let {
                binding.imageProfile.loadImageUrlCircle(Constance.BASE_URL + "/" + it)
            } ?: run {
                binding.imageProfile.loadImageResourceCircle(R.drawable.baseline_person_24px)
            }
        })
        viewModel.getProfileByCardId()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater: MenuInflater = menuInflater
        inflater.inflate(R.menu.profile_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        return when (item?.itemId) {
            R.id.edit_profile -> {
                Intent().apply {
                    this.setClass(this@ProfileActivity, EditProfileActivity::class.java)
                    this.putExtra(EditProfileActivity.USER, viewModel.profile)
                    startActivity(this)
                }
                true
            }
            else -> {
                super.onOptionsItemSelected(item)
            }
        }
    }
}
