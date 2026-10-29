package com.example.arrax.data.mapper

import com.example.arrax.data.remote.MemberDto
import com.example.arrax.domain.model.Member
import com.example.arrax.domain.model.Role

fun MemberDto.toDomain() = Member(
    uid = uid,
    name = name,
    role = Role.entries.firstOrNull { it.name == role } ?: Role.OPERATOR,
    joinedAt = joinedAt
)