package com.kobo.demo.challenge.adapters

import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.kobo.demo.challenge.R
import com.kobo.demo.challenge.models.UserDetailsModel
import com.squareup.picasso.Picasso
import java.util.*

class UserDetailsListAdapter(
    var usersList: MutableList<UserDetailsModel>
) : RecyclerView.Adapter<UserDetailsViewHolder>() {

    var usersInitialList: MutableList<UserDetailsModel>? = null

     override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserDetailsViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return UserDetailsViewHolder(inflater, parent)
    }

    override fun onBindViewHolder(holder: UserDetailsViewHolder, position: Int) {
        val userDetails: UserDetailsModel = usersList[position]
        holder.bind(userDetails)
    }

    override fun getItemCount(): Int = usersList.size

    fun sortAsc() {
        Collections.sort(usersList, compareBy({it.last_name}))
        notifyDataSetChanged()
    }

    fun sortDesc() {
        Collections.sort(usersList, compareByDescending({it.last_name}))
        notifyDataSetChanged()
    }

    fun filterNoAvatar() {
        synchronized(usersList) {
            usersInitialList = usersList.toMutableList()
            val userListIterator = usersList.iterator()
            while (userListIterator.hasNext()) {
                val userDetail = userListIterator.next()
                if (userDetail.avatar_large == null) {
                    userListIterator.remove()
                }
            }
            notifyDataSetChanged()
        }
    }

    fun filterAvatar() {
        synchronized(usersList) {
            usersInitialList?.let {
                if(it.size > 1) {
                    usersList.clear()
                    usersList = it.toMutableList()
                }
            }
            notifyDataSetChanged()
        }
    }

}

class UserDetailsViewHolder(inflater: LayoutInflater, parent: ViewGroup) :
    RecyclerView.ViewHolder(inflater.inflate(R.layout.user_details_list_item, parent, false)) {
    private var name: TextView? = null
    private var email: TextView? = null
    private var avatar: ImageView? = null


    init {
        name = itemView.findViewById(R.id.name)
        email = itemView.findViewById(R.id.email)
        avatar = itemView.findViewById(R.id.user_avatar)
    }

    fun bind(userDetail: UserDetailsModel) {
        itemView.setBackgroundColor(Color.parseColor("#FFFFFF"))
        userDetail.backgroundColor?.let {
            itemView.setBackgroundColor(Color.parseColor(it))
        }

        name?.text = userDetail.first_name + " "+ userDetail.last_name
        email?.text = userDetail.email
        Picasso.get()
            .load(userDetail.avatar_large)
            .placeholder(R.drawable.ic_person_black_24dp)
            .resize(400, 400)
            .centerCrop()
            .into(avatar)

    }

}