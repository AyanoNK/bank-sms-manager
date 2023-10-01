package com.example.bank_sms_manager


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
            val date = datePattern.find(text)?.value
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