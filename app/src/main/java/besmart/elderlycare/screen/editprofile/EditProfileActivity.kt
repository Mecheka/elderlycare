package besmart.elderlycare.screen.editprofile

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.IntentSender
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import besmart.elderlycare.R
import besmart.elderlycare.databinding.ActivityEditProfileBinding
import besmart.elderlycare.model.profile.ProfileResponce
import besmart.elderlycare.screen.base.BaseActivity
import besmart.elderlycare.util.BaseDialog
import com.afollestad.materialdialogs.MaterialDialog
import com.afollestad.materialdialogs.list.listItems
import com.google.android.gms.common.api.GoogleApiClient
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.LocationSettingsRequest
import com.google.android.gms.location.LocationSettingsStatusCodes
import com.layernet.thaidatetimepicker.date.DatePickerDialog
import kotlinx.android.synthetic.main.activity_main.*
import org.koin.android.viewmodel.ext.android.viewModel
import java.text.SimpleDateFormat
import java.util.*

class EditProfileActivity : BaseActivity(), DatePickerDialog.OnDateSetListener {

    companion object{
        const val USER = "user"
    }

    private lateinit var binding: ActivityEditProfileBinding
    private val viewModel: EditProfileViewModel by viewModel()
    private val profile: ProfileResponce by lazy {
        intent.getParcelableExtra(USER) as ProfileResponce
    }
    private lateinit var locationManager: LocationManager
    private var mGoogleApiClient: GoogleApiClient? = null
    private var mLocationRequest: LocationRequest? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_edit_profile)
        binding.viewModel = viewModel
        locationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager
        mLocationRequest = LocationRequest.create()
        mLocationRequest?.interval = 5000
        mLocationRequest?.fastestInterval = 1000
        mLocationRequest?.priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        initInstance()
        observerViewModel()
    }

    private fun initInstance() {
        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        toolbar.setNavigationOnClickListener {
            finish()
        }
        initDataViewModel()
        binding.editDate.setOnClickListener {
            val calendar = Calendar.getInstance()
            val dp = DatePickerDialog.newInstance(
                this,
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)
            )
            dp.maxDate = calendar
            dp.show(fragmentManager, "DatePicker")
        }

        binding.editGender.setOnClickListener {
            MaterialDialog(this).show {
                listItems(R.array.gender) { _, index, text ->
                    viewModel.genderId.set((index + 1).toString())
                    binding.editGender.setText(text)
                }
                positiveButton(R.string.dialog_ok)
                negativeButton(R.string.dialog_cancel)
            }
        }

        binding.textUpdataLocation.setOnClickListener {
            MaterialDialog(this).show {
                title(text = "เปลี่ยนแปลตำแหน่งในแผยที่")
                message(text = "ต้องการเปลี่ยนแปลงตำแหน่งของที่อยู่ในแผนที่หรือไม่")
                positiveButton(R.string.dialog_change) {
                    try {
                        if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) && hasGPSDevice(
                                this@EditProfileActivity
                            )
                        ) {
                            Log.e("keshav", "Gps already enabled")
                            Toast.makeText(this@EditProfileActivity, "Gps not enabled", Toast.LENGTH_SHORT).show()
                            enableLoc()
                        } else {
                            Log.e("keshav", "Gps already enabled")
                            Toast.makeText(this@EditProfileActivity, "Gps already enabled", Toast.LENGTH_SHORT).show()
                        }
                        locationManager.requestLocationUpdates(
                            LocationManager.NETWORK_PROVIDER,
                            0L,
                            0f,
                            locationListener
                        )
                    } catch (e: SecurityException) {
                        e.printStackTrace()
                    }
                }
                negativeButton(R.string.dialog_no_change)
            }
        }

        binding.btnSave.setOnClickListener {
            viewModel.onSaveClick(profile.id.toString())
        }
    }

    private fun observerViewModel() {
        viewModel.errorLiveData.observe(this, Observer {
            BaseDialog.warningDialog(this, it)
        })

        viewModel.successLiveData.observe(this, Observer { success ->
            setResult(Activity.RESULT_OK)
            finish()
        })

        viewModel.loadingLiveData.observe(this, Observer { loading ->
            if (loading) {
                showLoadingDialog(this)
            } else {
                dismissDialog()
            }
        })
    }

    @SuppressLint("SimpleDateFormat")
    private fun initDataViewModel() {
        val inputFormat = SimpleDateFormat("dd/MM/yyyy")
        val date = inputFormat.parse(profile.birthday)
        val outputFormat = SimpleDateFormat("dd MMM yyyy", Locale("TH"))

        viewModel.cardId.set(profile.cardID)
        viewModel.firstName.set(profile.firstName)
        viewModel.lastName.set(profile.lastName)
        viewModel.birthday.set(outputFormat.format(date))
        if (profile.genderID == 1) {
            viewModel.genderId.set("1")
            binding.editGender.setText("ชาย")
        } else {
            viewModel.genderId.set("2")
            binding.editGender.setText("หญิง")
        }
        viewModel.address.set(profile.address)
        viewModel.phone.set(profile.phone)
        viewModel.latitude.set(profile.latitude.toString())
        viewModel.longitude.set(profile.longitude.toString())
    }

    private fun hasGPSDevice(context: Context): Boolean {
        val mgr = context
            .getSystemService(Context.LOCATION_SERVICE) as LocationManager
        val providers = mgr.allProviders ?: return false
        return providers.contains(LocationManager.GPS_PROVIDER)
    }

    private fun enableLoc() {

        if (mGoogleApiClient == null) {
            mGoogleApiClient = GoogleApiClient.Builder(this)
                .addApi(LocationServices.API)
                .addConnectionCallbacks(object : GoogleApiClient.ConnectionCallbacks {
                    override fun onConnected(bundle: Bundle?) {

                    }

                    override fun onConnectionSuspended(i: Int) {
                        mGoogleApiClient?.connect()
                    }
                })
                .addOnConnectionFailedListener { connectionResult ->
                    Log.d(
                        "Location error",
                        "Location error " + connectionResult.errorCode
                    )
                }
                .build()
            mGoogleApiClient?.connect()

            if (mLocationRequest != null) {

                val builder = LocationSettingsRequest.Builder()
                    .addLocationRequest(mLocationRequest!!)

                builder.setAlwaysShow(true)

                val result = LocationServices.SettingsApi.checkLocationSettings(
                    mGoogleApiClient,
                    builder.build()
                )
                result.setResultCallback { result ->
                    val status = result.status
                    when (status.statusCode) {
                        LocationSettingsStatusCodes.RESOLUTION_REQUIRED -> try {
                            // Show the dialog by calling startResolutionForResult(),
                            // and check the result in onActivityResult().
                            status.startResolutionForResult(this, 101)

                        } catch (e: IntentSender.SendIntentException) {
                            // Ignore the error.
                        }

                    }
                }
            }
        }
    }

    private val locationListener: LocationListener = object : LocationListener {
        override fun onLocationChanged(location: Location) {
            viewModel.latitude.set(location.latitude.toString())
            viewModel.longitude.set(location.longitude.toString())
        }

        override fun onStatusChanged(provider: String, status: Int, extras: Bundle) {}
        override fun onProviderEnabled(provider: String) {}
        override fun onProviderDisabled(provider: String) {}
    }

    override fun onDateSet(view: DatePickerDialog?, year: Int, monthOfYear: Int, dayOfMonth: Int) {
        val outputFormat = SimpleDateFormat("dd MMM yyyy", Locale("TH"))
        val calendar = Calendar.getInstance()
        val newYear = year + 543
        calendar.set(newYear, monthOfYear, dayOfMonth)
        val date = outputFormat.format(calendar.time)
        viewModel.birthday.set(date)
    }
}
