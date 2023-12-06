package com.example.milkit.presentation.cart

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.selection.selectable
import androidx.compose.material.Card
import androidx.compose.material.Checkbox
import androidx.compose.material.DropdownMenuItem
import androidx.compose.material.RadioButton
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import com.example.milkit.R
import com.example.milkit.util.Screen

@Composable


fun CartScreen(navController: NavController) {


    val viewModel: CartViewModel = viewModel()


    val cartItems = viewModel.cartItems.observeAsState()


    Column(
        Modifier
            .background(color = colorResource(id = R.color.main_theme))
            .fillMaxSize()
    ) {

        LazyColumn(modifier = Modifier.weight(1f)) {
            items(count = cartItems.value?.size ?: 0) { index ->

                val product = cartItems.value?.get(index)

                product?.let {
                    SingleCartItem(
                        product = product
                    )
                }

            }
        }

        Button(onClick = {

            navController.navigate(Screen.CheckoutScreen.route)
        },
            modifier = Modifier.fillMaxWidth()) {
            Text(text = "Procced to Checkout",color = colorResource(id = R.color.white))
        }

    }

}

@Composable
@Preview
fun PreviewCart() {
    SingleCartItem(
        product = CartModel(
            productId = "11", name = "Cow Milk", description = "sa",
            quantityList = listOf("1", "2"), priceList = listOf("1", "2")
        )
    )
}


@Composable
fun SingleCartItem(
    product: CartModel
) {

    val viewModel: CartViewModel = viewModel()

    var indexOfSelectedItem = product.quantityList?.indexOf(product.selectedQuantity?:"") ?: 0
    if(indexOfSelectedItem == -1) indexOfSelectedItem = 0

    val frequency = listOf("One Time", "1 Week", "15 Days", "1 Month")

    var expanded by remember { mutableStateOf(false) }
    var selectedIndex by remember { mutableIntStateOf(indexOfSelectedItem) }

    var indexOfSelectedFrequency = frequency.indexOf(product.frequency?:"")
    if(indexOfSelectedFrequency == -1) indexOfSelectedFrequency =0


    var expandedFrequency by remember { mutableStateOf(false) }
    var selectedIndexFrequency by remember { mutableIntStateOf(indexOfSelectedFrequency) }

    var isCheckedForCheckout by remember {
        mutableStateOf(product.isCheckedOut)
    }




    val radioOptions = listOf("Morning", "Evening", "Both")

    val (selectedOption, onOptionSelected) = remember { mutableStateOf(radioOptions[2]) }



    var quantityPrice:Int = ((product.priceList?.get(selectedIndex))?.toInt()  ?: 0)

    var totalPrice by remember {
        mutableIntStateOf(quantityPrice)
    }

    LaunchedEffect(selectedIndex,selectedIndexFrequency,selectedOption,totalPrice,isCheckedForCheckout){

        quantityPrice = ((product.priceList?.get(selectedIndex))?.toInt()  ?: 0)

        when (selectedIndexFrequency) {
            0 -> {
                totalPrice = quantityPrice
            }
            1 -> {
                totalPrice = quantityPrice * 7
            }
            2 -> {
                totalPrice = quantityPrice * 15
            }
            3 -> {
                totalPrice = quantityPrice * 30
            }
        }
        when (selectedOption) {
            "Morning" , "Evening" -> {
            }
            "Both" -> {
                totalPrice *= 2
            }
        }
        product.selectedQuantity = product.quantityList?.get(selectedIndex)
        product.frequency = frequency[selectedIndexFrequency]
        product.morningOrEvening = selectedOption
        product.totalCost = totalPrice.toString()
        product.isCheckedOut  = isCheckedForCheckout

        viewModel.editCartItem(product)
    }


    Card(modifier = Modifier.padding(8.dp), elevation = 8.dp) {
        Column {


            Row {
                Column(horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.SpaceBetween) {

                    Image(
                        painter = rememberAsyncImagePainter(model = product.imageUrl),
                        contentDescription = "Something",
                        modifier = Modifier
                            .padding(8.dp)
                            .height(80.dp)
                            .width(80.dp)
                    )

                    Column() {
                        Checkbox(
                            checked = isCheckedForCheckout, onCheckedChange = {
                                isCheckedForCheckout = it
                            }
                        )
                        Text(text = "Checkout")
                    }
                }


                Column(Modifier.padding(8.dp)) {
                    Text(
                        text = product.name ?: "",
                        modifier = Modifier
                            .padding(top = 12.dp)
                            .fillMaxWidth(),
                        fontSize = 16.sp
                    )

                    Row(
                        Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Row(
                            modifier = Modifier.clickable {
                                expanded = true
                            },
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "Quantity: ${product.quantityList?.get(selectedIndex)}",
                            )
                            Icon(
                                painter = if (expanded) painterResource(id = R.drawable.dropdown_up) else painterResource(
                                    id = R.drawable.dropdown
                                ),
                                contentDescription = "Dropdown",
                            )
                        }

                        DropdownMenu(expanded = expanded,
                            onDismissRequest = { expanded = false }) {

                            for (ii in 0 until (product.quantityList?.size ?: 0)) {
                                DropdownMenuItem(
                                    onClick = {
                                        selectedIndex = ii
                                        expanded = false

                                    }
                                ) {
                                    Text(product.quantityList?.get(ii) ?: "")
                                }
                            }

                        }
                        Text(
                            text = "Price : \u20B9 ${product.priceList?.get(selectedIndex)}",
                        )
                    }



                    Row(modifier = Modifier
                        .clickable {
                            expandedFrequency = true
                        }
                        .fillMaxWidth()
                        .padding(top = 12.dp)) {
                        Text(
                            text = "Frequency: ${frequency[selectedIndexFrequency]}",
                        )

                        Icon(
                            painter = if (expandedFrequency) painterResource(id = R.drawable.dropdown_up) else painterResource(
                                id = R.drawable.dropdown
                            ),
                            contentDescription = "Dropdown",
                        )

                    }

                    DropdownMenu(expanded = expandedFrequency,
                        onDismissRequest = { expandedFrequency = false }) {
                        for (index in frequency.indices) {
                            DropdownMenuItem(
                                onClick = {

                                    selectedIndexFrequency = index
                                    expandedFrequency = false
                                }
                            ) {
                                Text(frequency[index])
                            }
                        }

                    }
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.Center
                    ) {
                        radioOptions.forEach { text ->
                            Column(
                                Modifier
                                    .selectable(
                                        selected = (text == selectedOption),
                                        onClick = { onOptionSelected(text) }
                                    )
                                    .padding(horizontal = 16.dp),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {

                                RadioButton(
                                    selected = (text == selectedOption),
                                    onClick = {

                                        onOptionSelected(text)
                                    }
                                )
                                Text(
                                    text = text,
                                )
                            }
                        }
                    }
                }

            }


            Text(
                text = "Total Price : $totalPrice",
                fontSize = 20.sp,
                textAlign = TextAlign.End,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            )
        }


    }


}



