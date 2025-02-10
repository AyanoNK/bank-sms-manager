package com.ayano.bank_sms_manager.receivers

import android.appwidget.AppWidgetManager
import android.content.Context
import android.widget.RemoteViews
import androidx.glance.appwidget.GlanceAppWidget
import androidx.glance.appwidget.GlanceAppWidgetReceiver
import com.ayano.bank_sms_manager.CurrencyFormatter
import com.ayano.bank_sms_manager.R
import com.ayano.bank_sms_manager.data.BankInvoiceDatabase
import com.ayano.bank_sms_manager.data.BankInvoiceRepository
import com.ayano.bank_sms_manager.data.OfflineBankInvoiceRepository
import com.ayano.bank_sms_manager.widgets.BankMetricsWidget
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

/**
 * Implementation of App Widget functionality.
 */
class BankMetricsWidgetReceiver : GlanceAppWidgetReceiver() {
    override val glanceAppWidget: GlanceAppWidget = BankMetricsWidget()
//    override fun onUpdate(
//        context: Context,
//        appWidgetManager: AppWidgetManager,
//        appWidgetIds: IntArray
//    ) {
//        // There may be multiple widgets active, so update all of them
//        for (appWidgetId in appWidgetIds) {
//            updateAppWidget(context, appWidgetManager, appWidgetId)
//        }
//    }

//    override fun onEnabled(context: Context) {
//        // Enter relevant functionality for when the first widget is created
//    }
//
//    override fun onDisabled(context: Context) {
//        // Enter relevant functionality for when the last widget is disabled
//    }
}

private fun updateAppWidget(
    context: Context,
    appWidgetManager: AppWidgetManager,
    appWidgetId: Int
) {
    // Construct the RemoteViews object
    val views = RemoteViews(context.packageName, R.layout.bank_metrics_widget)
    // Instruct the widget manager to update the widget
    appWidgetManager.updateAppWidget(appWidgetId, views)

    CoroutineScope(Dispatchers.IO).launch {
        val bankInvoiceRepository: BankInvoiceRepository by lazy {
            OfflineBankInvoiceRepository(
                BankInvoiceDatabase.getDatabase(context).bankInvoiceDao()
            )
        }

        val latestInvoice = bankInvoiceRepository.getLatestBankInvoice()
        val total = bankInvoiceRepository.getBankInvoicesTotal()

        launch {
            latestInvoice.collect {
                val latestInvoiceReference = it?.purchaseReference
//                views.setTextViewText(R.id.appwidget_last_purchase, latestInvoiceReference)
                appWidgetManager.updateAppWidget(appWidgetId, views)
            }
        }

        launch {
            total.collect {
                val moneyValue = CurrencyFormatter.formatCurrency(it)
//                views.setTextViewText(R.id.appwidget_value_total, moneyValue)
                appWidgetManager.updateAppWidget(appWidgetId, views)
            }
        }
    }
}

