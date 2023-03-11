package ca.guidomia.adapters

import androidx.annotation.DrawableRes

interface IRecycleELement

data class CarInfo(
    @DrawableRes val icon : Int? = null,
    val guidomiaCarInfo: GuidomiaData? = null
) : IRecycleELement

data class Divider(
    val isDivider: Boolean = true
) : IRecycleELement

