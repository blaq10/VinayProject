package com.hylton.vinayproject

import androidx.room.ColumnInfo
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "user_table")
data class User (
    @PrimaryKey
    @ColumnInfo(name = "first_name")
    val firstName: String,

    @ColumnInfo(name = "last_name")
    val lastName: String?,

    @ColumnInfo(name = "email")
    val email: String?,

    @Embedded(prefix = "add_")
    val address: Address
) {
//    @PrimaryKey(autoGenerate = true)
//    val id: Int = 0
}

data class Address(
    @ColumnInfo(name = "street")
    val street: String?,

    @ColumnInfo(name = "apartment_number")
    val apartmentNumber: String?,

    @ColumnInfo(name = "state")
    val state: String?,

    @ColumnInfo(name = "zipcode")
    val zipCode: Int
)