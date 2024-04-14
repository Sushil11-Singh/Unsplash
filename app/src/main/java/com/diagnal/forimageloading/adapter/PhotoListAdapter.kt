package com.diagnal.forimageloading.adapter

import com.diagnal.forimageloading.network.ImageLoader
import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.ProgressBar
import androidx.lifecycle.LifecycleCoroutineScope
import androidx.recyclerview.widget.RecyclerView
import com.diagnal.forimageloading.R
import com.diagnal.forimageloading.data.ForImageDTOItem
import kotlinx.coroutines.launch

class PhotoListAdapter(
    private val context: Context,
    private val lifecycleScope: LifecycleCoroutineScope
) : RecyclerView.Adapter<PhotoListAdapter.ViewHolder>() {
    private var photoList = mutableListOf<ForImageDTOItem>()
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.listing_row, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val photo = photoList[position]
        val imageLoader = ImageLoader(context)
        val errorPlaceholder = R.drawable.ic_launcher_foreground
        val imageUrl =
            photo.urls.regular // Replace with the actual image URL field in your Item model
        holder.progressBar.visibility = View.VISIBLE
        holder.ivPosterImage.visibility = View.GONE
        holder.ivPosterImage?.let { imageView ->

            lifecycleScope.launch {
                imageLoader.loadImage(imageUrl, imageView, errorPlaceholder,holder.progressBar)
            }
        }
    }


    override fun getItemCount(): Int {
        return photoList.size
    }

    @SuppressLint("NotifyDataSetChanged")
    fun addData(newContentList: List<ForImageDTOItem>) {
        photoList.addAll(newContentList)
        notifyDataSetChanged()
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val ivPosterImage: ImageView = itemView.findViewById(R.id.ivPosterImage)
        val progressBar: ProgressBar = itemView.findViewById(R.id.progressBar)
    }

    override fun getItemViewType(position: Int): Int {
        return super.getItemViewType(position)
    }


}