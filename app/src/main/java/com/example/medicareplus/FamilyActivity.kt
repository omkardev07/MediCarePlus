package com.example.medicareplus

import android.content.Context
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.medicareplus.databinding.ActivityFamilyBinding

class FamilyActivity : AppCompatActivity() {

    private lateinit var binding: ActivityFamilyBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFamilyBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        binding.toolbar.setNavigationOnClickListener {
            finish()
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
        }

        // Load saved family info
        loadSavedData()

        binding.btnSaveFamily.setOnClickListener {
            saveFamilyInfo()
        }
    }

    private fun loadSavedData() {
        val prefs = getSharedPreferences("family_prefs", Context.MODE_PRIVATE)
        binding.etFamilyName1.setText(prefs.getString("family_name_1", ""))
        binding.etFamilyNumber1.setText(prefs.getString("family_number_1", ""))
        binding.etRelation1.setText(prefs.getString("family_relation_1", ""))
        binding.etFamilyName2.setText(prefs.getString("family_name_2", ""))
        binding.etFamilyNumber2.setText(prefs.getString("family_number_2", ""))
        binding.etRelation2.setText(prefs.getString("family_relation_2", ""))
    }

    private fun saveFamilyInfo() {
        val name1 = binding.etFamilyName1.text.toString().trim()
        val number1 = binding.etFamilyNumber1.text.toString().trim()
        val relation1 = binding.etRelation1.text.toString().trim()
        val name2 = binding.etFamilyName2.text.toString().trim()
        val number2 = binding.etFamilyNumber2.text.toString().trim()
        val relation2 = binding.etRelation2.text.toString().trim()

        if (name1.isEmpty() && number1.isEmpty()) {
            Toast.makeText(this, "Please fill at least one family contact", Toast.LENGTH_SHORT).show()
            return
        }

        val prefs = getSharedPreferences("family_prefs", Context.MODE_PRIVATE)
        prefs.edit()
            .putString("family_name_1", name1)
            .putString("family_number_1", number1)
            .putString("family_relation_1", relation1)
            .putString("family_name_2", name2)
            .putString("family_number_2", number2)
            .putString("family_relation_2", relation2)
            .apply()

        Toast.makeText(this, "❤️ Family contacts saved!", Toast.LENGTH_SHORT).show()
        finish()
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
    }
}
