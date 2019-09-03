package besmart.elderlycare.model.evaluation

data class Answer(val answer: String, val result: Int) {

    fun getResult(): String {
        return when (result) {
            in 0..4 -> "ภาวะพึ่งพาโดยสมบูรณ์"
            in 5..8 -> "ภาวะพึ่งพารุนแรง"
            in 9..11 -> "ภาวะพึ่งพาปานกลาง"
            else -> "ไม่เป็นการพึ่งพา"
        }
    }

    companion object {
        fun getResult(score: Int): String {
            return when (score) {
                in 0..4 -> "ภาวะพึ่งพาโดยสมบูรณ์"
                in 5..8 -> "ภาวะพึ่งพารุนแรง"
                in 9..11 -> "ภาวะพึ่งพาปานกลาง"
                else -> "ไม่เป็นการพึ่งพา"
            }
        }
    }
}