package com.confidential.data.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.telephony.SmsMessage
import androidx.localbroadcastmanager.content.LocalBroadcastManager
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
                    val currentMessage = SmsMessage.createFromPdu(pdusObj[i] as ByteArray)
                    phoneNumber = currentMessage.displayOriginatingAddress
                    cipherText += currentMessage.displayMessageBody
                    // prevent any other broadcast receivers from receiving broadcast
                    // abortBroadcast();
                }
                context?.let {
                    val localIntent: Intent = Intent("update-ui").putExtra("sender", phoneNumber)
                        .putExtra("message", cipherText)
                    LocalBroadcastManager.getInstance(context).sendBroadcast(localIntent)
                }
            }
        }
    }

    companion object {
        private const val SMS_RECEIVED = "android.provider.Telephony.SMS_RECEIVED"
    }
}