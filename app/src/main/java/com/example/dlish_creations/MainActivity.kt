package com.example.dlish_creations

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.dlish_creations.model.Cart
import com.example.dlish_creations.model.Product
import com.example.dlish_creations.ui.theme.DLish_CreationsTheme
import kotlinx.coroutines.sync.Mutex
import java.text.NumberFormat
import java.util.Locale

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            DLish_CreationsTheme {
                App()
            }
        }
    }
}

@Composable
fun App() {

    //Cart
    val cart = remember { Cart() }

    //Nav
    val navController = rememberNavController()

    Scaffold(
        bottomBar = { BottomNavBar(navController, cart) }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = Screen.Home.route,
            modifier = Modifier.padding(innerPadding)
        ) {
            composable(Screen.Home.route) { HomeScreen(cart) }
            composable(Screen.Cart.route) { CartScreen(cart) }
        }
    }
}

@Composable
fun BottomNavBar(navController: NavController, cart: Cart) {
    val backStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = backStackEntry?.destination?.route

    NavigationBar {
        NavigationBarItem(
            icon = { Icon(Icons.Default.Home, contentDescription = "Home") },
            label = { Text("Home") },
            selected = currentRoute == Screen.Home.route,
            onClick = {
                navController.navigate(Screen.Home.route) {
                    popUpTo(navController.graph.startDestinationId) { saveState = true }
                    launchSingleTop = true
                    restoreState = true
                }
            }
        )
        NavigationBarItem(
            icon = {
                BadgedBox(
                    badge = {
                        if (cart.cartItems.isNotEmpty()) {
                            Badge {
                                Text("${cart.cartItems.sumOf { it.quantity }}")
                            }
                        }
                    }
                ) {
                    Icon(Icons.Default.ShoppingCart, contentDescription = "Cart")
                }
            },
            label = { Text("Cart") },
            selected = currentRoute == Screen.Cart.route,
            onClick = {
                navController.navigate(Screen.Cart.route) {
                    popUpTo(navController.graph.startDestinationId) { saveState = true }
                    launchSingleTop = true
                    restoreState = true
                }
            }
        )
    }
}

@Composable
fun HomeScreen(cart: Cart) {

    //product list
    val products = remember {
        listOf(
            Product(
                "CT001",

                "Fondant Mario Character Topper",
                "Fondant",
                Product.Size(4.0, 2.5, 2.0),
                35.0
            ),
            Product(
                "CT002",
                "Fondant Girl with Dog Topper",
                "Fondant",
                Product.Size(5.0, 3.0, 2.5),
                55.0
            ),
            Product(
                "CT003",
                "Gumpaste Flower Bouquet Topper",
                "Gumpaste",
                Product.Size(6.0, 4.0, 3.0),
                40.0
            ),
            Product(
                "CT004",
                "Edible Icing Printable Sheet",
                "Icing Sheet",
                Product.Size(8.0, 10.0, 0.1),
                18.0
            )
        )
    }

    //Products
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(10.dp)
    ) {
        item {
            Welcome()
        }

        items(products) { product ->
            ProductCard(product) {
                cart.addProduct(product)
            }
        }
    }
}

@Composable
fun CartScreen(cart: Cart) {

    LazyColumn() {
        item {
            Text(
                text = "Cart",
                modifier = Modifier.fillMaxSize(),
                textAlign = TextAlign.Center
            )
        }

        itemsIndexed(cart.cartItems) { index, item ->
            val backgroundColor = if (index % 2 == 0)
                Color.White
            else
                Color(0xFFF5F5F5)

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(backgroundColor)
                    .padding(vertical = 8.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = item.product.name,
                    )
                    Text(
                        text = formatPrice(item.product.price)
                    )
                }

                Row(
                    modifier = Modifier.width(140.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {

                    Button(
                        onClick = { item.quantity-- },
                        modifier = Modifier.size(36.dp),
                        contentPadding = PaddingValues(0.dp),
                        shape = RoundedCornerShape(1.dp)

                    ) {
                        Text("-")
                    }

                    Text(
                        text = "${item.quantity}",
                        modifier = Modifier.padding(horizontal = 12.dp),
                        textAlign = TextAlign.Center
                    )

                    Button(
                        onClick = { item.quantity++ },
                        modifier = Modifier.size(36.dp),
                        contentPadding = PaddingValues(0.dp),
                        shape = RoundedCornerShape(1.dp)
                    ) {
                        Text("+")
                    }
                }
            }
        }
        item() {
            Text("Total: ${formatPrice(cart.getTotal())}")
        }
    }
}


@Composable
fun Welcome() {
    Text(
        text = "Welcome to D-Lish Creations!"
    )
}

@Composable
fun ProductCard(product: Product, onAddToCart: (Product) -> Unit) {
    Spacer(modifier = Modifier.height(12.dp))

    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .padding(2.dp)
            .shadow(
                elevation = 4.dp,
                shape = RoundedCornerShape(10.dp)
            ),
        shape = RoundedCornerShape(10.dp)
    ) {
        Column(modifier = Modifier.padding(10.dp)) {
            Text(text = product.name)
            Text(text = "Price: ${formatPrice(product.price)}")
            Text(text = "Material: ${product.material}")
            Text(text = "Size: ${product.size.heightIn}h x ${product.size.widthIn}w x ${product.size.depthIn}d")
            Button(onClick = { onAddToCart(product) }) {
                Text("Add to Order")
            }
        }
    }
}

fun formatPrice(price: Double): String {
    return NumberFormat.getCurrencyInstance(Locale.US).format(price)
}


@Preview(showBackground = true)
@Composable
fun OrderScreenPreview() {
    DLish_CreationsTheme {
        App()
    }
}
