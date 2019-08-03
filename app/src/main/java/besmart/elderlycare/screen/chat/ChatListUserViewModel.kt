package besmart.elderlycare.screen.chat

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import besmart.elderlycare.model.chat.ChatItem
import besmart.elderlycare.repository.ProfileRepository
import besmart.elderlycare.util.ActionLiveData
import besmart.elderlycare.util.BaseViewModel
import besmart.elderlycare.util.HandingNetworkError

class ChatListUserViewModel(private val repository: ProfileRepository) : BaseViewModel() {

    private val _errorLiveEvent = ActionLiveData<String>()
    val errorLiveData: LiveData<String>
        get() = _errorLiveEvent

    private val _chatLiveData = MutableLiveData<List<ChatItem>>()
    val chatLiveData: LiveData<List<ChatItem>>
        get() = _chatLiveData

    private val _loadingLiveEvent = ActionLiveData<Boolean>()
    val loadingLiveData: LiveData<Boolean>
        get() = _loadingLiveEvent

    fun getAllUser() {
        _loadingLiveEvent.sendAction(true)
        addDisposable(
            repository.getAllProfile().subscribe({ responce ->
                _loadingLiveEvent.sendAction(false)
                if (responce.isSuccessful) {
                    val chatList = mutableListOf<ChatItem>()
                    chatList.add(ChatItem(ChatItem.HEAD, "กลุ่ม 2"))
                    chatList.add(ChatItem(ChatItem.GROUP, "สร้างกลุ่ม", "สร้างกลุ่มเพื่อคุยงาน"))
                    chatList.add(ChatItem(ChatItem.FREIND, "คุยเรื่องวัคซีนไวรัสตัวใหม่"))
                    chatList.add(ChatItem(ChatItem.FREIND, "คุยเรื่องวัคซีนไวรัสตัวใหม่"))
                    chatList.add(ChatItem(ChatItem.HEAD, "เพื่อน ${responce.body()!!.size}"))
                    chatList.addAll(responce.body()!!.map { ChatItem(ChatItem.FREIND, content = it) })
                    _chatLiveData.value = chatList
                } else {
                    responce.errorBody()?.let {
                        _errorLiveEvent.sendAction(HandingNetworkError.getErrorMessage(it))
                    }
                }
            }, { error ->
                _loadingLiveEvent.sendAction(false)
                _errorLiveEvent.sendAction(HandingNetworkError.handingError(error))
            })
        )
    }
}