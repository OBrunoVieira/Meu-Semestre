package com.doubleb.meusemestre.ui.activities

import android.content.Intent
import android.os.Bundle
import androidx.viewpager2.widget.ViewPager2
import com.doubleb.meusemestre.R
import com.doubleb.meusemestre.extensions.blendColorAnimation
import com.doubleb.meusemestre.models.User
import com.doubleb.meusemestre.ui.adapters.viewpager.WelcomePageAdapter
import com.doubleb.meusemestre.ui.dialogs.BottomSheetLogin
import com.facebook.*
import com.facebook.login.LoginManager
import com.facebook.login.LoginResult
import com.google.firebase.auth.FacebookAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.activity_welcome.*


class WelcomeActivity : BaseActivity(R.layout.activity_welcome), FacebookCallback<LoginResult>,
    BottomSheetLogin.LoginClickListener {
    private var previousPosition = 0

    private val colors by lazy {
        arrayOf(
            R.color.dark_purple,
            R.color.dark_blue,
            R.color.dark_yellow
        )
    }

    private val pageChangeCallback by lazy { onPageChangeCallback() }
    private val bottomSheet by lazy { BottomSheetLogin(this) }
    private val callbackManager by lazy { CallbackManager.Factory.create() }

    private val auth by lazy { FirebaseAuth.getInstance() }
    private val database by lazy { FirebaseDatabase.getInstance().reference }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        LoginManager.getInstance().registerCallback(callbackManager, this)

        welcome_view_pager.apply {
            adapter = WelcomePageAdapter(this@WelcomeActivity)
            welcome_page_indicator.count = (adapter as WelcomePageAdapter).itemCount
            registerOnPageChangeCallback(pageChangeCallback)
        }

        welcome_button_explore.setOnClickListener {
            startActivity(Intent(this, RegisterActivity::class.java))
        }

        welcome_button_login.setOnClickListener {
            bottomSheet.show(supportFragmentManager, BottomSheetLogin.TAG)
        }
    }

    override fun onDestroy() {
        GraphRequest(
            AccessToken.getCurrentAccessToken(),
            "/me/permissions/", null, HttpMethod.DELETE
        ) {
            AccessToken.setCurrentAccessToken(null)
            LoginManager.getInstance().logOut()
            auth.signOut()
        }.executeAsync()

        super.onDestroy()
        welcome_view_pager.unregisterOnPageChangeCallback(pageChangeCallback)
    }

    private fun onPageChangeCallback() = object : ViewPager2.OnPageChangeCallback() {
        override fun onPageSelected(position: Int) {
            super.onPageSelected(position)
            welcome_page_indicator.selection = position

            welcome_content_main.blendColorAnimation(
                colors[previousPosition % colors.size],
                colors[position % colors.size],
                "backgroundColor"
            )

            previousPosition = position
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        callbackManager.onActivityResult(requestCode, resultCode, data)
        super.onActivityResult(requestCode, resultCode, data)
    }

    //region Facebook Login's callback
    override fun onSuccess(result: LoginResult?) {
        result?.let {
            handleFacebookAccessToken(it.accessToken)
        }
    }

    override fun onCancel() {
    }

    override fun onError(error: FacebookException?) {
    }
    //endregion

    //region Login Button's callback
    override fun onFacebookLogin() {
        LoginManager.getInstance()
            .logInWithReadPermissions(this, listOf("public_profile", "email"))
    }

    override fun onGoogleLogin() {
    }

    private fun handleFacebookAccessToken(token: AccessToken) {
        val credential = FacebookAuthProvider.getCredential(token.token)
        auth.signInWithCredential(credential)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {

                    GraphRequest.newMeRequest(
                        AccessToken.getCurrentAccessToken()
                    ) { json, response ->
                        val data = json.getJSONObject("picture").getJSONObject("data")
                        database.child("users")
                            .child(token.userId)
                            .setValue(
                                User(
                                    json.getString("name"),
                                    json.getString("email"),
                                    data.getString("url")
                                )
                            )

                        startActivity(Intent(this, HomeActivity::class.java))
                        finish()
                    }.also {
                        it.parameters =
                            Bundle().apply {
                                putString(
                                    "fields",
                                    "id,name,email,gender,birthday,picture.type(large)"
                                )
                            }
                    }.executeAsync()


                } else {
                    //failure
                }
            }
    }

}