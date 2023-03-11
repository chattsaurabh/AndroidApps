package ca.richmond.guidomia.adapters

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RatingBar
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import ca.richmond.guidomia.R

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
                    rowData.icon?.let {
                        holder.carImage.setImageResource(it)
                    }
                    rowData.guidomiaCarInfo?.let {
                        holder.name.text = it.make + " " +it.model
                        holder.price.text = StringBuilder()
                            .append(context?.getText(R.string.price_prefix))
                            .append(it.marketPrice).toString()
                        holder.rating.rating = (it.rating?.toFloat() ?: 5.0) as Float
                    }
                }
            }
        }
    }

    override fun getItemCount(): Int {
        return data.size
    }
}