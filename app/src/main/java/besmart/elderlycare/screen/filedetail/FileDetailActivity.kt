package besmart.elderlycare.screen.filedetail

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import besmart.elderlycare.R
import besmart.elderlycare.model.file.FileData
import besmart.elderlycare.screen.flie.FileActivity
import besmart.elderlycare.util.Constance
import com.bumptech.glide.Glide
import com.krishna.fileloader.FileLoader
import com.krishna.fileloader.listener.FileRequestListener
import com.krishna.fileloader.pojo.FileResponse
import com.krishna.fileloader.request.FileLoadRequest
import kotlinx.android.synthetic.main.activity_file_detail.*
import kotlinx.android.synthetic.main.activity_main.toolbar
import java.io.File

class FileDetailActivity : AppCompatActivity() {

    companion object{
        const val FILE_DATA="filedata"
    }

    private val fileData: FileData by lazy {
        intent.getParcelableExtra(FILE_DATA) as  FileData
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_file_detail)
        initInstance()
    }

    private fun initInstance(){
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        toolbar.setNavigationOnClickListener {
            finish()
        }
        if (fileData.pathFile?.contains("pdf")!!) {
            pdfView.visibility = View.VISIBLE
            photoView.visibility = View.GONE
            FileLoader.with(pdfView.context)
                .load(Constance.DEVMAN_URL + fileData.pathFile)
                .fromDirectory(FileActivity.FILE_DIR, FileLoader.DIR_CACHE)
                .asFile(object : FileRequestListener<File> {
                    override fun onLoad(
                        request: FileLoadRequest?,
                        response: FileResponse<File>?
                    ) {
                        response?.body?.let {
                            pdfView.fromFile(it)
                                .load()
                        }
                    }

                    override fun onError(request: FileLoadRequest?, t: Throwable?) {
                        Log.e("File Adapter", "Error")
                    }
                })
        } else {
            pdfView.visibility = View.GONE
            photoView.visibility = View.VISIBLE
            Glide.with(this)
                .load(Constance.DEVMAN_URL + fileData.pathFile)
                .into(photoView)
        }
    }
}
