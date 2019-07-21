package besmart.elderlycare.screen.news

import android.content.Intent
import android.os.Bundle
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import besmart.elderlycare.R
import besmart.elderlycare.model.news.NewsData
import besmart.elderlycare.screen.base.BaseActivity
import besmart.elderlycare.screen.newsdetail.NewsDetailActivity
import besmart.elderlycare.util.BaseDialog
import besmart.elderlycare.util.SimpleOnItemClick
import kotlinx.android.synthetic.main.activity_flie.recyclerView
import kotlinx.android.synthetic.main.activity_select_user_type.toolbar
import org.koin.android.viewmodel.ext.android.viewModel

class NewsActivity : BaseActivity(), SimpleOnItemClick<NewsData> {

    private val viewModel: NewsViewModel by viewModel()

    private lateinit var newsAdapter: NewsAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_news)
        initInstance()
        observerViewModel()
    }

    private fun initInstance() {
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        toolbar.setNavigationOnClickListener {
            finish()
        }
        recyclerView.layoutManager = LinearLayoutManager(this, RecyclerView.VERTICAL, false)
        recyclerView.addItemDecoration(DividerItemDecoration(this, DividerItemDecoration.VERTICAL))
        recyclerView.hasFixedSize()
    }

    private fun observerViewModel() {
        viewModel.errorLiveData.observe(this, Observer {
            BaseDialog.warningDialog(this, it)
        })

        viewModel.loadingLiveData.observe(this, Observer {
            if (it) {
                showLoadingDialog(this)
            } else {
                dismissDialog()
            }
        })

        viewModel.newsLiveData.observe(this, Observer {
            newsAdapter = NewsAdapter(it.data!!, this)
            recyclerView.adapter = newsAdapter
        })
        viewModel.getAllNews()
    }

    override fun onItemClick(item: NewsData) {
        Intent().apply {
            this.setClass(this@NewsActivity,NewsDetailActivity::class.java)
            this.putExtra(NewsDetailActivity.NEWS, item)
            startActivity(this)
        }
    }
}
