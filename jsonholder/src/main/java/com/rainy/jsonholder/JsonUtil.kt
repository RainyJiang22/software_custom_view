package com.rainy.jsonholder

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.JsonDeserializer
import com.google.gson.JsonObject
import com.google.gson.JsonSyntaxException
import com.google.gson.reflect.TypeToken


val gson: Gson by lazy(mode = LazyThreadSafetyMode.SYNCHRONIZED) {
    val builder = GsonBuilder()
    builder.registerTypeAdapter(
        JsonObject::class.java,
        JsonDeserializer<Any> { jsonElement, type, jsonDeserializationContext -> jsonElement.asJsonObject })
    builder.disableHtmlEscaping()
        .setLenient()
        .create()
}


fun <T> ArrayList<T>.listToJson(): String {
    return gson.toJson(this)
}

inline fun <reified T> String.jsonToList(token: TypeToken<ArrayList<T>>): ArrayList<T> {
    return gson.fromJson(this, token.type)
}

inline fun <reified T> String.jsonToList(token: TypeToken<List<T>>): List<T> {
    return gson.fromJson(this, token.type)
}


inline fun <reified T> T.clazzToJson(): String {
    val token = object : TypeToken<T>() {}.type
    return gson.toJson(this, token)
}


inline fun <reified T> String.jsonToClazz(): T {
    val token = object : TypeToken<T>() {}.type
    return gson.fromJson(this, token)
}