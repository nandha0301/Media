package com.example.media.ui.repository


import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthInvalidUserException
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

/*
class AuthRepository
@Inject
constructor(){
    private val auth: FirebaseAuth = FirebaseAuth.getInstance()
    private val database = FirebaseDatabase.getInstance().getReference("users")

    // ✅ Sign Up User & Send Email Verification
    suspend fun signUp(name: String, email: String, password: String): Boolean {
        return try {
            val result = auth.createUserWithEmailAndPassword(email, password).await()
            val user = result.user

            user?.sendEmailVerification()?.await() // 🔥 Send Email Verification

            user?.let {
                saveUserToDatabase( name, email) // ✅ Save user details
            }

            true // Success
        } catch (e: Exception) {
            e.printStackTrace()
            false // Failed
        }
    }


    private suspend fun saveUserToDatabase(name: String, email: String) {
        val usersRef = database.child("users")

        // Get the current highest user ID and generate the next one
        val snapshot = usersRef.get().await()
        val nextId = (snapshot.childrenCount + 1).toInt() // Sequential ID

        // User data
        val userMap = mapOf(
            "name" to name,
            "email" to email
        )

        // Save user directly under the generated ID
        usersRef.child(nextId.toString()).setValue(userMap).await()
    }

    // ✅ Login User
    suspend fun login(email: String, password: String): Boolean {
        return try {
            val result = auth.signInWithEmailAndPassword(email, password).await()
            val user = result.user
            user?.isEmailVerified == true // ✅ Check if email is verified
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }

    // ✅ Check if user is logged in
    fun getCurrentUser(): FirebaseUser? {
        return auth.currentUser
    }

    // ✅ Logout
    fun logout() {
        auth.signOut()
    }
}*/

class AuthRepository @Inject constructor() {
    private val auth: FirebaseAuth = FirebaseAuth.getInstance()
    private val firestore = FirebaseFirestore.getInstance()

    // ✅ Sign Up User & Send Email Verification
    suspend fun signUp(name: String, email: String, password: String): Boolean {
        return try {
            val result = auth.createUserWithEmailAndPassword(email, password).await()
            val user = result.user

            user?.sendEmailVerification()?.await() // 🔥 Send Email Verification

            user?.let {
                saveUserToFirestore(it.uid, name, email) // ✅ Save user details in Firestore
            }

            true // Success
        } catch (e: Exception) {
            e.printStackTrace()
            false // Failed
        }
    }

    // ✅ Save User Data in Firestore
    private suspend fun saveUserToFirestore(userId: String, name: String, email: String) {
        val userMap = hashMapOf(
            "id" to userId, // Firebase Auth UID
            "name" to name,
            "email" to email
        )

        firestore.collection("users").document(userId).set(userMap).await()
    }
/*
    // ✅ Login User
    suspend fun login(email: String, password: String): Boolean {
        return try {
            val result = auth.signInWithEmailAndPassword(email, password).await()
            val user = result.user
            user?.isEmailVerified == true // ✅ Check if email is verified
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }*/

    suspend fun login(email: String, password: String): Pair<Boolean, String?> {
        return try {
            val result = auth.signInWithEmailAndPassword(email, password).await()
            val user = result.user

            if (user?.isEmailVerified == true) {
                Pair(true, null) // ✅ Login successful
            } else {
                Pair(false, "Verify your email first!") // 🔥 Email not verified
            }
        } catch (e: FirebaseAuthInvalidUserException) {
            Pair(false, "Your email is not registered") // 🔥 Email not found
        } catch (e: FirebaseAuthInvalidCredentialsException) {
            Pair(false, "Incorrect email or password") // 🔥 Incorrect password
        } catch (e: Exception) {
            e.printStackTrace()
            Pair(false, "Login failed. Try again") // 🔥 Generic error
        }
    }

    // ✅ Get Current User from Firestore
    suspend fun getUserDetails(userId: String): DocumentSnapshot? {
        return try {
            firestore.collection("users").document(userId).get().await()
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    // ✅ Check if user is logged in
    fun getCurrentUser(): FirebaseUser? {
        return auth.currentUser
    }

    // ✅ Logout
    fun logout() {
        auth.signOut()
    }
}
