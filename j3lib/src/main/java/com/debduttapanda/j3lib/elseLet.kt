package com.debduttapanda.j3lib

import kotlin.contracts.ExperimentalContracts
import kotlin.contracts.InvocationKind
import kotlin.contracts.contract

@OptIn(ExperimentalContracts::class)
public inline fun <T, R> T?.elseLet(elseBlock: () -> R, block: (T) -> R): R {
    contract {
        callsInPlace(block, InvocationKind.EXACTLY_ONCE)
    }
    return if (this == null){
        elseBlock()
    } else {
        block(this)
    }

}
