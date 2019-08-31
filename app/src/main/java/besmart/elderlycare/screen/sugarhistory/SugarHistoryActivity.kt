package besmart.elderlycare.screen.sugarhistory

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

class SugarHistoryActivity : BaseActivity() {

    companion object {
        const val PROFILE = "profile"
    }

    private val viewModel: SugarHistoryViewModel by viewModel()
    private val profile: ProfileResponce by lazy {
        intent.getParcelableExtra(PROFILE) as ProfileResponce
    }
    private lateinit var adapterSugar: SugarHistoryAdapter
    private var deletePosition: Int? = null
    private var isRemoveItem = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sugar_history)
        initInstance()
        observerViewModel()
    }

    private fun initInstance() {
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        toolbar.setNavigationOnClickListener {
            onBackPressed()
        }

        recyclerView.apply {
            layoutManager =
                LinearLayoutManager(
                    this@SugarHistoryActivity,
                    RecyclerView.VERTICAL,
                    false
                )
            hasFixedSize()
        }

        val swipeController = SwipeController(object : SwipeControllerActions {

            override fun onRightClicked(position: Int) {
                super.onRightClicked(position)
                Log.i("Right Click", "click $position")
                deletePosition = position
                val item = adapterSugar.getItemByPosition(position)
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

        viewModel.sugarLiveData.observe(this, Observer {
            adapterSugar = SugarHistoryAdapter(it.toMutableList())
            recyclerView.adapter = adapterSugar
        })

        viewModel.removeSuccessLiveEvent.observe(this, Observer { isSuccess ->
            if (isSuccess) {
                deletePosition?.let {
                    isRemoveItem = true
                    adapterSugar.removeItem(it)
                }
            }
        })
        viewModel.getSugarHistory(profile.cardID!!)
    }

    override fun onBackPressed() {
        if (isRemoveItem) {
            setResult(Activity.RESULT_OK)
        }
        super.onBackPressed()
    }
}
