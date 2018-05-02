package com.gena.domain.extensions

object EnumExt {

    interface EnumInt {
        val value: Int
    }

    inline fun <reified T> enumFromInt(value: Int?): T?
            where T : EnumInt, T : Enum<T> =
            value?.let { v ->
                enumValues<T>().let { values ->
                    values.find {
                        v == it.value
                    } ?: throw IllegalArgumentException("Invalid int value: $v")
                }
            }

}