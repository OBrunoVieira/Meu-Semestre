package com.doubleb.meusemestre.extensions

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultCallback
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment

fun Fragment.createActivityResultLauncher(callback: ActivityResultCallback<ActivityResult>) =
    registerForActivityResult(ActivityResultContracts.StartActivityForResult(), callback)

inline fun <reified T : Activity> Fragment.openActivity(extras: Bundle? = null) =
    startActivity(
        Intent(context, T::class.java).also { intent ->
            extras?.let { intent.putExtras(it) }
        }
    )