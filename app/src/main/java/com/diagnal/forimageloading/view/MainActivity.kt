package com.diagnal.forimageloading.view

import android.os.Bundle
import android.view.View
import android.widget.ProgressBar
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.diagnal.forimageloading.R
import com.diagnal.forimageloading.adapter.PhotoListAdapter
import com.diagnal.forimageloading.data.ForImageDTOItem
import com.diagnal.forimageloading.network.ApiService
import com.diagnal.forimageloading.network.RetrofitHelper
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class MainActivity : AppCompatActivity() {
    private lateinit var rvPhoto: RecyclerView
    private lateinit var toolbar: Toolbar
    private lateinit var photoListAdapter: PhotoListAdapter
    private lateinit var layoutManager: GridLayoutManager
    private lateinit var apiService : ApiService
    private var isLoading = false
    private var currentPage = 1 // Track current page number
    private lateinit var progressBar: ProgressBar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        fetchIdAndData()
    }

    private fun fetchIdAndData() {
        apiService = RetrofitHelper.getInstance().create(ApiService::class.java)
        rvPhoto = findViewById(R.id.rvPhoto)
        progressBar = findViewById(R.id.progressBar)
        layoutManager = GridLayoutManager(this, 3)
        rvPhoto.layoutManager = layoutManager
        rvPhoto.setHasFixedSize(true)

        photoListAdapter = PhotoListAdapter(this,lifecycleScope)
        rvPhoto.adapter = photoListAdapter
        // Pagination Logic
        fetchData(currentPage)
        setUpPagination()
    }

    private fun fetchData(currentPage: Int) {
        val call=apiService.getPhoto(currentPage,30)
        progressBar.visibility = View.VISIBLE
        call.enqueue(object : Callback<List<ForImageDTOItem>> {
            override fun onResponse(call: Call<List<ForImageDTOItem>>, response: Response<List<ForImageDTOItem>>) {
                if (response.isSuccessful) {
                    val photo = response.body()
                    isLoading = false
                    if (photo != null) {
                        photoListAdapter.addData(photo)
                    }
                    progressBar.visibility = View.GONE

                } else {
                    // Handle error
                    progressBar.visibility = View.GONE
                }
            }

            override fun onFailure(call: Call<List<ForImageDTOItem>>, t: Throwable) {
                // Handle network failure
                progressBar.visibility = View.GONE
            }
        })
    }


    private fun setUpPagination() {
        rvPhoto.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)

                val visibleItemCount = layoutManager.childCount
                val totalItemCount = layoutManager.itemCount
                val firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition()
                if (!isLoading && !isLoading) {
                    if (visibleItemCount + firstVisibleItemPosition >= totalItemCount
                        && firstVisibleItemPosition >= 0
                    ) {
                        loadMoreItems()
                    }
                }
            }
        })
    }

    private fun loadMoreItems() {
        isLoading = true
        currentPage++
        fetchData(currentPage)
    }


}