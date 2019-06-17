package com.example.vendorapp.newOrderScreen.view.expandableRecyclerView

import android.os.Parcel
import android.os.Parcelable

class ChildDataClass(var itemName : String? , var price : String? , var quantity : String? , var itemId : String?) : Parcelable
{
    constructor(parcel: Parcel) : this(parcel.readString() , parcel.readString() , parcel.readString() , parcel.readString()) {
        itemName = parcel.readString()
        price = parcel.readString()
        quantity = parcel.readString()
        itemId = parcel.readString()
    }

    override fun writeToParcel(dest: Parcel?, flags: Int) {
        dest!!.writeString(itemName)
        dest!!.writeString(price)
        dest!!.writeString(quantity)
        dest!!.writeString(itemId)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<ChildDataClass> {
        override fun createFromParcel(parcel: Parcel): ChildDataClass {
            return ChildDataClass(parcel)
        }

        override fun newArray(size: Int): Array<ChildDataClass?> {
            return arrayOfNulls(size)
        }
    }


}