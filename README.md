# Photo List Adapter

The `PhotoListAdapter` is an Android RecyclerView adapter designed to efficiently load and display a list of photos fetched from an API using an `ImageLoader`. It provides support for asynchronous image loading, caching, error handling, and pagination.

## Features

- Asynchronous loading of images from API URLs.
- Efficient caching mechanism for storing images in memory and/or disk cache.
- Graceful handling of network errors and image loading failures.
- Support for displaying progress bars while loading images.
- Seamless integration with the Android RecyclerView.
- Pagination support for loading more photos as the user scrolls.

## Usage

### Installation

To use `PhotoListAdapter` in your Android project, follow these steps:

1. Add the `PhotoListAdapter` class to your project.
2. Ensure you have the `ImageLoader` class available in your project or import it as needed.
3. Use the `PhotoListAdapter` in your RecyclerView by creating an instance and setting it as the adapter.

### Example

```kotlin
// Create an instance of PhotoListAdapter
val adapter = PhotoListAdapter(context, lifecycleScope)

// Set the adapter to your RecyclerView
recyclerView.adapter = adapter

// Populate the adapter with data
adapter.addData(photoList)
