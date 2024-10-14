package com.map.hung.myapplication.activity

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast
import com.map.hung.myapplication.IconSpinnerAdapter
import com.map.hung.myapplication.R
import com.map.hung.myapplication.dao.CategoryDao
import com.map.hung.myapplication.model.IconItem

class AddCatAct : Activity() {
    private lateinit var thuChiSpinner: Spinner
    private lateinit var nameEditText: EditText
    private lateinit var iconSpinner: Spinner
    private lateinit var parentSpinner: Spinner
    private lateinit var saveBtn: Button
    private lateinit var resetBtn: Button
    private lateinit var backBtn: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.add_cat)

        thuChiSpinner = findViewById(R.id.thu_chi_spinner)
        nameEditText = findViewById(R.id.name_editText)
        iconSpinner = findViewById(R.id.icon_spinner)
        parentSpinner = findViewById(R.id.parent_spinner)
        saveBtn = findViewById(R.id.btn_Save)
        resetBtn = findViewById(R.id.btn_Reset)
        backBtn = findViewById(R.id.btn_back)

        // thuChiSpinner setup
        val items = listOf("Thu", "Chi")
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, items)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        thuChiSpinner.adapter = adapter

        // Lắng nghe sự kiện khi người dùng thay đổi giá trị của `thuChiSpinner`
        thuChiSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {
                val selectedItem = items[position]
                if (selectedItem == "Thu") {
                    // Gọi hàm xử lý income
                    incomeBtnOnClick()
                } else if (selectedItem == "Chi") {
                    // Gọi hàm xử lý expense
                    expenseBtnOnClick()
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>) {
                // Không làm gì cả
            }
        }

        // Example usage in AddCatAct
        val iconItems = listOf(
            R.drawable.ic_bo_me_cho,
            R.drawable.ic_cho,
            R.drawable.ic_electric,
            R.drawable.ic_giai_tri,
            R.drawable.ic_hang_ngay,
            R.drawable.ic_hieu_hi,
            R.drawable.ic_hoc_phi,
            R.drawable.ic_lam_them,
            R.drawable.ic_salary,
            R.drawable.ic_lam_them,
            R.drawable.ic_scholar,
            R.drawable.ic_nha,
            R.drawable.ic_water,
            R.drawable.ic_phone,
            R.drawable.ic_tien_an
        )

        val nameIcons = listOf(
            "Bố mẹ cho",
            "Cho",
            "Điện",
            "Giải trí",
            "Hàng ngày",
            "Hiếu hỉ",
            "Học phí",
            "Làm thêm",
            "Lương",
            "Làm thêm",
            "Học bổng",
            "Nhà",
            "Nước",
            "Điện thoại",
            "Tiền ăn"
        )

        val iconAdapter = IconSpinnerAdapter(this, iconItems, nameIcons)
        iconSpinner.adapter = iconAdapter



        backBtn.setOnClickListener {
            backOnClick();
        }

        saveBtn.setOnClickListener {
            // Lấy dữ liệu từ các view
            val name = nameEditText.text.toString()
            // icon = name of icon
            // icon = name of icon
            val selectedView = iconSpinner.selectedView
            val iconTextView = selectedView.findViewById<TextView>(R.id.icon_text)
            val icon = iconTextView.text.toString() // This gets the text from the TextView inside the selected spinner item


            val parent = parentSpinner.selectedItem.toString()
            val inOut = thuChiSpinner.selectedItem.toString() == "Thu"

            // Lưu dữ liệu vào database
            val categoryDao = CategoryDao(this)
            categoryDao.insert(name, icon, parent, inOut)
            Toast.makeText(this, "Thêm thành công", Toast.LENGTH_SHORT).show()
        }

        resetBtn.setOnClickListener {
            nameEditText.text.clear()
            iconSpinner.setSelection(0)
            parentSpinner.setSelection(0)
            thuChiSpinner.setSelection(0)
        }
    }

    private fun incomeBtnOnClick() {
        val categoryDao = CategoryDao(this)
        val items = categoryDao.searchByInOut(true) // True là income (Thu)
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, items)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        parentSpinner.adapter = adapter
    }

    private fun expenseBtnOnClick() {
        val categoryDao = CategoryDao(this)
        val items = categoryDao.searchByInOut(false) // False là expense (Chi)
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, items)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        parentSpinner.adapter = adapter
    }

    private fun backOnClick() {
        val intent = Intent(this, AddAct::class.java)
        startActivity(intent)
    }
}
