package com.example.arrax.data.mapper

import com.example.arrax.data.remote.TenantDto
import com.example.arrax.domain.model.Tenant

fun TenantDto.toDomain(): Tenant = Tenant(id = id, name = name, createdAt = createdAt)
