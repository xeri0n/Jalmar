package com.jalmarquest.shared.di

import android.content.Context
import com.jalmarquest.shared.persistence.FileIO
import org.koin.dsl.module

actual val platformModule = module {
    single { FileIO(get<Context>()) }
}
