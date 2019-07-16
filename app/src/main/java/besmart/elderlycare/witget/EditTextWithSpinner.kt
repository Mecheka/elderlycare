package besmart.elderlycare.witget

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Context
import android.text.Editable
import android.text.InputType
import android.text.TextWatcher
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.ViewGroup
import android.view.Window
import android.widget.FrameLayout
import androidx.annotation.Nullable
import androidx.databinding.*
import androidx.databinding.adapters.ListenerUtil
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import besmart.elderlycare.R
import besmart.elderlycare.databinding.WidgetEdittextWithSpinnerBinding

class EditTextWithSpinner @JvmOverloads constructor(
    context: Context, attrs: AttributeSet, defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr) {

    lateinit var binding: WidgetEdittextWithSpinnerBinding
    var dialog: Dialog? = null

    var text: String = ""
        get() = binding.txtType.text.toString()
        set(value) {
            field = value
            binding.txtType.setText(value)
        }

    var hintText: String = ""
        get() = binding.txtLayout.hint.toString()
        set(value) {
            field = value
            binding.txtLayout.hint = value
        }

    init {
        initInflate()
        initWithAttrs(attrs, defStyleAttr, defStyleAttr)
        initInstance()
    }

    fun addTextChangedListener(listener: TextWatcher) =
        binding.txtType.addTextChangedListener(listener)

    @InverseBindingMethods(
        InverseBindingMethod(
            type = EditTextWithSpinner::class,
            attribute = "android:text",
            method = "getText"
        )
    )

    object EditTextWithSpinnerBinder {

        @JvmStatic
        @BindingAdapter("android:text")
        fun setText(editTextWithSpinner: EditTextWithSpinner, text: String?) {
            text?.let {
                if (text != editTextWithSpinner.text) {
                    editTextWithSpinner.text = it
                    editTextWithSpinner.binding.txtType.setText(it)
                }
            }
        }

        @JvmStatic
        @BindingAdapter("android:text")
        fun setText(editTextWithSpinner: EditTextWithSpinner, text: ObservableField<String>) {
            text.get()?.let {
                if (text.get() != editTextWithSpinner.text) {

                    editTextWithSpinner.text = it.toUpperCase()
                    editTextWithSpinner.binding.txtType.setText(it.toUpperCase())
                }
            }
        }

        @JvmStatic
        @InverseBindingAdapter(attribute = "android:text", event = "android:textAttrChanged")
        fun getText(editTextWithTitle: EditTextWithSpinner): String {
            return editTextWithTitle.text
        }

        @JvmStatic
        @BindingAdapter(
            value = ["android:beforeTextChanged", "android:onTextChanged", "android:afterTextChanged", "android:textAttrChanged"],
            requireAll = false
        )
        fun setTextWatcher(
            editTextWithTitle: EditTextWithSpinner, before: BeforeTextChanged?,
            on: OnTextChanged?, after: AfterTextChanged?,
            textAttrChanged: InverseBindingListener?
        ) {
            val newValue: TextWatcher?
            if (before == null && after == null && on == null && textAttrChanged == null) {
                newValue = null
            } else {
                newValue = object : TextWatcher {
                    override fun beforeTextChanged(
                        s: CharSequence,
                        start: Int,
                        count: Int,
                        after: Int
                    ) {
                        before?.beforeTextChanged(s, start, count, after)
                    }

                    override fun onTextChanged(
                        s: CharSequence,
                        start: Int,
                        before: Int,
                        count: Int
                    ) {
                        on?.onTextChanged(s, start, before, count)
                        textAttrChanged?.onChange()
                    }

                    override fun afterTextChanged(s: Editable) {
                        after?.afterTextChanged(s)
                    }
                }
            }
            val oldValue = ListenerUtil.trackListener<TextWatcher>(
                editTextWithTitle,
                newValue,
                R.id.textWatcher
            )
            if (oldValue != null) {
                editTextWithTitle.binding.txtType.removeTextChangedListener(oldValue)
            }
            if (newValue != null) {
                editTextWithTitle.binding.txtType.addTextChangedListener(newValue)
            }
        }

        interface AfterTextChanged {
            fun afterTextChanged(s: Editable)
        }

        interface BeforeTextChanged {
            fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int)
        }

        interface OnTextChanged {
            fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int)
        }
    }

    private fun initInflate() {
        binding = DataBindingUtil.inflate(
            LayoutInflater.from(context),
            R.layout.widget_edittext_with_spinner,
            this,
            true
        )
    }

    private fun initWithAttrs(@Nullable attrs: AttributeSet, defStyleAttr: Int, defStyleRes: Int) {
        val array =
            context.theme.obtainStyledAttributes(
                attrs,
                R.styleable.EditTextWithSpinner,
                defStyleAttr,
                defStyleRes
            )

        try {
            text = ""
            array.getString(R.styleable.EditTextWithSpinner_android_text)?.let {
                text = it
            }

            hintText = ""
            array.getString(R.styleable.EditTextWithSpinner_hintText)?.let {
                hintText = it
            }

        } finally {
            array.recycle()
        }
    }

    @SuppressLint("ResourceType")
    private fun initInstance() {
        binding.txtType.setText(text)
        binding.txtLayout.hint = hintText

    }

    @SuppressLint("ClickableViewAccessibility")
    fun setAdapter(adapter: RecyclerView.Adapter<*>) {
        val recyclerView = RecyclerView(context).apply {
            this.layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
            this.addItemDecoration(DividerItemDecoration(context, 0))
            this.adapter = adapter
            adapter.notifyDataSetChanged()
        }
        dialog = Dialog(context, R.style.BaseDialogTheme).apply {
            this.requestWindowFeature(Window.FEATURE_NO_TITLE)
            this.window!!.setLayout(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT
            )
            this.setCancelable(true)
            this.setContentView(recyclerView)
        }
        binding.txtType.setOnTouchListener { _, event ->
            val inputType = binding.txtType.inputType
            binding.txtType.inputType = InputType.TYPE_NULL
            binding.txtType.onTouchEvent(event)
            binding.txtType.inputType = inputType
            dialog?.show()
            return@setOnTouchListener true
        }
    }
}