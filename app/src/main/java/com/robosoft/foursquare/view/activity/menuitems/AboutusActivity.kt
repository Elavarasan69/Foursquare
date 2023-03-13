package com.robosoft.foursquare.view.activity.menuitems

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.robosoft.foursquare.databinding.ActivityAboutusBinding
import com.robosoft.foursquare.model.network.ProjectService

class AboutusActivity : AppCompatActivity() {

    private lateinit var aboutusBinding: ActivityAboutusBinding
    private val projectApi = ProjectService()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        aboutusBinding = ActivityAboutusBinding.inflate(layoutInflater)
        setContentView(aboutusBinding.root)

        aboutusBinding.backAboutIbn.setOnClickListener {
            onBackPressed()
        }
        projectApi.aboutUS {
            if (it != null) {
                if (it.message.isEmpty()){
                    Toast.makeText(this,"Something went wrong",Toast.LENGTH_SHORT).show()
                } else {
                    aboutusBinding.aboutUs.text = it.message
                    Log.d("about us respone",it.message )
                }
            }
        }
    }
}