package com.example.vendorapp.singletonObjects

import com.google.firebase.firestore.FirebaseFirestore

class FirestoreInstance {

    companion object{

        var firestoreInstance : FirebaseFirestore? = null

        @Synchronized fun getInstance() : FirebaseFirestore{

            if (firestoreInstance == null){
                firestoreInstance = FirebaseFirestore.getInstance()
            }

            return firestoreInstance!!
        }

    }

}