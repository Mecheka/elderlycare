package besmart.elderlycare.screen.gps

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter

class GPSPagerAdapter(fm: FragmentManager, behavior: Int = BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) :
    FragmentStatePagerAdapter(fm, behavior) {
    override fun getItem(position: Int): Fragment = when (position) {
        0 -> MapsFragment()
        else -> GPSListUserFragment()
    }

    override fun getCount(): Int = 2

    override fun getPageTitle(position: Int): CharSequence? = when (position) {
        0 -> "แผนที่"
        else -> "รายชื่อ"
    }
}