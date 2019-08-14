package besmart.elderlycare.model.evaluation

interface QuestType {
    var type: Int

    companion object {
        const val HEADER = 1
        const val HEADERWITHCHOINCE = 2
        const val CHOINCE = 3
    }
}