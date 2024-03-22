package fr.isen.daoulas.androideresraurant

import android.app.Activity
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.android.volley.Request
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import org.json.JSONObject
import com.google.gson.Gson
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.android.volley.Response

import fr.isen.daoulas.androideresraurant.ui.theme.AndroidEResraurantTheme
import org.json.JSONException


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
                    MenuContent(category, this)
                }
            }
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

@Composable
fun MenuContent(category: String, activity: Activity) {
    var menuItems by remember { mutableStateOf<List<MonMenuItem>>(emptyList()) }
    var error by remember { mutableStateOf(false) }
    var loading by remember { mutableStateOf(true) }

    val queue = Volley.newRequestQueue(activity)
    val url = "http://test.api.catering.bluecodegames.com/menu"
    val params = JSONObject()
    params.put("id_shop", "1")

    val request = JsonObjectRequest(
        Request.Method.POST, url, params,
        Response.Listener { response ->
            try {
                val gson = Gson()
                val menuData = gson.fromJson(response.toString(), MenuData::class.java)
                val categoryItems = menuData.data.filter { it.name_fr == category }
                if (categoryItems.isNotEmpty()) {
                    menuItems = categoryItems[0].items
                }
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
    
    Column {
        MenuItemName(menuItems)
    }
}

@Composable
fun MenuItemName(menuItems: List<MonMenuItem>){
    Column {
        menuItems.forEach { item ->
            Log.d("test",item.name_fr)
            Column(
                modifier = Modifier.fillMaxWidth()
            ){
                Text(item.name_fr)
            }
        }
    }
}


@Preview(showBackground = true)
@Composable
fun CategoryTitlePreview() {
    AndroidEResraurantTheme {
        MenuContent("Plats", Activity())
    }
}