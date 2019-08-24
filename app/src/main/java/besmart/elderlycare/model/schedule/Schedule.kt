package besmart.elderlycare.model.schedule

import android.graphics.Color
import kotlin.random.Random

data class Schedule(
    var title: String,
    var note: String,
    var day: Int,
    var hour: Int,
    var categoryColor: Int
) {

    companion object {
        fun randomValue(): Schedule {
            val titleList = listOf("นัดเจออสม.", "ตอนเที่ยงไปโรงพบาบาล", "กรอกแบบประเมิน").random()
            val noteList =
                listOf("มีนัดกับผู้สูงอายุ นายก", "มีนัดกับผู้สูงอายุ นางข", "ประชุม").random()
            val day = Random.nextInt(0, 27)
            val hour = Random.nextInt(0, 23)
            val colorList = listOf<Int>(
                Color.RED,
                Color.YELLOW,
                Color.BLUE,
                Color.BLACK,
                Color.MAGENTA
            ).random()
            return Schedule(titleList, noteList, day, hour, colorList)
        }
    }
}