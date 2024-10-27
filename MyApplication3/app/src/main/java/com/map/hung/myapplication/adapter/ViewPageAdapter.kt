package com.map.hung.myapplication.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import com.map.hung.myapplication.fragment.MonthFragment

class ViewPagerAdapter(fm: FragmentManager, private val year: Int) : FragmentStatePagerAdapter(fm) {

    override fun getCount(): Int {
        return 12 // 12 tháng trong năm
    }

    override fun getItem(position: Int): Fragment {
        return MonthFragment.newInstance(position, year) // Tạo Fragment cho tháng tương ứng
    }

    override fun getPageTitle(position: Int): CharSequence? {
        val months = arrayOf(
            "January", "February", "March", "April", "May", "June",
            "July", "August", "September", "October", "November", "December"
        )
        return months[position]
    }
}
