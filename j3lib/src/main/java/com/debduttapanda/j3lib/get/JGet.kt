package com.debduttapanda.j3lib.get

object jGet{
    val _resolved = mutableMapOf<Class<*>,Any>()
    val _map = mutableMapOf<Class<*>,()->Any>()
    inline fun <reified T>set(noinline r: ()->Any){
        _map[T::class.java] = r
    }

    inline fun <reified T>get(): T{
        val type = T::class.java
        if(_resolved.containsKey(type)){
            return _resolved[type] as T
        }
        val r = _map[type]
        val value = r!!()
        var rvalue = if(value is Function<*>){
            ((value as (()->Any))()) as T
        } else{
            value as T
        }
        if(value !is Function<*>){
            _map.remove(type)
            _resolved[type] = rvalue!!
        }
        return rvalue
    }
}