package besmart.elderlycare.screen.bodymasshistory

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

@Suppress("NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")
class BodyMassHistoryActivity : BaseActivity() {

    companion object {
        const val PROFILE = "profile"
    }

    private val viewModel: BodyMassHistoryViewModel by viewModel()
    private lateinit var profile: ProfileResponce
    private var deletePosition: Int? = null
    private var isRemoveItem = false
    private lateinit var bodyAdapter: BodyMassHistoryAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_body_mass_history)
        profile = intent.getParcelableExtra(PROFILE)
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
                val item = bodyAdapter.getItemByPosition(position)
                viewModel.removeBodyMassHistory(item)
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

        viewModel.bodyMassLiveData.observe(this, Observer {
            bodyAdapter = BodyMassHistoryAdapter(it.toMutableList())
            recyclerView.apply {
                this.layoutManager =
                    LinearLayoutManager(this@BodyMassHistoryActivity, RecyclerView.VERTICAL, false)
                this.hasFixedSize()
                this.adapter = bodyAdapter
            }
        })

        viewModel.removeSuccessLiveEvent.observe(this, Observer {isSuccess->
            if (isSuccess){
                deletePosition?.let {
                    isRemoveItem = true
                    bodyAdapter.removeItem(it)
                }
            }
        })

        viewModel.getBodyMassHistory(profile.cardID!!)
    }

    override fun onBackPressed() {
        if (isRemoveItem){
            setResult(Activity.RESULT_OK)
        }
        super.onBackPressed()
    }
}
