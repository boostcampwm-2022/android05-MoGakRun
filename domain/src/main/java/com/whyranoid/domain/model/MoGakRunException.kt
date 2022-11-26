package com.whyranoid.domain.model

sealed class MoGakRunException : Exception() {

    object NetworkFailureException : MoGakRunException()

    object FileNotFoundedException : MoGakRunException()

    object OtherException : MoGakRunException()
}
