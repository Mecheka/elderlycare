package besmart.elderlycare.screen.chat

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import besmart.elderlycare.R
import kotlinx.android.synthetic.main.activity_gps.*
import kotlinx.android.synthetic.main.activity_select_user_type.toolbar

class ChatActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat)
        initInstance()
    }

    private fun initInstance(){
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        toolbar.setNavigationOnClickListener {
            finish()
        }
        viewPager.beginFakeDrag()
        viewPager.adapter = ChatPagerAdapter(supportFragmentManager)
        tabLayout.setupWithViewPager(viewPager)
    }
}
