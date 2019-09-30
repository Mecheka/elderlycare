package besmart.elderlycare.screen.flie

import android.annotation.SuppressLint
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import besmart.elderlycare.databinding.ItemFileBinding
import besmart.elderlycare.model.file.FileData
import besmart.elderlycare.util.Constance
import besmart.elderlycare.util.SimpleOnItemClick
import besmart.elderlycare.util.loadImageUrl
import com.krishna.fileloader.FileLoader
import com.krishna.fileloader.listener.FileRequestListener
import com.krishna.fileloader.pojo.FileResponse
import com.krishna.fileloader.request.FileLoadRequest
import java.io.File


class FileAdapter(
    private val list: List<FileData>,
    private val onClick: SimpleOnItemClick<FileData>
) :
    RecyclerView.Adapter<FileAdapter.FileHolder>() {

    private lateinit var binding: ItemFileBinding

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FileHolder {
        binding = ItemFileBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return FileHolder(binding, onClick)
    }

    override fun getItemCount(): Int = list.size

    override fun onBindViewHolder(holder: FileHolder, position: Int) {
        holder.bind(list[position])
    }

    class FileHolder(
        private val binding: ItemFileBinding,
        private val onClick: SimpleOnItemClick<FileData>
    ) : RecyclerView.ViewHolder(binding.root) {

        @SuppressLint("SetJavaScriptEnabled")
        fun bind(file: FileData) {
            binding.model = file
            binding.holder = this
            if (file.pathFile?.contains("pdf")!!) {
                binding.imageFile.visibility = View.INVISIBLE
                binding.pdfView.visibility = View.VISIBLE
                FileLoader.with(binding.pdfView.context)
                    .load(Constance.DEVMAN_URL + file.pathFile)
                    .fromDirectory(FileActivity.FILE_DIR, FileLoader.DIR_CACHE)
                    .asFile(object : FileRequestListener<File> {
                        override fun onLoad(
                            request: FileLoadRequest?,
                            response: FileResponse<File>?
                        ) {
                            response?.body?.let {
                                binding.pdfView.fromFile(it)
                                    .defaultPage(0)
                                    .enableSwipe(false)
                                    .enableDoubletap(false)
                                    .onLoad {
                                    }
                                    .load()
                            }
                        }

                        override fun onError(request: FileLoadRequest?, t: Throwable?) {
                            Log.e("File Adapter", "Error")
                        }
                    })
            } else {
                binding.imageFile.visibility = View.VISIBLE
                binding.pdfView.visibility = View.GONE
                binding.imageFile.loadImageUrl(Constance.DEVMAN_URL + file.pathFile)
            }
            binding.executePendingBindings()
        }

        fun onDetailClick(model: FileData) {
            onClick.onItemClick(model)
        }
    }
}