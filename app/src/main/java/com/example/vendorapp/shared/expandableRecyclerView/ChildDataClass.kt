package com.example.vendorapp.shared.expandableRecyclerView

import android.os.Parcel
import android.os.Parcelable

class ChildDataClass(var itemName : String? , var price : Int? , var quantity : Int? , var itemId : Int?) : Parcelable
{
    constructor(parcel: Parcel) : this(parcel.readString() , parcel.readInt() , parcel.readInt() , parcel.readInt()) {
        itemName = parcel.readString()
        price = parcel.readInt()
        quantity = parcel.readInt()
        itemId = parcel.readInt()
    }

    override fun writeToParcel(dest: Parcel?, flags: Int) {
        dest!!.writeString(itemName)
        dest!!.writeInt(price!!)
        dest!!.writeInt(quantity!!)
        dest!!.writeInt(itemId!!)
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