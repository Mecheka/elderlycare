package besmart.elderlycare.screen.chat

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import besmart.elderlycare.databinding.ItemChatGroupBinding
import besmart.elderlycare.model.profile.ProfileResponce

class ChatAdapter(private val list: List<ProfileResponce>) :
    RecyclerView.Adapter<ChatAdapter.ChatHolder>() {

    private lateinit var binding: ItemChatGroupBinding

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChatHolder {
        binding = ItemChatGroupBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ChatHolder(binding)
    }

    override fun getItemCount(): Int = list.size

    override fun onBindViewHolder(holder: ChatHolder, position: Int) {
        holder.bind(list[position])
    }

    class ChatHolder(private val binding: ItemChatGroupBinding) :
        RecyclerView.ViewHolder(binding.root) {
        @SuppressLint("SetTextI18n")
        fun bind(profileResponce: ProfileResponce) {
            binding.textTitle.text = profileResponce.firstName + " " + profileResponce.lastName
            binding.textContent.text = "สวัดดี"
        }
    }
}