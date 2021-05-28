package com.doubleb.meusemestre.extensions

import android.app.Activity
import android.content.Context
import android.content.Intent
import androidx.activity.ComponentActivity
import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultCallback
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.os.bundleOf

inline fun <reified T : Activity> Activity.openActivity(vararg pairs: Pair<String, Any?>) =
    startActivity(
        Intent(this, T::class.java).also { intent ->
            intent.putExtras(bundleOf(*pairs))
        }
    )

inline fun <reified T : Activity> Activity.openClearedActivity(vararg pairs: Pair<String, Any?>) =
    startActivity(
        Intent(this, T::class.java)
            .also { intent ->
                intent.putExtras(bundleOf(*pairs))
            }
            .setFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
    )

fun ComponentActivity.createActivityResultLauncher(callback: ActivityResultCallback<ActivityResult>) =
    registerForActivityResult(ActivityResultContracts.StartActivityForResult(), callback)

inline fun <reified T> ActivityResultLauncher<Intent>.launchActivity(
    context: Context?,
    vararg pairs: Pair<String, Any?>,
) = context?.let {
    launch(
        Intent(context, T::class.java).also { intent ->
            intent.putExtras(bundleOf(*pairs))
        }
    )
}
