package com.example.bank_sms_manager

import android.appwidget.AppWidgetManager
import android.content.BroadcastReceiver
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.provider.Telephony
import android.widget.RemoteViews

class SmsBroadcastReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        // check if the broadcast is triggered
        println("RECEIVED ACTION")
        println(intent.action)
        println("EXPECTED ACTION")
        println(Telephony.Sms.Intents.SMS_RECEIVED_ACTION)
        // This method is called when the BroadcastReceiver is receiving an Intent broadcast.
        if (Telephony.Sms.Intents.SMS_RECEIVED_ACTION == intent.action) {
            updateWidgetCounter(context)
        }
    }

    private fun updateWidgetCounter(context: Context) {
        println("UPDATING WIDGET COUNTER")
        val appWidgetManager = AppWidgetManager.getInstance(context)
        val appWidgetIds = appWidgetManager.getAppWidgetIds(ComponentName(context, BankMetricsWidget::class.java))

        // update BankMetricsWidget
        for (appWidgetId in appWidgetIds) {
            val views = RemoteViews(context.packageName, R.layout.bank_metrics_widget)
            val messageCount = SmsReceiver.getMessagesCount(context)
            println("MESSAGE COUNT")
            println(messageCount)
            views.setTextViewText(R.id.appwidget_text, messageCount.toString())
            appWidgetManager.updateAppWidget(appWidgetId, views)
        }
    }
}