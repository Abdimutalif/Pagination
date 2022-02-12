package uz.mobiler.pagingg

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import uz.mobiler.pagingg.adapters.PaginationAdapter
import uz.mobiler.pagingg.databinding.ActivityMainBinding
import uz.mobiler.pagingg.models.Data
import uz.mobiler.pagingg.models.UserData
import uz.mobiler.pagingg.retrofit.ApiClient
import uz.mobiler.pagingg.utils.PaginationScrollListener

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    private lateinit var list: ArrayList<Data>
    private lateinit var paginationAdapter: PaginationAdapter

    private var currentPage = 1

    private var isLoading = false
    private var isLastPage = false
    private var TOTAL_PAGES = 2

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        list = ArrayList()
        paginationAdapter = PaginationAdapter(list)
        val linearLayoutManager = LinearLayoutManager(this)
        binding.rv.layoutManager = linearLayoutManager
        binding.rv.adapter = paginationAdapter

        getUsers(currentPage)

        binding.rv.addOnScrollListener(object : PaginationScrollListener(linearLayoutManager) {
            override fun loadMoreItems() {
                isLoading = true
                currentPage++
                getNextUsers(currentPage)
            }

            override fun isLastPage(): Boolean {
                return isLastPage
            }

            override fun isLoading(): Boolean {
                return isLoading
            }
        })
        // custom pagination

        // paging 3
    }

    fun getUsers(page: Int) {
        ApiClient.apiService.getUsers(page).enqueue(object : Callback<UserData> {
            override fun onResponse(call: Call<UserData>, response: Response<UserData>) {
                if (response.isSuccessful) {
                    list.addAll(response.body()?.data ?: emptyList())
                    paginationAdapter.notifyDataSetChanged()

                    if (currentPage <= TOTAL_PAGES) {
                        paginationAdapter.addLoading()
                    } else {
                        isLastPage = true
                    }
                }
            }

            override fun onFailure(call: Call<UserData>, t: Throwable) {

            }
        })
    }

    fun getNextUsers(page: Int) {
        ApiClient.apiService.getUsers(page).enqueue(object : Callback<UserData> {
            override fun onResponse(call: Call<UserData>, response: Response<UserData>) {
                if (response.isSuccessful) {
                    paginationAdapter.removeLoading()
                    isLoading = false
                    paginationAdapter.addAll(response.body()?.data ?: emptyList())

                    if (currentPage != TOTAL_PAGES) {
                        paginationAdapter.addLoading()
                    } else {
                        isLastPage = true
                    }
                }
            }

            override fun onFailure(call: Call<UserData>, t: Throwable) {

            }
        })
    }
}