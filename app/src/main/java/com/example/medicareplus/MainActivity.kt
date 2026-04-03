package com.example.medicareplus

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.animation.DecelerateInterpolator
import android.view.animation.OvershootInterpolator
import android.widget.PopupMenu
import android.widget.TextView
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.medicareplus.adapter.MedicineAdapter
import com.example.medicareplus.data.Medicine
import com.example.medicareplus.viewmodel.MedicineViewModel
import com.google.android.material.card.MaterialCardView

class MainActivity : AppCompatActivity() {

    private val viewModel: MedicineViewModel by viewModels()
    private lateinit var adapter: MedicineAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val tvWelcome = findViewById<TextView>(R.id.tvWelcome)
        val tvUserName = findViewById<TextView>(R.id.tvUserName)
        val tvHealthTip = findViewById<TextView>(R.id.tvHealthTip)
        val tvMedicineCount = findViewById<TextView>(R.id.tvMedicineCount)
        val rvMedicines = findViewById<RecyclerView>(R.id.rvMedicines)
        val emptyState = findViewById<LinearLayout>(R.id.emptyState)
        val ivLogout = findViewById<ImageView>(R.id.ivLogout)

        // Feature cards
        val cardAddMedicine = findViewById<MaterialCardView>(R.id.cardAddMedicine)
        val cardHospital = findViewById<MaterialCardView>(R.id.cardHospital)
        val cardFamily = findViewById<MaterialCardView>(R.id.cardFamily)
        val cardHistory = findViewById<MaterialCardView>(R.id.cardHistory)

        // ========== USER GREETING ==========
        val prefs = getSharedPreferences("user_prefs", Context.MODE_PRIVATE)
        val username = prefs.getString("logged_in_user", "User") ?: "User"
        val fullName = prefs.getString("${username}_fullname", username) ?: username

        // Capitalize first name
        val displayName = fullName.split(" ").firstOrNull()?.replaceFirstChar { it.uppercase() } ?: fullName
        tvUserName.text = "$displayName 👋"

        // ========== RANDOM HEALTH TIP ==========
        val healthTips = resources.getStringArray(R.array.health_tips)
        tvHealthTip.text = healthTips.random()

        // ========== ANIMATIONS ==========
        animateHomeScreen(tvWelcome, tvUserName, tvHealthTip, cardAddMedicine, cardHospital, cardFamily, cardHistory)

        // ========== MEDICINE LIST ==========
        setupRecyclerView(rvMedicines)

        viewModel.allMedicines.observe(this) { medicines ->
            adapter.submitList(medicines)
            tvMedicineCount.text = "${medicines.size} active"

            if (medicines.isEmpty()) {
                emptyState.visibility = View.VISIBLE
                rvMedicines.visibility = View.GONE
            } else {
                emptyState.visibility = View.GONE
                rvMedicines.visibility = View.VISIBLE
            }
        }

        // ========== FEATURE CARD CLICKS ==========
        cardAddMedicine.setOnClickListener {
            startActivity(Intent(this, AddMedicineActivity::class.java))
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
        }

        cardHospital.setOnClickListener {
            startActivity(Intent(this, HospitalActivity::class.java))
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
        }

        cardFamily.setOnClickListener {
            startActivity(Intent(this, FamilyActivity::class.java))
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
        }

        cardHistory.setOnClickListener {
            startActivity(Intent(this, HistoryActivity::class.java))
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
        }

        // ========== LOGOUT ==========
        ivLogout.setOnClickListener {
            AlertDialog.Builder(this)
                .setTitle("Logout")
                .setMessage("Are you sure you want to logout?")
                .setPositiveButton("Logout") { _, _ ->
                    prefs.edit()
                        .putBoolean("keep_logged_in", false)
                        .remove("logged_in_user")
                        .apply()
                    startActivity(Intent(this, LoginActivity::class.java))
                    overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
                    finish()
                }
                .setNegativeButton("Cancel", null)
                .show()
        }
    }

    private fun animateHomeScreen(vararg views: View) {
        // Welcome card slide in from top
        val welcomeParent = views[0].parent?.parent as? View
        welcomeParent?.let {
            it.translationY = -60f
            it.alpha = 0f
            it.animate().translationY(0f).alpha(1f).setDuration(600)
                .setInterpolator(DecelerateInterpolator()).start()
        }

        // Feature cards scale in with stagger
        val cards = views.drop(3) // cardAddMedicine, cardHospital, cardFamily, cardHistory
        cards.forEachIndexed { index, view ->
            view.scaleX = 0.8f
            view.scaleY = 0.8f
            view.alpha = 0f
            view.animate()
                .scaleX(1f).scaleY(1f).alpha(1f)
                .setDuration(500)
                .setStartDelay(300L + (index * 100L))
                .setInterpolator(OvershootInterpolator(1.2f))
                .start()
        }
    }

    private fun setupRecyclerView(rv: RecyclerView) {
        adapter = MedicineAdapter { medicine ->
            showPopupMenu(medicine)
        }
        rv.layoutManager = LinearLayoutManager(this)
        rv.adapter = adapter
    }

    private fun showPopupMenu(medicine: Medicine) {
        val rv = findViewById<RecyclerView>(R.id.rvMedicines)
        val viewHolder = rv.findViewHolderForAdapterPosition(adapter.currentList.indexOf(medicine))
        val view = viewHolder?.itemView?.findViewById<View>(R.id.ivMoreActions)

        val popup = PopupMenu(this, view ?: rv)
        popup.menuInflater.inflate(R.menu.menu_medicine_options, popup.menu)
        popup.setOnMenuItemClickListener { item ->
            when (item.itemId) {
                R.id.action_edit -> {
                    val intent = Intent(this, AddMedicineActivity::class.java).apply {
                        putExtra("MEDICINE_ID", medicine.id)
                    }
                    startActivity(intent)
                    true
                }
                R.id.action_delete -> {
                    AlertDialog.Builder(this)
                        .setTitle("Delete Medicine")
                        .setMessage("Remove ${medicine.name} from your list?")
                        .setPositiveButton("Delete") { _, _ -> viewModel.delete(medicine) }
                        .setNegativeButton("Cancel", null)
                        .show()
                    true
                }
                else -> false
            }
        }
        popup.show()
    }
}
