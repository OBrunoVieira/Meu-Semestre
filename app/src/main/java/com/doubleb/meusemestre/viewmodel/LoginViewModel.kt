package com.doubleb.meusemestre.viewmodel

import android.os.Bundle
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.doubleb.meusemestre.models.User
import com.doubleb.meusemestre.repository.UserRepository
import com.facebook.*
import com.facebook.login.LoginManager
import com.facebook.login.LoginResult
import com.google.firebase.auth.FacebookAuthProvider
import com.google.firebase.auth.FirebaseAuth

class LoginViewModel(private val userRepository: UserRepository, private val auth: FirebaseAuth) :
    ViewModel(),
    FacebookCallback<LoginResult> {

    companion object {
        private const val FACEBOOK_NAME = "name"
        private const val FACEBOOK_MAIL = "email"
        private const val FACEBOOK_PICTURE = "picture"
        private const val FACEBOOK_PICTURE_DATA = "data"
        private const val FACEBOOK_PICTURE_URL = "url"
        private const val FACEBOOK_FIELDS = "fields"
        private const val FACEBOOK_FIELDS_VALUE =
            "id,name,email,gender,birthday,picture.type(large)"
    }

    //region immutable vars
    val facebookCallbackManager: CallbackManager by lazy { CallbackManager.Factory.create() }
    val liveData = MutableLiveData<DataSource<User>>()
    //endregion

    init {
        LoginManager.getInstance().registerCallback(facebookCallbackManager, this)
    }

    fun validateCurrentSession() {
        if (isAlreadyLogged()) {
//            userRepository.getUser()
            liveData.postValue(DataSource(DataState.SUCCESS))
        }
    }

    //region Facebook Login's callback
    override fun onSuccess(result: LoginResult?) {
        result?.let {
            signInWithAccessToken(it.accessToken)
        } ?: run {
            liveData.postValue(DataSource(DataState.ERROR))
        }
    }

    override fun onCancel() {
    }

    override fun onError(error: FacebookException?) {
        liveData.postValue(DataSource(DataState.ERROR, throwable = error?.cause))
    }
    //endregion

    fun logout(block: () -> Unit = {}) {
        GraphRequest(
            AccessToken.getCurrentAccessToken(),
            "/me/permissions/", null, HttpMethod.DELETE
        ) {
            AccessToken.setCurrentAccessToken(null)
            LoginManager.getInstance().logOut()
            auth.signOut()
            block.invoke()
        }.executeAsync()
    }

    private fun signInWithAccessToken(token: AccessToken) {
        val credential = FacebookAuthProvider.getCredential(token.token)
        auth.signInWithCredential(credential)
            .addOnFailureListener {
                liveData.postValue(DataSource(DataState.ERROR, throwable = it))
            }
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    recoverFacebookUser()
                } else {
                    liveData.postValue(DataSource(DataState.ERROR))
                }
            }
    }

    private fun recoverFacebookUser() =
        GraphRequest.newMeRequest(
            AccessToken.getCurrentAccessToken()
        ) { json, _ ->
            val user = User(
                auth.currentUser?.uid,
                json.getString(FACEBOOK_NAME),
                json.getString(FACEBOOK_MAIL),
                json.getJSONObject(FACEBOOK_PICTURE)
                    .getJSONObject(FACEBOOK_PICTURE_DATA)
                    .getString(FACEBOOK_PICTURE_URL)
            )

            userRepository.createUser(user)
            liveData.postValue(DataSource(DataState.SUCCESS, user))
        }.also {
            it.parameters =
                Bundle().apply { putString(FACEBOOK_FIELDS, FACEBOOK_FIELDS_VALUE) }
        }.executeAsync()

    private fun isAlreadyLogged() = auth.currentUser != null
}