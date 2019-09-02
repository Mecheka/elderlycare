package besmart.elderlycare.screen.question

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import besmart.elderlycare.model.evaluation.EvaluationResponse
import besmart.elderlycare.model.profile.ProfileResponce

class QuestionPagerAdapter(
    fm: FragmentManager,
    behavior: Int = BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT,
    private val evaluation: EvaluationResponse,
    private val profile: ProfileResponce
) :
    FragmentPagerAdapter(fm, behavior) {
    override fun getItem(position: Int): Fragment {
        return when (position) {
            0 -> QuestionFragment.newInstance(evaluation, profile)
            else -> QuestionHistoryFragment.newInstance()
        }
    }

    override fun getCount(): Int = 2

    override fun getPageTitle(position: Int): CharSequence? = when (position) {
        0 -> "แบบประเมิน"
        else -> "ประวัตการประเมิน"
    }
}