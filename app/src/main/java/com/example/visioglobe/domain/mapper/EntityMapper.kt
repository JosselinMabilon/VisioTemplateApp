package com.example.visioglobe.domain.mapper

interface EntityMapper <Entity, DomainModel> {

    fun mapToDomainModel(entity: Entity): DomainModel

    fun mapToEntity(model: DomainModel): Entity
}