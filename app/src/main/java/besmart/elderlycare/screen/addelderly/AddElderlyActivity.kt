package besmart.elderlycare.screen.addelderly

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import besmart.elderlycare.R
import kotlinx.android.synthetic.main.activity_calendar.*

class AddElderlyActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_elderly)
    }

    private fun initInstance() {
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        toolbar.setNavigationOnClickListener {
            finish()
        }
    }
}
