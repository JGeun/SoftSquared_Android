package jgeun.study.networkapiairport

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Base64
import android.util.Log
import android.widget.Toast
import com.kakao.sdk.auth.LoginClient
import com.kakao.sdk.auth.model.OAuthToken
import com.nhn.android.naverlogin.OAuthLogin
import com.nhn.android.naverlogin.OAuthLoginHandler
import jgeun.study.networkapiairport.databinding.ActivityLoginBinding
import java.net.URLEncoder.encode
import java.security.MessageDigest
import java.util.*

class LoginActivity : AppCompatActivity() {
    private val TAG = "LoginActivity"
    private lateinit var mContext: Context
    private lateinit var mOAuthLoginModule: OAuthLogin

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityLoginBinding.inflate(layoutInflater)

        mContext = applicationContext

        binding.btnLogin.setOnClickListener{
            startActivity(Intent(this, SelectActivity::class.java))
        }

        binding.btnKakaoLogin.setOnClickListener{
            Log.d(TAG, "kakao 클릭")
            kakaoLogin()
        }

        binding.btnNaverLogin.setOnClickListener{
            naverLogin()
            startActivity(Intent(this, SelectActivity::class.java))
        }
        setContentView(binding.root)
    }
    private fun naverLogin(){
        mOAuthLoginModule = OAuthLogin.getInstance()
        mOAuthLoginModule.init(
                mContext,
                getString(R.string.naver_client_id),
                getString(R.string.naver_client_secret),
                getString(R.string.naver_client_name)
        )

        @SuppressLint("HandlerLeak")
        var OAuthLoginHandler = object : OAuthLoginHandler(){
            override fun run(success: Boolean) {
                if(success){
                    val accessToken = mOAuthLoginModule.getAccessToken(mContext)
                    val refreshToken = mOAuthLoginModule.getRefreshToken(mContext)
                    val expiresAt = mOAuthLoginModule.getExpiresAt(mContext)
                    val tokenType = mOAuthLoginModule.getTokenType(mContext)

                    Log.i("LoginData","accessToken : "+ accessToken);
                    Log.i("LoginData","refreshToken : "+ refreshToken);
                    Log.i("LoginData","expiresAt : "+ expiresAt);
                    Log.i("LoginData","tokenType : "+ tokenType);
                }else {
                    val errorCode = mOAuthLoginModule.getLastErrorCode(mContext).getCode();
                    val errorDesc = mOAuthLoginModule.getLastErrorDesc(mContext);
                    Toast.makeText(mContext, "errorCode:" + errorCode
                            + ", errorDesc:" + errorDesc, Toast.LENGTH_SHORT).show();
                }
            }
        }
        mOAuthLoginModule.startOauthLoginActivity(this, OAuthLoginHandler)
    }

    private fun kakaoLogin(){
        val callback: (OAuthToken?, Throwable?) -> Unit = { token, error ->
            if (error != null) {
                Log.e(TAG, "로그인 실패", error)
            }
            else if (token != null) {
                Log.i(TAG, "로그인 성공 ${token.accessToken}")
                startActivity(Intent(this, SelectActivity::class.java))
            }
        }
        Log.d(TAG, "callback check 끝")
// 카카오톡이 설치되어 있으면 카카오톡으로 로그인, 아니면 카카오계정으로 로그인
        if (LoginClient.instance.isKakaoTalkLoginAvailable(this)) {
            LoginClient.instance.loginWithKakaoTalk(this, callback = callback)
        } else {
            LoginClient.instance.loginWithKakaoAccount(this, callback = callback)
        }
    }
    private fun getAppKeyHash() {
        try {
            val info = packageManager.getPackageInfo(packageName, PackageManager.GET_SIGNATURES)
            for (signature in info.signatures) {
                var md: MessageDigest
                md = MessageDigest.getInstance("SHA")
                md.update(signature.toByteArray())
                val something: String = String(Base64.encode(md.digest(), 0))
                Log.e("Hash key", something)
            }
        } catch (e: Exception) {
            // TODO Auto-generated catch block
            Log.e("name not found", e.toString())
        }
    }
}