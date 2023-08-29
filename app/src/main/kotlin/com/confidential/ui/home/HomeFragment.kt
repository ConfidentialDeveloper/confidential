package com.confidential.ui.home

import android.Manifest
import android.content.*
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.telephony.SmsManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.confidential.R
import com.confidential.data.receiver.SmsBroadcastReceiver.Companion.INTENT_ACTION_TAG
import com.confidential.data.receiver.SmsBroadcastReceiver.Companion.MESSAGE_UI_UPDATE_INTENT_TAG
import com.confidential.data.receiver.SmsBroadcastReceiver.Companion.PUBLIC_KEY_UI_UPDATE_INTENT_TAG
import com.confidential.databinding.FragmentHomeBinding
import com.confidential.ui.base.BaseFragment
import com.google.android.material.snackbar.Snackbar
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
        _binding?.lifecycleOwner = viewLifecycleOwner
        _binding?.viewModel = viewModel

        requestPermission()

        viewModel.sendPublicKey.observe(viewLifecycleOwner) {
            sendSMS(viewModel.publicKey, isPublicKey = true)
        }

        viewModel.sendMessage.observe(viewLifecycleOwner) {
            sendSMS(binding.message.text.toString())
        }

        viewModel.verifyConnection.observe(viewLifecycleOwner) {
            Snackbar.make(
                requireView(),
                "This feature hasn't been released yet.",
                Snackbar.LENGTH_LONG
            ).show()
        }

        viewModel.decryptionUnsuccessful.observe(viewLifecycleOwner) {
            showDialog(
                title = "Something went wrong!",
                message = "There is something wrong with your cipherText decryption. There are multiple ways to identify and resolve that.\nFirst, please ensure you and your other chat party have exchanges your public keys.\n" +
                        "If you have already done that, please press the CLEAR PUBLIC KEY button and notify your other chat party too so they do the same. Once that's done, please ensure to exchange the public keys again.",
                positiveButtonLabel = "OK",
            ) { dialog, which ->
                when (which) {
                    DialogInterface.BUTTON_POSITIVE -> dialog.dismiss()
                }
            }
        }

        LocalBroadcastManager.getInstance(requireContext())
            .registerReceiver(object : BroadcastReceiver() {
                override fun onReceive(context: Context?, intent: Intent?) {
                    val publicKey: String? =
                        intent?.getStringExtra(PUBLIC_KEY_UI_UPDATE_INTENT_TAG)
                    val message =
                        intent?.getStringExtra(MESSAGE_UI_UPDATE_INTENT_TAG)
                    message?.let {
                        binding.plainText.text =
                            viewModel.decrypt(it) ?: return viewModel.decryptionIsUnsuccessful()
                    }
                    publicKey?.let {
                        binding.publicKey.text = viewModel.refinePublicKeyValue(it)
                        viewModel.storeOtherPartyPublicKeyString(
                            publicKey = viewModel.refinePublicKeyValue(it)
                        )
                    }
                }
            }, IntentFilter(INTENT_ACTION_TAG))
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun sendSMS(message: String, isPublicKey: Boolean = false) {
        val smsManager = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            requireContext().getSystemService(SmsManager::class.java)
        } else {
            ContextCompat.getSystemService(requireContext(), SmsManager::class.java)
        }
        val cipherText = if (isPublicKey) {
            "$PUBLIC_KEY_SMS_TAG$message"
        } else {
            viewModel.encrypt(message)
        }
        val dividedMessages = smsManager?.divideMessage(cipherText)
        smsManager?.sendMultipartTextMessage(
            binding.cellphone.text.toString(),
            null,
            dividedMessages,
            null,
            null
        )
    }

    private fun showDialog(
        title: String,
        message: String,
        positiveButtonLabel: String,
        negativeButtonLabel: String? = null,
        onClickListener: DialogInterface.OnClickListener
    ) {
        val builder = AlertDialog.Builder(requireContext())
        builder.setTitle(title)
        builder.setMessage(message)
        builder.setPositiveButton(positiveButtonLabel, onClickListener)
        negativeButtonLabel?.let {
            builder.setNegativeButton(negativeButtonLabel, onClickListener)
        }
        val dialog: AlertDialog = builder.create()
        dialog.show()
    }


    private fun requestPermission() {
        when {
            ContextCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.SEND_SMS
            ) == PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.READ_SMS
            ) == PackageManager.PERMISSION_GRANTED &&
                    ContextCompat.checkSelfPermission(
                        requireContext(),
                        Manifest.permission.RECEIVE_SMS
                    ) == PackageManager.PERMISSION_GRANTED &&
                    ContextCompat.checkSelfPermission(
                        requireContext(),
                        Manifest.permission.BROADCAST_SMS
                    ) == PackageManager.PERMISSION_GRANTED
            -> {
                // You can use the API that requires the permission.
            }
            shouldShowRequestPermissionRationale(Manifest.permission.SEND_SMS) -> {
                // In an educational UI, explain to the user why your app requires this
                // permission for a specific feature to behave as expected, and what
                // features are disabled if it's declined. In this UI, include a
                // "cancel" or "no thanks" button that lets the user continue
                // using your app without granting the permission.
                showDialog(
                    title = getString(R.string.home_screen_permission_dialog_title),
                    message = getString(R.string.home_screen_permission_dialog_message),
                    positiveButtonLabel = getString(R.string.permission_dialog_ok_button_label),
                    negativeButtonLabel = getString(R.string.permission_dialog_exit_the_application_button_label)
                ) { _, which ->
                    when (which) {
                        DialogInterface.BUTTON_POSITIVE -> requestPermission()
                        DialogInterface.BUTTON_NEGATIVE -> activity?.finish()
                    }
                }
            }
            else -> {
                // You can directly ask for the permission.
                // The registered ActivityResultCallback gets the result of this request.
                requestPermissionLauncher.launch(Manifest.permission.SEND_SMS)
                requestPermissionLauncher.launch(Manifest.permission.READ_SMS)
                requestPermissionLauncher.launch(Manifest.permission.RECEIVE_SMS)
                requestPermissionLauncher.launch(Manifest.permission.BROADCAST_SMS)

            }
        }
    }

    // Register the permissions callback, which handles the user's response to the
// system permissions dialog. Save the return value, an instance of
// ActivityResultLauncher. You can use either a val, as shown in this snippet,
// or a lateinit var in your onAttach() or onCreate() method.
    private val requestPermissionLauncher =
        registerForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) { isGranted: Boolean ->
            if (isGranted) {
                // Permission is granted. Continue the action or workflow in your
                // app.
                println("Permission is granted")
            } else {
                // Explain to the user that the feature is unavailable because the
                // feature requires a permission that the user has denied. At the
                // same time, respect the user's decision. Don't link to system
                // settings in an effort to convince the user to change their
                // decision.
            }
        }

    companion object {
        const val PUBLIC_KEY_SMS_TAG = "confidential_public_key:"
    }
}