package besmart.elderlycare.screen.chat

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import besmart.elderlycare.R
import kotlinx.android.synthetic.main.activity_select_user_type.*

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
    }
}
