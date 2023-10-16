package com.example.bank_sms_manager

import com.example.bank_sms_manager.data.DataBankInvoice
import java.time.format.DateTimeFormatter


class BankSMSMessageParser {
    companion object {
        fun extractBancolombiaData(text: String): Map<String, String?> {
            val purchaseValuePattern = "\\$\\d+\\.\\d+".toRegex()
            val purchaseReferencePattern = "(?<=en\\s)([^.]+)".toRegex()
            val datePattern = "\\d{2}/\\d{2}/\\d{4}".toRegex()
            val hourPattern = "\\d{2}:\\d{2}".toRegex()

            val bankName = "Bancolombia"
            val purchaseReference = purchaseReferencePattern.find(text)?.value.let {
                it?.replace("\\.", "")?.replace("$", "")
                it?.substring(0, it.length - 6)
            }


            val inputFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy")
            val outputFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")

            var date = datePattern.find(text)?.value

            if (date != null) {
                val parsedDate = inputFormatter.parse(date)
                date = outputFormatter.format(parsedDate)
            }

            val hour = hourPattern.find(text)?.value


            // transform purchaseValue to number
            val purchaseValueMatch = purchaseValuePattern.find(text)?.value
            val purchaseValue = purchaseValueMatch?.let {
                it?.replace("\\.", "")?.replace("$", "")?.replace(".", "")
            }

            return mapOf(
                "bankName" to bankName,
                "purchaseValue" to purchaseValue,
                "purchaseReference" to purchaseReference,
                "date" to date,
                "hour" to hour
            )
        }
    }
}