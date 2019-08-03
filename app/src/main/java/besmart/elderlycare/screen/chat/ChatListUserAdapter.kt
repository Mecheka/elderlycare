package besmart.elderlycare.screen.chat

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import besmart.elderlycare.databinding.ItemChatFriendBinding
import besmart.elderlycare.databinding.ItemChatGroupBinding
import besmart.elderlycare.databinding.ItemChatHeadBinding
import besmart.elderlycare.model.chat.ChatItem
import besmart.elderlycare.model.profile.ProfileResponce

class ChatListUserAdapter(private val list: List<ChatItem>) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private lateinit var bindingHead: ItemChatHeadBinding
    private lateinit var bindingGroup: ItemChatGroupBinding
    private lateinit var bindingFriend: ItemChatFriendBinding

    override fun getItemViewType(position: Int): Int {
        return list[position].itemType
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        return when (viewType) {
            ChatItem.HEAD -> {
                bindingHead = ItemChatHeadBinding.inflate(layoutInflater, parent, false)
                HeadHolder(bindingHead)
            }
            ChatItem.GROUP -> {
                bindingGroup = ItemChatGroupBinding.inflate(layoutInflater, parent, false)
                GroupHolder(bindingGroup)
            }
            else -> {
                bindingFriend = ItemChatFriendBinding.inflate(layoutInflater, parent, false)
                FriendHolder(bindingFriend)
            }
        }
    }

    override fun getItemCount(): Int = list.size

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (list[position].itemType) {
            ChatItem.HEAD -> {
                val view = holder as HeadHolder
                view.bind(list[position])
            }
            ChatItem.GROUP -> {
                val view = holder as GroupHolder
                view.bind(list[position])
            }
            else -> {
                val view = holder as FriendHolder
                view.bind(list[position])
            }
        }
    }

    class HeadHolder(private val binding: ItemChatHeadBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(chatItem: ChatItem) {
            binding.model = chatItem
            binding.executePendingBindings()
        }
    }

    class GroupHolder(private val binding: ItemChatGroupBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(chatItem: ChatItem) {
            binding.textTitle.text = chatItem.title
            binding.textContent.text = chatItem.content.toString()
        }
    }

    class FriendHolder(private val binding: ItemChatFriendBinding) :
        RecyclerView.ViewHolder(binding.root) {
        @SuppressLint("SetTextI18n")
        fun bind(chatItem: ChatItem) {
            var model: ProfileResponce
            chatItem.content?.let {
                model = it as ProfileResponce
                binding.textContent.text = "${model.firstName} ${model.lastName}"
            }?:run{
                binding.textContent.text = chatItem.title
            }
        }
    }
}