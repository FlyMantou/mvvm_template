package com.library.aaron.module.ui

import android.app.ProgressDialog
import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.library.aaron.module.db.SourceEntity
import com.library.aaron.module.ui.model.ArticlesResponse
import com.library.aaron.core.ui.BaseFragment
import com.library.aaron.core.vo.Resource
import com.library.aaron.module.R
import com.library.aaron.module.adapter.NewsArticleAdapter
import com.library.aaron.module.adapter.NewsSourceAdapter
import com.library.aaron.module.viewmodel.NewsViewModel
import kotlinx.android.synthetic.main.fragment_news.*

/**
 * Created by abhinav.sharma on 01/11/17.
 */
class NewsFragment : BaseFragment(), (SourceEntity) -> Unit {


    private lateinit var newsViewModel: NewsViewModel
    private lateinit var observerNewsSource: Observer<Resource<List<SourceEntity>>>
    private lateinit var observerNewsArticle: Observer<ArticlesResponse>
    private lateinit var newsSourceAdapter: NewsSourceAdapter
    private lateinit var newsArticleAdapter: NewsArticleAdapter
    private val sourceList = ArrayList<SourceEntity>()
    private lateinit var progressDialog: ProgressDialog

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        val view: View = inflater.inflate(R.layout.fragment_news, container, false)
        newsViewModel = ViewModelProviders.of(this).get(NewsViewModel::class.java)
        progressDialog = ProgressDialog.show(activity, "News API", "Loading News Source from Web-Service")
        progressDialog.show()
        return view
    }
    override fun initView(view: View) {
        newsSourceAdapter = NewsSourceAdapter(this, sourceList)
        recyclerView.adapter = newsSourceAdapter
        recyclerView.layoutManager = LinearLayoutManager(activity)

        observerNewsSource = Observer { newsSource ->
            if (newsSource?.data != null && newsSource.data!!.isNotEmpty()) {
                progressDialog.dismiss()
                newsSourceAdapter.updateDataSet(newsSource.data!!)
            }

        }

        observerNewsArticle = Observer { newsArticle ->
            if (newsArticle != null) {
                newsArticleAdapter = NewsArticleAdapter(newsArticle.articles!!)
                recyclerView.adapter = newsArticleAdapter
            }
        }


        newsViewModel.getNewsSource(null, null, null)
                .observe(this, observerNewsSource)
    }

    override fun invoke(source: SourceEntity) {
//        newsViewModel.getNewsArticles(source.id!!, null)
//                .observe(this, observerNewsArticle)
    }

    fun onBackPressed(): Boolean {
        return when {
            recyclerView.adapter is NewsArticleAdapter -> {
                recyclerView.adapter = newsSourceAdapter
                true
            }
            else -> false
        }
    }
}