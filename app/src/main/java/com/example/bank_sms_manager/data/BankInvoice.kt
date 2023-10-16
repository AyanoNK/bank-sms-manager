package com.example.bank_sms_manager.data

import androidx.room.Entity
import androidx.room.PrimaryKey


/**
 * Entity data class represents a single row in the database.
 */
@Entity(tableName="bank_invoices")
data class BankInvoice(
    @PrimaryKey(autoGenerate=true)
    val id: Int,
    val bankName: String,
    val purchaseValue: Int,
    val purchaseReference: String,
    val date: String,
    val hour: String
)

data class DataBankInvoice(
    val id: Int? = 0,
    val bankName: String = "",
    val purchaseValue: Int = 0,
    val purchaseReference: String = "",
    val date: String = "",
    val hour: String = ""
)

fun DataBankInvoice.toBankInvoice(): BankInvoice {
    return BankInvoice(
        id = id?: 0,
        bankName = bankName,
        purchaseValue = purchaseValue,
        purchaseReference = purchaseReference,
        date = date,
        hour = hour
    )
}