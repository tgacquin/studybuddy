package com.example.studybuddy


import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.firebase.auth.FirebaseAuth
import android.os.Bundle
import com.example.studybuddy.R
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.firebase.auth.FirebaseUser
import android.widget.Toast
import android.content.Intent
import android.util.Log
import android.view.View
import android.widget.Button
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.AuthCredential
import com.google.firebase.auth.GoogleAuthProvider
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.auth.AuthResult
import com.example.studybuddy.MainMenu

class LogIn : AppCompatActivity() {
    lateinit var signIn: Button
    var mGoogleSignInClient: GoogleSignInClient? = null
    var mAuth: FirebaseAuth? = null
    val TAG = "Google Sign In"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        this.setContentView(R.layout.login)
        signIn = findViewById(R.id.signInBtn)
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id)) //Default_web_client_id will be matched with the
            .requestEmail()
            .build()
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso)
        mAuth = FirebaseAuth.getInstance()
        signIn.setOnClickListener(View.OnClickListener { signInToGoogle() })
    }

    public override fun onStart() {
        super.onStart()
        val currentUser = mAuth!!.currentUser
        if (currentUser != null) {
            Log.d(TAG, "Currently Signed in: " + currentUser.email)
            Toast.makeText(
                this@LogIn,
                "Currently Logged in: " + currentUser.email,
                Toast.LENGTH_LONG
            ).show()
        }
    }

    public override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

// Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == 9001) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            try {
// Google Sign In was successful, authenticate with Firebase
                val account = task.getResult(ApiException::class.java)
                Toast.makeText(this, "Google Sign in Succeeded", Toast.LENGTH_LONG).show()
                firebaseAuthWithGoogle(account)
            } catch (e: ApiException) {
// Google Sign In failed, update UI appropriately
                Log.w(TAG, "Google sign in failed", e)
                Toast.makeText(this, "Google Sign in Failed $e", Toast.LENGTH_LONG).show()
            }
        }
    }

    private fun firebaseAuthWithGoogle(acct: GoogleSignInAccount) {
        Log.d(TAG, "firebaseAuthWithGoogle:" + acct.id)
        //Calling get credential from the oogleAuthProviderG
        val credential = GoogleAuthProvider.getCredential(acct.idToken, null)
        mAuth!!.signInWithCredential(credential)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    val user = mAuth!!.currentUser
                    Log.d(TAG, "signInWithCredential:success: currentUser: " + user!!.email)
                    Toast.makeText(
                        this@LogIn,
                        "Firebase Authentication Succeeded ",
                        Toast.LENGTH_LONG
                    ).show()
                    val i = Intent(this@LogIn, MainMenu::class.java)
                    startActivity(i)
                } else {
                    Log.w(TAG, "signInWithCredential:failure", task.exception)
                    Toast.makeText(
                        this@LogIn,
                        "Firebase Authentication failed:" + task.exception,
                        Toast.LENGTH_LONG
                    ).show()
                }
            }
    }
    

    fun signInToGoogle() {
        //Calling Intent and call startActivityForResult() method
        val signInIntent = mGoogleSignInClient!!.signInIntent
        startActivityForResult(signInIntent, 9001)
    }
}
