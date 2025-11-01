package com.jalmarquest

import android.app.Application
import com.jalmarquest.shared.di.platformModule
import com.jalmarquest.shared.di.sharedModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class JalmarQuestApp : Application() {
    
    override fun onCreate() {
        super.onCreate()
        
        startKoin {
            androidContext(this@JalmarQuestApp)
            modules(sharedModule, platformModule)
        }
    }
}
