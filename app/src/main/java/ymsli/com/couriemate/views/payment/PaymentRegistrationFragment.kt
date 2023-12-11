package ymsli.com.couriemate.views.payment

import ymsli.com.couriemate.R
import ymsli.com.couriemate.base.BaseFragment
import ymsli.com.couriemate.di.component.FragmentComponent
import ymsli.com.couriemate.views.tasklist.TaskListViewModel
import android.Manifest
import android.app.Activity.RESULT_OK
import android.content.Intent
import android.content.pm.PackageManager
import android.database.Cursor
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.text.Editable
import android.text.TextWatcher
import android.util.Base64
import android.view.View
import android.view.WindowManager
import android.widget.AdapterView
import androidx.core.app.ActivityCompat
import androidx.lifecycle.Observer
import io.fabric.sdk.android.services.network.HttpRequest
import kotlinx.android.synthetic.main.fragment_payment_registration.*
import ymsli.com.couriemate.utils.common.Event
import java.io.ByteArrayOutputStream
import java.util.*

/**
 * Project Name : Couriemate
 * @company YMSLI
 * @author  Sushant Somani (VE00YM129)
 * @date   January 10, 2020
 * Copyright (c) 2019, Yamaha Motor Solutions (INDIA) Pvt Ltd.
 *
 * Description
 * -----------------------------------------------------------------------------------
 * PaymentRegistrationFragment : This is the PaymentFragment in the couriemate android application
 * -----------------------------------------------------------------------------------
 *
 * Revision History
 * -----------------------------------------------------------------------------------
 * Modified By          Modified On         Description
 *
 * -----------------------------------------------------------------------------------
 */
class PaymentRegistrationFragment : BaseFragment<TaskListViewModel>() {

    companion object {
        const val OPEN_CAMERA = 1
        const val OPEN_GALLERY = 2
        const val TAG = "PaymentRegistrationFragment"
        fun newInstance(): PaymentRegistrationFragment {
            val args = Bundle()
            val fragment = PaymentRegistrationFragment()
            fragment.arguments = args
            return fragment
        }
    }

    override fun provideLayoutId(): Int = R.layout.fragment_payment_registration

    override fun injectDependencies(fragmentComponent: FragmentComponent) =
        fragmentComponent.inject(this)

    override fun setupView(view: View) {
        progressBar.visibility = View.GONE
        uploaded_constraintLayout.visibility = View.GONE
        explain.visibility = View.GONE
        setHasOptionsMenu(true)
        //choose from gallery
        gallery_constraintLayout.setOnClickListener {
            val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
            if (intent.resolveActivity(activity!!.packageManager) == null){
                showMessage("No application found to perform this action.")
            }
            else{
                startActivityForResult(intent, OPEN_GALLERY)
                uploaded_constraintLayout.visibility = View.VISIBLE
            }
        }

        //open camera and click new image
        camera_constraintLayout.setOnClickListener {
            val cameraIntent = Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE)
            cameraIntent.putExtra("android.intent.extras.LENS_FACING_FRONT", 1)
            cameraIntent.putExtra("android.intent.extra.USE_FRONT_CAMERA", true)
            if (cameraIntent.resolveActivity(activity!!.packageManager) == null){
                showMessage("No application found to perform this action.")
            }
            else{
                startActivityForResult(cameraIntent, OPEN_CAMERA)
                uploaded_constraintLayout.visibility = View.VISIBLE
            }
        }

        reason_spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onNothingSelected(parent: AdapterView<*>?) {

            }

            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                if(position==4){
                    explain.visibility = View.VISIBLE
                    viewModel.paymentReceivingBank.postValue(resources.getStringArray(R.array.paid_to)[position])
                }
                else {
                    explain.visibility = View.GONE
                    viewModel.paymentNotes.postValue("")
                    viewModel.paymentReceivingBank.postValue(resources.getStringArray(R.array.paid_to)[position])
                }
            }
        }

        amount_value.addTextChangedListener(object : TextWatcher{
            override fun afterTextChanged(s: Editable?) {
                if(!s.isNullOrEmpty())viewModel.amoundPaid.postValue(s.toString().trim().toDouble())
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }

        })

        explain.addTextChangedListener(object : TextWatcher{
            override fun afterTextChanged(s: Editable?) {
                viewModel.paymentNotes.postValue(s.toString().trim())
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }

        })
        upload_button.setOnClickListener {
            viewModel.uploadPaymentRegistrationReceipt()
        }
    }

    override fun setupObservers() {
        super.setupObservers()
        viewModel.showProgress.observe(this, Observer {
            if(it) {
                progressBar.visibility = View.VISIBLE
                activity!!.window.setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)
            }
            else {
                progressBar.visibility = View.GONE
                activity!!.window.clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)
            }
        })

        viewModel.paymentReceiptUserAck.observe(this, Observer {
            it.data?.run { showMessage(this) }
        })
    }
    private lateinit var photo: Bitmap

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == RESULT_OK) {
            when (requestCode) {
                1 -> {//uploading via Camera
                    photo = data?.extras?.get("data") as Bitmap
                    uploaded_image_view.setImageBitmap(photo)
                    val baos = ByteArrayOutputStream()
                    photo.compress(Bitmap.CompressFormat.PNG,100,baos)

                    val byteArr : ByteArray= baos.toByteArray() //if image size is more than 2 MB, compress it by 40%
                    if((byteArr.size)/1000>=2000){
                        val tempPhoto = data?.extras?.get("data") as Bitmap
                        val tempBaos = ByteArrayOutputStream()
                        tempPhoto.compress(Bitmap.CompressFormat.JPEG,10,tempBaos)
                        viewModel.paymentReceipt.postValue(HttpRequest.Base64.encodeBytes(tempBaos.toByteArray()))

                    }
                    else //viewModel.paymentReceipt.postValue(Base64.encodeToString(baos.toByteArray(),Base64.CRLF))
                        viewModel.paymentReceipt.postValue(HttpRequest.Base64.encodeBytes(baos.toByteArray()))
                }

                2 -> { //uploading via Gallery
                    val selectedImage
                            : Uri = data!!.data!!
                    val filePath =
                        arrayOf(MediaStore.Images.Media.DATA)
                    val c
                            : Cursor = activity!!.getContentResolver().query(
                        selectedImage,
                        filePath,
                        null,
                        null,
                        null
                    )!!
                    c.moveToFirst()
                    val columnIndex
                            : Int = c.getColumnIndex(filePath[0])
                    val picturePath
                            : String = c.getString(columnIndex)
                    c.close()
                    val thumbnail: Bitmap = BitmapFactory.decodeFile(picturePath)
                    uploaded_image_view.setImageBitmap(thumbnail)
                    val baos = ByteArrayOutputStream()
                    thumbnail.compress(Bitmap.CompressFormat.JPEG,100,baos)

                    val byteArr : ByteArray= baos.toByteArray()
                    if((byteArr.size)/1000>=2000){ //if image size is more than 2 MB, compress it by 40%
                        //thumbnail.compress(Bitmap.CompressFormat.PNG,40,baos)
                        val smallImage : Bitmap = BitmapFactory.decodeFile(picturePath)
                        val tempBaos = ByteArrayOutputStream()
                        smallImage.compress(Bitmap.CompressFormat.JPEG,10,tempBaos)
                        viewModel.paymentReceipt.postValue(HttpRequest.Base64.encodeBytes(tempBaos.toByteArray()))
                        //viewModel.paymentReceipt.postValue(Base64.encodeToString(tempBaos.toByteArray(),Base64.DEFAULT))
                        //viewModel.paymentReceipt.postValue(Base64.encodeToString(tempBaos.toByteArray(),Base64.CRLF))
                    }
                    else //viewModel.paymentReceipt.postValue(Base64.encodeToString(baos.toByteArray(),Base64.CRLF))
                        viewModel.paymentReceipt.postValue(HttpRequest.Base64.encodeBytes(baos.toByteArray()))
                }
            }
        }
    }

    override fun onResume() {
        super.onResume()
        setPermissions()
    }

    /**
     * Set the required permissions if not allowed by the user.
     */
    private fun setPermissions() {
        if (ActivityCompat.checkSelfPermission(context!!,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
            ) !== PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                context!!,
                Manifest.permission.READ_EXTERNAL_STORAGE
            ) !== PackageManager.PERMISSION_GRANTED
        ) {
            requestPermissions(arrayOf(
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.READ_EXTERNAL_STORAGE
            ),100)
        }

        else if (ActivityCompat.checkSelfPermission(context!!,
                Manifest.permission.CAMERA
            ) !== PackageManager.PERMISSION_GRANTED) {
            requestPermissions(arrayOf(
                Manifest.permission.CAMERA
            ),101)
        }
    }
}