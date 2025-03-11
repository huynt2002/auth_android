package me.auth_android.auth_kit.utils

import com.google.common.truth.Truth.assertThat
import me.auth_android.auth_kit.presentation.utils.PasswordError
import me.auth_android.auth_kit.presentation.utils.getPasswordError
import org.junit.Test

class StringExtTest {
//    @Test
//    fun `email valid test`(){
//        val email = "abc@ccm.aab"
//        assertThat(email.getEmailValid()).isEqualTo(true)
//    }
//
//    @Test
//    fun `email invalid test`(){
//        val email = " abc@ccc"
//        assertThat(email.getEmailValid()).isEqualTo(false)
//    }

    @Test
    fun `password valid test`(){
        val password = "123456"
        assertThat(password.getPasswordError()).isEqualTo(null)
    }

    @Test
    fun `password invalid short test`(){
        val password = "abc"
        assertThat(password.getPasswordError()).isEqualTo(PasswordError.SHORT)
    }
}