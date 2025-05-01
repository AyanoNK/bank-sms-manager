package com.ayano.bank_sms_manager

import java.time.format.DateTimeFormatter


class BankSMSMessageParser {
    companion object {
        fun extractBancolombiaData(text: String): Map<String, String?> {
            val purchaseValuePattern = "COP\\d+(?:\\.\\d+)*".toRegex()
            // TODO: better pattern to get all names between En and Con
            val purchaseReferencePattern = "en\\s([A-Za-z0-9\\*]+(?: [A-Za-z0-9]+)*)\\scon".toRegex()
            val datePattern = "\\d{2}/\\d{2}/\\d{4}".toRegex()
            val hourPattern = "\\d{2}:\\d{2}".toRegex()

            val bankName = "Bancolombia"

            val matchResult = purchaseReferencePattern.find(text)
            val purchaseReference = matchResult?.groupValues?.get(1)

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