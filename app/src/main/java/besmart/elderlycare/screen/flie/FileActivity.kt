package besmart.elderlycare.screen.flie

import android.content.Intent
import android.os.Bundle
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import besmart.elderlycare.R
import besmart.elderlycare.model.file.FileData
import besmart.elderlycare.screen.base.BaseActivity
import besmart.elderlycare.screen.filedetail.FileDetailActivity
import besmart.elderlycare.util.BaseDialog
import besmart.elderlycare.util.SimpleOnItemClick
import com.krishna.fileloader.FileLoader
import kotlinx.android.synthetic.main.activity_main.*
import org.koin.android.viewmodel.ext.android.viewModel

class FileActivity : BaseActivity(), SimpleOnItemClick<FileData> {

    companion object{
        const val FILE_DIR = "PDFFile"
    }

    private val viewModel: FileViewModel by viewModel()
    private lateinit var fileAdapter: FileAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_flie)
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

        viewModel.devmanLiveData.observe(this, Observer { file ->
            file.data?.let {
                fileAdapter = FileAdapter(it, this)
                recyclerView.adapter = fileAdapter
            }
        })
        viewModel.getAllFile()
    }

    override fun onItemClick(item: FileData) {
        Intent().apply {
            this.setClass(this@FileActivity, FileDetailActivity::class.java)
            this.putExtra(FileDetailActivity.FILE_DATA, item)
            startActivity(this)
        }
    }

    override fun onDestroy() {
        FileLoader.deleteWith(this)
            .fromDirectory(FILE_DIR, FileLoader.DIR_CACHE)
            .deleteAllFiles()
        super.onDestroy()
    }
}
