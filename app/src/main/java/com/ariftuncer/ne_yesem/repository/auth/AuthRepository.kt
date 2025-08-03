package com.ariftuncer.ne_yesem.repository

import android.util.Log
import com.ariftuncer.ne_yesem.data.model.User
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.firestore.firestore
import kotlinx.coroutines.tasks.await

class AuthRepository {

    private val auth: FirebaseAuth = FirebaseAuth.getInstance()
    private val firestore = Firebase.firestore

    //email ve password ile login
     fun login(email : String, password: String, onResult: (Boolean, String?) -> Unit){
        auth.signInWithEmailAndPassword(email,password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful){
                    onResult(true,null)
                }
                else{
                    onResult(false,task.exception?.message)
                }
            }
    }

     fun register(email : String, password: String, onResult: (Boolean, String?) -> Unit){
        auth.createUserWithEmailAndPassword(email,password)
            .addOnCompleteListener { task ->
                if(task.isSuccessful){
                    val uid = auth.currentUser!!.uid
                    val user = User(uid,email)
                    firestore.collection("users").document(uid)
                        .set(user)
                        .addOnSuccessListener { onResult(true,null) }
                        .addOnFailureListener { onResult(false,"firestore hatası") }

                }
                else{
                    onResult(false,task.exception?.message)
                }
            }
    }

    fun registerWithGoogle(idToken: String, onResult: (Boolean, String?) -> Unit) {
        val credential = GoogleAuthProvider.getCredential(idToken, null)
        auth.signInWithCredential(credential)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val isNewUser = task.result.additionalUserInfo?.isNewUser == true
                    val firebaseUser = auth.currentUser

                    if (firebaseUser != null && isNewUser) {
                        val uid = firebaseUser.uid
                        val email = firebaseUser.email ?: ""
                        val user = User(uid, email)

                        firestore.collection("users").document(uid)
                            .set(user)
                            .addOnSuccessListener { onResult(true, null) }
                            .addOnFailureListener { onResult(false, "Firestore kullanıcı kaydı hatası") }
                    } else {
                        // Zaten kayıtlıysa sadece giriş yapılmış olur
                        onResult(true, null)
                    }
                } else {
                    onResult(false, task.exception?.message)
                }
            }
    }

    fun loginWithGoogle(idToken: String, onResult: (Boolean, String?) -> Unit) {
        val credential = GoogleAuthProvider.getCredential(idToken, null)
        auth.signInWithCredential(credential)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    onResult(true, "Google ile giriş başarılı")
                } else {
                    onResult(false, task.exception?.message ?: "Google ile giriş başarısız")
                }
            }

    }

    fun logout() {
        auth.signOut()
    }

    fun getCurrentUser(): User? {
        val firebaseUser = auth.currentUser
        return firebaseUser?.let {
            User(
                uid = it.uid,
                email = it.email ?: ""
            )
        }
    }
}
