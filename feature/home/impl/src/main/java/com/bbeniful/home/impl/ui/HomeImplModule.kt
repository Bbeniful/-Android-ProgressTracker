package com.bbeniful.home.impl.ui

import com.bbeniful.domain.DomainModule
import org.koin.core.annotation.ComponentScan
import org.koin.core.annotation.Configuration
import org.koin.core.annotation.Module

@Module(includes = [DomainModule::class])
@Configuration
@ComponentScan("com.bbeniful.home.impl.ui")
class HomeImplModule