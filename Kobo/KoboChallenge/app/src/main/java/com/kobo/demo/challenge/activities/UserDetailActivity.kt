package com.kobo.demo.challenge.activities

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.RelativeLayout
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.kobo.demo.challenge.R
import com.kobo.demo.challenge.adapters.UserDetailsListAdapter
import com.kobo.demo.challenge.models.UserDetailsModel
import com.kobo.demo.challenge.viewmodels.UserDetailsViewModel
import kotlinx.android.synthetic.main.activity_user_details.*
import java.util.*


class UserDetailActivity: AppCompatActivity() {

    companion object {
        val USER_URL = "user_url"
    }

    var progress: ProgressBar? = null
    var adapter: UserDetailsListAdapter? = null
    var usersList: MutableList<UserDetailsModel>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_details)
        initProgress()
        registerObservers()
    }

    private fun initProgress() {
        progress = ProgressBar(this)
        val params = RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        params.addRule(RelativeLayout.CENTER_IN_PARENT);
        progress?.layoutParams = params

        progress?.isIndeterminate = true
        user_details_root_relative_layout.addView(progress)
    }

    private fun registerObservers() {
        val url = intent.getStringExtra(USER_URL)

        val viewModel = ViewModelProvider(this).get(UserDetailsViewModel::class.java)
        viewModel.fetchUserDetails(this, url)

        viewModel.progressLiveData.observe(this, Observer {
            progress?.visibility = View.VISIBLE
        })

        viewModel.homeUsersLiveData.observe(this, Observer { model->
            progress?.visibility = View.INVISIBLE
            usersList = model.toMutableList()
            adapter = UserDetailsListAdapter(model.toMutableList())

            user_details_list_view.apply {
                layoutManager = LinearLayoutManager(this@UserDetailActivity)
                adapter = this@UserDetailActivity.adapter
            }

        })
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.menu_options, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.getItemId()) {
            R.id.sort_asc -> {
                adapter?.sortAsc()
            }
            R.id.sort_desc -> {
                adapter?.sortDesc()
            }
            R.id.filter_avatar -> {
                adapter?.filterAvatar()
            }
            R.id.filter_no_avatar -> {
                adapter?.filterNoAvatar()
            }
        }
        return true
    }
}