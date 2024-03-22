package fr.isen.daoulas.androideresraurant
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import androidx.compose.ui.tooling.preview.Preview
import fr.isen.daoulas.androideresraurant.ui.theme.AndroidEResraurantTheme

class DetailsActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val selectedDish = intent.getStringExtra("selected_dish")
        val backgroundColor = intent.getStringExtra("background_color") ?: "#FFFFFF"
        setContent {
            AndroidEResraurantTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = Color(android.graphics.Color.parseColor(backgroundColor))
                ) {
                    Dish(selectedDish ?: "Plat inconnu")
                }
            }
        }
    }
}

@Composable
fun Dish(selectedDish: String?, modifier: Modifier = Modifier) {
    Text(
        text = selectedDish ?: "Plat inconnu",
        style = TextStyle(fontSize = 24.sp, fontWeight = FontWeight.Bold),
        color = Color(0xFFFFA500),
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun DishPreview() {
    AndroidEResraurantTheme {
        Dish("Android")
    }
}


