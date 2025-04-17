package com.example.androidlab5_products.additional

class State<T>(val initialValue: T) {
    private var onChangeAction: ((T) -> Unit)? = null

    var value: T = initialValue
        get() = field
        set(value) {
            field = value
            onChangeAction?.invoke(value)
        }

    fun onChange(action : (T) -> Unit) {
        onChangeAction = action
    }
}