package com.confidential.ui.home

import android.Manifest
import android.os.Build
import android.os.Bundle
import android.telephony.SmsManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import com.confidential.databinding.FragmentHomeBinding
import com.confidential.ui.base.BaseFragment
import com.confidential.utils.checkPermission
import com.google.android.material.textview.MaterialTextView
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomeFragment : BaseFragment<HomeViewModel>() {

    override val viewModel: HomeViewModel by lazy {
        ViewModelProvider(this)[HomeViewModel::class.java]
    }
    override val screeName: String = "HomeFragment"

    private var _binding: FragmentHomeBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val textView: MaterialTextView = binding.homeTitle
        viewModel.text.observe(viewLifecycleOwner) {
            textView.text = it
        }
        binding.sendMessageBtn.setOnClickListener {
            if (requireContext().checkPermission(Manifest.permission.SEND_SMS)) {
                val smsManager = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    requireContext().getSystemService(SmsManager::class.java)
                } else {
                    SmsManager.getDefault()
                }
                val plainText = binding.message.text.toString()
                val cipherText = viewModel.encrypt(plainText)
                viewModel.decrypt(cipherText)
                smsManager.sendTextMessage(
                    binding.cellphone.text.toString(),
                    null,
                    binding.message.text.toString(),
                    null,
                    null
                )
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}