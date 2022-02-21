package com.example.visioglobe.network.mapper

import com.example.visioglobe.domain.mapper.EntityMapper
import com.example.visioglobe.domain.model.User
import com.example.visioglobe.network.entity.UserEntity

class UserNetworkMapper : EntityMapper<UserEntity, User> {
    override fun mapToDomainModel(entity: UserEntity): User {
        return User(
            id = entity.id,
            email = entity.email,
            firstName = entity.firstName,
            lastName = entity.lastName,
            permission = entity.permission,
            phone = entity.phone,
            site = entity.site
        )
    }

    override fun mapToEntity(model: User): UserEntity {
        return UserEntity(
            id = model.id,
            email = model.email,
            firstName = model.firstName,
            lastName = model.lastName,
            permission = model.permission,
            phone = model.phone,
            site = model.site
        )
    }

    fun toDomainModelList(initial: List<UserEntity>) : List<User> {
        return initial.map { mapToDomainModel(it) }
    }

    fun toModelInEntityList(initial: List<User>) : List<UserEntity> {
        return initial.map { mapToEntity(it) }
    }

    fun toDomainInModel(initial: UserEntity) : User {
        return mapToDomainModel(initial)
    }

    fun toModelInEntity(initial: User) : UserEntity {
        return mapToEntity(initial)
    }
}