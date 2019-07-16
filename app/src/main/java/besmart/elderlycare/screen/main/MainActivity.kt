package besmart.elderlycare.screen.main

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import besmart.elderlycare.R
import besmart.elderlycare.screen.SelectType
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_select_user_type.*
import kotlinx.android.synthetic.main.activity_select_user_type.toolbar

class MainActivity : AppCompatActivity() {

    private val selectType:String by lazy {
        intent.getStringExtra(SelectType.SELECTTYPE)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)

        if (selectType == SelectType.PERSON){

        }

        recyclerView.apply {
            this.layoutManager = GridLayoutManager(this@MainActivity, 3)
        }
    }
}
