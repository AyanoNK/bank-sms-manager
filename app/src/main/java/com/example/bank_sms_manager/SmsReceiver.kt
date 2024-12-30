package com.example.bank_sms_manager

import android.content.Context
import android.content.pm.PackageManager
import android.database.Cursor
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
                arrayOf(Telephony.Sms._ID, Telephony.Sms.ADDRESS, Telephony.Sms.BODY, Telephony.Sms.DATE),
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

        fun getLastMessageTransaction(context: Context): Map<String, String?> {
            println("Entering getLastMessageTransaction")

            val permission = android.Manifest.permission.READ_SMS
            val grant = context.checkSelfPermission(permission)
            if (grant != PackageManager.PERMISSION_GRANTED) {
                // log error
                // TODO: create logs file to send to server.
                return mapOf()
            }
            val cursor = context.contentResolver.query(
                Telephony.Sms.CONTENT_URI,
                arrayOf(Telephony.Sms._ID, Telephony.Sms.ADDRESS, Telephony.Sms.BODY, Telephony.Sms.DATE),
                null,
                null,
                null
            )


            if (cursor != null) {
                cursor?.moveToFirst()
                val messageText = cursor.getString(2)

                // TODO: this is not scalable. Check how to map a bank to a parser.
                if (!messageText.contains("Bancolombia")) {
                    cursor.close()
                    return mapOf()
                }
                val messageResult = BankSMSMessageParser.extractBancolombiaData(messageText)
                cursor.close()
                return messageResult
            }

            return mapOf()
        }
    }
}