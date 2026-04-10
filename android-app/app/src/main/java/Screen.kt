sealed class Screen(val route: String) {
    object Home : Screen("Home")
    object Cart : Screen("Cart")
    object Chat : Screen("Chat")
}