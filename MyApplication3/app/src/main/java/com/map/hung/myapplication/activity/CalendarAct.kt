package com.map.hung.myapplication.activity

import android.content.Intent
import android.os.Bundle
import android.widget.TextView
import androidx.fragment.app.FragmentActivity
import androidx.viewpager.widget.ViewPager
import com.map.hung.myapplication.R
import com.map.hung.myapplication.adapter.ViewPagerAdapter
import java.text.SimpleDateFormat
import java.util.*

class CalendarAct : FragmentActivity() {

    private lateinit var viewPager: ViewPager
    private lateinit var currentMonthTextView: TextView
    private val calendar = Calendar.getInstance()
    private lateinit var backBtn: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.calendar)

        viewPager = findViewById(R.id.calendar_view_pager)
        currentMonthTextView = findViewById(R.id.current_month)
        backBtn = findViewById(R.id.back_Btn)

        backBtn.setOnClickListener(){
            back()
        }

        // Set adapter cho ViewPager
        val adapter = ViewPagerAdapter(supportFragmentManager, calendar.get(Calendar.YEAR))
        viewPager.adapter = adapter

        // Set tiêu đề cho tháng hiện tại
        val sdf = SimpleDateFormat("MMMM yyyy", Locale.getDefault())
        currentMonthTextView.text = sdf.format(calendar.time)

        // Cập nhật tháng khi người dùng vuốt
        viewPager.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {}

            override fun onPageSelected(position: Int) {
                calendar.set(Calendar.MONTH, position)
                currentMonthTextView.text = sdf.format(calendar.time)
            }

            override fun onPageScrollStateChanged(state: Int) {}
        })
    }

    fun back(){
        val intent = Intent(this, HomeAct::class.java)
        startActivity(intent)
    }
}
