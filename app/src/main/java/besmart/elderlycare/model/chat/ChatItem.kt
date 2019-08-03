package besmart.elderlycare.model.chat

data class ChatItem(
    override var itemType: Int,
    val title: String? = null,
    val content: Any? = null
) : ChatItemType {

    companion object {
        const val HEAD = 0
        const val GROUP = 1
        const val FREIND = 2
    }
}