package com.example.notes.presentation

import android.content.Context
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.outlined.StarOutline
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.content.edit

@Composable
fun ProductListScreen() {

    val context = LocalContext.current
    val prefs = remember { context.getSharedPreferences("Prefs", Context.MODE_PRIVATE) }

    fun loadProductsFromPrefs(): SnapshotStateList<String> {
        val list = mutableStateListOf<String>()
        val savedData = prefs.getStringSet("Data", emptySet()) ?: emptySet()
        list.addAll(savedData)
        return list
    }

    val productList = remember { loadProductsFromPrefs() }

//        val savedProductList = prefs.getStringSet("Data", emptySet()) ?: emptySet()
//        val products = prefs.getStringSet("Data", emptySet())!!.toList()
//        SnapshotStateList<String>(products.size) { products[it] }
//    }

    var productName by remember { mutableStateOf("") }
    var productRating by remember { mutableIntStateOf(0) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        TextField(
            value = productName,
            onValueChange = { productName = it })
        Spacer(modifier = Modifier.height(8.dp))
        RatingBar(
            rating = productRating,
            onRatingChange = { productRating = it }
        )
        Spacer(modifier = Modifier.height(8.dp))
        Button(
            onClick = {
                if (productName.isNotBlank()) {
                    val productInfoWithKey = "${System.currentTimeMillis()} | $productName - $productRating"
                    productList.add(productInfoWithKey)
                    prefs.edit {
                        putStringSet("Data", productList.toSet())
                    }
                    productName = ""
                    productRating = 0
                }
            }
        ) {
            Text("Save")
        }
        Spacer(modifier = Modifier.height(8.dp))
        LazyColumn {
            items(productList.size) { index ->
                Text(productList[index].split(" | ").last())
            }
        }
    }
}

@Composable
fun RatingBar(
    rating: Int,
    onRatingChange: (Int) -> Unit
) {
    Row {
        for (i in 1..5) {
            Icon(
                imageVector = if (i <= rating) Icons.Filled.Star else Icons.Outlined.StarOutline,
                contentDescription = "Star $i",
                modifier = Modifier
                    .size(32.dp)
                    .clickable { onRatingChange(i) }
            )
        }
    }
}

@Preview
@Composable
fun ProductListScreen_Preview() {
    ProductListScreen()
}