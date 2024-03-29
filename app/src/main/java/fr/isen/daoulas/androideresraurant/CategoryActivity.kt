package fr.isen.daoulas.androideresraurant

import fr.isen.daoulas.androideresraurant.ui.theme.AndroidEResraurantTheme
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import android.os.Bundle
import androidx.compose.foundation.clickable
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import coil.compose.rememberImagePainter
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.google.gson.Gson
import org.json.JSONException
import org.json.JSONObject
import androidx.compose.material3.Button
import androidx.compose.ui.text.style.TextAlign
import java.io.File
import java.io.FileWriter
import androidx.compose.material3.AlertDialog



class CategoryActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val category = intent.getStringExtra("category") ?: ""
        setContent {
            AndroidEResraurantTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val navController = rememberNavController()
                    Column {
                        ToolBar()
                        NavHost(navController = navController, startDestination = "categoryList") {
                            composable("categoryList") {
                                MenuContent(category, this@CategoryActivity)
                            }
                        }
                    }
                }
            }
        }
    }

    @Composable
    fun MenuContent(
        selectedCategory: String,
        activity: ComponentActivity
    ) {
        var menuData by remember { mutableStateOf<MenuData?>(null) }
        var error by remember { mutableStateOf(false) }
        var loading by remember { mutableStateOf(true) }

        val listState = rememberLazyListState()

        LaunchedEffect(Unit) {
            val queue = Volley.newRequestQueue(activity)
            val url = "http://test.api.catering.bluecodegames.com/menu"
            val params = JSONObject()
            params.put("id_shop", "1")

            val request = JsonObjectRequest(
                Request.Method.POST, url, params,
                Response.Listener { response ->
                    try {
                        val gson = Gson()
                        menuData = gson.fromJson(response.toString(), MenuData::class.java)
                    } catch (e: JSONException) {
                        error = true
                        e.printStackTrace()
                    } finally {
                        loading = false
                    }
                },
                Response.ErrorListener { error ->
                    error.printStackTrace()
                    loading = false
                }
            )

            queue.add(request)
        }

        if (loading) {
            // Affichez ici un indicateur de chargement si nécessaire
        } else {
            if (error) {
                // Gérez l'erreur si nécessaire
            } else {
                menuData?.data?.forEach { category ->
                    if (category.name_fr == selectedCategory) {
                        Box(modifier = Modifier.fillMaxSize()) {
                            LazyColumn(
                                modifier = Modifier.fillMaxWidth(),
                                state = listState,
                                verticalArrangement = Arrangement.spacedBy(8.dp) // Ajoute un espace entre chaque élément
                            ) {
                                item {
                                    Text(
                                        text = category.name_fr,
                                        style = TextStyle(
                                            fontWeight = FontWeight.Bold,
                                            fontSize = 40.sp,
                                            textAlign = TextAlign.Center // Centrer le texte
                                        ),
                                        modifier = Modifier
                                            .padding(vertical = 16.dp)
                                            .fillMaxWidth() // Remplir toute la largeur
                                    )
                                }
                                items(category.items) { item ->
                                    MenuItem(item)
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    @Composable
    fun MenuItem(item: MonMenuItem) {
        var expanded by remember { mutableStateOf(false) }

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 8.dp)
        ) {
            LazyRow(contentPadding = PaddingValues(start = 0.dp, end = 0.dp)) { // Aucun padding horizontal pour le LazyRow
                items(item.images) { imageUrl ->
                    Image(
                        painter = rememberImagePainter(
                            data = imageUrl,
                            builder = {
                                crossfade(true)
                            }
                        ),
                        contentDescription = null,
                        modifier = Modifier
                            .size(380.dp) // Taille de l'image
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = item.name_fr,
                style = TextStyle(fontSize = 20.sp), // Taille de police modifiée ici
                modifier = Modifier.clickable { expanded = !expanded }
            )

            if (expanded) {
                MenuItemDetails(item)
            }

            // Ajout d'un espace après le nom du plat
            Spacer(modifier = Modifier.height(15.dp))
        }
    }

    @Composable
    fun MenuItemDetails(item: MonMenuItem) {
        var selectedPriceIndex by remember { mutableStateOf(0) }
        var quantity by remember { mutableStateOf(1) }
        var showDialog by remember { mutableStateOf(false) } // Boolean to track if dialog should be shown

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
        ) {
            Text(
                text = "Prix:",
                fontWeight = FontWeight.Bold,
                fontSize = 22.sp,
                modifier = Modifier.padding(top = 12.dp)
            )

            item.prices.forEachIndexed { index, price ->
                Button(
                    onClick = { selectedPriceIndex = index },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp)
                        .background(
                            color = if (index == selectedPriceIndex) Color.Gray else Color.Transparent
                        )
                ) {
                    Text(text = "${price.price}€")
                }
            }

            Text(
                text = "Ingrédients:",
                fontWeight = FontWeight.Bold,
                fontSize = 22.sp,
                modifier = Modifier.padding(vertical = 12.dp)
            )

            Text(
                text = item.ingredients.joinToString(", ") { it.name_fr },
                fontSize = 18.sp,
                modifier = Modifier.padding(start = 20.dp)
            )

            Spacer(modifier = Modifier.height(12.dp))

            Row(
                horizontalArrangement = Arrangement.Center,
                modifier = Modifier.fillMaxWidth()
            ) {
                Button(
                    onClick = { if (quantity > 1) quantity-- },
                    modifier = Modifier.weight(1f)
                ) {
                    Text(text = "-")
                }

                Spacer(modifier = Modifier.width(8.dp))

                Text(
                    text = "$quantity",
                    fontSize = 18.sp,
                    modifier = Modifier.weight(1f).wrapContentWidth(Alignment.CenterHorizontally)
                )

                Spacer(modifier = Modifier.width(8.dp))

                Button(
                    onClick = { quantity++ },
                    modifier = Modifier.weight(1f)
                ) {
                    Text(text = "+")
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            Button(
                onClick = {
                    saveToCart(item)
                    showDialog = true // Set showDialog to true to show the AlertDialog
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
            ) {
                Text(text = "Total : ${item.prices[selectedPriceIndex].price * quantity}€")
            }

            Spacer(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp)
                    .background(Color.White)
            )
        }

        // AlertDialog to show confirmation message
        if (showDialog) {
            AlertDialog(
                onDismissRequest = {
                    showDialog = false // Dismiss the dialog when backdrop is clicked or back button is pressed
                },
                title = {
                    Text(text = "Ajouté au panier")
                },
                confirmButton = {
                    Button(
                        onClick = {
                            showDialog = false // Dismiss the dialog when OK button is clicked
                        }
                    ) {
                        Text(text = "OK")
                    }
                }
            )
        }
    }


    @Composable
    fun ToolBar(modifier: Modifier = Modifier) {
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
                    fontSize = 30.sp
                ),
                modifier = Modifier
                    .background(Color(0xFFFFA500))
                    .fillMaxWidth()
                    .padding(vertical = 30.dp, horizontal = 16.dp)
            )
        }
    }

    // Fonction pour stocker les informations dans un fichier JSON
    private fun saveToCart(item: MonMenuItem) {
        val cartFile = File(applicationContext.filesDir, "cart.json")
        val json = Gson().toJson(item)

        FileWriter(cartFile, true).use { fileWriter ->
            fileWriter.write(json)
        }
    }
}

data class MenuData(
    val data: List<CategoryItem>
)

data class CategoryItem(
    val name_fr: String,
    val items: List<MonMenuItem>
)

data class MonMenuItem(
    val itemId: String,
    val name_fr: String,
    val images: List<String>,
    val prices: List<Price>,
    val ingredients: List<Ingredient>
)

data class Price(
    val price: Double,
)

data class Ingredient(
    val name_fr: String
)