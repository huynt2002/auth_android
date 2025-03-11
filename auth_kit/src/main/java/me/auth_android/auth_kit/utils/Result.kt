package me.auth_android.auth_kit.utils

sealed class Result<out T, out E>

data class Success<out T>(val value: T) : Result<T, Nothing>()

data class Failure<out E>(val reason: E) : Result<Nothing, E>()
