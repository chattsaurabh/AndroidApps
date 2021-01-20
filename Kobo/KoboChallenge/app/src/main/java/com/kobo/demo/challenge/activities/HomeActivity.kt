package com.kobo.demo.challenge.activities

import android.app.ProgressDialog
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.ProgressBar
import android.widget.RelativeLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.kobo.demo.challenge.R
import com.kobo.demo.challenge.adapters.HomeListAdapter
import com.kobo.demo.challenge.viewmodels.HomeViewModel
import kotlinx.android.synthetic.main.activity_home.*

class HomeActivity : AppCompatActivity() {
    var progress: ProgressBar? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        progress = ProgressBar(this)
        val params = RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        params.addRule(RelativeLayout.CENTER_IN_PARENT);
        progress?.layoutParams = params

        progress?.isIndeterminate = true
        home_root_relative_layout.addView(progress)


        val viewModel = ViewModelProvider(this).get(HomeViewModel::class.java)
        viewModel.fetchUsers(this)

        viewModel.progressLiveData.observe(this, Observer {
            progress?.visibility = View.VISIBLE
        })

        viewModel.homeUsersLiveData.observe(this, Observer {
            progress?.visibility = View.INVISIBLE

            it.pages?.let { userList ->
                val adapter = HomeListAdapter(this, userList)
                home_list_view.adapter = adapter
                home_list_view.setOnItemClickListener { parent, view, position, id ->
                    val selectedUser = parent.getItemAtPosition(position) as String
                    val intent = Intent(this@HomeActivity, UserDetailActivity::class.java)
                    intent.putExtra(UserDetailActivity.USER_URL, selectedUser)
                    startActivity(intent)
                }
            }

        })



    }
}