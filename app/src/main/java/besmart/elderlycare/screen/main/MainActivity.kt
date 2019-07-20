package besmart.elderlycare.screen.main

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import besmart.elderlycare.R
import besmart.elderlycare.model.MenuItem
import besmart.elderlycare.screen.SelectType
import besmart.elderlycare.screen.elderly.ElderlyActivity
import besmart.elderlycare.screen.splach.SplachScreenActivity
import com.orhanobut.hawk.Hawk
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_select_user_type.toolbar

class MainActivity : AppCompatActivity(), MainMenuAdapter.OnMenuItemClick {

    private val selectType:String by lazy {
        intent.getStringExtra(SelectType.SELECTTYPE)
    }

    private lateinit var menuAdapter: MainMenuAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)

        val personList = listOf(
            MenuItem(getString(R.string.menu_profile), R.drawable.baseline_person_24px),
            MenuItem(getString(R.string.menu_calendar), R.drawable.baseline_calendar_today_24px),
            MenuItem(getString(R.string.menu_news), R.drawable.baseline_subtitles_24px),
            MenuItem(
                getString(R.string.menu_notification),
                R.drawable.baseline_notifications_active_24px
            ),
            MenuItem(getString(R.string.menu_chat), R.drawable.baseline_forum_24px)
        )
        val employeeList = listOf(
            MenuItem(getString(R.string.menu_elderly), R.drawable.baseline_supervisor_account_24px),
            MenuItem(getString(R.string.menu_file), R.drawable.baseline_library_books_24px),
            MenuItem(getString(R.string.menu_knowlege), R.drawable.baseline_extension_24px),
            MenuItem(getString(R.string.menu_news), R.drawable.baseline_subtitles_24px),
            MenuItem(
                getString(R.string.menu_notification),
                R.drawable.baseline_notifications_active_24px
            ),
            MenuItem(getString(R.string.menu_chat), R.drawable.baseline_forum_24px),
            MenuItem(getString(R.string.menu_profile), R.drawable.baseline_person_24px)
        )

        menuAdapter = if (selectType == SelectType.PERSON) {
            MainMenuAdapter(personList, this)
        } else {
            MainMenuAdapter(employeeList, this)
        }

        recyclerView.apply {
            this.layoutManager = GridLayoutManager(this@MainActivity, 3)
            this.adapter = menuAdapter
        }
    }

    override fun onMenuClick(icon: Int) {
        when (icon) {
            R.drawable.baseline_person_24px -> {
            }
            R.drawable.baseline_calendar_today_24px -> {
            }
            R.drawable.baseline_subtitles_24px -> {
            }
            R.drawable.baseline_notifications_active_24px -> {
            }
            R.drawable.baseline_forum_24px -> {
            }
            R.drawable.baseline_supervisor_account_24px -> {
                Intent().apply {
                    this.setClass(this@MainActivity, ElderlyActivity::class.java)
                    startActivity(this)
                }
            }
            R.drawable.baseline_library_books_24px -> {
            }
            R.drawable.baseline_extension_24px -> {
            }
        }
    }

    fun onLogoutClick(view: View) {
        Hawk.deleteAll()
        Intent().apply {
            this.setClass(this@MainActivity, SplachScreenActivity::class.java)
            startActivity(this)
            finish()
        }
    }
}