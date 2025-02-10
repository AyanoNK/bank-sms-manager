package com.ayano.bank_sms_manager.widgets


import android.content.Context
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.sp
import androidx.glance.GlanceId
import androidx.glance.GlanceModifier
import androidx.glance.appwidget.GlanceAppWidget
import androidx.glance.appwidget.provideContent
import androidx.glance.background
import androidx.glance.layout.Alignment
import androidx.glance.layout.Column
import androidx.glance.layout.fillMaxSize
import androidx.glance.text.FontFamily
import androidx.glance.text.Text
import androidx.glance.text.TextAlign
import androidx.glance.text.TextStyle
import com.ayano.bank_sms_manager.CurrencyFormatter
import com.ayano.bank_sms_manager.data.BankInvoiceDatabase
import com.ayano.bank_sms_manager.data.OfflineBankInvoiceRepository

class BankMetricsWidget : GlanceAppWidget() {
    override suspend fun provideGlance(context: Context, id: GlanceId) {
        provideContent {
            MainContent(context)
        }
    }

    @Composable
    private fun MainContent(context: Context) {
        val offlineBankInvoiceRepository = remember {
            OfflineBankInvoiceRepository(
                BankInvoiceDatabase.getDatabase(context).bankInvoiceDao()
            )
        }
        val currentMonthtotal =
            offlineBankInvoiceRepository.getBankInvoicesTotal().collectAsState(initial = 0)
        val lastMonthTotal =
            offlineBankInvoiceRepository.getLastMonthBankInvoicesTotal().collectAsState(initial = 0)
        val lastInvoice =
            offlineBankInvoiceRepository.getLatestBankInvoice().collectAsState(initial = null)


        // TODO: add dynamic gradient based on the amount spent
        val green = Color(0xFFDEFDE0)
        val yellow = Color(0xFFFCF7DE)
        val red = Color(0xFFFDDFDF)

        // TODO: put breakpoints somewhere else. Hopefully changeable in the app

        val minToYellow = 4500000
        val minToRed = 5000000

        var backgroundColor = green
        if (currentMonthtotal.value >= minToYellow) {
            backgroundColor = yellow
        }
        if (currentMonthtotal.value >= minToRed) {
            backgroundColor = red
        }


        return Column(
            modifier = GlanceModifier.fillMaxSize().background(color = backgroundColor),
            verticalAlignment = Alignment.CenterVertically,
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            // TODO: custom font with canvas
            // https://proandroiddev.com/jetpack-glance-no-way-to-custom-fonts-e761b789567d
            Text(
                "You have spent", style = TextStyle(
                    fontFamily = FontFamily("sans-serif"), fontSize = 12.sp,
                )
            )
            Text(
                CurrencyFormatter.formatCurrency(currentMonthtotal.value), style = TextStyle(
                    fontFamily = FontFamily("sans-serif"),
                    fontSize = 18.sp,
                )
            )
            if (lastInvoice.value != null) {
                Text(
                    "Last purchase at ${lastInvoice.value!!.purchaseReference}", style = TextStyle(
                        fontFamily = FontFamily("sans-serif"),
                        fontSize = 12.sp,
                        textAlign = TextAlign.Center
                    )
                )
            } else {
                Text(
                    "No purchases yet!", style = TextStyle(
                        fontFamily = FontFamily("sans-serif"),
                        fontSize = 12.sp,
                    )
                )
            }
//            Text(text = "Where to?", modifier = GlanceModifier.padding(12.dp))
            Text(
                "Spent ${CurrencyFormatter.formatCurrency(lastMonthTotal.value)} last month",
                style = TextStyle(
                    fontFamily = FontFamily("sans-serif"),
                    fontSize = 12.sp,
                )
            )

        }
    }
}