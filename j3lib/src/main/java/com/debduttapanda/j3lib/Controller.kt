package com.debduttapanda.j3lib


class Controller(
    val resolver: _Resolver = _Resolver(),
    val notificationService: _NotificationService = _NotificationService()
){
    fun restricted(): RestrictedController = RestrictedController(
        Resolver(resolver),
        NotificationService(notificationService)
    )
}