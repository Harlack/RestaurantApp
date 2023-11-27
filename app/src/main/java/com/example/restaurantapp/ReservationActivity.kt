package com.example.restaurantapp

import android.app.Dialog
import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.ViewGroup
import android.widget.DatePicker
import android.widget.EditText
import android.widget.TextView
import android.widget.TimePicker
import android.widget.Toast
import com.example.restaurantapp.databinding.ActivityReservationBinding
import com.example.restaurantapp.reservations.Reservation
import com.example.restaurantapp.user.User
import com.example.restaurantapp.viewModel.ReservationViewModel
import java.time.LocalDate
import java.time.LocalDateTime
import java.util.Calendar

class ReservationActivity : AppCompatActivity() {
    private lateinit var binding: ActivityReservationBinding
    private lateinit var reservationViewModel: ReservationViewModel
    private lateinit var listOfReservations: List<Reservation>
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_reservation)
        binding = ActivityReservationBinding.inflate(layoutInflater)
        setContentView(binding.root)
        reservationViewModel = ReservationViewModel(application)
        val user = intent.getStringExtra("user")
        reservationViewModel.getListOfReservations()
        observerReservationList()
        val current = LocalDateTime.now()
        binding.datePicker.updateDate(current.year, current.monthValue, current.dayOfMonth)
        binding.timePicker.hour.rangeUntil(12)



        val tableNumber = intent.getIntExtra("tableNumber", 0)
        findViewById<TextView>(R.id.emailText).text = "Email: $user"
        binding.tableText.text = "Numer stolika: $tableNumber"
        binding.dateText.text = "Data: " + binding.datePicker.year + "-" + binding.datePicker.month + "-" + binding.datePicker.dayOfMonth
        binding.timeText.text = "Godzina: " + String.format("%02d:%02d", binding.timePicker.hour, binding.timePicker.minute)
        binding.datePicker.setOnDateChangedListener { _, year, monthOfYear, dayOfMonth ->
            binding.dateText.text = "Data: $year-$monthOfYear-$dayOfMonth"
        }
        binding.timePicker.setOnTimeChangedListener { _, hourOfDay, minute ->
            binding.timeText.text = "Godzina: " + String.format("%02d:%02d", hourOfDay, minute)
        }
        binding.acceptBtn.setOnClickListener {
            val reservation = Reservation(
                binding.commentEdit.text.toString(),
                "" + binding.datePicker.year + "-" + binding.datePicker.month + "-" + binding.datePicker.dayOfMonth,
                user!!,
                tableNumber,
                binding.timePicker.hour
            )
            for (r in listOfReservations) {
                if (r.reservationTable == reservation.reservationTable && r.reservationDate == reservation.reservationDate && r.reservationTime == reservation.reservationTime) {
                    Toast.makeText(this, "Stolik jest zajęty", Toast.LENGTH_SHORT).show()
                    return@setOnClickListener
                }
            }
            reservationViewModel.addReservation(reservation)
            reservationViewModel.getListOfReservations()
            observerReservationList()
            finish()
            Toast.makeText(this, "Rezerwacja została dodana", Toast.LENGTH_SHORT).show()
        }
    binding.declineBtn.setOnClickListener {
            finish()
        }

    }

    private fun observerReservationList() {
        reservationViewModel.getListOfReservationLD().observe(this) { value ->
            listOfReservations = value
            Log.d("listOfReservations", listOfReservations.toString())
        }
    }

}