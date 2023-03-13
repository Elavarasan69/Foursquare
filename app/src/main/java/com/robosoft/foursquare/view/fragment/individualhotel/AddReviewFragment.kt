package com.robosoft.foursquare.view.fragment.individualhotel

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.robosoft.foursquare.R
import com.robosoft.foursquare.SharedPreferenceManager
import com.robosoft.foursquare.databinding.FragmentAddReviewBinding
import com.robosoft.foursquare.model.dataclass.addreview.ImageList
import com.robosoft.foursquare.model.dataclass.addreview.ReviewBody
import com.robosoft.foursquare.model.dataclass.addreview.ReviewImageRequest
import com.robosoft.foursquare.viewModel.AddReviewViewModel
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.File

class AddReviewFragment : Fragment() {

    private lateinit var addReviewBinding: FragmentAddReviewBinding
    private lateinit var viewModel: AddReviewViewModel
    private var imageList = mutableListOf<Uri>()
    private var imagePath = mutableListOf<String>()
    val permissionList =
        arrayOf(Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE)

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment

        addReviewBinding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_add_review, container, false)
        viewModel = ViewModelProvider(this)[AddReviewViewModel::class.java]

        val placeBundle = arguments
        val placeId = placeBundle?.getString("placeId")
        val placeName = placeBundle?.getString("placeName")

        Log.d("place", placeId.toString())

        addReviewBinding.reviewBack.setOnClickListener {
            requireActivity().onBackPressed()
        }

        addReviewBinding.hotelName.text = placeName


        addReviewBinding.addImgOne?.setOnClickListener {
            if (!checkPermission(activity?.applicationContext!!, permissionList[0])) {
                ActivityCompat.requestPermissions(
                    context as Activity,
                    arrayOf(permissionList[0]), 1
                )
            } else {
                var intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
                startActivityForResult(intent, 1)
            }
        }

        addReviewBinding.reviewSubmit?.setOnClickListener {

            val sharedPreferences =
                activity?.applicationContext?.getSharedPreferences(
                    "sharedPreference",
                    Context.MODE_PRIVATE
                )
            val accessToken =
                SharedPreferenceManager(activity?.applicationContext!!).getAccessToken()

            val review = addReviewBinding.reviewEt?.text.toString()
            addReviewPage()
            val data = ReviewBody(placeId.toString(), review)
            addReviewBody(accessToken, data)

            var images: MutableList<ImageList>? = mutableListOf<ImageList>()

            for (i in imagePath) {
                val file: File = File(i)
                val requestFile =
                    RequestBody.create(MediaType.parse("multipart/form-data"), file)

                val body: MultipartBody.Part =
                    MultipartBody.Part.createFormData("image", file.name, requestFile)
                images?.add(ImageList(body))
            }

            val place = RequestBody.create(
                MediaType.parse("multipart/form-data"),
                placeId.toString()
            )
            if (images?.isEmpty() == true)
                images = null
            val reviewImageRequest = ReviewImageRequest(images)

            addReviewImage()
            addImage(accessToken, place, reviewImageRequest.imageList)
        }

        return addReviewBinding.root
    }


    fun addReviewPage() {
        viewModel.getAddReviewDataObserver().observe(viewLifecycleOwner, Observer {
            if (it != null) {
                Toast.makeText(
                    activity?.applicationContext,
                    it.message,
                    Toast.LENGTH_SHORT
                ).show()
            } else {
                Toast.makeText(
                    activity?.applicationContext,
                    "Something went wrong",
                    Toast.LENGTH_SHORT
                ).show()
            }
        })
    }

    fun addReviewBody(
        accessToken: String,
        data: ReviewBody
    ) {
        viewModel.addReviewByText(accessToken, data)
    }


    fun addReviewImage() {
        viewModel.getReviewDataObserver().observe(viewLifecycleOwner, Observer {
            if (it != null) {
                Toast.makeText(
                    activity?.applicationContext,
                    it.message,
                    Toast.LENGTH_SHORT
                ).show()
            } else {
                Toast.makeText(
                    activity?.applicationContext,
                    "Something went wrong",
                    Toast.LENGTH_SHORT
                ).show()
            }
        })
    }

    fun addImage(
        accessToken: String,
        placeId: RequestBody, imageList: MutableList<ImageList>?
    ) {
        viewModel.addImage(accessToken, placeId, imageList)
    }

    private fun checkPermission(context: Context?, PERMISSION: String): Boolean {
        context?.apply {
            PERMISSION.apply {
                if (ActivityCompat.checkSelfPermission(
                        context,
                        PERMISSION
                    ) != PackageManager.PERMISSION_GRANTED
                ) {
                    return false
                }
            }
        }
        return true
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == 1) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                if (permissions[0].equals(permissionList[0])) {
                    var intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
                    startActivityForResult(intent, 1)
                } else {
                    val intent = Intent(Intent.ACTION_PICK)
                    intent.type = "image/*"
                    startActivityForResult(intent, 2)
                }
            }
        }
    }

    @Deprecated("Deprecated in Java")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == 1 && resultCode == AppCompatActivity.RESULT_OK) {
            var bmp = data?.extras?.get("data") as Bitmap
            addReviewBinding.addImgOne?.setImageBitmap(bmp)
        } else if (requestCode == 2) {
            addReviewBinding.addImgOne?.setImageURI(data?.data)
        }
    }
}
