package ca.guidomia.adapters

import android.view.View.OnClickListener
import androidx.annotation.DrawableRes

interface IRecycleELement

data class CarInfo(
    @DrawableRes val icon : Int? = null,
    val guidomiaCarInfo: GuidomiaData? = null,
    val isExpanded: Boolean = false,
    val clickListener: OnClickListener? = null
) : IRecycleELement

data class Divider(
    val isDivider: Boolean = true
) : IRecycleELement

