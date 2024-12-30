package com.ayano.bank_sms_manager.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [BankInvoice::class], version = 1, exportSchema = false)
abstract class BankInvoiceDatabase : RoomDatabase() {
    abstract fun bankInvoiceDao(): BankInvoiceDao

    companion object {
        private var Instance: BankInvoiceDatabase? = null

        fun getDatabase(context: Context): BankInvoiceDatabase {
            // if the Instance is not null, return it, otherwise create a new database instance.
            return Instance ?: synchronized(this) {
                Room.databaseBuilder(context, BankInvoiceDatabase::class.java, "item_database")
                    .build()
                    .also { Instance = it }
            }
        }
    }
}