package besmart.elderlycare.screen.web

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import besmart.elderlycare.R
import kotlinx.android.synthetic.main.activity_main.toolbar
import kotlinx.android.synthetic.main.activity_web_view.*

class WebViewActivity : AppCompatActivity() {

    companion object {
        const val URL = "url"
        const val TITLE = "title"
    }

    private val url: String by lazy {
        intent.getStringExtra(URL)
    }

    private val titleBar: String by lazy {
        intent.getStringExtra(TITLE)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_web_view)

        setSupportActionBar(toolbar)
        supportActionBar?.title = titleBar
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        toolbar.setNavigationOnClickListener {
            finish()
        }

        webView.loadUrl(url)
    }
}
