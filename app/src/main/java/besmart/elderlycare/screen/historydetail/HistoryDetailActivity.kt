package besmart.elderlycare.screen.historydetail

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import besmart.elderlycare.R
import besmart.elderlycare.model.history.HistoryResponce
import org.koin.android.viewmodel.ext.android.viewModel

class HistoryDetailActivity : AppCompatActivity() {

    companion object {
        const val HISTORY = "history"
    }

    private val history: HistoryResponce by lazy {
        intent.getParcelableExtra(HISTORY)
    }
    private val viewModel: HistoryDetailViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_history_detail)
    }

    private fun observerViewModel(){
        viewModel.getHistoryDetail(history)
    }
}
