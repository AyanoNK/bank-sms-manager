package com.example.bank_sms_manager.data

import android.content.Context

interface AppContainer {
    val bankInvoiceRepository: BankInvoiceRepository
}

class AppDataContainer(context: Context) : AppContainer {
    override val bankInvoiceRepository: BankInvoiceRepository by lazy {
        OfflineBankInvoiceRepository(
            BankInvoiceDatabase.getDatabase(context).bankInvoiceDao()
        )
    }
}