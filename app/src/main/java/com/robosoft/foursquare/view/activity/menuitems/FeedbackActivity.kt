package com.robosoft.foursquare.view.activity.menuitems

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.robosoft.foursquare.R
import com.robosoft.foursquare.SharedPreferenceManager
import com.robosoft.foursquare.databinding.ActivityFeedbackBinding
import com.robosoft.foursquare.model.dataclass.feedback.FeedbackBody
import com.robosoft.foursquare.viewModel.FeedbackViewModel

class FeedbackActivity : AppCompatActivity() {


    private lateinit var feedbackBinding: ActivityFeedbackBinding
    private lateinit var viewModel: FeedbackViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        feedbackBinding = DataBindingUtil.setContentView(this, R.layout.activity_feedback)
        viewModel = ViewModelProvider(this)[FeedbackViewModel::class.java]

        feedbackBinding.backFeedbackIbn.setOnClickListener {
            onBackPressed()
        }




        viewModel = ViewModelProvider(this)[FeedbackViewModel::class.java]

        feedbackBinding.addFeedback.setOnClickListener {
            val accessToken = SharedPreferenceManager(this).getAccessToken()
            val data = FeedbackBody(feedbackBinding.feedback.text.toString())
            viewModel.getFeedbackDataObserver().observe(this, Observer {
                if (it != null) {
                    Log.d("feedback response", it.toString())
                    Toast.makeText(this, it.message, Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(
                        this,
                        "Something went wrong",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            })
            viewModel.giveFeedback(accessToken, data)
            feedbackBinding.feedback.text.clear()
        }
    }
}