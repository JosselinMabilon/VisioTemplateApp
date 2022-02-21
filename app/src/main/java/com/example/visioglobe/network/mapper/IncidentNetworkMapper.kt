package com.example.visioglobe.network.mapper

import android.net.Uri
import com.example.visioglobe.domain.mapper.EntityMapper
import com.example.visioglobe.domain.model.Furniture
import com.example.visioglobe.domain.model.Incident
import com.example.visioglobe.network.entity.IncidentEntity
import com.google.firebase.Timestamp
import java.util.*

class IncidentNetworkMapper : EntityMapper<IncidentEntity, Incident> {
    override fun mapToDomainModel(entity: IncidentEntity): Incident {
        return Incident(
            reporterName = entity.lastName,
            reporterFirstname = entity.firstName,
            email = entity.email,
            phoneNumber = entity.phoneNumber,
            site = entity.siteName,
            location = entity.location,
            time = entity.date.toDate().time,
            title = entity.title,
            description = entity.description,
            furniture = Furniture(entity.incidentType),
            imageId = entity.imageId
        )
    }

    override fun mapToEntity(model: Incident): IncidentEntity {
        return IncidentEntity(
            date = Timestamp(Date(model.time)),
            description = model.description,
            email = model.email,
            firstName = model.reporterFirstname,
            incidentType = model.furniture.name,
            lastName = model.reporterName,
            location = model.location,
            phoneNumber = model.phoneNumber,
            siteId = "",
            siteName = model.site,
            title = model.title,
            imageId = model.imageId
        )
    }
}