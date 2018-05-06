package com.library.aaron.module.ui

import android.content.Intent
import android.os.Bundle
import com.library.aaron.core.ui.BaseActivity
import com.library.aaron.module.R
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import java.util.concurrent.TimeUnit

/**
 * Created by abhinav.sharma on 30/10/17.
 */

class SplashActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        Observable.interval(3, TimeUnit.SECONDS)
                .take(1)
                .observeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe { aLong -> navigateToHome() }
    }

    private fun navigateToHome() {
        val homeIntent = Intent(this, MainActivity::class.java)
        startActivity(homeIntent)
        finish()
    }
}
