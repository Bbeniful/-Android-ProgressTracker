package com.bbeniful.progresstrackergym

import com.bbeniful.data.DataModule
import com.bbeniful.home.impl.ui.HomeImplModule
import org.koin.core.annotation.ComponentScan
import org.koin.core.annotation.Module

@Module(
    includes = [
        DataModule::class,
       // DomainModule::class,
        HomeImplModule::class
    ]
)
@ComponentScan("com.bbeniful.progresstrackergym")
class AppModule