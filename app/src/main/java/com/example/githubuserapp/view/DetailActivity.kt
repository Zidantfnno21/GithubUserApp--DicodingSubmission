package com.example.githubuserapp.view

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.annotation.StringRes
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.viewpager2.widget.ViewPager2
import com.bumptech.glide.Glide
import com.example.githubuserapp.R
import com.example.githubuserapp.view.adapter.SectionsPagerAdapter
import com.example.githubuserapp.databinding.ActivityDetailBinding
import com.example.githubuserapp.viewModel.MainViewModel
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator

class DetailActivity : AppCompatActivity() {

    private lateinit var activityDetailBinding : ActivityDetailBinding
    private lateinit var mainViewModel : MainViewModel

    companion object {
        @StringRes
        private val TAB_TITLES = intArrayOf(
            R.string.following ,
            R.string.follower
        )
    }

    override fun onCreate(savedInstanceState : Bundle?) {
        super.onCreate(savedInstanceState)
        activityDetailBinding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(activityDetailBinding.root)

        val username = intent.getStringExtra("username")
        mainViewModel = ViewModelProvider(this,ViewModelProvider.NewInstanceFactory())[MainViewModel::class.java]

        setPagerAdapter(username!!)
        detailUsers(username)

        mainViewModel.isLoading.observe(this){
            isLoading(it)
        }

    }

    override fun onCreateOptionsMenu(menu : Menu) : Boolean {
        menuInflater.inflate(R.menu.detail_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item : MenuItem) : Boolean {
        when (item.itemId) {
            R.id.menu_favorite -> TODO()

        }
        return super.onOptionsItemSelected(item)
    }

    private fun setPagerAdapter(username : String){
        val sectionsPagerAdapter = SectionsPagerAdapter(this)
        val viewPager: ViewPager2 = activityDetailBinding.viewPager
        viewPager.adapter = sectionsPagerAdapter
        val tabs: TabLayout = activityDetailBinding.tabs
        sectionsPagerAdapter.username = username
        TabLayoutMediator(tabs, viewPager) {tab, position ->
            tab.text = resources.getString(TAB_TITLES[position])
        }.attach()

    }

    private fun detailUsers(username : String) {
        mainViewModel.gitHubDetailUser(username)
        mainViewModel.detailDataUsers.observe(this) { DetailUserResponse ->
            activityDetailBinding.tvDetailId.text = DetailUserResponse.name
            activityDetailBinding.tvDetailUsername.text = DetailUserResponse.login
            Glide.with(this)
                .load(DetailUserResponse.avatarUrl)
                .into(activityDetailBinding.ivDetail)
            activityDetailBinding.tvDetailFollowing.text = getString(R.string.number_following , DetailUserResponse.following)
            activityDetailBinding.tvDetailFollower.text = getString(R.string.number_follower , DetailUserResponse.followers)

        }
    }

    private fun isLoading(loading: Boolean) {
        activityDetailBinding.progressBar.visibility = if (loading) View.VISIBLE else View.GONE
    }

}