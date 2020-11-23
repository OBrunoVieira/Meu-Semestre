package com.doubleb.meusemestre.ui.activities

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import com.doubleb.meusemestre.R
import kotlinx.android.synthetic.main.activity_exam_registration.*

class ExamRegistrationActivity : BaseActivity(R.layout.activity_exam_registration) {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setSupportActionBar(exam_registration_toolbar)

        supportActionBar?.setHomeButtonEnabled(true)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowTitleEnabled(false)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menu?.clear()
        menuInflater.inflate(R.menu.menu_exam_registration, menu)
        return super.onCreateOptionsMenu(menu)
    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.menu_exam_registration_delete) {
            onBackPressed()
        }
        return super.onOptionsItemSelected(item)
    }
}