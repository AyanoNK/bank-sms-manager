package com.ayano.bank_sms_manager.widgets


import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.LinearGradient
import android.graphics.Paint
import android.graphics.Shader
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.unit.sp
import androidx.glance.GlanceId
import androidx.glance.GlanceModifier
import androidx.glance.appwidget.GlanceAppWidget
import androidx.glance.appwidget.provideContent
import androidx.glance.background
import androidx.glance.layout.Alignment
import androidx.glance.layout.Column
import androidx.glance.layout.fillMaxSize
import com.ayano.bank_sms_manager.ColorGradient
import com.ayano.bank_sms_manager.CurrencyFormatter
import com.ayano.bank_sms_manager.components.GlanceText
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
//        val yellow = Color(0xFFFCF7DE)
        val red = Color(0xFFFDDFDF)


        // TODO: put breakpoints somewhere else. Hopefully changeable in the app
        val budgetLimit = 6000000

        // max division at 1
        val percentage =
            if (currentMonthtotal.value > budgetLimit) 1f else currentMonthtotal.value.toFloat() / budgetLimit

        val backgroundColor: Color = ColorGradient.pointBetweenColors(
            from = green, to = red, percent = percentage
        )

        val gradientBitmap = createGradientBitmap(200, 300, green.toArgb(), red.toArgb())

        return Column(
//            modifier = GlanceModifier.fillMaxSize().background(ImageProvider(gradientBitmap)),
            modifier = GlanceModifier.fillMaxSize().background(backgroundColor),

            verticalAlignment = Alignment.CenterVertically,
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            // TODO: custom font with canvas
            // https://proandroiddev.com/jetpack-glance-no-way-to-custom-fonts-e761b789567d
            GlanceText(
                text = CurrencyFormatter.formatCurrency(currentMonthtotal.value),
                fontSize = 24.sp,
            )
            GlanceText(
                text = "out of ${CurrencyFormatter.formatCurrency(budgetLimit)} (${
                    (percentage).times(
                        100
                    ).toInt()
                }%)",
                fontSize = 12.sp,
            )
            GlanceText(
                text = "${CurrencyFormatter.formatCurrency(budgetLimit - currentMonthtotal.value)} left (${
                    (1 - percentage).times(
                        100
                    ).toInt()
                }%)",
                fontSize = 12.sp,
            )

            if (lastInvoice.value != null) {
                GlanceText(
                    text = "${lastInvoice.value!!.purchaseReference}: ${
                        CurrencyFormatter.formatCurrency(
                            lastInvoice.value!!.purchaseValue
                        )
                    }",
                    fontSize = 12.sp,
                )


            } else {
                GlanceText(
                    text = "No purchases yet!",
                    fontSize = 12.sp,
                )
            }
//            Text(text = "Where to?", modifier = GlanceModifier.padding(12.dp))
            GlanceText(
                text = "Spent ${CurrencyFormatter.formatCurrency(lastMonthTotal.value)} last month",
                fontSize = 12.sp,
            )
        }
    }

    // TODO: consider if gradient is better than static color
    private fun createGradientBitmap(
        width: Int,
        height: Int,
        startColor: Int,
        endColor: Int
    ): Bitmap {
        val bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bitmap)
        val paint = Paint()

        val gradient = LinearGradient(
            -200f, 0f, width.toFloat(), 0f,
            startColor, endColor,
            Shader.TileMode.CLAMP
        )

        paint.shader = gradient
        canvas.drawRect(0f, 0f, width.toFloat(), height.toFloat(), paint)
        return bitmap
    }
}