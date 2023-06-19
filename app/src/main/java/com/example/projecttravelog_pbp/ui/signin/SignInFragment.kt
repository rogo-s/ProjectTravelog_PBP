package com.example.projecttravelog_pbp.ui.signin

import android.content.Intent
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.provider.Settings
import android.util.Patterns
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.projecttravelog_pbp.R
import com.example.projecttravelog_pbp.databinding.FragmentSignInBinding
import com.example.projecttravelog_pbp.databinding.FragmentWelcomeBinding

class SignInFragment : Fragment() {

    lateinit var binding: FragmentSignInBinding
    private val viewModel: SignInViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSignInBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //viewModel session berfungsi untuk jika akun sudah login dia ke home, dan jika belum dia tetap ke login
        viewModel.session()
        //logika untuk menentukan akun sudah login atau belum, jika akun sudah login nav ke home, jika belum tetap di forom login
        viewModel.user.observe(viewLifecycleOwner) {
            if (it != null) {
                findNavController().navigate(R.id.action_signInFragment_to_signUpFragment)
            }
        }

        binding.btnLogin.setOnClickListener {
            findNavController().navigate(R.id.action_signInFragment_to_homeFragment)
        }
        binding.forgotPass.setOnClickListener {
            findNavController().navigate(R.id.action_signInFragment_to_forgotPasswordFragment)
        }
        binding.btnLogin.setOnClickListener {
            val email = binding.txtUsername.text.toString()
            val password = binding.txtPass.text.toString()
            if (email.isEmpty()) {
                binding.txtUsername.error = R.string.emailRequired.toString()
                binding.txtUsername.requestFocus()
            }
            if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                binding.txtPass.error = R.string.invalidEmail.toString()
                binding.txtPass.requestFocus()
            }
            if (password.isEmpty()) {
                binding.txtPass.error = R.string.passwordIsRequired.toString()
                binding.txtPass.requestFocus()
            }
            if (binding.txtUsername.error == null && binding.txtPass.error == null) {
                viewModel.loginFirebase(email, password)
            }
        }
        viewModel.login.observe(viewLifecycleOwner) {
            if (it.equals("Login Success!", true)) {
                Toast.makeText(requireContext(), it, Toast.LENGTH_SHORT).show()
                findNavController().navigate(R.id.action_signInFragment_to_homeFragment)
            } else {
                Toast.makeText(requireContext(), it, Toast.LENGTH_SHORT).show()
            }
        }
    }
}