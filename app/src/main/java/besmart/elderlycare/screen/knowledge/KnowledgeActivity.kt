package besmart.elderlycare.screen.knowledge

import android.content.Intent
import android.os.Bundle
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import besmart.elderlycare.R
import besmart.elderlycare.model.knowlege.KnowlegeResponce
import besmart.elderlycare.screen.web.WebViewActivity
import besmart.elderlycare.screen.base.BaseActivity
import besmart.elderlycare.util.BaseDialog
import besmart.elderlycare.util.SimpleOnItemClick
import kotlinx.android.synthetic.main.activity_main.*
import org.koin.android.viewmodel.ext.android.viewModel

class KnowledgeActivity : BaseActivity(), SimpleOnItemClick<KnowlegeResponce> {

    private val viewModel: KnowlegeViewModel by viewModel()
    private lateinit var knowlegeAdapter: KnowlegeAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_knowledge)
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

        viewModel.knowlegeLiveData.observe(this, Observer { knowlege ->
            knowlegeAdapter = KnowlegeAdapter(knowlege, this)
            recyclerView.adapter = knowlegeAdapter
        })
        viewModel.getAllKnowlege()
    }

    override fun onItemClick(item: KnowlegeResponce) {
        Intent().apply {
            this.setClass(this@KnowledgeActivity, WebViewActivity::class.java)
            this.putExtra(WebViewActivity.URL, item.subdomain)
            this.putExtra(WebViewActivity.TITLE, item.topic)
            startActivity(this)
        }
    }
}
