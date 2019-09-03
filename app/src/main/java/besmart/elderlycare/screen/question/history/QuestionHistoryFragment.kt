package besmart.elderlycare.screen.question.history


import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import besmart.elderlycare.R
import besmart.elderlycare.model.evaluation.EvaluationResponse
import besmart.elderlycare.model.profile.ProfileResponce
import besmart.elderlycare.screen.base.BaseFragment
import besmart.elderlycare.screen.question.QuestionFragment
import besmart.elderlycare.util.BaseDialog
import kotlinx.android.synthetic.main.fragment_question_history.*
import org.koin.android.viewmodel.ext.android.viewModel

/**
 * A simple [Fragment] subclass.
 */
class QuestionHistoryFragment : BaseFragment() {

    companion object {
        const val EVALUATION = "evaluation"
        const val PROFILE = "profile"
        fun newInstance(evaluation: EvaluationResponse, profile: ProfileResponce) =
            QuestionHistoryFragment().apply {
                arguments = Bundle().apply {
                    putParcelable(EVALUATION, evaluation)
                    putParcelable(PROFILE, profile)
                }
            }
    }

    private val viewModel: QuestionHistoryViewModel by viewModel()
    private lateinit var evaluation: EvaluationResponse
    private lateinit var profile: ProfileResponce
    private lateinit var questionHistoryAdapter: QuestionHistoryAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.getParcelable<EvaluationResponse>(QuestionFragment.EVALUATION)?.let {
            evaluation = it
        }
        arguments?.getParcelable<ProfileResponce>(QuestionFragment.PROFILE)?.let {
            profile = it
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_question_history, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observerViewModel()
    }

    private fun observerViewModel() {
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
            questionHistoryAdapter = QuestionHistoryAdapter(it)
            recyclerView.apply {
                layoutManager = LinearLayoutManager(requireContext(), RecyclerView.VERTICAL, false)
                setHasFixedSize(true)
                addItemDecoration(
                    DividerItemDecoration(
                        requireContext(),
                        DividerItemDecoration.VERTICAL
                    )
                )
                adapter = questionHistoryAdapter
            }
            questionHistoryAdapter.onItemClick = {
                Intent().apply {
                    setClass(requireActivity(), QuestHistorySingleActivity::class.java)
                    putExtra(QuestHistorySingleActivity.USER_EVALUATION, it)
                    startActivity(this)
                }
            }
        })

        viewModel.getEvaluetionHistory(evaluation.id!!, profile.cardID!!)

    }
}
