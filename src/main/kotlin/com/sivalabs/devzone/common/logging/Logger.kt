package com.sivalabs.devzone.common.logging

import org.slf4j.Logger
import org.slf4j.LoggerFactory
import kotlin.reflect.full.companionObject

inline fun <reified T : Any> T.logger(): Logger = getLogger(getClassForLogging(T::class.java))

fun getLogger(forClass: Class<*>): Logger = LoggerFactory.getLogger(forClass)

fun <T : Any> getClassForLogging(javaClass: Class<T>): Class<*> {
    return javaClass.enclosingClass?.takeIf {
        it.kotlin.companionObject?.java == javaClass
    } ?: javaClass
}
