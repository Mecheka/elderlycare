package besmart.elderlycare.screen.main

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import besmart.elderlycare.R
import besmart.elderlycare.model.MenuItem
import besmart.elderlycare.screen.SelectType
import besmart.elderlycare.screen.calendar.CalendarActivity
import besmart.elderlycare.screen.chat.ChatActivity
import besmart.elderlycare.screen.elderly.ElderlyActivity
import besmart.elderlycare.screen.flie.FileActivity
import besmart.elderlycare.screen.knowledge.KnowledgeActivity
import besmart.elderlycare.screen.news.NewsActivity
import besmart.elderlycare.screen.notification.NotificationActivity
import besmart.elderlycare.screen.profile.ProfileActivity
import besmart.elderlycare.screen.splach.SplachScreenActivity
import besmart.elderlycare.util.Constance
import com.orhanobut.hawk.Hawk
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_select_user_type.toolbar

class MainActivity : AppCompatActivity(), MainMenuAdapter.OnMenuItemClick {

    private val selectType = Hawk.get<String>(Constance.LOGIN_TYPE)

    private lateinit var menuAdapter: MainMenuAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)
        supportActionBar?.title = getTitleByType()

        val employeeList = listOf(
            MenuItem(getString(R.string.menu_profile), R.drawable.icon_profile),
            MenuItem(getString(getElderlTitleMenuBySelectType()), R.drawable.baseline_supervisor_account_24px),
            MenuItem(
                getString(R.string.menu_notification),
                R.drawable.baseline_notifications_active_24px
            ),
            MenuItem(getString(R.string.menu_file), R.drawable.baseline_library_books_24px),
            MenuItem(getString(R.string.menu_knowlege), R.drawable.baseline_extension_24px),
            MenuItem(getString(R.string.menu_news), R.drawable.baseline_subtitles_24px),
            MenuItem(getString(R.string.menu_chat), R.drawable.baseline_forum_24px)
        )
        menuAdapter = MainMenuAdapter(employeeList, this)
        recyclerView.apply {
            this.layoutManager = GridLayoutManager(this@MainActivity, 3)
            this.adapter = menuAdapter
        }
    }

    override fun onMenuClick(icon: Int) {
        when (icon) {
            R.drawable.icon_profile -> {
                Intent().apply {
                    this.setClass(this@MainActivity, ProfileActivity::class.java)
                    startActivity(this)
                }
            }
            R.drawable.baseline_calendar_today_24px -> {
                Intent().apply {
                    this.setClass(this@MainActivity, CalendarActivity::class.java)
                    startActivity(this)
                }
            }
            R.drawable.baseline_subtitles_24px -> {
                Intent().apply {
                    this.setClass(this@MainActivity, NewsActivity::class.java)
                    startActivity(this)
                }
            }
            R.drawable.baseline_notifications_active_24px -> {
                Intent().apply {
                    this.setClass(this@MainActivity, NotificationActivity::class.java)
                    startActivity(this)
                }
            }
            R.drawable.baseline_forum_24px -> {
                Intent().apply {
                    this.setClass(this@MainActivity, ChatActivity::class.java)
                    startActivity(this)
                }
            }
            R.drawable.baseline_supervisor_account_24px -> {
                Intent().apply {
                    this.setClass(this@MainActivity, ElderlyActivity::class.java)
                    startActivity(this)
                }
            }
            R.drawable.baseline_library_books_24px -> {
                Intent().apply {
                    this.setClass(this@MainActivity, FileActivity::class.java)
                    startActivity(this)
                }
            }
            R.drawable.baseline_extension_24px -> {
                Intent().apply {
                    this.setClass(this@MainActivity, KnowledgeActivity::class.java)
                    startActivity(this)
                }
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

    private fun getTitleByType(): String {
        return when (selectType) {
            SelectType.ORSOMO -> getString(R.string.orsomo)
            SelectType.HEALTH -> getString(R.string.public_health)
            else -> getString(R.string.person)
        }
    }

    private fun getElderlTitleMenuBySelectType():Int{
        return when(selectType){
            SelectType.ORSOMO -> R.string.menu_orsomor_elderly
            SelectType.HEALTH -> R.string.menu_public_elderly
            else -> R.string.menu_person_elderly
        }
    }
}
