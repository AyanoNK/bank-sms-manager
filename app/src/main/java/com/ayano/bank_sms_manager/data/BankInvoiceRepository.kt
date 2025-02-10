package com.ayano.bank_sms_manager.data

import kotlinx.coroutines.flow.Flow

interface BankInvoiceRepository {
    fun getAllBankInvoicesStream(): Flow<List<BankInvoice>>
    fun getBankInvoicesTotal(): Flow<Int>
    fun getLastMonthBankInvoicesTotal(): Flow<Int>
    fun getLatestBankInvoice(): Flow<BankInvoice?>
    fun getBankInvoice(id: Int): Flow<BankInvoice?>
    suspend fun insertBankInvoice(bankInvoice: BankInvoice)
    suspend fun updateBankInvoice(bankInvoice: BankInvoice)
    suspend fun deleteBankInvoice(bankInvoice: BankInvoice)
}