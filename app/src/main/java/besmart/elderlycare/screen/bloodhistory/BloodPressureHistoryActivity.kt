package besmart.elderlycare.screen.bloodhistory

import android.app.Activity
import android.graphics.Canvas
import android.os.Bundle
import android.util.Log
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import besmart.elderlycare.R
import besmart.elderlycare.model.profile.ProfileResponce
import besmart.elderlycare.screen.base.BaseActivity
import besmart.elderlycare.util.BaseDialog
import besmart.elderlycare.util.SwipeController
import besmart.elderlycare.util.SwipeControllerActions
import kotlinx.android.synthetic.main.activity_main.*
import org.koin.android.viewmodel.ext.android.viewModel

class BloodPressureHistoryActivity : BaseActivity() {

    companion object {
        const val PROFILE = "profile"
    }

    private val profile: ProfileResponce by lazy {
        intent.getParcelableExtra(PROFILE) as ProfileResponce
    }
    private val viewModel: BloodPressureHistoryViewModel by viewModel()
    private var deletePosition: Int? = null
    private var isRemoveItem = false
    private lateinit var bloodPressureHistoryAdapter: BloodPressureHistoryAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_blood_pressure_history)
        initInstance()
        observerViewModel()
    }

    private fun initInstance() {
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        toolbar.setNavigationOnClickListener {
            onBackPressed()
        }

        val swipeController = SwipeController(object : SwipeControllerActions {

            override fun onRightClicked(position: Int) {
                super.onRightClicked(position)
                Log.i("Right Click", "click $position")
                deletePosition = position
                val item = bloodPressureHistoryAdapter.getItemByPosition(position)
                viewModel.removeBloodPressureHistory(item)
            }
        })
        val itemTouchHelper = ItemTouchHelper(swipeController)
        itemTouchHelper.attachToRecyclerView(recyclerView)

        recyclerView.addItemDecoration(object : RecyclerView.ItemDecoration() {
            override fun onDraw(c: Canvas, parent: RecyclerView, state: RecyclerView.State) {
                swipeController.onDraw(c)
            }
        })
    }

    private fun observerViewModel() {
        viewModel.errorLiveData.observe(this, Observer {
            BaseDialog.warningDialog(this, it)
        })

        viewModel.loadingLiveData.observe(this, Observer {
            if (it) {
                showLoadingDialog(this)
            } else {
                dismissDialog()
            }
        })

        viewModel.evaluationLiveData.observe(this, Observer {
            bloodPressureHistoryAdapter = BloodPressureHistoryAdapter(it.toMutableList())
            recyclerView.apply {
                this.layoutManager =
                    LinearLayoutManager(
                        this@BloodPressureHistoryActivity,
                        RecyclerView.VERTICAL,
                        false
                    )
                this.hasFixedSize()
                this.adapter = bloodPressureHistoryAdapter
            }
        })

        viewModel.removeSuccessLiveEvent.observe(this, Observer {isSuccess->
            if (isSuccess){
                deletePosition?.let {
                    isRemoveItem = true
                    bloodPressureHistoryAdapter.removeItem(it)
                }
            }
        })

        viewModel.getEvaluationHistory(profile.cardID!!)
    }

    override fun onBackPressed() {
        if (isRemoveItem) {
            setResult(Activity.RESULT_OK)
        }
        super.onBackPressed()
    }
}
