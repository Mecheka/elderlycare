package besmart.elderlycare.screen.flie

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import besmart.elderlycare.databinding.ItemFileBinding
import besmart.elderlycare.model.file.FileData
import besmart.elderlycare.util.Constance
import besmart.elderlycare.util.SimpleOnItemClick
import besmart.elderlycare.util.loadImageUrl

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

        fun bind(file: FileData) {
            binding.model = file
            binding.holder = this
            binding.imageFile.loadImageUrl(Constance.DEVMAN_URL + file.pathFile)
            binding.executePendingBindings()
        }

        fun onDetailClick(model: FileData) {
            onClick.onItemClick(model)
        }
    }
}