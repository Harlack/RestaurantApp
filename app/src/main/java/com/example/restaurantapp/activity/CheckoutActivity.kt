package com.example.restaurantapp.activity

import android.app.AlertDialog
import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.TextView
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.restaurantapp.R
import com.example.restaurantapp.adapter.TableAdapter
import com.example.restaurantapp.meals.ShopMeal
import com.example.restaurantapp.meals.Order
import com.example.restaurantapp.meals.OrderMeals
import com.example.restaurantapp.meals.OrderResponse
import com.example.restaurantapp.reservations.Reservation
import com.example.restaurantapp.reservations.Table
import com.example.restaurantapp.viewModel.CartViewModel
import com.example.restaurantapp.viewModel.OrderViewModel
import com.example.restaurantapp.viewModel.ReservationViewModel
import com.stripe.android.PaymentConfiguration
import com.stripe.android.paymentsheet.PaymentSheet
import com.stripe.android.paymentsheet.PaymentSheetResult
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class CheckoutActivity : AppCompatActivity() {
    private lateinit var totalPrice : TextView
    private lateinit var emailTextView : TextView
    private lateinit var emailEditText: EditText
    private lateinit var comment : EditText
    private lateinit var backButton : ImageView
    private lateinit var orderButton : Button
    private lateinit var tableRecyclerView : RecyclerView
    private lateinit var chooseTable : Button
    private lateinit var paymentGroup : RadioGroup
    private lateinit var userMealList : ArrayList<ShopMeal>
    private lateinit var cartViewModel : CartViewModel
    private lateinit var reservationViewModel: ReservationViewModel
    private lateinit var orderViewModel: OrderViewModel
    private lateinit var adapter: TableAdapter
    private lateinit var user : String
    private lateinit var tables : List<Table>
    private lateinit var order : Order
    private lateinit var reservations : List<Reservation>
    private lateinit var paymentSheet: PaymentSheet
    private lateinit var customerConfig: PaymentSheet.CustomerConfiguration
    private lateinit var paymentIntentClientSecret: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.the_payment_activity_checkout)

        totalPrice = findViewById(R.id.checkoutTotalPrice)
        emailTextView = findViewById(R.id.checkoutEmailText)
        emailEditText = findViewById(R.id.checkoutEmailEdit)
        comment = findViewById(R.id.checkoutCommentsEdit)
        backButton = findViewById(R.id.checkoutBackBtn)
        orderButton = findViewById(R.id.orderBtn)
        tableRecyclerView = findViewById(R.id.table_recycler_view)
        chooseTable = findViewById(R.id.chooseTableBtn)
        paymentGroup = findViewById(R.id.paymentMethodGroup)

        paymentSheet = PaymentSheet(this, ::onPaymentSheetResult)

        cartViewModel = ViewModelProvider(this)[CartViewModel::class.java]
        reservationViewModel = ViewModelProvider(this)[ReservationViewModel::class.java]
        orderViewModel = ViewModelProvider(this)[OrderViewModel::class.java]

        userMealList = ArrayList()
        reservations = listOf()
        order = Order()

        var paymentStatus = ""

        user = getSharedPreferences("user", Context.MODE_PRIVATE)?.getString("user", null).toString()
        if (user != "Guest"){
            emailTextView.text = "Email: $user"
            emailEditText.visibility = View.GONE
            order.userEmail = user
        }

        adapter = TableAdapter(userMealList)
        tableRecyclerView.layoutManager = LinearLayoutManager(this)
        tableRecyclerView.adapter = adapter

        chooseTable.setOnClickListener {
            tableDialog()
        }

        paymentGroup.setOnCheckedChangeListener { _, checkedId ->
            val radio: RadioButton = findViewById(checkedId)
            paymentStatus = radio.text.toString()
            if (paymentStatus == "Płatność online" && user == "Guest"){
                Toast.makeText(this, "Musisz się zalogować, aby zapłacić kartą", Toast.LENGTH_SHORT).show()
                return@setOnCheckedChangeListener
            }
        }

        backButton.setOnClickListener{
            onBackPressedDispatcher.onBackPressed()
        }
        orderButton.setOnClickListener{
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

            val mealsData = userMealList.map { item ->
                OrderMeals(
                    name = item.productName,
                    quantity = item.quantity,
                    price = item.productPrice + " zł"
                )
            }
            order.meals = mealsData
            order.totalPrice = totalPrice.text.toString().replace("Łączna cena: ", "").replace(" zł", "").toDouble()
            order.comments = comment.text.toString()

            if (paymentStatus == "Płatność online" && user != "Guest"){
                var token = application.getSharedPreferences("user", MODE_PRIVATE).getString("token",null)
                order.userToken = token.toString()
                paymentOnline()
            }else{
                val token = "Użytkownik niezalogowany"
                order.userEmail = emailEditText.text.toString()
                order.userToken = token
                order.paymentStatus = "Płatność przy odbiorze"
                orderViewModel.makeOrder(order)
                cartViewModel.clearCart()
                checkoutDialog("Zamówienie zostało złożone. Proszę zapłacić przy odbiorze")
            }
        }
        reservationViewModel.getListOfTables()
        reservationViewModel.getListOfReservations()
        cartViewModel.loadCart()

        observerTables()
        observerReservationList()
        observerList()

    }

    override fun onSupportNavigateUp(): Boolean {
        finish()
        return true
    }

    private fun onPaymentSheetResult(paymentSheetResult: PaymentSheetResult) {
        when (paymentSheetResult) {
            is PaymentSheetResult.Canceled -> {
                Toast.makeText(this, "Płatność anulowana", Toast.LENGTH_SHORT).show()
                finish()
            }
            is PaymentSheetResult.Failed -> {
                Toast.makeText(this, "Płatność nieudana, proszę spróbować ponownie", Toast.LENGTH_SHORT).show()
            }
            is PaymentSheetResult.Completed -> {
                Toast.makeText(this, "Płatność zakończona", Toast.LENGTH_SHORT).show()
                cartViewModel.clearCart()
                checkoutDialog("Zamówienie zostało złożone")
            }
        }
    }
    private fun paymentSheet() {
        paymentSheet.presentWithPaymentIntent(
            paymentIntentClientSecret,
            PaymentSheet.Configuration(
                merchantDisplayName = "RestaurantApp",
                customer = customerConfig,
                allowsDelayedPaymentMethods = true
            )
        )
    }
    private fun paymentOnline(){
        try {
            orderViewModel.paymentSheet(order)
            var response: OrderResponse
            orderViewModel.getResponse().observe(this) { r ->
                response = r
                paymentIntentClientSecret = response.paymentIntent
                customerConfig = PaymentSheet.CustomerConfiguration(
                    response.customer,
                    response.ephemeralKey
                )
                PaymentConfiguration.init(this, response.publishableKey)
                paymentSheet()
            }
        }catch (e: Exception) {
            Toast.makeText(this, "Trwa ładowanie płatności", Toast.LENGTH_SHORT).show()
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

    private fun checkoutDialog(message: String){
        val dialog = AlertDialog.Builder(this)
        dialog.setTitle("Dziękujemy za zamówienie")
        dialog.setMessage(message)
        dialog.setNegativeButton("Ok") { _, _ ->
            finish()
        }
        dialog.show()
    }
    private fun tableDialog(){
        val dialog = AlertDialog.Builder(this)
        dialog.setTitle("Wybierz stolik")
        val dialogView = layoutInflater.inflate(R.layout.dialog, null)
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