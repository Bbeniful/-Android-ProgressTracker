package com.bbeniful.data

import com.bbeniful.domain.DomainModule
import org.koin.core.annotation.ComponentScan
import org.koin.core.annotation.Module


@Module(includes = [DomainModule::class])
@ComponentScan("com.bbeniful.data")
class DataModule


