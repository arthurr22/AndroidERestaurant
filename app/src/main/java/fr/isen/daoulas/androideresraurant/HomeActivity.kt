package fr.isen.daoulas.androideresraurant

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.ui.text.TextStyle
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.material3.Surface

import fr.isen.daoulas.androideresraurant.ui.theme.AndroidEResraurantTheme

class HomeActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            AndroidEResraurantTheme {
                Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background) {
                    CategorySelection(context = this)
                }
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d("HomeActivity", "HomeActivity destroyed")
    }
}

@Composable
fun CategorySelection(context: Context) {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Top

    ) {
        Header()
        Spacer(modifier = Modifier.height(16.dp))
        Banner()
        Spacer(modifier = Modifier.height(16.dp))
        Column(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            CategoryButton("Entrées", context)
            Divider(
                color = Color.Gray,
                thickness = 1.dp,
                modifier = Modifier.padding(horizontal = 16.dp)
            )
            CategoryButton("Plats", context)
            Divider(
                color = Color.Gray,
                thickness = 1.dp,
                modifier = Modifier.padding(horizontal = 16.dp)
            )
            CategoryButton("Desserts", context)
        }
    }
}

@Composable
fun Header(modifier: Modifier= Modifier) {
    Column(
        modifier = modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(35.dp))
        Text(
            text = "DroidRestaurant",
            color = Color.White,
            style = TextStyle(
                fontWeight = FontWeight.Bold,
                fontSize = 30.sp // Augmentez la taille du texte ici
            ),
            modifier = Modifier
                .background(Color(0xFFFFA500))
                .fillMaxWidth()
                .padding(vertical = 30.dp, horizontal = 16.dp)

        )
    }
}


@Composable
fun Banner(modifier: Modifier = Modifier) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.End,
        verticalAlignment = Alignment.Top
    ) {
        Column(
            modifier = Modifier.align(Alignment.Top)
        ) {
            Text(
                text = "Bienvenue",
                color = Color(0xFFFFA500),
                textAlign = TextAlign.End,
                style = TextStyle(fontSize = 30.sp),
                modifier = Modifier.padding(start = 80.dp)

            )
            Text(
                text = "chez",
                color = Color(0xFFFFA500),
                textAlign = TextAlign.End,
                style = TextStyle(fontSize = 30.sp),
                modifier = Modifier.padding(start = 165.dp)
            )
            Text(
                text = "DroidRestaurant",
                color = Color(0xCD310010),
                textAlign = TextAlign.End,
                style = TextStyle(fontWeight = FontWeight.Bold, fontSize = 30.sp)
            )
        }
        Image(
            painter = painterResource(id = R.drawable.android),
            contentDescription = "Android",
            modifier = Modifier.size(150.dp), // Ajuster la taille
            alignment = Alignment.TopEnd
        )
    }
}

@Composable
fun CategoryButton(category: String, context: Context) {
    Button(
        onClick = {
            val intent = Intent(context, CategoryActivity::class.java)
            intent.putExtra("category", category)
            context.startActivity(intent)
            // Afficher le toast après le démarrage de l'activité
            Toast.makeText(
                context,
                "Vous avez cliqué sur la catégorie: $category",
                Toast.LENGTH_SHORT
            ).show()
        },
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp)
    ) {
        Text(text = category)
    }
}