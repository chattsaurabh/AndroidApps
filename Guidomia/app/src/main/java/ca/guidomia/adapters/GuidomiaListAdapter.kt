package ca.guidomia.adapters

import android.R.color
import android.annotation.SuppressLint
import android.content.Context
import android.text.SpannableString
import android.text.SpannableStringBuilder
import android.text.Spanned
import android.text.style.BulletSpan
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RatingBar
import android.widget.TextView
import androidx.core.text.toSpannable
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import ca.guidomia.R


class GuidomiaListAdapter(
    private val context: Context?
): RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private lateinit var data : List<IRecycleELement>

    fun setData(data : List<IRecycleELement>) {
        this.data = data
    }

    private val inflater = LayoutInflater.from(context)
    private enum class ViewHolderType {
        TYPE_CAR,
        TYPE_DIVIDER
    }

    class CarViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        companion object {
            val layoutId: Int = R.layout.row_car
        }

        val carImage: ImageView = itemView.findViewById(R.id.car_image)
        val name: TextView = itemView.findViewById(R.id.name)
        val price: TextView = itemView.findViewById(R.id.price)
        val rating: RatingBar = itemView.findViewById(R.id.rating)
        val prosTitle: TextView = itemView.findViewById(R.id.pros_title)
        val pros: TextView = itemView.findViewById(R.id.pros_list)
        val consTitle: TextView = itemView.findViewById(R.id.cons_title)
        val cons: TextView = itemView.findViewById(R.id.cons_list)
    }

    class DividerViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        companion object {
            val layoutId: Int = R.layout.row_divider
        }
    }

    override fun getItemViewType(position: Int): Int {
        return when (data[position]) {
            is CarInfo -> ViewHolderType.TYPE_CAR.ordinal
            is Divider -> ViewHolderType.TYPE_DIVIDER.ordinal
            else -> -1
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val viewHolder = when(ViewHolderType.values()[viewType]) {
            ViewHolderType.TYPE_CAR -> {
                val carView = inflater.inflate(CarViewHolder.layoutId, parent, false)
                CarViewHolder(carView)
            }
            ViewHolderType.TYPE_DIVIDER -> {
                val dividerView = inflater.inflate(DividerViewHolder.layoutId, parent, false)
                DividerViewHolder(dividerView)
            }
        }
        return viewHolder
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val rowData = data[position]
        when (holder) {
            is DividerViewHolder -> {
                if(rowData is Divider) {
                    // adding divider if divider data is added.
                }
            }
            is CarViewHolder -> {
                if (rowData is CarInfo) {
                    rowData.clickListener?.let {
                        holder.itemView.setOnClickListener(it)
                    }
                    rowData.icon?.let {
                        holder.carImage.setImageResource(it)
                    }
                    rowData.guidomiaCarInfo?.let {
                        holder.name.text = it.make + " " +it.model
                        holder.price.text = StringBuilder()
                            .append(context?.getText(R.string.price_prefix))
                            .append(it.marketPrice.toK()).toString()
                        holder.rating.rating = (it.rating?.toFloat() ?: 5.0) as Float

                        if(rowData.isExpanded) {
                            buildProsString(it)?.let { pros ->
                                holder.prosTitle.isVisible = true
                                holder.pros.isVisible = true
                                holder.pros.text = pros
                            }
                            buildConsString(it)?.let { cons ->
                                holder.consTitle.isVisible = true
                                holder.cons.isVisible = true
                                holder.cons.text = cons
                            }
                        } else {
                            holder.prosTitle.isVisible = false
                            holder.pros.isVisible = false
                            holder.consTitle.isVisible = false
                            holder.cons.isVisible = false
                        }

                    }
                }
            }
        }
    }

    private fun buildProsString(data: GuidomiaData): SpannableString? {
        if(data.prosList.isNullOrEmpty()){
            return null
        }
        return createSpannableStringList(data.prosList!!)
    }

    private fun buildConsString(data: GuidomiaData): SpannableString? {
        if(data.consList.isNullOrEmpty()){
            return null
        }
        return createSpannableStringList(data.consList!!)
    }

    private fun createSpannableStringList(stringList: List<String?>): SpannableString? {
        var builder = SpannableStringBuilder()
        stringList.forEachIndexed { index, s ->
            if(!s.isNullOrEmpty()) {
                val string = SpannableString(s)
                string.setSpan(
                    context?.getColor(R.color.orange_)?.let { BulletSpan(40, it, 10) },
                    0,
                    string.length,
                    Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
                )
                builder.append(string)
                if (index < stringList.lastIndex) {
                    builder.append("\n")
                }
            }
        }


        return SpannableString(builder)
    }

    override fun getItemCount(): Int {
        return data.size
    }
}

private fun Int?.toK(): String {
    return this?.div(1000).toString() + "k"
}
