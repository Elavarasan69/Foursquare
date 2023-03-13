package com.robosoft.foursquare.view.fragment

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.robosoft.foursquare.R
import com.robosoft.foursquare.databinding.FragmentOtpBinding
import com.robosoft.foursquare.model.dataclass.VerifyOtpBody
import com.robosoft.foursquare.model.dataclass.forgetpassword.ForgetPasswordBody
import com.robosoft.foursquare.model.network.ProjectService

class OtpFragment : Fragment() {

    private lateinit var otpBinding: FragmentOtpBinding
    private val projectApi = ProjectService()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        otpBinding = FragmentOtpBinding.inflate(inflater, container, false)

        val sharedPreferences =
            activity?.applicationContext?.getSharedPreferences(
                "sharedPreference",
                Context.MODE_PRIVATE
            )
        val email = sharedPreferences?.getString("email", "")!!

        otpBinding.resentTv.setOnClickListener {
            val data = ForgetPasswordBody(email)
            projectApi.resendOtp(data){
                if (it != null){
                    if (it.message == "Otp is resent, please check your mail"){
                        Toast.makeText(activity?.applicationContext,it.message,Toast.LENGTH_SHORT).show()
                    }
                    else {
                        Toast.makeText(activity?.applicationContext,it.message,Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }

        otpBinding.otpBtn.setOnClickListener {
            val otp = otpBinding.otpEt.text.toString()
            val data = VerifyOtpBody(email, otp)
            projectApi.verifyOtp(data) {
                if (it != null) {
                    Log.d("otp click",it.toString())
                    if (it.message == "true") {
                        activity?.supportFragmentManager?.beginTransaction()
                            ?.replace(
                                R.id.fragment_container,
                                PasswordFragment()
                            )
                            ?.addToBackStack(null)
                            ?.commit()
                    } else {
                        Toast.makeText(
                            activity?.applicationContext,
                            "Invalid OTP",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                } else {
                    Toast.makeText(
                        activity?.applicationContext,
                        "Something went wrong",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }
                return otpBinding.root
    }
}