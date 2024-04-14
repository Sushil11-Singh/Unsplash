package com.diagnal.forimageloading.network

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.view.View
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.Toast
import androidx.collection.LruCache
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.IOException
import java.net.HttpURLConnection
import java.net.URL

class ImageLoader(private val context: Context) {

    sealed class Result {
        data class Success(val bitmap: Bitmap) : Result()
        data object Error : Result()
    }

    private val memoryCache: LruCache<String, Bitmap> = LruCache(maxMemorySize())

    suspend fun loadImage(
        url: String,
        imageView: ImageView,
        errorPlaceholder: Int,
        progressBar: ProgressBar
    ) {
        val bitmap = memoryCache.get(url)
        if (bitmap != null) {
            progressBar.visibility = View.GONE
            imageView.visibility = View.VISIBLE
            imageView.setImageBitmap(bitmap)

        } else {
            val downloadedBitmap = downloadBitmap(url)
            if (downloadedBitmap != null) {
                memoryCache.put(url, downloadedBitmap)
                imageView.setImageBitmap(downloadedBitmap)
                Result.Success(downloadedBitmap)
                progressBar.visibility = View.GONE
                imageView.visibility = View.VISIBLE
            } else {
                imageView.setImageResource(errorPlaceholder)
                Toast.makeText(context, "Failed to load image", Toast.LENGTH_SHORT).show()
                progressBar.visibility = View.VISIBLE
                imageView.visibility = View.GONE
            }
        }
    }

    private suspend fun downloadBitmap(url: String): Bitmap? {
        return withContext(Dispatchers.IO) {
            var connection: HttpURLConnection? = null
            try {
                val imageUrl = URL(url)
                connection = imageUrl.openConnection() as HttpURLConnection
                connection.connect()
                val inputStream = connection.inputStream
                BitmapFactory.decodeStream(inputStream)
            } catch (e: IOException) {
                e.printStackTrace()
                null
            } finally {
                connection?.disconnect()
            }
        }
    }

    private fun maxMemorySize(): Int {
        val maxMemory = Runtime.getRuntime().maxMemory()
        return (maxMemory / 8).toInt() // Use 1/8th of the available memory for the cache
    }
}
