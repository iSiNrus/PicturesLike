package ru.barsik.pictureslike.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.FragmentManager
import com.google.android.material.tabs.TabLayout
import ru.barsik.pictureslike.R
import ru.barsik.pictureslike.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val fragManager: FragmentManager = supportFragmentManager
    private lateinit var randomPicsFrag: RandomPicsFrag
    private lateinit var likePicsFrag: LikePicsFrag

    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityMainBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        randomPicsFrag = RandomPicsFrag.newInstance(application)
        likePicsFrag = LikePicsFrag.newInstance(application)

        fragManager.beginTransaction().replace(binding.mainContent.id, randomPicsFrag).commit()

        initViews()
    }

    private fun initViews(){
        binding.tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {

            override fun onTabSelected(tab: TabLayout.Tab?) {
                fragManager.beginTransaction().replace(binding.mainContent.id,
                    if(binding.tabLayout.selectedTabPosition==0) randomPicsFrag else likePicsFrag).commit()
            }

            override fun onTabReselected(tab: TabLayout.Tab?) {
                // Handle tab reselect
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {
                // Handle tab unselect
            }
        })
    }
}