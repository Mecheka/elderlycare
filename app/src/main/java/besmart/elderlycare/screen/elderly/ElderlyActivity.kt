package besmart.elderlycare.screen.elderly

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import besmart.elderlycare.R
import besmart.elderlycare.screen.SelectType
import besmart.elderlycare.screen.calendar.CalendarActivity
import besmart.elderlycare.screen.gps.GpsActivity
import besmart.elderlycare.screen.myelderlyprofile.MyElderlyProfileActivity
import besmart.elderlycare.util.Constance
import com.orhanobut.hawk.Hawk
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_main.toolbar
import kotlinx.android.synthetic.main.activity_my_elderly.*

class ElderlyActivity : AppCompatActivity() {

    private val selectType = Hawk.get<String>(Constance.LOGIN_TYPE)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_my_elderly)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        toolbar.setNavigationOnClickListener {
            finish()
        }
        toolbar.title = getString(getElderlyTitleMenuBySelectType())
        textMenuElderly.text = getString(getElderlyTitleMenuBySelectType())
    }

    fun onCalendarClick(view: View) {
        Intent().apply {
            this.setClass(this@ElderlyActivity, CalendarActivity::class.java)
            startActivity(this)
        }
    }

    fun onElderlyProfileClick(view: View) {
        Intent().apply {
            this.setClass(this@ElderlyActivity, MyElderlyProfileActivity::class.java)
            startActivity(this)
        }
    }

    fun onGPSClick(view: View){
        Intent().apply {
            this.setClass(this@ElderlyActivity, GpsActivity::class.java)
            startActivity(this)
        }
    }

    private fun getElderlyTitleMenuBySelectType(): Int {
        return when (selectType) {
            SelectType.ORSOMO -> R.string.orsomor_elderly_profile
            else -> R.string.public_elderly_profile
        }
    }
}
