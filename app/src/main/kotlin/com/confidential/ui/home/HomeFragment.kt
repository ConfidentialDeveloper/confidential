package com.confidential.ui.home

import android.Manifest
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

    override val viewModel : HomeViewModel by lazy {
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
        val root: View = binding.root

        val textView: MaterialTextView = binding.homeTitle
        viewModel.text.observe(viewLifecycleOwner) {
            textView.text = it
        }
        binding.sendMessageBtn.setOnClickListener {
            if(requireContext().checkPermission(Manifest.permission.SEND_SMS)){
                val smsManager = SmsManager.getDefault()
                smsManager.sendTextMessage(binding.cellphone.text.toString(),null,binding.message.text.toString(),null,null)
            }
        }
        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}