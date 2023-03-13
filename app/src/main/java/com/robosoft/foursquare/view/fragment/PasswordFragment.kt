package com.robosoft.foursquare.view.fragment

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.robosoft.foursquare.R
import com.robosoft.foursquare.databinding.FragmentPasswordBinding
import com.robosoft.foursquare.model.dataclass.ChangePasswordBody
import com.robosoft.foursquare.model.network.ProjectService
import java.util.regex.Pattern

class PasswordFragment : Fragment() {

    private lateinit var passwordBinding: FragmentPasswordBinding
    private val projectApi = ProjectService()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        passwordBinding = FragmentPasswordBinding.inflate(inflater,container,false)

        val sharedPreferences =
            activity?.applicationContext?.getSharedPreferences(
                "sharedPreference",
                Context.MODE_PRIVATE
            )
        val email = sharedPreferences?.getString("email", "")!!

        passwordBinding.submitBtn.setOnClickListener {
            val password = passwordBinding.enterPasswordEt.text.toString()
            val confirmPassword = passwordBinding.confirmPasswordEt.text.toString()

            if (password != confirmPassword){
                Toast.makeText(activity?.applicationContext,"Passwords doesn't match",Toast.LENGTH_SHORT).show()
            }else {
                if (validatePassword(confirmPassword)) {
                    val data = ChangePasswordBody(email, confirmPassword)
                    projectApi.changePassword(data) {
                        if (it != null) {
                            when (it.message) {
                                "Your new password is updated. Please log in..." -> {
                                    Toast.makeText(
                                        activity?.applicationContext,
                                        it.message,
                                        Toast.LENGTH_SHORT
                                    ).show()
                                    activity?.supportFragmentManager?.beginTransaction()
                                        ?.replace(
                                            R.id.fragment_container,
                                            LoginFragment()
                                        )
                                        ?.addToBackStack(null)
                                        ?.commit()
                                }
                                else -> {
                                    Toast.makeText(
                                        activity?.applicationContext,
                                        it.message,
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
                } else {
                    Toast.makeText(
                        activity?.applicationContext,
                        "Password must contains atleast one capital letter,small letter,special character,number",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }

        return passwordBinding.root
    }

    private fun validatePassword(confirmPassword: String): Boolean {
        val pattern = Pattern.compile("^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=\\S+$).{6,}$")
        val matcher = pattern.matcher(confirmPassword)
        return matcher.matches()
    }
}