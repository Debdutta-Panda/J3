package com.debduttapanda.j3lib

fun Any.simpleName(): String = this::class.java.simpleName
fun Any.specialName(): String = this::class.java.simpleName + this.hashCode()