package com.kobo.demo.challenge.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import com.kobo.demo.challenge.R
import com.kobo.demo.challenge.models.HomeDataModel
import kotlinx.android.synthetic.main.home_list_item.view.*

class HomeListAdapter(
    val context: Context,
    val usersList: List<String>
) : BaseAdapter() {

    private val inflater: LayoutInflater
            = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater

    override fun getView(position: Int, p1: View?, parent: ViewGroup?): View {
        val rowView = inflater.inflate(R.layout.home_list_item, parent, false)
        rowView.userIndex.setText("User : "+position)
        return rowView
    }

    override fun getItem(position: Int): Any {
        return usersList[position]
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getCount(): Int {
        return usersList.size
    }


}