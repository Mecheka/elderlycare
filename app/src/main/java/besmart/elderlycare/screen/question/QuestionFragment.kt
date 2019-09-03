package besmart.elderlycare.screen.question


import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import besmart.elderlycare.R
import besmart.elderlycare.model.evaluation.Answer
import besmart.elderlycare.model.evaluation.EvaluationResponse
import besmart.elderlycare.model.profile.ProfileResponce
import besmart.elderlycare.screen.SelectType
import besmart.elderlycare.screen.base.BaseFragment
import besmart.elderlycare.util.BaseDialog
import besmart.elderlycare.util.Constance
import com.orhanobut.hawk.Hawk
import kotlinx.android.synthetic.main.activity_main.recyclerView
import kotlinx.android.synthetic.main.dialog_score.view.*
import kotlinx.android.synthetic.main.fragment_question.*
import org.koin.android.viewmodel.ext.android.viewModel

/**
 * A simple [Fragment] subclass.
 */
class QuestionFragment : BaseFragment(), QuestionAdapter.QuestionClick {

    companion object {

        const val EVALUATION = "evaluation"
        const val PROFILE = "profile"

        fun newInstance(evaluation: EvaluationResponse, profile: ProfileResponce) =
            QuestionFragment().apply {
                arguments = Bundle().apply {
                    putParcelable(EVALUATION, evaluation)
                    putParcelable(PROFILE, profile)
                }
            }
    }

    private lateinit var evaluation: EvaluationResponse
    private lateinit var profile: ProfileResponce
    private val viewModel: QuestionViewModel by viewModel()
    private lateinit var questionAdapter: QuestionAdapter
    private val selectType = Hawk.get<String>(Constance.LOGIN_TYPE)
    private var isPerson = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.getParcelable<EvaluationResponse>(EVALUATION)?.let {
            evaluation = it
        }
        arguments?.getParcelable<ProfileResponce>(PROFILE)?.let {
            profile = it
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_question, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initInstance()
        observerViewModel()
    }

    private fun initInstance(){
        if (selectType == SelectType.PERSON){
            btnSave.visibility = View.GONE
            isPerson = false
        }else{
            btnSave.visibility = View.VISIBLE
            isPerson = true
        }

        btnSave.setOnClickListener {
            questionAdapter.getAnswer()
        }
    }

    private fun observerViewModel(){
        viewModel.errorLiveData.observe(this, Observer {
            BaseDialog.warningDialog(requireContext(), it)
        })

        viewModel.loadingLiveData.observe(this, Observer {
            if (it) {
                showLoadingDialog(requireContext())
            } else {
                dismissDialog()
            }
        })

        viewModel.evaluationLiveData.observe(this, Observer {
            questionAdapter = QuestionAdapter(it.item!!, it.userEvaluarion, this, isPerson)
            recyclerView.apply {
                layoutManager = LinearLayoutManager(requireContext(), RecyclerView.VERTICAL,  false)
                setHasFixedSize(true)
                adapter = questionAdapter
            }
        })

        viewModel.scroreLiveEvent.observe(this, Observer {
            showScoreDialog(it.result, it.getResult())
        })

        viewModel.getQuestion(evaluation.id!!, profile.cardID, evaluation.id)
    }

    override fun onError() {
        showScoreDialog(0, "ภาวะพึ่งพาโดยสมบูรณ์")
    }

    override fun onSuccess(answer: Answer) {
//        showScoreDialog(answer.result, answer.getResult())
        viewModel.addAnswer(answer, profile.cardID, evaluation.id)
    }

    @SuppressLint("InflateParams", "SetTextI18n")
    private fun showScoreDialog(score: Int, result: String) {
        val view = layoutInflater.inflate(R.layout.dialog_score, null)
        val dialog = AlertDialog.Builder(requireContext())
        dialog.setView(view)

        view.textScore.text = "$score คะแนแ"
        view.textResult.text = result

        val alertDialog = dialog.create()
        alertDialog.show()

        view.btnOK.setOnClickListener {
            alertDialog.dismiss()
        }
    }
}
