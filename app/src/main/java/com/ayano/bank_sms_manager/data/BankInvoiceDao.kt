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
    @Query("WITH TODAY AS (\n" +
            "    SELECT\n" +
            "    CASE \n" +
            "        WHEN CAST(strftime('%d', 'now') AS INT) < 15 \n" +
            "        THEN date('now', 'start of month', '-1 month', '+14 days')\n" +
            "        ELSE date('now', 'start of month', '+14 days') \n" +
            "    END AS START_TIME,\n" +
            "    CASE \n" +
            "        WHEN CAST(strftime('%d', 'now') AS INT) < 15 \n" +
            "        THEN date('now', 'start of month', '+14 days')\n" +
            "        ELSE date('now', 'start of month', '+1 month', '+14 days') \n" +
            "    END AS END_TIME\n" +
            ")\n" +
            "SELECT DISTINCT SUM(purchaseValue)  from bank_invoices, TODAY\n" +
            "WHERE date >= TODAY.START_TIME AND date < TODAY.END_TIME")
    fun getBankInvoicesTotal(): Flow<Int>

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