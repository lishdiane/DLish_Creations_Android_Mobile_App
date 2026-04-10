package com.example.dlish_creations

import Screen
import android.R.attr.delay
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.animateScrollBy
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.input.InputTransformation
import androidx.compose.foundation.text.input.TextFieldLineLimits
import androidx.compose.foundation.text.input.maxLength
import androidx.compose.foundation.text.input.rememberTextFieldState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material.icons.materialIcon
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.Button
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.setValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.VIEW_MODEL_STORE_OWNER_KEY
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.dlish_creations.model.Cart
import com.example.dlish_creations.model.Product
import com.example.dlish_creations.ui.theme.DLish_CreationsTheme
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext
import okhttp3.Dispatcher
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONObject
import java.text.NumberFormat
import java.util.Locale

// Create OkHttp Client for Chat requests
val client = OkHttpClient()

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

/**
 *  Main App
 */
@Composable
fun App() {

    //Cart
    val cart = remember { Cart() }

    //Nav Controller
    val navController = rememberNavController()

    //Set "sticky" bottom navigation bar
    Scaffold(
        bottomBar = { BottomNavBar(navController, cart) }) { innerPadding ->
        NavHost(
            //Set navigation controller, starting route, and scaffold padding
            navController = navController,
            startDestination = Screen.Home.route,
            modifier = Modifier.padding(innerPadding)
        ) {
            //Determine home and cart views based on route
            composable(Screen.Home.route) { HomeScreen(cart) }
            composable(Screen.Cart.route) { CartScreen(cart) }
            composable(Screen.Chat.route) { ChatScreen() }
        }
    }
}

/**
 * Creates the bottom navigation bar and handles navigation between screens
 */
@Composable
fun BottomNavBar(navController: NavController, cart: Cart) {

    val backStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = backStackEntry?.destination?.route

    NavigationBar {
        //Navigation for Home
        NavigationBarItem(
            icon = { Icon(Icons.Default.Home, contentDescription = "Home") },
            label = { Text("Home") },
            selected = currentRoute == Screen.Home.route,
            onClick = {
                //Navigate to Home screen - prevents duplicates and preserves state
                navController.navigate(Screen.Home.route) {
                    popUpTo(navController.graph.startDestinationId) { saveState = true }
                    launchSingleTop = true
                    restoreState = true
                }
            })
        //Navigation for Cart
        NavigationBarItem(icon = {
            //Cart badge for item count
            BadgedBox(
                badge = {
                    if (cart.cartItems.isNotEmpty()) {
                        Badge {
                            Text("${cart.cartItems.sumOf { it.quantity }}")
                        }
                    }
                }) {
                Icon(Icons.Default.ShoppingCart, contentDescription = "Cart")
            }
        }, label = { Text("Cart") }, selected = currentRoute == Screen.Cart.route, onClick = {
            //Navigate to Cart screen - prevents duplicates and preserves state
            navController.navigate(Screen.Cart.route) {
                popUpTo(navController.graph.startDestinationId) { saveState = true }
                launchSingleTop = true
                restoreState = true
            }
        })

        //Navigation for Chat
        NavigationBarItem(icon = {
            Icon(
                painter = painterResource(id = R.drawable.chat_24px),
                contentDescription = "Chat",
                tint = MaterialTheme.colorScheme.onSurface
            )
        }, label = { Text("Chat") }, selected = currentRoute == Screen.Chat.route, onClick = {
            //Navigation to Chat screen - prevents duplicates and preserves state
            navController.navigate(Screen.Chat.route) {
                popUpTo(navController.graph.startDestinationId) { saveState = true }
                launchSingleTop = true
                restoreState = true
            }
        })
    }
}

/**
 *  Displays the Home screen with product listings
 */
@Composable
fun HomeScreen(cart: Cart) {

    //product list data
    val products = remember {
        listOf(
            Product(
                "CT001",

                "Fondant Mario Character Topper", "Fondant", Product.Size(4.0, 2.5, 2.0), 35.0
            ), Product(
                "CT002",
                "Fondant Girl with Dog Topper",
                "Fondant",
                Product.Size(5.0, 3.0, 2.5),
                55.0
            ), Product(
                "CT003",
                "Gumpaste Flower Bouquet Topper",
                "Gumpaste",
                Product.Size(6.0, 4.0, 3.0),
                40.0
            ), Product(
                "CT004",
                "Edible Icing Printable Sheet",
                "Icing Sheet",
                Product.Size(8.0, 10.0, 0.1),
                18.0
            )
        )
    }

    //Displays Home page Welcome and Products
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(10.dp)
    ) {
        item {
            Welcome()
        }

        //Displays products and passes a callback function that adds item to the cart
        items(products) { product ->
            ProductCard(product) {
                cart.addProduct(product)
            }
        }
    }
}

/**
 * Displays the cart screen with items, quantity controls,
 * and the total price.
 */
@Composable
fun CartScreen(cart: Cart) {
    LazyColumn() {
        //Page Title for Cart screen
        item {
            Text(
                text = "Cart", modifier = Modifier.fillMaxSize(), textAlign = TextAlign.Center
            )
        }

        //Cart items displayed with prices and quantities
        itemsIndexed(cart.cartItems) { index, item ->
            val backgroundColor = if (index % 2 == 0) Color.White
            else Color(0xFFF5F5F5)

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(backgroundColor)
                    .padding(vertical = 8.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    //Product name and price
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

                    //Subtract item quantity button
                    Button(
                        onClick = { item.quantity-- },
                        modifier = Modifier.size(36.dp),
                        contentPadding = PaddingValues(0.dp),
                        shape = RoundedCornerShape(1.dp)

                    ) {
                        Text("-")
                    }

                    //Current item quantity
                    Text(
                        text = "${item.quantity}",
                        modifier = Modifier.padding(horizontal = 12.dp),
                        textAlign = TextAlign.Center
                    )

                    //Add item quantity button
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
        //Displays cart total.
        item() {
            Text("Total: ${formatPrice(cart.getTotal())}")
        }
    }
}

// ChatMessage class that stores the message string and a bool to determine if the message is from a user
data class ChatMessage(val text: String, val isUser: Boolean)

/**
 * Displays the chat screen with preset "help" options and
 * a message text field. Connects to network to receive responses.
 */
@Composable
fun ChatScreen() {
    // State variables
    val options = remember {
        listOf("Contact Information", "Order Status")
    }
    var messageText by remember { mutableStateOf("") }
    val messages = remember { mutableStateListOf<ChatMessage>() }
    val scope = rememberCoroutineScope()
    val listState = rememberLazyListState()
    val isTyping = remember { mutableStateOf(false) }
    val typingDots = remember { mutableStateOf("") }

    // If the server is typing, scroll to bottom of the message list
    LaunchedEffect(isTyping.value) {
        if (isTyping.value) {
            listState.animateScrollToItem(messages.size)
        }
    }

    // Chat screen layout with options, messages, and text field
    Column(modifier = Modifier.fillMaxSize()) {
        LazyColumn(modifier = Modifier.weight(1f), state = listState) {
            item {
                Text(
                    text = "Select an option below or send a message.",
                    modifier = Modifier.padding(12.dp),
                    textAlign = TextAlign.Center
                )
            }
            // List of preset help option buttons
            items(options) { option ->
                ElevatedButton(
                    onClick = {
                        // Launch network request on IO thread to avoid blocking the UI
                        scope.launch(Dispatchers.IO) {
                            // Return a response to user based on option
                            try {
                                if (option == "Contact Information") {

                                    // Add user chat bubble for option selected and scroll to bottom on the main thread
                                    withContext(Dispatchers.Main) {
                                        messages.add(
                                            ChatMessage(
                                                "Contact Information", isUser = true
                                            )
                                        )
                                        listState.animateScrollToItem(messages.size - 1)
                                    }

                                    // Start typing animation while waiting for server response
                                    isTyping.value = true
                                    startTypingAnimation(scope, isTyping, typingDots)

                                    // Delay server response for a chat like experience
                                    delay(2000)

                                    // Send a GET request to server
                                    val request =
                                        Request.Builder().url("http://10.0.2.2:5000/contact-info")
                                            .build()

                                    // Parse JSON response into readable text
                                    val response = client.newCall(request).execute()
                                    val body = response.body?.string()
                                    body?.let {
                                        println("Response: $it")
                                        val json = JSONObject(it)

                                        val info = """
                                            Call, email, or send a chat below. 
                                            We will get back with you within 24 hours.
                                            
                                            Phone: ${json.getString("phone")}
                                            Email: ${json.getString("email")}
                                            """.trimIndent()

                                        // Stop typing animation, display server message, and scroll to bottom
                                        withContext(Dispatchers.Main) {
                                            isTyping.value = false
                                            messages.add(ChatMessage(info, isUser = false))
                                            listState.animateScrollToItem(messages.size - 1)
                                        }
                                    }

                                } else if (option == "Order Status") {

                                    // Add user chat bubble for option selected and scroll to bottom on the main thread
                                    withContext(Dispatchers.Main) {
                                        messages.add(ChatMessage("Order Status", isUser = true))
                                        listState.animateScrollToItem(messages.size - 1)
                                    }

                                    // Start typing animation while waiting for server response
                                    isTyping.value = true
                                    startTypingAnimation(scope, isTyping, typingDots)

                                    // Delay server response for a chat like experience
                                    delay(2000)

                                    // Send a GET request to server
                                    val request =
                                        Request.Builder().url("http://10.0.2.2:5000/order-status")
                                            .build()

                                    // Parse JSON response into readable text
                                    val response = client.newCall(request).execute()
                                    val body = response.body?.string()
                                    body?.let {
                                        val json = JSONObject(it)
                                        val responseMessage = json.getString("order_status")

                                        // Stop typing animation, display server message, and scroll to bottom
                                        withContext(Dispatchers.Main) {
                                            isTyping.value = false
                                            messages.add(
                                                ChatMessage(
                                                    responseMessage, isUser = false
                                                )
                                            )
                                            listState.animateScrollToItem(messages.size - 1)
                                        }
                                    }
                                }
                            } catch (e: Exception) {
                                println(e)
                            }
                        }
                    }, modifier = Modifier.padding(12.dp)
                ) { Text(option) }
            }

            // Message displayed to user - color and alignment dependent on server or user based messages
            items(messages) { message ->
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = if (message.isUser) Arrangement.End else Arrangement.Start
                ) {
                    Surface(
                        shape = RoundedCornerShape(8.dp),
                        modifier = Modifier.padding(12.dp),
                        color = if (message.isUser) MaterialTheme.colorScheme.primaryContainer
                        else MaterialTheme.colorScheme.secondaryContainer,
                    ) {
                        Text(
                            message.text,
                            color = MaterialTheme.colorScheme.onSurface,
                            modifier = Modifier.padding(12.dp)
                        )
                    }
                }
            }
            // Display typing animation
            item {
                if (isTyping.value) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.Start
                    ) {
                        Surface(
                            shape = RoundedCornerShape(8.dp),
                            modifier = Modifier
                                .padding(12.dp)
                                .width(50.dp),
                            color = MaterialTheme.colorScheme.secondaryContainer

                        ) {
                            Text(
                                typingDots.value,
                                modifier = Modifier.padding(12.dp),
                                fontWeight = FontWeight.Bold,
                            )
                        }
                    }
                }
            }
        }
        // Row for text field and send button
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp)
        ) {
            OutlinedTextField(
                value = messageText,
                onValueChange = { messageText = it },
                placeholder = { Text("Message") },
                modifier = Modifier
                    .weight(1f)
                    .fillMaxHeight(),
                maxLines = 1,
                shape = RoundedCornerShape(
                    topStart = 2.dp, topEnd = 0.dp, bottomStart = 2.dp, bottomEnd = 0.dp
                )

            )
            // Send button POSTs message to server
            Button(
                onClick = {
                    // Save message and reset text field
                    val textToSend = messageText
                    messageText = ""

                    // Display user message
                    messages.add(ChatMessage(textToSend, isUser = true))

                    // Scroll to bottom
                    scope.launch {
                        listState.animateScrollToItem(messages.size - 1)
                    }

                    // Launch network request on IO thread to avoid blocking the UI
                    scope.launch(Dispatchers.IO) {

                        // Start typing animation while waiting for server response
                        isTyping.value = true
                        startTypingAnimation(scope, isTyping, typingDots)

                        // Delay server response for a chat like experience
                        delay(2000)

                        // Turn input string to JSON
                        val jsonBody = JSONObject()
                        jsonBody.put("text", textToSend)

                        // Send POST request to server
                        val requestBody =
                            jsonBody.toString().toRequestBody("application/json".toMediaType())

                        val request =
                            Request.Builder().url("http://10.0.2.2:5000/message").post(requestBody)
                                .build()

                        // Parse JSON response into readable text
                        val response = client.newCall(request).execute()
                        val body = response.body?.string()
                        body?.let {
                            val json = JSONObject(it)
                            val responseMessage =
                                ChatMessage(json.getString("reply"), isUser = false)

                            // Stop typing animation, display server message, and scroll to bottom
                            withContext(Dispatchers.Main) {
                                isTyping.value = false
                                messages.add(responseMessage)
                                listState.animateScrollToItem(messages.size - 1)

                            }
                        }
                    }

                }, modifier = Modifier.fillMaxHeight(),

                shape = RoundedCornerShape(
                    topStart = 0.dp, topEnd = 2.dp, bottomStart = 0.dp, bottomEnd = 2.dp
                )
            ) {
                Text("Send")
            }
        }
    }
}

/**
 *  Displays welcome title for Home page
 */
@Composable
fun Welcome() {

    Text(
        text = "Welcome to D-Lish Creations!"
    )
}

/**
 * Creates product card with data and an add to cart button.
 */
@Composable
fun ProductCard(product: Product, onAddToCart: (Product) -> Unit) {
    Spacer(modifier = Modifier.height(12.dp))

    // Product card
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .padding(2.dp)
            .shadow(
                elevation = 4.dp, shape = RoundedCornerShape(10.dp)
            ), shape = RoundedCornerShape(10.dp)
    ) {
        // Data displayed for each product
        Column(modifier = Modifier.padding(10.dp)) {
            Text(text = product.name)
            Text(text = "Price: ${formatPrice(product.price)}")
            Text(text = "Material: ${product.material}")
            Text(text = "Size: ${product.size.heightIn}h x ${product.size.widthIn}w x ${product.size.depthIn}d")
            // Add to cart button
            Button(onClick = { onAddToCart(product) }) {
                Text("Add to Order")
            }
        }
    }
}

/**
 *  Reusable US price formating eg. $10.00
 */
fun formatPrice(price: Double): String {
    return NumberFormat.getCurrencyInstance(Locale.US).format(price)
}

/**
 *  Reusable typing animation
 */

fun startTypingAnimation(
    scope: CoroutineScope,
    isTyping: MutableState<Boolean>,
    typingDots: MutableState<String>
) {

    isTyping.value = true

    scope.launch {
        while (isTyping.value) {
            typingDots.value = "."
            delay(300)
            typingDots.value = ".."
            delay(300)
            typingDots.value = "..."
            delay(300)
        }
        typingDots.value = ""
    }
}

/**
 *  Preview for Android studio
 */
@Preview(showBackground = true)
@Composable
fun OrderScreenPreview() {
    DLish_CreationsTheme {
        App()
    }
}

@Preview(showBackground = true)
@Composable
fun ChatPagePreview() {
    ChatScreen()
}
