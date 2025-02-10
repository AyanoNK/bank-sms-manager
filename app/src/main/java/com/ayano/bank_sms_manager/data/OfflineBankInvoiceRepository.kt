package com.ayano.bank_sms_manager.data

import kotlinx.coroutines.flow.Flow

class OfflineBankInvoiceRepository(private val bankInvoiceDao: BankInvoiceDao) :
    BankInvoiceRepository {
    override fun getAllBankInvoicesStream(): Flow<List<BankInvoice>> =
        bankInvoiceDao.getAllBankInvoices()

    override fun getBankInvoicesTotal(): Flow<Int> = bankInvoiceDao.getBankInvoicesTotal()

    override fun getLastMonthBankInvoicesTotal(): Flow<Int> = bankInvoiceDao.getLastMonthBankInvoicesTotal()

    override fun getLatestBankInvoice(): Flow<BankInvoice?> = bankInvoiceDao.getLatestBankInvoice()

    override fun getBankInvoice(id: Int): Flow<BankInvoice?> = bankInvoiceDao.getBankInvoice(id)

    override suspend fun insertBankInvoice(bankInvoice: BankInvoice) =
        bankInvoiceDao.insert(bankInvoice)

    override suspend fun deleteBankInvoice(bankInvoice: BankInvoice) =
        bankInvoiceDao.delete(bankInvoice)

    override suspend fun updateBankInvoice(bankInvoice: BankInvoice) =
        bankInvoiceDao.update(bankInvoice)
}