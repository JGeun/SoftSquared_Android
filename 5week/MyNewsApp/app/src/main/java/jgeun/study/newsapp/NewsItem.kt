package jgeun.study.newsapp

import android.net.Uri
import java.io.Serializable
import java.net.URI
import java.net.URL

data class NewsItem(val imageResource: Uri, var title: String?, var content: String?) : Serializable