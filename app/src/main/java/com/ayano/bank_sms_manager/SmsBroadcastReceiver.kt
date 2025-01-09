package com.ayano.bank_sms_manager

import android.appwidget.AppWidgetManager
import android.content.BroadcastReceiver
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.provider.Telephony
import android.widget.RemoteViews
import com.ayano.bank_sms_manager.data.BankInvoice
import com.ayano.bank_sms_manager.data.BankInvoiceDatabase
import com.ayano.bank_sms_manager.data.BankInvoiceRepository
import com.ayano.bank_sms_manager.data.OfflineBankInvoiceRepository
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
            updateWidgetCounter(context)
        }
    }

    private fun updateWidgetCounter(context: Context) {
        val appWidgetManager = AppWidgetManager.getInstance(context)
        val appWidgetIds =
            appWidgetManager.getAppWidgetIds(ComponentName(context, BankMetricsWidget::class.java))

        // update BankMetricsWidget
        for (appWidgetId in appWidgetIds) {
            val views = RemoteViews(context.packageName, R.layout.bank_metrics_widget)
            val messageCount = SmsReceiver.getMessagesCount(context)
            println("MESSAGE NO. $messageCount")
            val lastMessageParse = SmsReceiver.getLastMessageTransaction(context)
            // transform lastMessageParse to BankInvoice
            if (!parseIsComplete(lastMessageParse)) {
                return
            }
            val bankInvoice = parseToDataBankInvoice(lastMessageParse)

            CoroutineScope(Dispatchers.IO).launch {
                val bankInvoiceRepository: BankInvoiceRepository by lazy {
                    OfflineBankInvoiceRepository(
                        BankInvoiceDatabase.getDatabase(context).bankInvoiceDao()
                    )
                }
                bankInvoiceRepository.insertBankInvoice(bankInvoice)

                val thisMonthTotal = bankInvoiceRepository.getBankInvoicesTotal()
                val latestInvoice = bankInvoiceRepository.getLatestBankInvoice()

                launch {
                    latestInvoice.collect {
                        val latestInvoiceReference = it?.purchaseReference
                        views.setTextViewText(R.id.appwidget_last_purchase, latestInvoiceReference)
                        appWidgetManager.updateAppWidget(appWidgetId, views)
                    }
                }
                launch {
                    thisMonthTotal.collect {
                        val moneyValue = CurrencyFormatter.formatCurrency(it)
                        views.setTextViewText(R.id.appwidget_value_total, moneyValue)
                        appWidgetManager.updateAppWidget(appWidgetId, views)
                    }
                }
            }

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