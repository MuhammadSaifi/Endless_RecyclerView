package com.example.endless_recyclerview

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.activity_main.*

// import that library in in gradle file otherwise recyclerview will not be worked and
// give us error
//    implementation'com.android.support:recyclerview-v7:26.0.0'

class MainActivity : AppCompatActivity() {

    // line 22 will hold our number/size of recycler view

    val numberList: MutableList<String> = ArrayList()
// line 25 is our page number

    var page = 0
// line 28 check our page loading or not

    var isLoading = false
    // line 30 responsible for display items in per progress it can be also 8 and 5 etc
    val limit = 10

    lateinit var adapter: NumberAdapter
    lateinit var layoutManager: LinearLayoutManager


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

// line 36 and 38 will be show our recylcer view

        layoutManager = LinearLayoutManager(this)
        recyclerView.layoutManager = layoutManager
        getPage()


        recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {

//        it will not load our second page data        if (dy > 0) {
                // line 52 count our visible items
                val visibleItemCount = layoutManager.childCount
                // line 54 check position of first item
                val pastVisibleItem = layoutManager.findFirstCompletelyVisibleItemPosition()

                // total items store in it
                val total = adapter.itemCount
// if loading will false then we will get data of next page
                if (!isLoading) {
// line 61 check our items end or not
                    if ((visibleItemCount + pastVisibleItem) >= total) {
                        page++
                        getPage()
                    }

                }
            //}

                super.onScrolled(recyclerView, dx, dy)
            }
        })

    }

    // this function will get our page data from 0 to onwords
    fun getPage() {

        isLoading = true

        // line 77 will visible our page data
        progressBar.visibility = View.VISIBLE
        // line 78 start from page * limit
        val start = ((page) * limit) + 1
        // line 81 for end the page data limit
        val end = (page + 1) * limit
// loop will be display our data between start and end variable
        for (i in start..end) {
            numberList.add("Item " + i.toString())
        }
        // handler is our progressbar
        Handler().postDelayed({
            // if condition check adapter is initialize or not
            if (::adapter.isInitialized) {
                adapter.notifyDataSetChanged()

                // else check our adapter is not initialized
            } else {
               adapter = NumberAdapter(this)
                recyclerView.adapter = adapter
            }
            // it load page and visibility of progress gone after loading data
            isLoading = false
            progressBar.visibility = View.GONE
            // progress bar will ber appear 5s
        }, 5000)

    }
    class NumberAdapter(val activity: MainActivity) : RecyclerView.Adapter<NumberAdapter.NumberViewHolder>() {
        override fun onCreateViewHolder(p0: ViewGroup, p1: Int): NumberViewHolder {
            return NumberViewHolder(LayoutInflater.from(activity).inflate(R.layout.rv_child_number, p0, false))
        }

        // getitem will return our size of recyclerview

        override fun getItemCount(): Int {
            return activity.numberList.size
        }

        //

        override fun onBindViewHolder(p0: NumberViewHolder, p1: Int) {
            p0.tvNumber.text = activity.numberList[p1]
        }

        class NumberViewHolder(v: View) : RecyclerView.ViewHolder(v) {
            val tvNumber = v.findViewById<TextView>(R.id.tv_number)
        }
    }
}
