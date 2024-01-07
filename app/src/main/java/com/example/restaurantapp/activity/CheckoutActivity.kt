package com.example.restaurantapp.activity

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.TextView
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.app.ActivityCompat
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.restaurantapp.R
import com.example.restaurantapp.adapter.TableAdapter
import com.example.restaurantapp.meals.ShopMeal
import com.example.restaurantapp.order.Order
import com.example.restaurantapp.order.OrderMeals
import com.example.restaurantapp.reservations.Reservation
import com.example.restaurantapp.reservations.ReservationResponse
import com.example.restaurantapp.reservations.Table
import com.example.restaurantapp.viewModel.CartViewModel
import com.example.restaurantapp.viewModel.MealViewModel
import com.example.restaurantapp.viewModel.PaymentViewModel
import com.example.restaurantapp.viewModel.ReservationViewModel
import com.example.restaurantapp.viewModel.UserViewModel
import com.stripe.android.PaymentConfiguration
import com.stripe.android.paymentsheet.PaymentSheet
import com.stripe.android.paymentsheet.PaymentSheetResult
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.Calendar

class CheckoutActivity : AppCompatActivity() {
    private lateinit var totalPrice : TextView
    private lateinit var emailTextView : TextView
    private lateinit var comment : EditText
    private lateinit var orderButton : Button
    private lateinit var tableRecyclerView : RecyclerView
    private lateinit var chooseTable : Button
    private lateinit var paymentGroup : RadioGroup
    private lateinit var userMealList : ArrayList<ShopMeal>
    private lateinit var cartViewModel : CartViewModel
    private lateinit var reservationViewModel: ReservationViewModel
    private lateinit var paymentViewModel: PaymentViewModel
    private lateinit var adapter: TableAdapter
    private lateinit var user : String
    private lateinit var tables : List<Table>
    private lateinit var order : Order
    private lateinit var reservations : List<Reservation>
    private lateinit var paymentSheet : PaymentSheet
    private var PUBLISH_KEY = "pk_test_51NUCO4CTvIeCfZ48NcnZga4vVwWBjMV21jqsmPWuBgc9i6CSHUQIfC3hIgjBrdOiu5uMosaLlwmEhQzrWPEAdqYZ00NcG5v8jk"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_checkout)

        totalPrice = findViewById(R.id.checkoutTotalPrice)
        emailTextView = findViewById(R.id.checkoutEmailText)
        comment = findViewById(R.id.checkoutCommentsEdit)
        orderButton = findViewById(R.id.orderBtn)
        tableRecyclerView = findViewById(R.id.table_recycler_view)
        chooseTable = findViewById(R.id.chooseTableBtn)
        paymentGroup = findViewById(R.id.paymentMethodGroup)

        paymentSheet = PaymentSheet(this, this::onPaymentSheetResult)

        cartViewModel = ViewModelProvider(this)[CartViewModel::class.java]
        reservationViewModel = ViewModelProvider(this)[ReservationViewModel::class.java]
        paymentViewModel = ViewModelProvider(this)[PaymentViewModel::class.java]

        userMealList = ArrayList()
        reservations = listOf()
        order = Order()

        user = getSharedPreferences("user", Context.MODE_PRIVATE)?.getString("user", null).toString()
        if (user != "Guest"){
            emailTextView.text = "Email: $user"
        }

        adapter = TableAdapter(userMealList)
        tableRecyclerView.layoutManager = LinearLayoutManager(this)
        tableRecyclerView.adapter = adapter

        chooseTable.setOnClickListener {
            tableDialog()
        }
        var paymentStatus = ""
        paymentGroup.setOnCheckedChangeListener { _, checkedId ->
            val radio: RadioButton = findViewById(checkedId)
            paymentStatus = radio.text.toString()
        }

        orderButton.setOnClickListener{
            if (user == "Guest"){
                Toast.makeText(this, "Musisz sie zalogować!", Toast.LENGTH_SHORT).show()
            }else{
                order.userEmail = user
            }
            if (order.tableNumber == 0){
                Toast.makeText(this, "Wybierz stolik", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            if (userMealList.isEmpty()){
                Toast.makeText(this, "Dodaj coś do zamówienia", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            if (paymentStatus == ""){
                Toast.makeText(this, "Wybierz metode płatności", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            var token = application.getSharedPreferences("user", MODE_PRIVATE).getString("token",null)
            val mealsData = userMealList.map { item ->
                OrderMeals(
                    name = item.productName,
                    quantity = item.quantity,
                    price = item.productPrice + " zł"
                )
            }
            order.meals = mealsData
            order.totalPrice = parsePrice(totalPrice.text.toString().substring(15, totalPrice.text.toString().length-3))
            order.userToken = token.toString()
            order.comments = comment.text.toString()

            if (paymentStatus == "Płatność online"){
                PaymentConfiguration.init(applicationContext, PUBLISH_KEY)
                paymentViewModel.doPayment(order)

                return@setOnClickListener
            }
            val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
            val current = LocalDateTime.now().format(formatter)
            /*reservationViewModel.addReservation(Reservation(
                "",
                current,
                order.userEmail,
                order.tableNumber,
                0
            ))*/
            reservationViewModel.getListOfReservations()
            observerReservationList()
            cartViewModel.clearCart()
            finish()
        }
        reservationViewModel.getListOfTables()
        reservationViewModel.getListOfReservations()
        cartViewModel.loadCart()

        observerTables()
        observerReservationList()
        observerList()

    }



    private fun onPaymentSheetResult(paymentSheetResult: PaymentSheetResult) {
        when (paymentSheetResult) {
            is PaymentSheetResult.Canceled -> {
                Toast.makeText(this, "Płatność anulowana", Toast.LENGTH_SHORT).show()
            }
            is PaymentSheetResult.Failed -> {
                Toast.makeText(this, "Płatność nieudana", Toast.LENGTH_SHORT).show()
            }
            is PaymentSheetResult.Completed -> {
                Toast.makeText(this, "Płatność zakończona", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun observerList(){
        cartViewModel.cartMealListLD.observe(this) { t -> ArrayList<ShopMeal>()
            userMealList = t
            adapter.updateData(userMealList)
            setTotalPrice()
            Log.d("MealList", userMealList.size.toString())
        }
    }

    private fun observerTables(){
        reservationViewModel.getListOfTablesLD().observe(this) { t ->
            tables = t.toList()
        }
    }

    private fun observerReservationList() {
        reservationViewModel.getListOfReservationLD().observe(this) { t ->
            reservations = t.toList()

        }
    }

    private fun tableDialog(){
        val dialog = AlertDialog.Builder(this)
        dialog.setTitle("Wybierz stolik")
        val dialogView = layoutInflater.inflate(R.layout.custom_tables_layout, null)
        val tablesLayout = dialogView.findViewById<ConstraintLayout>(R.id.tablesLayout)
        addTables(tablesLayout)
        dialog.setView(dialogView)
        dialog.setNegativeButton("Cofnij") { _, _ ->
            return@setNegativeButton
        }
        dialog.show()
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

    private fun addTables(layout: ConstraintLayout) {

        for (table in tables) {
            val tableButton = Button(this)
            tableButton.text = table.tableNumber.toString()
            tableButton.id = table.tableNumber
            val params = ConstraintLayout.LayoutParams(
                ConstraintLayout.LayoutParams.WRAP_CONTENT,
                ConstraintLayout.LayoutParams.WRAP_CONTENT
            )
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

            if ( table.tableStatus == "Wolny") {
                tableButton.setBackgroundColor(resources.getColor(R.color.green, null))
            } else {
                tableButton.setBackgroundColor(resources.getColor(R.color.red, null))
            }
            val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
            val current = LocalDateTime.now().format(formatter)
            for(reservation in reservations){
                if (reservation.reservationTable == table.tableNumber && reservation.reservationDate == current ){
                    tableButton.setBackgroundColor(resources.getColor(R.color.red, null))
                }
            }

            layout.addView(tableButton, params)
            tableButton.setOnClickListener {
                order.tableNumber = table.tableNumber
                Toast.makeText(this, "Wybrano stolik nr ${table.tableNumber}", Toast.LENGTH_SHORT).show()
            }

        }
    }

    private fun setTotalPrice() {
        if (userMealList.isEmpty()) {
            totalPrice.text = "Łączna cena: 0.00 zł"
            return
        }
        var suma = userMealList.sumOf { parsePrice(it.productPrice) * it.quantity.toDouble() }
        totalPrice.text = "Łączna cena: ${String.format("%.2f", suma)} zł"
    }

    private fun parsePrice(price: String): Double {
        val cleanedPrice = price.replace(",", ".")
        return cleanedPrice.toDoubleOrNull() ?: 0.0
    }
}