package com.example.bank_sms_manager

import android.content.Context
import android.content.pm.PackageManager
import android.provider.Telephony

class SmsReceiver {
    companion object {
        fun getMessagesCount(context: Context): Int {
            // get messages count from SMS database
            // check SMS permissions
            val permission = android.Manifest.permission.READ_SMS
            val grant = context.checkSelfPermission(permission)
            if (grant != PackageManager.PERMISSION_GRANTED) {
                return 2
            }
            val cursor = context.contentResolver.query(
                Telephony.Sms.CONTENT_URI,
                arrayOf(Telephony.Sms._ID),
                null,
                null,
                null
            )

            if (cursor != null) {
                val count = cursor.count
                cursor.close()
                return count
            }

            return 5
        }
    }
}