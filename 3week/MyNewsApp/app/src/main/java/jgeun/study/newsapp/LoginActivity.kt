package jgeun.study.newsapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import kotlinx.android.synthetic.main.activity_login.*

class LoginActivity : AppCompatActivity() {
    private val TAG = "MainActivity"
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        //Initialize Firebase Auth
        auth = FirebaseAuth.getInstance()

        var user = auth.currentUser
        if(user != null){
            val intent = Intent(this, MainActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
            startActivity(intent)
        }else{
            btn_login.setOnClickListener{
                loginEmail()
            }
            tv_signUp.setOnClickListener {
                startActivity(Intent(this, SignUpActivity::class.java))
            }
        }
    }

    override fun onStart() {
        super.onStart()

    }

    private fun loginEmail(){
        btn_login.setOnClickListener(View.OnClickListener {
            Log.d("LoginActivityEmail", "Click")
            val email = et_email.text.toString()
            val password = et_passWd.text.toString()
            if(!email.equals("") && !password.equals("")) {
                auth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this) { task ->
                        if (task.isSuccessful) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithEmail:success")
                            val intent = Intent(this, MainActivity::class.java)
                            startActivity(intent)

                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithEmail:failure", task.exception)
                            Toast.makeText(
                                baseContext, "Authentication failed.",
                                Toast.LENGTH_SHORT
                            ).show()
                            // ...
                        }
                    }
            }
        })
    }
}
