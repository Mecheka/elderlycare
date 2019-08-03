package besmart.elderlycare.screen.web

import android.annotation.SuppressLint
import android.os.Bundle
import android.webkit.WebView
import android.webkit.WebViewClient
import besmart.elderlycare.R
import besmart.elderlycare.screen.base.BaseActivity
import besmart.elderlycare.util.BaseDialog
import kotlinx.android.synthetic.main.activity_main.toolbar
import kotlinx.android.synthetic.main.activity_web_view.*

class WebViewActivity : BaseActivity() {

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

    private var isShowLoading: Boolean = true

    @SuppressLint("SetJavaScriptEnabled")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_web_view)

        setSupportActionBar(toolbar)
        supportActionBar?.title = titleBar
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        toolbar.setNavigationOnClickListener {
            finish()
        }

        webView.settings.javaScriptEnabled = true
        webView.scrollBarStyle = WebView.SCROLLBARS_OUTSIDE_OVERLAY

        showLoadingDialog(this)

        webView.webViewClient = object : WebViewClient() {
            override fun shouldOverrideUrlLoading(view: WebView?, url: String?): Boolean {
                view?.loadUrl(url)
                return true
            }

            override fun onPageFinished(view: WebView?, url: String?) {
                super.onPageFinished(view, url)
                if (isShowLoading) {
                    dismissDialog()
                }
            }

            override fun onReceivedError(
                view: WebView?,
                errorCode: Int,
                description: String?,
                failingUrl: String?
            ) {
                BaseDialog.warningDialog(this@WebViewActivity, description!!)
            }
        }

        webView.loadUrl(url)
    }
}
