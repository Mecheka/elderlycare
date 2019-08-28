package besmart.elderlycare.screen.profile

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
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
import besmart.elderlycare.util.*
import com.afollestad.materialdialogs.MaterialDialog
import com.afollestad.materialdialogs.list.listItems
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.karumi.dexter.Dexter
import com.karumi.dexter.MultiplePermissionsReport
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.multi.MultiplePermissionsListener
import com.mlsdev.rximagepicker.RxImagePicker
import com.mlsdev.rximagepicker.Sources
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.addTo
import kotlinx.android.synthetic.main.activity_main.*
import org.koin.android.viewmodel.ext.android.viewModel
import java.io.File

@Suppress("NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")
class ProfileActivity : BaseActivity() {

    private val viewModel: ProfileViewModel by viewModel()
    private lateinit var binding: ActivityProfileBinding
    private val compositeDisposable = CompositeDisposable()
    private val EDIT_PROFILE_CODE = 301
    private val GALLERY_CODE = 302
    private val CAMERA_CODE = 303

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_profile)
        binding.viewModel = viewModel
        binding.lifecycleOwner = this
        initInstance()
        observerViewModel()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {
            EDIT_PROFILE_CODE -> {
                if (resultCode == Activity.RESULT_OK) {
                    viewModel.getProfileByCardId()
                }
            }
        }

    }

    @SuppressLint("SetTextI18n")
    private fun initInstance() {
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        toolbar.setNavigationOnClickListener {
            finish()
        }
        val packageInfo = packageManager.getPackageInfo(packageName, 0)
        binding.textVersion.text = "Version ${packageInfo.versionName}"
        binding.imageProfile.setOnClickListener {
            Dexter.withActivity(this)
                .withPermissions(
                    Manifest.permission.READ_EXTERNAL_STORAGE,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE,
                    Manifest.permission.CAMERA
                ).withListener(object : MultiplePermissionsListener {
                    override fun onPermissionsChecked(report: MultiplePermissionsReport) {
                        val item = listOf("Gallery", "Camera")
                        MaterialDialog(this@ProfileActivity).show {
                            listItems(items = item) { _, index, text ->
                                if (text == "Gallery") {
                                    applyImage(Sources.GALLERY)
                                } else {
                                    applyImage(Sources.CAMERA)
                                }
                            }
                            positiveButton(R.string.dialog_ok)
                            negativeButton(R.string.dialog_cancel)
                        }
                    }

                    override fun onPermissionRationaleShouldBeShown(
                        permissions: List<PermissionRequest>,
                        token: PermissionToken
                    ) {
                        // request permission when call method again
                        token.continuePermissionRequest()

                        // ask permission once time
                        token.cancelPermissionRequest()
                    }
                }).check()

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

        viewModel.profileLiveData.observe(this, Observer { profile ->
            profile.imagePath?.let {
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
                    startActivityForResult(this, EDIT_PROFILE_CODE)
                }
                true
            }
            else -> {
                super.onOptionsItemSelected(item)
            }
        }
    }

    private fun applyImage(sources: Sources) {
        RxImagePicker.with(supportFragmentManager).requestImage(sources).subscribe {
            Glide.with(this).load(it)
                .apply(RequestOptions.circleCropTransform())
                .into(binding.imageProfile)
            val file = File(FileManager.newInstant().getPath(this, it))
            CreateImage(this, it, file.absolutePath).apply {
                callbackListener = viewModel
                if (sources == Sources.CAMERA) {
                    addDeleteFileAfterFinish(file)
                }
                execute()
            }
        }.addTo(compositeDisposable)
    }
}
