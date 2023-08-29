package com.confidential.data.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import android.telephony.SmsMessage
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.confidential.ui.home.HomeFragment
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class SmsBroadcastReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context?, intent: Intent) {
        if (intent.action == SMS_RECEIVED) {
            val bundle = intent.extras
            if (bundle != null) {
                val pdusObj = bundle["pdus"] as Array<*>
                var cipherText = ""
                var phoneNumber = ""
                for (i in pdusObj.indices) {
                    val currentMessage = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        val format: String? = bundle.getString("format")
                        SmsMessage.createFromPdu(pdusObj[i] as ByteArray, format)
                    } else {
                        SmsMessage.createFromPdu(pdusObj[i] as ByteArray)
                    }
                    phoneNumber = currentMessage.displayOriginatingAddress
                    cipherText += currentMessage.displayMessageBody
                    // prevent any other broadcast receivers from receiving broadcast
                    abortBroadcast()
                }
                val isPublicKey = cipherText.contains(HomeFragment.PUBLIC_KEY_SMS_TAG)
                context?.let {
                    val localIntent = Intent(INTENT_ACTION_TAG)
                    localIntent.putExtra(SENDER_UI_UPDATE_INTENT_TAG, phoneNumber)
                    if (isPublicKey) {
                        localIntent.putExtra(PUBLIC_KEY_UI_UPDATE_INTENT_TAG, cipherText)
                    } else {
                        localIntent.putExtra(MESSAGE_UI_UPDATE_INTENT_TAG, cipherText)
                    }

                    LocalBroadcastManager.getInstance(context).sendBroadcast(localIntent)
                }
            }
        }
    }

    companion object {
        private const val SMS_RECEIVED = "android.provider.Telephony.SMS_RECEIVED"
        const val PUBLIC_KEY_UI_UPDATE_INTENT_TAG = "confidential_publicKey"
        const val MESSAGE_UI_UPDATE_INTENT_TAG = "confidential_message"
        const val SENDER_UI_UPDATE_INTENT_TAG = "confidential_sender"
        const val INTENT_ACTION_TAG = "update-ui"
    }
}