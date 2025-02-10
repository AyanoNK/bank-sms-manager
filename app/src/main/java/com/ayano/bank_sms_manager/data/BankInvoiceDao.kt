package com.ayano.bank_sms_manager.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

/**
 * Database access object to access the Inventory database
 */

@Dao
interface BankInvoiceDao {
    @Query("SELECT * FROM bank_invoices")
    fun getAllBankInvoices(): Flow<List<BankInvoice>>

    // TODO: move this to user logic and not a burned SQL query
    // Pass in params like getBankInvoicesTotal(start_date: String, end_date: String)
    @Query("WITH DATE_RANGE AS (\n" +
            "    SELECT\n" +
            "date('now', 'start of month') AS START_TIME,\n" +
            "date('now', 'start of month', '+1 month', '-1 days') AS END_TIME\n" +
            ")\n" +
            "SELECT DISTINCT SUM(purchaseValue)  from bank_invoices, DATE_RANGE\n" +
            "WHERE date >= DATE_RANGE.START_TIME AND date < DATE_RANGE.END_TIME")
    fun getBankInvoicesTotal(): Flow<Int>

    @Query("WITH DATE_RANGE AS (\n" +
            "    SELECT\n" +
            "date('now', 'start of month', '-1 day', 'start of month') AS START_TIME,\n" +
            "date('now', 'start of month', '-1 day') AS END_TIME\n" +
            ")\n" +
            "SELECT DISTINCT SUM(purchaseValue)  from bank_invoices, DATE_RANGE\n" +
            "WHERE date >= DATE_RANGE.START_TIME AND date < DATE_RANGE.END_TIME")
    fun getLastMonthBankInvoicesTotal(): Flow<Int>

    @Query("SELECT * FROM bank_invoices WHERE id = :id")
    fun getBankInvoice(id: Int): Flow<BankInvoice>

    @Query("SELECT * FROM bank_invoices ORDER BY id DESC LIMIT 1")
    fun getLatestBankInvoice(): Flow<BankInvoice>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(bankInvoice: BankInvoice)

    @Update
    suspend fun update(bankInvoice: BankInvoice)

    @Delete
    suspend fun delete(bankInvoice: BankInvoice)
}