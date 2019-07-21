package besmart.elderlycare.screen.newsdetail

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import besmart.elderlycare.R
import besmart.elderlycare.model.news.NewsData
import kotlinx.android.synthetic.main.activity_news_detail.*
import kotlinx.android.synthetic.main.activity_select_user_type.toolbar

class NewsDetailActivity : AppCompatActivity() {

    companion object {
        const val NEWS = "news"
    }

    private lateinit var news:NewsData

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_news_detail)
        news = intent.getParcelableExtra(NEWS)
        initInstance()
    }

    private fun initInstance() {
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = news.newsTopic
        toolbar.setNavigationOnClickListener {
            finish()
        }

        textTitle.text = news.newsTopic
        textContent.text = news.newsContents
        textDate.text = news.createdAt
    }
}
