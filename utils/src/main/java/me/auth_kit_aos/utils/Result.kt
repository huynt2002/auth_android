package me.auth_kit_aos.utils

sealed class Result<out T, out E>

data class Success<out T>(val value: T) : Result<T, Nothing>()

data class Failure<out E>(val reason: E) : Result<Nothing, E>()
