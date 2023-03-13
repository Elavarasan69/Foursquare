package com.robosoft.foursquare.view.fragment

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.robosoft.foursquare.R
import com.robosoft.foursquare.databinding.FragmentLoginBinding
import com.robosoft.foursquare.model.dataclass.forgetpassword.ForgetPasswordBody
import com.robosoft.foursquare.model.dataclass.signin.SignInBody
import com.robosoft.foursquare.model.network.ProjectService
import com.robosoft.foursquare.view.activity.HomeActivity

class LoginFragment : Fragment() {

    private lateinit var loginBinding: FragmentLoginBinding
    private val projectApi = ProjectService()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        loginBinding = FragmentLoginBinding.inflate(inflater, container, false)


        loginBinding.loginBtn.isEnabled

        loginBinding.skipTv.setOnClickListener {
            activity?.startActivity(Intent(activity,HomeActivity::class.java))
        }

        loginBinding.createAccountTv.setOnClickListener {
            activity?.supportFragmentManager?.beginTransaction()
                ?.replace(
                    R.id.fragment_container,
                    RegistrationFragment()
                )
                ?.addToBackStack(null)
                ?.commit()
        }

        loginBinding.forgetPasswordTv.setOnClickListener {
            val email = loginBinding.emailEt.text.toString()
            val data = ForgetPasswordBody(email)
            projectApi.forgetPassword(data) {
                if (it != null) {
                    when (it.message) {
                        "Otp sent, please check your mail" -> {

                            val sharedPreferences =
                                activity?.applicationContext?.getSharedPreferences(
                                    "sharedPreference",
                                    Context.MODE_PRIVATE
                                )
                            val editor = sharedPreferences?.edit()
                            editor?.putString("email", loginBinding.emailEt.text.toString())
                            editor?.apply()

                            Toast.makeText(
                                activity?.applicationContext,
                                it.message,
                                Toast.LENGTH_SHORT
                            ).show()
                            activity?.supportFragmentManager?.beginTransaction()
                                ?.replace(
                                    R.id.fragment_container,
                                    OtpFragment()
                                )
                                ?.addToBackStack(null)
                                ?.commit()
                        }
                        else -> {
                            Toast.makeText(
                                activity?.applicationContext,
                                "Invalid Email Id",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }
                } else {
                    Toast.makeText(
                        activity?.applicationContext,
                        "Enter Email ID",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }

        loginBinding.loginBtn.setOnClickListener {
            val email = loginBinding.emailEt.text.toString()
            val password = loginBinding.passwordEt.text.toString()
            val data = SignInBody(email, password)
            projectApi.signIn(data) {
                if (it != null) {
                    when (it.message) {
                        "Login successful" -> {
                            Log.d("sign in response", it.toString())
                            Log.d("accessToken", it.access_token)
                            val sharedPreferences =
                                activity?.applicationContext?.getSharedPreferences(
                                    "sharedPreference",
                                    Context.MODE_PRIVATE
                                )
                            val editor = sharedPreferences?.edit()
                            editor?.putString("accessToken", it.access_token)
                            editor?.putString("Login","Login")
                            editor?.apply()
                            loginBinding.emailEt.text.clear()
                            loginBinding.passwordEt.text.clear()
                            activity?.startActivity(
                                Intent(
                                    activity,
                                    HomeActivity::class.java
                                )
                            )
                        }
                        "No user found" -> {
                            Toast.makeText(
                                activity?.applicationContext,
                                "Invalid Email Id",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                        else -> {
                            Toast.makeText(
                                activity?.applicationContext,
                                "Invalid Password",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
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
        !loginBinding.loginBtn.isEnabled
        return loginBinding.root
    }
}
