package com.example.milkit.presentation.products

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
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material.DropdownMenuItem
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CardElevation
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.rememberAsyncImagePainter
import com.example.milkit.R
import com.example.milkit.presentation.home.HomeViewModel
import com.example.milkit.presentation.home.Product

@Preview
@Composable
fun ProductsScreen() {

    val viewModel: ProductsViewModel = viewModel()


    val allProdcuts: List<Product> = remember {
        viewModel.allProducts
    }


    if (viewModel.isLoading) {
        Column(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            CircularProgressIndicator(
                color = colorResource(id = R.color.main_theme)
            )
        }
    } else {

        LazyColumn {
            items(count = allProdcuts.size) { item ->
                Item(product = allProdcuts[item], onAddToCart = {
                    viewModel.addToCart(allProdcuts[item])
                })
            }
        }
    }
}

@Composable

fun Item(
    product: Product,
    onAddToCart: () -> Unit

) {

    var expanded by remember { mutableStateOf(false) }
    var selectedIndex by remember { mutableIntStateOf(0) }

    var expandedType by remember { mutableStateOf(false) }
    var selectedIndexType by remember { mutableIntStateOf(0) }

    val frequency = listOf("One Time", "Daily (Until I Cancel)", "1 Week", "1 Month")


    Card(
        modifier = Modifier
            .fillMaxSize()
            .padding(8.dp),
        elevation = CardDefaults.cardElevation(8.dp)
    ) {
        Column(
            Modifier
                .background(color = colorResource(id = R.color.main_theme))
                .padding(8.dp)
        ) {
            Row {
                Image(
                    painter = rememberAsyncImagePainter(model = product.imageUrl),
                    contentDescription = "Something",
                    modifier = Modifier
                        .padding(top = 8.dp)
                        .height(100.dp)
                        .width(100.dp)
                )

                Column(Modifier.padding(8.dp)) {
                    Text(
                        text = product.name ?: "",
                        color = colorResource(id = R.color.white),
                        modifier = Modifier
                            .padding(top = 12.dp)
                            .fillMaxWidth(),
                        fontSize = 16.sp
                    )

                    Text(
                        text = product.description ?: "",
                        color = colorResource(id = R.color.white),
                        modifier = Modifier
                            .padding(top = 12.dp)
                            .fillMaxWidth(),
                        fontSize = 12.sp
                    )
                }
            }
            Divider(thickness = 1.dp, modifier = Modifier.padding(top = 8.dp, bottom = 8.dp))


            Row(
                Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(modifier = Modifier.clickable {
                    expanded = true
                }) {
                    Text(
                        text = "Quantity: ${product.quantity[selectedIndex]}",
                        color = colorResource(id = R.color.white)
                    )
                    Icon(
                        painter = if (expanded) painterResource(id = R.drawable.dropdown_up) else painterResource(
                            id = R.drawable.dropdown
                        ),
                        contentDescription = "Dropdown",
                        tint = colorResource(id = R.color.white)
                    )
                }

                DropdownMenu(expanded = expanded,
                    onDismissRequest = { expanded = false }) {
                    // Create menu items
                    for (index in 0 until product.quantity.size) {
                        DropdownMenuItem(
                            onClick = {
                                selectedIndex = index
                                expanded = false
                            }
                        ) {
                            Text(product.quantity[index])
                        }
                    }

                }
                Text(
                    text = "Price : \u20B9 ${product.price[selectedIndex]}",
                    color = colorResource(id = R.color.white)
                )
            }

            Button(modifier = Modifier
                .fillMaxWidth()
                .padding(top = 16.dp, start = 8.dp, end = 8.dp),
                onClick = {
                    onAddToCart.invoke()
                }) {
                Text(text = "Add to Cart")
            }




        }
    }
}
