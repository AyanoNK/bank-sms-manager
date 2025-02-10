package com.ayano.bank_sms_manager.receivers

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.provider.Telephony
import androidx.glance.appwidget.updateAll
import com.ayano.bank_sms_manager.SmsReceiver
import com.ayano.bank_sms_manager.data.BankInvoice
import com.ayano.bank_sms_manager.data.BankInvoiceDatabase
import com.ayano.bank_sms_manager.data.BankInvoiceRepository
import com.ayano.bank_sms_manager.data.OfflineBankInvoiceRepository
import com.ayano.bank_sms_manager.widgets.BankMetricsWidget
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


class SmsBroadcastReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {

        // check if the broadcast is triggered
        println("RECEIVED ACTION")
        println(Telephony.Sms.Intents.SMS_RECEIVED_ACTION)
        // This method is called when the BroadcastReceiver is receiving an Intent broadcast.
        if (Telephony.Sms.Intents.SMS_RECEIVED_ACTION == intent.action) {
            consumeSMSMessage(context)
        }
    }

    private fun consumeSMSMessage(context: Context) {
        // get the message count
        val messageCount = SmsReceiver.getMessagesCount(context)
        println("MESSAGE NO. $messageCount")

        // get and parse the last message transaction
        val lastMessageParse = SmsReceiver.getLastMessageTransaction(context)
        if (!parseIsComplete(lastMessageParse)) {
            println("Incomplete message, parser probably failed")
            return
        }
        val bankInvoice = parseToDataBankInvoice(lastMessageParse)

        // save the bank invoice to the database
        CoroutineScope(Dispatchers.IO).launch {
            val bankInvoiceRepository: BankInvoiceRepository by lazy {
                OfflineBankInvoiceRepository(
                    BankInvoiceDatabase.getDatabase(context).bankInvoiceDao()
                )
            }
            bankInvoiceRepository.insertBankInvoice(bankInvoice)
            BankMetricsWidget().updateAll(context)
        }
    }

    private fun parseIsComplete(lastMessageParse: Map<String, String?>): Boolean =
        lastMessageParse.isNotEmpty() && lastMessageParse.size == 5 && lastMessageParse.all {
            it.value != null
        }

    private fun parseToDataBankInvoice(lastMessageParse: Map<String, String?>): BankInvoice {
        println(lastMessageParse)
        val bankName = lastMessageParse["bankName"] ?: ""
        val purchaseValue = lastMessageParse["purchaseValue"] ?: ""
        val purchaseReference = lastMessageParse["purchaseReference"] ?: ""
        val date = lastMessageParse["date"] ?: ""
        val hour = lastMessageParse["hour"] ?: ""
        return BankInvoice(
            0,
            bankName,
            purchaseValue.toInt(),
            purchaseReference,
            date,
            hour
        )
    }
}