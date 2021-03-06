package besmart.elderlycare.screen.gps

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import besmart.elderlycare.R
import besmart.elderlycare.screen.base.BaseFragment
import besmart.elderlycare.util.BaseDialog
import kotlinx.android.synthetic.main.activity_flie.*
import org.koin.android.viewmodel.ext.android.sharedViewModel

class GPSListUserFragment : BaseFragment() {

    private val viewModel: GPSViewModel by sharedViewModel()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_gps_list_user, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initInstance()
        observerViewModel()
    }

    private fun initInstance() {
        recyclerView.layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
        recyclerView.addItemDecoration(
            DividerItemDecoration(
                context,
                DividerItemDecoration.VERTICAL
            )
        )
        recyclerView.hasFixedSize()
    }

    private fun observerViewModel() {
        viewModel.errorLiveData.observe(this, Observer {
            BaseDialog.warningDialog(context!!, it)
        })

        viewModel.loadingLiveData.observe(this, Observer {
            if (it) {
                showLoadingDialog(context!!)
            } else {
                dismissDialog()
            }
        })

        viewModel.profileLiveData.observe(this, Observer {
            recyclerView.adapter = GPSListUserAdapter(it)
        })

        viewModel.getGPSListUser()
    }
}
