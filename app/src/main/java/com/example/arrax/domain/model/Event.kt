package com.example.arrax.domain.model


data class Event(
    val id: String = "",
    val name: String = "",
    val date: Long = 0L,
    val pigCount: Int = 0,
//    val tenantId: String="",<--agregando esto se corrige "e: file:///C:/Users/likbe/AndroidStudioProjects/Arrax/app/src/main/java/com/example/arrax/data/mapper/EventMapper.kt:26:19 Unresolved reference 'tenantId'.
//Ask Gemini"
    val status: EventStatus = EventStatus.OPEN,
    val createdBy: String = ""
)
enum class EventStatus {
    OPEN,
    CLOSED
}