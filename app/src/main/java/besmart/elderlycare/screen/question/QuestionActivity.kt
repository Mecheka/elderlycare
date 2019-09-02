package besmart.elderlycare.screen.question

import android.os.Bundle
import besmart.elderlycare.R
import besmart.elderlycare.model.evaluation.EvaluationResponse
import besmart.elderlycare.model.profile.ProfileResponce
import besmart.elderlycare.screen.base.BaseActivity
import kotlinx.android.synthetic.main.activity_main.toolbar
import kotlinx.android.synthetic.main.activity_question.*

class QuestionActivity : BaseActivity() {

    companion object {
        const val EVALUATION = "evaluation"
        const val PROFILE = "profile"
    }

    private val evaluation: EvaluationResponse by lazy {
        intent.getParcelableExtra(EVALUATION) as EvaluationResponse
    }
    private val profile: ProfileResponce by lazy {
        intent.getParcelableExtra(PROFILE) as ProfileResponce
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_question)

        initInstance()
    }

    private fun initInstance() {
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        toolbar.setNavigationOnClickListener {
            finish()
        }

        viewPager.adapter =
            QuestionPagerAdapter(supportFragmentManager, evaluation = evaluation, profile = profile)
        tabLayout.setupWithViewPager(viewPager)
//        btnSave.setOnClickListener {
//            questionAdapter.getAnswer()
//        }
    }
}
