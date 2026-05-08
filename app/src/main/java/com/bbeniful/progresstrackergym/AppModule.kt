package com.bbeniful.progresstrackergym

import com.bbeniful.add.impl.ui.AddModule
import com.bbeniful.data.DataModule
import com.bbeniful.domain.DomainModule
import com.bbeniful.feature.settings.impl.ui.SettingsModule
import com.bbeniful.feature.statistic.impl.ui.StatisticModule
import com.bbeniful.home.impl.ui.HomeImplModule
import org.koin.core.annotation.ComponentScan
import org.koin.core.annotation.Module

@Module(
    includes = [
        DataModule::class,
        DomainModule::class,
        HomeImplModule::class,
        AddModule::class,
        SettingsModule::class,
        StatisticModule::class
    ]
)
@ComponentScan("com.bbeniful.progresstrackergym")
class AppModule
