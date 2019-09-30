package besmart.elderlycare.util

import android.annotation.SuppressLint
import android.app.Activity
import android.app.ProgressDialog
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import android.media.ExifInterface
import android.net.Uri
import android.os.AsyncTask
import android.os.Environment
import android.provider.MediaStore
import besmart.elderlycare.R
import java.io.File
import java.io.FileNotFoundException
import java.io.FileOutputStream
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*
import kotlin.math.min
import kotlin.math.roundToInt

class CreateImage(
    private val activity: Activity,
    private val source: Uri,
    private val path: String?
) : AsyncTask<Void, Int, File>() {
    private var fileTemp: File? = null
    var callbackListener: OnCallBackListener? = null
    var updateListener: OnConnectionUpdateListener? = null
    private var b: Bitmap? = null
    private var width: Int = 0
    private var height: Int = 0
    var progressDialog = ProgressDialog(activity)
    private var deleteFileAfterFinish: File? = null

    private fun getPath(uri: Uri): String {
        val cursor = activity.contentResolver.query(uri, null, null, null, null)
        var result = ""
        cursor?.use {
            it.moveToFirst()
            val idx = it.getColumnIndex(MediaStore.Images.ImageColumns.DATA)
            result = it.getString(idx)
        }

        return result
    }

    fun addDeleteFileAfterFinish(file: File): CreateImage {
        deleteFileAfterFinish = file
        return this
    }

    @SuppressLint("SimpleDateFormat")
    override fun doInBackground(vararg args: Void): File? {

        fileTemp = File(
            Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM).toString()
                + "/${SimpleDateFormat("yyyyMMdd_HHmmssSSS").format(Date())}.png"
        )


        try {
            val `is` = activity.contentResolver.openInputStream(Uri.parse(source.toString()))
            b = BitmapFactory.decodeStream(`is`)

            try {
                val exif = ExifInterface(path ?: getPath(source))

                when (exif.getAttributeInt(
                    ExifInterface.TAG_ORIENTATION,
                    ExifInterface.ORIENTATION_NORMAL
                )) {
                    0 -> {
                    }
                    1 -> {
                    }
                    2 -> matrixs?.postScale(-1f, 1f)
                    3 -> b = rotate(b, 180f)
                    4 -> {
                        b = rotate(b, 180f)
                        matrixs?.postScale(-1f, 1f)
                    }
                    5 -> {
                        b = rotate(b, 90f)
                        matrixs?.postScale(-1f, 1f)
                    }
                    6 -> b = rotate(b, 90f)
                    7 -> {
                        b = rotate(b, 270f)
                        matrixs?.postScale(-1f, 1f)
                    }
                    8 -> b = rotate(b, 270f)
                    else -> {
                    }
                }

            } catch (e: IllegalArgumentException) {
            }

            val max =
                if (b!!.width > 1300 || b!!.height > 1300) 1300 else if (b!!.width > b!!.height) b!!.width else if (b!!.height > b!!.width) b!!.height else b!!.height
            val fos = FileOutputStream(fileTemp!!)
            val ratio = min(
                max.toFloat() / b!!.width,
                max.toFloat() / b!!.height
            )
            width = (ratio * b!!.width).roundToInt()
            height = (ratio * b!!.height).roundToInt()
            b = Bitmap.createScaledBitmap(b!!, width, height, false)
            b!!.compress(Bitmap.CompressFormat.JPEG, 90, fos)

            fos.close()

            return fileTemp
        } catch (e: FileNotFoundException) {
            if (fileTemp != null) {
                fileTemp!!.delete()
            }
            return null
        } catch (e: IOException) {
            if (fileTemp != null) {
                fileTemp!!.delete()
            }
            return null
        }

    }

    override fun onPreExecute() {
        super.onPreExecute()

        progressDialog.setMessage(activity.getString(R.string.loading))
        progressDialog.setCancelable(false)
        progressDialog.show()
    }

    override fun onProgressUpdate(vararg values: Int?) {
        if (updateListener != null) {
            updateListener!!.onUpdate(*values)
        }
    }


    override fun onPostExecute(result: File?) {
        progressDialog.dismiss()

        deleteFileAfterFinish?.let {
            if (it.exists()) it.delete()
        }
        deleteFileAfterFinish = null

        if (result != null) {
            super.onPostExecute(result)
            try {
                callbackListener!!.onCreateImageSuccess(result, "" + width, "" + height)
            } catch (e: NullPointerException) {
            }

        }

    }

    interface OnCallBackListener {
        fun onCreateImageSuccess(result: File, width: String, height: String)
    }

    interface OnConnectionUpdateListener {
        fun onUpdate(vararg progress: Int?)
    }

    companion object {
        var matrixs: Matrix? = null
        fun rotate(source: Bitmap?, angle: Float): Bitmap {
            matrixs = Matrix()
            matrixs?.postRotate(angle)
            return Bitmap.createBitmap(source!!, 0, 0, source.width, source.height, matrixs, false)
        }
    }


}
