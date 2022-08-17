package com.sivalabs.devzone.common.annotations

import java.lang.annotation.Inherited

@Target(AnnotationTarget.VALUE_PARAMETER)
@Retention(AnnotationRetention.RUNTIME)
@Inherited
@MustBeDocumented
annotation class CurrentUser
