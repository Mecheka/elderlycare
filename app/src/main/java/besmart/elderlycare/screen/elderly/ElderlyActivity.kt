package besmart.elderlycare.screen.elderly

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import besmart.elderlycare.R
import besmart.elderlycare.screen.calendar.CalendarActivity
import besmart.elderlycare.screen.myelderlyprofile.MyElderlyProfileActivity
import besmart.elderlycare.screen.gps.GpsActivity
import kotlinx.android.synthetic.main.activity_main.*

class ElderlyActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_my_elderly)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        toolbar.setNavigationOnClickListener {
            finish()
        }
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
}
