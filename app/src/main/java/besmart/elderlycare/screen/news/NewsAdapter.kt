package besmart.elderlycare.screen.news

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import besmart.elderlycare.databinding.ItemNewsBinding
import besmart.elderlycare.model.news.NewsData
import besmart.elderlycare.util.Constance
import besmart.elderlycare.util.SimpleOnItemClick
import besmart.elderlycare.util.loadImageUrl

class NewsAdapter(
    private val list: List<NewsData>,
    private val onItemClick: SimpleOnItemClick<NewsData>
) :
    RecyclerView.Adapter<NewsAdapter.KnowlegeHolder>() {

    private lateinit var binding: ItemNewsBinding

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): KnowlegeHolder {
        binding = ItemNewsBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return KnowlegeHolder(binding, onItemClick)
    }

    override fun getItemCount(): Int = list.size

    override fun onBindViewHolder(holder: KnowlegeHolder, position: Int) {
        holder.bind(list[position])
    }

    class KnowlegeHolder(
        private val binding: ItemNewsBinding,
        private val onItemClick: SimpleOnItemClick<NewsData>
    ) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(news: NewsData) {
            binding.model = news
            binding.imageKnowlege.loadImageUrl(Constance.DEVMAN_URL + news.thumbnail)
            binding.executePendingBindings()
            binding.layoutRoot.setOnClickListener {
                onItemClick.onItemClick(news)
            }
        }
    }
}