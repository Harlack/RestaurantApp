package com.example.restaurantapp.fragment

import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import com.example.restaurantapp.R
import com.example.restaurantapp.databinding.FragmentReservationBinding
import com.example.restaurantapp.reservations.Reservation
import com.example.restaurantapp.reservations.Table
import com.example.restaurantapp.user.User
import com.example.restaurantapp.viewModel.ReservationViewModel



class ReservationFragment : Fragment() {

    private lateinit var binding: FragmentReservationBinding
    private lateinit var reservationViewModel: ReservationViewModel
    private lateinit var listOfReservations: List<Reservation>
    private lateinit var userData: User
    private lateinit var user: String
    private lateinit var tables: List<Table>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        reservationViewModel = ReservationViewModel(requireActivity().application)
        reservationViewModel.getListOfReservations()
        reservationViewModel.getListOfTables()
        userData = User()
        tables = listOf()
        user = activity?.getSharedPreferences("user", Context.MODE_PRIVATE)?.getString("user", null).toString()
        if (user != "Guest"){
            reservationViewModel.getUserData(user)
            Log.d("userData",userData.toString())
        }else{
            userData.data.email = "Guest"
        }
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentReservationBinding.inflate(inflater, container, false)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.datePicker.minDate = System.currentTimeMillis() - 1000
        binding.timePicker.hour.rangeUntil(12)
        observerReservationList()
        observerUserData()
        observerTables()
    }
    private fun observerUserData() {
        reservationViewModel.getUserDataLD().observe(viewLifecycleOwner) { t ->
            userData.data = t
            Log.d("UserData",t.toString())
        }
    }


    private fun observerReservationList() {
        reservationViewModel.getListOfReservationLD().observe(viewLifecycleOwner) { t ->
            listOfReservations = t.toList()
            Log.d("ReservationList",listOfReservations.toString())
        }
    }

    private fun observerTables(){
        reservationViewModel.getListOfTablesLD().observe(viewLifecycleOwner) { t ->
            tables = t.toList()
            addTables()
            Log.d("TableList",tables.toString())
        }
    }

    private fun convertCoordinatesToDP(x: Float, y: Float): Pair<Float, Float> {
        val density = resources.displayMetrics.density
        val screenWidthInDP = 394f
        val screenHeightInDP = 230f

        // Przelicz współrzędne na dp
        val xInDP = x / density / screenWidthInDP * 1920
        val yInDP = y / density / screenHeightInDP * 1080

        return Pair(xInDP, yInDP)
    }

    private fun addTables() {
        for (table in tables) {
            val tableButton = Button(requireContext())
            tableButton.text = table.tableNumber.toString()
            tableButton.id = View.generateViewId()
            val params = ConstraintLayout.LayoutParams(
                ConstraintLayout.LayoutParams.WRAP_CONTENT,
                ConstraintLayout.LayoutParams.WRAP_CONTENT
            )
            if (table.tableStatus == "Wolny") {
                tableButton.setBackgroundColor(resources.getColor(R.color.green,null))
            } else {
                tableButton.setBackgroundColor(resources.getColor(R.color.red,null))
            }
            if (table.tableNumber < 10) {
                params.width = 70
            } else {
                params.width = 100
            }
            params.height = 100
            val convertedCoordinates = convertCoordinatesToDP(table.x.toFloat(), table.y.toFloat())
            val xInDP = convertedCoordinates.first
            val yInDP = convertedCoordinates.second
            params.startToStart = ConstraintLayout.LayoutParams.PARENT_ID
            params.topToTop = ConstraintLayout.LayoutParams.PARENT_ID
            params.leftMargin = xInDP.toInt()
            params.topMargin = yInDP.toInt() - 50
            binding.tablesLayout.addView(tableButton, params)

            tableButton.setOnClickListener {
                if (userData.data.email == "Guest") {
                    dialogForEmail()
                    return@setOnClickListener
                }

                val dialog = Dialog(requireContext())
                dialog.window?.setLayout(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT
                )

                dialog.setContentView(R.layout.reservation_layout)
                val tableNumber = dialog.findViewById<TextView>(R.id.tableText)
                val tableDate = dialog.findViewById<TextView>(R.id.dateText)
                val tableTime = dialog.findViewById<TextView>(R.id.timeText)
                dialog.findViewById<TextView>(R.id.emailText).text = "Email: " + userData.data.email
                tableNumber.text = "Numer stolika: " + table.tableNumber.toString()
                tableDate.text = "Data: " + binding.datePicker.year.toString() + "-" + binding.datePicker.month.toString() + "-" + binding.datePicker.dayOfMonth.toString()
                tableTime.text = "Godzina: " + binding.timePicker.hour.toString() + ":" + binding.timePicker.minute.toString()
                dialog.findViewById<TextView>(R.id.declineBtn).setOnClickListener {
                    dialog.dismiss()
                }
                dialog.findViewById<TextView>(R.id.acceptBtn).setOnClickListener {
                    val reservation = Reservation(
                            dialog.findViewById<EditText>(R.id.commentEdit).text.toString(),
                            binding.datePicker.year.toString() + "-" + binding.datePicker.month.toString() + "-" + binding.datePicker.dayOfMonth.toString(),
                            userData.data.email,
                            table.tableNumber,
                            binding.timePicker.hour
                    )
                    for (r in listOfReservations) {
                        if (r.reservationTable == reservation.reservationTable && r.reservationDate == reservation.reservationDate && r.reservationTime == reservation.reservationTime) {
                            Toast.makeText(context, "Stolik jest zajęty", Toast.LENGTH_SHORT).show()
                            return@setOnClickListener
                        }
                    }
                    reservationViewModel.addReservation(reservation)
                    reservationViewModel.getListOfReservationLD()
                    Toast.makeText(context, "Rezerwacja została dodana", Toast.LENGTH_SHORT).show()
                    dialog.dismiss()
                }

                dialog.show()
            }

        }
    }



    private fun dialogForEmail() {
        val alertDialog = AlertDialog.Builder(requireContext())
        alertDialog.setTitle("Podaj email")
        val input = EditText(requireContext())
        val lp = ViewGroup.LayoutParams(
            ViewGroup.LayoutParams.WRAP_CONTENT,
            ViewGroup.LayoutParams.MATCH_PARENT
        )

        input.layoutParams = lp
        alertDialog.setView(input)
        alertDialog.setPositiveButton("OK") { _, _ ->
            if (input.text.toString().isEmpty()|| input.text.contains("@").not()) {
                Toast.makeText(context, "Błędny email", Toast.LENGTH_SHORT).show()
            }else {
                userData.data.email = input.text.toString()
                Toast.makeText(context, "Email został dodany", Toast.LENGTH_SHORT).show()
            }
        }
        alertDialog.show()
    }


}