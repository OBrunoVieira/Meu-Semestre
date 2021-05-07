package com.doubleb.meusemestre.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.doubleb.meusemestre.extensions.takeIfValid
import com.doubleb.meusemestre.models.AccessDeniedException
import com.doubleb.meusemestre.models.CanceledException
import com.doubleb.meusemestre.models.GraduationInfo
import com.doubleb.meusemestre.models.User
import com.doubleb.meusemestre.repository.UserRepository
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener

class UserViewModel(private val userRepository: UserRepository, private val auth: FirebaseAuth) :
    ViewModel() {

    //region immutable vars
    val liveDataGraduationInfo = MutableLiveData<DataSource<User>>()
    val liveDataUserCreation = MutableLiveData<DataSource<User>>()
    val liveDataUser = MutableLiveData<DataSource<User>>()
    //endregion

    fun recoverUserId() = auth.currentUser?.uid

    fun createGraduationInfo(graduationInfo: GraduationInfo) {
        auth.currentUser?.uid.takeIfValid()?.let { userId ->
            userRepository.createGraduationInfo(userId, graduationInfo)
                .addOnSuccessListener {
                    liveDataGraduationInfo.postValue(DataSource(DataState.SUCCESS))
                }
                .addOnFailureListener {
                    liveDataGraduationInfo.postValue(DataSource(DataState.ERROR, throwable = it))
                }
                .addOnCanceledListener {
                    liveDataGraduationInfo
                        .postValue(DataSource(DataState.ERROR, throwable = CanceledException()))
                }
        } ?: run {
            liveDataGraduationInfo.postValue(
                DataSource(DataState.ERROR, throwable = AccessDeniedException())
            )
        }
    }

    fun getUser(userId: String? = recoverUserId()) {
        userId.takeIfValid()?.let {
            userRepository.getUser(it)
                .addListenerForSingleValueEvent(
                    object : ValueEventListener {
                        override fun onDataChange(snapshot: DataSnapshot) {
                            snapshot.getValue(User::class.java)?.let { user ->
                                liveDataUser.postValue(DataSource(DataState.SUCCESS, user))
                            } ?: run {
                                liveDataUser.postValue(DataSource(DataState.ERROR))
                            }
                        }

                        override fun onCancelled(error: DatabaseError) {
                            liveDataUser
                                .postValue(DataSource(DataState.ERROR,
                                    throwable = error.toException()))
                        }

                    }
                )
        } ?: run {
            liveDataUser.postValue(DataSource(DataState.ERROR))
        }
    }

    fun getUserOrCreate(localUser: User?, userId: String? = recoverUserId()) {
        if (userId.isNullOrBlank()) {
            liveDataUser.postValue(DataSource(DataState.ERROR))
            return
        }

        userRepository.getUser(userId).addListenerForSingleValueEvent(
            object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val serverUser = snapshot.getValue(User::class.java)

                    when {
                        serverUser?.isValid() == true ->
                            liveDataUser.postValue(DataSource(DataState.SUCCESS, serverUser))

                        localUser?.isValid() == true ->
                            createUser(localUser)

                        else ->
                            liveDataUser.postValue(DataSource(DataState.ERROR))
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    liveDataUser
                        .postValue(DataSource(DataState.ERROR, throwable = error.toException()))
                }
            }
        )
    }

    fun createUser(user: User) {
        userRepository.createUser(user)
            .addOnSuccessListener {
                liveDataUserCreation.postValue(DataSource(DataState.SUCCESS, user))
            }
            .addOnFailureListener {
                liveDataUserCreation.postValue(DataSource(DataState.ERROR, throwable = it))
            }
            .addOnCanceledListener {
                liveDataUserCreation.postValue(
                    DataSource(
                        DataState.ERROR,
                        throwable = CanceledException()
                    )
                )
            }
    }
}