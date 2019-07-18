package besmart.elderlycare.screen.main

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import besmart.elderlycare.databinding.ItemMainMenuBinding
import besmart.elderlycare.model.MenuItem

class MainMenuAdapter(private val menuList: List<MenuItem>, private val onMenuItemClick: OnMenuItemClick):RecyclerView.Adapter<MainMenuAdapter.MainMenuHolder>(){

    private lateinit var binding: ItemMainMenuBinding

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MainMenuHolder {
        binding = ItemMainMenuBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return MainMenuHolder(binding, onMenuItemClick)
    }

    override fun getItemCount(): Int = menuList.size

    override fun onBindViewHolder(holder: MainMenuHolder, position: Int) {
        holder.bind(menuList[position])
    }

    class MainMenuHolder(
        private val binding: ItemMainMenuBinding,
        private val onMenuItemClick: OnMenuItemClick
    ):RecyclerView.ViewHolder(binding.root){
        fun bind(menu: MenuItem) {
            binding.imageView.setImageResource(menu.icon)
            binding.textView.text = menu.title
            binding.menuRoot.setOnClickListener {
                onMenuItemClick.onMenuClick(menu.icon)
            }
        }
    }

    interface OnMenuItemClick{
        fun onMenuClick(icon: Int)
    }
}