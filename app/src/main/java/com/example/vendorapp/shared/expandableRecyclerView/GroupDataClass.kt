package com.example.vendorapp.shared.expandableRecyclerView

import android.annotation.SuppressLint
import com.thoughtbot.expandablerecyclerview.models.ExpandableGroup

@SuppressLint("ParcelCreator")
class GroupDataClass (heading : String
                      , order : List<ChildDataClass>)
    :ExpandableGroup<ChildDataClass>(heading , order)