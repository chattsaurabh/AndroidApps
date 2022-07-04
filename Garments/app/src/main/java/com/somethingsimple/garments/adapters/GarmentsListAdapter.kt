package com.somethingsimple.garments.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.somethingsimple.garments.R
import com.somethingsimple.garments.models.Garment


class GarmentsListAdapter(
    private var mData: ArrayList<Garment>
    ): RecyclerView.Adapter<GarmentsListAdapter.GarmentRowViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GarmentRowViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return GarmentRowViewHolder(inflater, parent)
    }

    override fun onBindViewHolder(holder: GarmentRowViewHolder, position: Int) {
        val garment = mData[position]
        holder.bind(garment)
    }

    override fun getItemCount(): Int {
        return mData.size
    }

    fun updateData(garments: ArrayList<Garment>) {
        mData.clear()
        mData.addAll(garments.clone() as Collection<Garment>)
    }

    class GarmentRowViewHolder(inflater: LayoutInflater, parent: ViewGroup) :
        RecyclerView.ViewHolder(inflater.inflate(R.layout.recyclerview_row, parent, false)) {
        private var mGarmentNameView: TextView? = null

        init {
            mGarmentNameView = itemView.findViewById(R.id.garment_row_name)
        }

        fun bind(garment: Garment) {
            mGarmentNameView?.text = garment.name
        }

    }

}