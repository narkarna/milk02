package com.example.milkit.presentation.home

import android.widget.Toast
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.DropdownMenuItem
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import com.example.milkit.R
import com.example.milkit.util.Screen

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun HomeScreen(navController: NavController) {

    val viewModel: HomeViewModel = viewModel()


    val featuredProduct: List<Product> = remember {
        viewModel.featuredProducts
    }

    val pagerState = rememberPagerState(pageCount = {
        featuredProduct.size
    })

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

        Column(
            modifier = Modifier
                .background(colorResource(id = R.color.main_theme))
                .fillMaxSize()
                .padding(12.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = "Featured Products",
                color = colorResource(id = R.color.white),
                fontSize = 20.sp
            )

            if (featuredProduct.isNotEmpty()) {

                HorizontalPager(
                    modifier = Modifier
                        .padding(start = 12.dp, end = 12.dp),
                    state = pagerState
                ) { index ->
                    val item = featuredProduct[index]
                    DisplayCard(item)
                }
            }



            Button(modifier = Modifier
                .fillMaxWidth()
                .padding(top = 16.dp, start = 24.dp, end = 24.dp),
                colors = ButtonDefaults.buttonColors(containerColor = colorResource(id = R.color.white)),

                onClick = {
                    navController.navigate(Screen.ProductsScreen.route)
                }) {
                Text( text = "See All Products", color = colorResource(id = R.color.main_theme))
            }

        }
    }
}


@Composable
fun DisplayCard(product: Product) {

    val viewModel: HomeViewModel = viewModel()

    var expanded by remember { mutableStateOf(false) }
    var selectedIndex by remember { mutableIntStateOf(0) }

    val context = LocalContext.current

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(colorResource(id = R.color.main_theme))
            .padding(8.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            painter = rememberAsyncImagePainter(model = product.imageUrl),
            contentDescription = "Something",
            modifier = Modifier
                .padding(top = 8.dp)
                .height(200.dp)
                .width(200.dp)
        )


        Text(
            text = product.name ?: "",
            color = colorResource(id = R.color.white),
            fontSize = 16.sp ,
            modifier = Modifier
                .padding(top = 12.dp)
                .fillMaxWidth()
        )

        Text(
            text = product.description ?: "",
            color = colorResource(id = R.color.white),
            modifier = Modifier
                .padding(top = 12.dp)
                .fillMaxWidth()
        )

        Divider(thickness = 1.dp, modifier = Modifier.padding(top = 8.dp, bottom = 8.dp))

        Row(
            Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(modifier = Modifier.clickable {
                expanded = true
            }) {
                Text(
                    text = product.quantity[selectedIndex],
                    color = colorResource(id = R.color.white),
                    fontSize = 14.sp
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
                text = "\u20B9 ${product.price[selectedIndex]}",
                color = colorResource(id = R.color.white),
                fontSize = 14.sp
            )
        }


        Button(modifier = Modifier
            .fillMaxWidth()
            .padding(top = 16.dp, start = 8.dp, end = 8.dp),
            colors = ButtonDefaults.buttonColors(containerColor = colorResource(id = R.color.main_theme2)),

        onClick = {
            viewModel.addToCart(product = product)
            Toast.makeText(context,"Item Added to Cart",Toast.LENGTH_LONG).show()
        }) {
            Text(text = "Add to Cart")
        }
    }
}