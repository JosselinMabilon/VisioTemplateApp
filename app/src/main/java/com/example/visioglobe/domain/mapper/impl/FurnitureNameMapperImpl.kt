package com.example.visioglobe.domain.mapper.impl

import com.example.visioglobe.R
import com.example.visioglobe.domain.mapper.FurnitureNameMapper

class FurnitureNameMapperImpl : FurnitureNameMapper {

    override fun mapFurnitureNameToResId(name: String): Int {
        return when (name) {
            "ArmChair" -> R.string.furniture_armchair
            "Cabinet" -> R.string.furniture_cabinet
            "Chair" -> R.string.furniture_chair
            "CoffeeMachine" -> R.string.furniture_coffee_machine
            "ComputerScreen" -> R.string.furniture_computer_screen
            "Elevator" -> R.string.furniture_elevator
            "Extinguisher" -> R.string.furniture_extinguisher
            "HandTowel" -> R.string.furniture_hand_towel
            "HighChair" -> R.string.furniture_high_chair
            "HydroalcoholicGelDispenser" -> R.string.furniture_hydroalcoholic_gel_dispenser
            "Lamp" -> R.string.furniture_lamp
            "Locker" -> R.string.furniture_locker
            "LowTable" -> R.string.furniture_low_table
            "Microwave" -> R.string.furniture_microwave
            "Phone" -> R.string.furniture_phone
            "Shower" -> R.string.furniture_shower
            "Sink" -> R.string.furniture_sink
            "Table" -> R.string.furniture_table
            "Tablet" -> R.string.furniture_tablet
            "Trash" -> R.string.furniture_trash
            "TV" -> R.string.furniture_tv
            "WaterFountain" -> R.string.furniture_water_fountain
            "WC" -> R.string.furniture_wc
            "WheelChair" -> R.string.furniture_wheel_chair
            else -> R.string.string_other
        }
    }
}