package com.debduttapanda.j3lib

class Resolver(private val resolver: _Resolver){
    fun <T>get(key: Any): T{
        return resolver.get(key)
    }
    fun <T>getOrNull(key: Any): T?{
        return resolver.getOrNull(key)
    }
}

class _Resolver(){
    private val _map: MutableMap<Any, Any?> = mutableMapOf()
    fun clear(){
        _map.clear()
    }
    fun <T>get(key: Any): T{
        return _map[key] as T
    }
    fun <T>getOrNull(key: Any): T?{
        return _map[key] as? T
    }

    operator fun set(key: Any, value: Any?){
        _map[key] = value
    }

    fun addAll(map: Map<Any, Any?>){
        _map.putAll(map)
    }

    fun addAll(vararg pairs: Pair<Any, Any?>){
        _map.putAll(pairs)
    }
}