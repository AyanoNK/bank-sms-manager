package com.ayano.bank_sms_manager

import java.time.format.DateTimeFormatter


class BankSMSMessageParser {
    companion object {
        fun extractBancolombiaData(text: String): Map<String, String?> {
            val purchaseValuePattern = "COP(\\d{1,3}(\\.\\d{3}))".toRegex()
            val purchaseReferencePattern = "en\\s([A-Z]+(?:\\s[A-Z]+)*)".toRegex()
            val datePattern = "\\d{2}/\\d{2}/\\d{4}".toRegex()
            val hourPattern = "\\d{2}:\\d{2}".toRegex()

            val bankName = "Bancolombia"
            val purchaseReference = purchaseReferencePattern.find(text)?.value.let {
                it?.replace("en ", "")
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
            val purchaseValue = purchaseValuePattern.find(text)?.value.let {
                it?.replace(".", "")?.replace("COP", "")
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