package besmart.elderlycare.screen.villagehealthvoluntor

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import androidx.databinding.DataBindingUtil
import besmart.elderlycare.R
import besmart.elderlycare.databinding.ActivityVillageHealthVolunteerBinding
import besmart.elderlycare.model.profile.ProfileResponce
import besmart.elderlycare.screen.SelectType
import besmart.elderlycare.screen.editprofile.EditProfileActivity
import besmart.elderlycare.util.Constance
import besmart.elderlycare.util.loadImageResourceCircle
import besmart.elderlycare.util.loadImageUrlCircle
import kotlinx.android.synthetic.main.activity_main.*

class VillageHealthVolunteerActivity : AppCompatActivity() {

    companion object {
        const val PROFILE = "profile"
    }
    private lateinit var binding: ActivityVillageHealthVolunteerBinding
    private val profile: ProfileResponce by lazy {
        intent.getParcelableExtra(PROFILE) as ProfileResponce
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_village_health_volunteer)
        binding.model = profile
        initInstalce()
    }

    private fun initInstalce(){
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        toolbar.setNavigationOnClickListener {
            finish()
        }
        binding.imageProfile.loadImageUrlCircle(Constance.BASE_URL + "/" + profile.imagePath)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater: MenuInflater = menuInflater
            inflater.inflate(R.menu.public_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        return when (item?.itemId) {
            R.id.delete_elderly -> {
//                viewModel.removeElderly(profile)
                true
            }
            else -> {
                super.onOptionsItemSelected(item)
            }
        }
    }


}
