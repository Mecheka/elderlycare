package besmart.elderlycare.screen.historydetail

import android.graphics.Canvas
import android.os.Bundle
import android.util.Log
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import besmart.elderlycare.R
import besmart.elderlycare.model.history.HistoryResponce
import besmart.elderlycare.screen.base.BaseActivity
import besmart.elderlycare.screen.bloodhistory.BloodPressureHistoryAdapter
import besmart.elderlycare.screen.bodymasshistory.BodyMassHistoryAdapter
import besmart.elderlycare.screen.sugarhistory.SugarHistoryAdapter
import besmart.elderlycare.util.BaseDialog
import besmart.elderlycare.util.SwipeController
import besmart.elderlycare.util.SwipeControllerActions
import kotlinx.android.synthetic.main.activity_main.*
import org.koin.android.viewmodel.ext.android.viewModel

@Suppress("IMPLICIT_CAST_TO_ANY")
class HistoryDetailActivity : BaseActivity() {

    companion object {
        const val HISTORY = "history"
    }

    private val history: HistoryResponce by lazy {
        intent.getParcelableExtra(HISTORY) as HistoryResponce
    }
    private var adapterBloodPressure: BloodPressureHistoryAdapter? = null
    private var adapterBodyMass: BodyMassHistoryAdapter? = null
    private var adapterSugar: SugarHistoryAdapter? = null
    private var deletePosition: Int? = null
    private val viewModel: HistoryDetailViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_history_detail)
        initInstance()
        observerViewModel()
    }

    private fun initInstance() {
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        toolbar.setNavigationOnClickListener {
            finish()
        }
        recyclerView.layoutManager = LinearLayoutManager(this, RecyclerView.VERTICAL, false)
        recyclerView.addItemDecoration(DividerItemDecoration(this, DividerItemDecoration.VERTICAL))
        recyclerView.hasFixedSize()

        val swipeController = SwipeController(object : SwipeControllerActions {

            override fun onRightClicked(position: Int) {
                super.onRightClicked(position)
                Log.i("Right Click", "click $position")
                val item = when (history.typeID) {
                    1 -> adapterBodyMass?.getItemByPosition(position)
                    2 -> adapterBloodPressure?.getItemByPosition(position)
                    else -> adapterSugar?.getItemByPosition(position)
                }
                deletePosition = position
                viewModel.removeHistoryByItemType(item, history.typeID)
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

        viewModel.bodymassLiveEvent.observe(this, Observer {
            adapterBodyMass = BodyMassHistoryAdapter(it.toMutableList())
            recyclerView.adapter = adapterBodyMass
        })
        viewModel.bloodPressure.observe(this, Observer {
            adapterBloodPressure = BloodPressureHistoryAdapter(it.toMutableList())
            recyclerView.adapter = adapterBloodPressure
        })
        viewModel.sugarLiveEvent.observe(this, Observer {
            adapterSugar = SugarHistoryAdapter(it.toMutableList())
            recyclerView.adapter = adapterSugar
        })

        viewModel.removeSuccessLiveEvent.observe(this, Observer { isSuccess ->
            if (isSuccess) {
                deletePosition?.let {
                    when (history.typeID) {
                        1 -> {
                            adapterBodyMass?.removeItem(it)
                        }
                        2 -> {
                            adapterBloodPressure?.removeItem(it)
                        }
                        else -> {
                            adapterSugar?.removeItem(it)
                        }
                    }
                }

            }
        })
        viewModel.getHistoryDetail(history)
    }
}
