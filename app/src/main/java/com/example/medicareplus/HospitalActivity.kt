package com.example.medicareplus

import android.content.Context
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.medicareplus.databinding.ActivityHospitalBinding

class HospitalActivity : AppCompatActivity() {

    private lateinit var binding: ActivityHospitalBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHospitalBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        binding.toolbar.setNavigationOnClickListener {
            finish()
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
        }

        // Load saved hospital info
        loadSavedData()

        binding.btnSaveHospital.setOnClickListener {
            saveHospitalInfo()
        }
    }

    private fun loadSavedData() {
        val prefs = getSharedPreferences("hospital_prefs", Context.MODE_PRIVATE)
        binding.etHospitalName.setText(prefs.getString("hospital_name", ""))
        binding.etHospitalBranch.setText(prefs.getString("hospital_branch", ""))
        binding.etHospitalNumber.setText(prefs.getString("hospital_number", ""))
        binding.etDoctorName.setText(prefs.getString("doctor_name", ""))
        binding.etNotes.setText(prefs.getString("hospital_notes", ""))
    }

    private fun saveHospitalInfo() {
        val hospitalName = binding.etHospitalName.text.toString().trim()
        val hospitalBranch = binding.etHospitalBranch.text.toString().trim()
        val hospitalNumber = binding.etHospitalNumber.text.toString().trim()
        val doctorName = binding.etDoctorName.text.toString().trim()
        val notes = binding.etNotes.text.toString().trim()

        if (hospitalName.isEmpty()) {
            Toast.makeText(this, "Please enter hospital name", Toast.LENGTH_SHORT).show()
            return
        }

        val prefs = getSharedPreferences("hospital_prefs", Context.MODE_PRIVATE)
        prefs.edit()
            .putString("hospital_name", hospitalName)
            .putString("hospital_branch", hospitalBranch)
            .putString("hospital_number", hospitalNumber)
            .putString("doctor_name", doctorName)
            .putString("hospital_notes", notes)
            .apply()

        Toast.makeText(this, "🏥 Hospital info saved!", Toast.LENGTH_SHORT).show()
        finish()
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
    }
}
