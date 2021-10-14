package com.katyrin.thundergram

import com.katyrin.thundergram.view.inputvalidators.LoginCodeValidator
import com.katyrin.thundergram.view.inputvalidators.PhoneNumberValidator
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class ValidatorsUnitTest {

    @Test
    fun phoneNumberValidator_WithoutPlusCharPhoneNumber_ReturnFalse() {
        assertFalse(PhoneNumberValidator.isValidPhoneNumber("89997776655"))
    }

    @Test
    fun phoneNumberValidator_ShortPhoneNumber_ReturnFalse() {
        assertFalse(PhoneNumberValidator.isValidPhoneNumber("+799977"))
    }

    @Test
    fun phoneNumberValidator_CorrectPhoneNumber_ReturnTrue() {
        assertTrue(PhoneNumberValidator.isValidPhoneNumber("+79997776655"))
    }

    @Test
    fun phoneNumberValidator_NullPhoneNumber_ReturnsFalse() {
        assertFalse(PhoneNumberValidator.isValidPhoneNumber(null))
    }

    @Test
    fun loginCodeValidator_ShortOrLongCode_ReturnFalse() {
        assertFalse(LoginCodeValidator.isValidCode("765"))
        assertFalse(LoginCodeValidator.isValidCode("76554756236"))
    }

    @Test
    fun loginCodeValidator_ContainsLetter_ReturnFalse() {
        assertFalse(LoginCodeValidator.isValidCode("7a655"))
    }

    @Test
    fun loginCodeValidator_CorrectCode_ReturnTrue() {
        assertTrue(LoginCodeValidator.isValidCode("76655"))
    }

    @Test
    fun loginCodeValidator_NullCode_ReturnsFalse() {
        assertFalse(LoginCodeValidator.isValidCode(null))
    }
}