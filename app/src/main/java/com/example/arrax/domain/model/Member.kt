package com.example.arrax.domain.model

data class Member(
    val uid: String,
    val name: String,
    val role: Role,
    val joinedAt: Long
)