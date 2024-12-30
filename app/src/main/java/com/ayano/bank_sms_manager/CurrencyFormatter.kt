package com.ayano.bank_sms_manager

import android.icu.text.NumberFormat
import android.icu.util.Currency

class CurrencyFormatter {
    companion object {
        fun formatCurrency(value: Int): String {
            val format = NumberFormat.getCurrencyInstance()
            format.maximumFractionDigits = 0
            format.currency = Currency.getInstance("COP")
            return format.format(value)
        }

    }
}