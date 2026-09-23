package com.example.arrax.data.mapper

import com.example.arrax.data.remote.CutCatalogDto
import com.example.arrax.domain.model.CutCatalog

fun CutCatalogDto.toDomain(): CutCatalog = CutCatalog(id = id, name = name, active = active)
