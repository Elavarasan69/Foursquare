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
import com.robosoft.foursquare.databinding.FragmentRegistrationBinding
import com.robosoft.foursquare.model.dataclass.signup.SignUpBody
import com.robosoft.foursquare.model.network.ProjectService
import com.robosoft.foursquare.view.activity.HomeActivity
import java.util.regex.Pattern


class RegistrationFragment : Fragment() {

    private lateinit var registrationBinding: FragmentRegistrationBinding
    private val projectApi = ProjectService()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        registrationBinding = FragmentRegistrationBinding.inflate(inflater, container, false)

        registrationBinding.submitBtn.setOnClickListener {
            val email = registrationBinding.emailEt.text.toString()
            val name = registrationBinding.nameEt.text.toString()
            val phone = registrationBinding.mobileNumberEt.text.toString()
            val password = registrationBinding.enterPasswordEt.text.toString()
            val confirmPassword = registrationBinding.confirmPasswordEt.text.toString()

            if (name.isEmpty() || email.isEmpty() || phone.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
                Toast.makeText(
                    activity?.applicationContext,
                    "Enter all the data",
                    Toast.LENGTH_SHORT
                ).show()
            } else {
                if (validFullName(name)) {
                    Log.d("name",name)
                    if (validEmail(email)) {
                        if (validatePassword(password)) {
                            if (validatePhone(phone)) {
                                if (password == confirmPassword) {
                                    val data = SignUpBody(email, name, phone, confirmPassword)
                                    Log.d("user details", data.toString())
                                    projectApi.signUp(data) {
                                        if (it != null) {
                                            Log.d("response",it.toString())
                                            val sharedPreferences =
                                                activity?.applicationContext?.getSharedPreferences(
                                                    "sharedPreference",
                                                    Context.MODE_PRIVATE
                                                )
                                            val editor = sharedPreferences?.edit()
                                            editor?.putString("email", registrationBinding.emailEt.text.toString())
                                            editor?.apply()

                                            Toast.makeText(
                                                activity?.applicationContext,
                                                "User registered successfully",
                                                Toast.LENGTH_SHORT
                                            ).show()

                                            activity?.startActivity(Intent(activity,HomeActivity::class.java))

                                        } else {
                                            Log.d("error",it.toString())
                                            Toast.makeText(
                                                activity?.applicationContext,
                                                "Something went wrong",
                                                Toast.LENGTH_SHORT
                                            ).show()
                                        }
                                    }
                                } else {
                                    Toast.makeText(
                                        activity?.applicationContext,
                                        "Password and Confirm Password",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                }
                            } else {
                                Toast.makeText(
                                    activity?.applicationContext,
                                    "Invalid Mobile number format",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        } else {
                            Toast.makeText(
                                activity?.applicationContext,
                                "Invalid Password format",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    } else {
                        Toast.makeText(
                            activity?.applicationContext,
                            "Invalid email format",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                } else {
                    Toast.makeText(
                        activity?.applicationContext,
                        "Enter full name",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }

        return registrationBinding.root
    }

    private fun validFullName(name: String): Boolean {
        val pattern = Pattern.compile("\\b([A-ZÀ-ÿ][-,a-z. ']+[ ]*)+")
        val matcher = pattern.matcher(name)
        return matcher.matches()
    }

    private fun validEmail(email: String): Boolean {
        val pattern = Pattern.compile("^[\\w-.]+@([\\w-]+\\.)+[\\w-]{2,4}\$")
        val matcher = pattern.matcher(email)
        return matcher.matches()
    }

    private fun validatePassword(confirmPassword: String): Boolean {
        val pattern = Pattern.compile("^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=\\S+$).{6,}$")
        val matcher = pattern.matcher(confirmPassword)
        return matcher.matches()
    }

    private fun validatePhone(phoneNumber: String): Boolean {
        val pattern = Pattern.compile("[6-9]{1}[0-9]{9}")
        val matcher = pattern.matcher(phoneNumber)
        return matcher.matches()
    }
}