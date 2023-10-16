package com.debduttapanda.j3lib

import androidx.navigation.NamedNavArgument
import androidx.navigation.NavArgument
import androidx.navigation.NavDeepLink

data class Route(
    val name: String,
    val arguments: List<NamedNavArgument> = emptyList(),
    val deepLinks: List<NavDeepLink> = emptyList(),
) {

    fun navigation(values: MutableMap<String,Any>.()->Unit): String{
        val map = mutableMapOf<String,Any>()
        values(map)
        return name+arguments.stringWithValue(map)
    }

    val full by lazy { "$name${arguments.string()}" }

    private fun List<NamedNavArgument>.string(): String {
        val requiredList = mutableListOf<String>()
        val optionalList = mutableListOf<String>()
        forEach {
            val name = it.name
            val required = !it.argument.run { defaultValue != null || isNullable }
            if (required) {
                requiredList.add("{$name}")
            } else {
                optionalList.add("$name={$name}")
            }
        }
        var required = requiredList.joinToString("/")
        if (required.isNotEmpty()) {
            required = "/$required"
        }
        var optional = optionalList.joinToString("&")
        if (optional.isNotEmpty()) {
            optional = "?$required"
        }
        return "$required$optional"
    }

    private fun List<NamedNavArgument>.stringWithValue(map: MutableMap<String,Any>): String {
        val requiredList = mutableListOf<String>()
        val optionalList = mutableListOf<String>()
        forEach {
            val name = it.name
            if(map.containsKey(name)){
                val value = map[name]
                val required = !it.argument.run { defaultValue != null || isNullable }
                if (required) {
                    requiredList.add("$value")
                } else {
                    optionalList.add("$name=$value")
                }
            }
        }
        var required = requiredList.joinToString("/")
        if (required.isNotEmpty()) {
            required = "/$required"
        }
        var optional = optionalList.joinToString("&")
        if (optional.isNotEmpty()) {
            optional = "?$required"
        }
        return "$required$optional"
    }
}