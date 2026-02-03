package com.helloworld.dynamicformbuilder.form.submission

sealed class SubmissionState{
    object Idle : SubmissionState()
    object Submitting : SubmissionState()
    object Success : SubmissionState()
    data class Error(val message: String) : SubmissionState()
}