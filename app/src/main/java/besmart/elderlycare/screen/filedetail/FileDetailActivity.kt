package besmart.elderlycare.screen.filedetail

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import besmart.elderlycare.R
import besmart.elderlycare.model.file.FileData
import besmart.elderlycare.util.Constance
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.activity_file_detail.*
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_main.toolbar

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
        Glide.with(this)
            .load(Constance.DEVMAN_URL+fileData.pathFile)
            .into(photoView)
    }
}
