package com.example.bank_sms_manager.data

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

    @Query("SELECT * FROM bank_invoices WHERE id = :id")
    fun getBankInvoice(id: Int): Flow<BankInvoice>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(bankInvoice: BankInvoice)

    @Update
    suspend fun update(bankInvoice: BankInvoice)

    @Delete
    suspend fun delete(bankInvoice: BankInvoice)
}