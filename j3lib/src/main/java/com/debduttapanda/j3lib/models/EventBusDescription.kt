package com.debduttapanda.j3lib.models

import com.debduttapanda.j3lib.EventBus

data class EventBusDescription(
    val eventBusId: String,
    val eventBusTopics: (EventBus.TopicsBuilder.() -> Unit)? = null,
    val eventBusAction: ((pattern: String, topic: String, value: Any?) -> Unit)? = null
)