package com.example.suitmediaintern

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.example.suitmediaintern.SecondActivity.Companion.EXTRA_NAME
import com.example.suitmediaintern.SecondActivity.Companion.EXTRA_NAME_FROM_THIRD
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ThirdActivity : AppCompatActivity() {

    private lateinit var swipeRefreshLayout: SwipeRefreshLayout
    private lateinit var recyclerViewUsers: RecyclerView
    private lateinit var userAdapter: UserAdapter

    private var currentPage = 1
    private val perPage = 10
    private var isLoading = false

    private val userService: UserService = RetrofitClient.createService()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_third)

        swipeRefreshLayout = findViewById(R.id.swipeRefreshLayout)
        recyclerViewUsers = findViewById(R.id.recyclerViewUsers)

        setupRecyclerView()
        setupSwipeRefreshLayout()
        fetchUsers()

        val btnBack = findViewById<Button>(R.id.btnBack)
        btnBack.setOnClickListener {
            val nameFromFirstScreen = intent.getStringExtra(SecondActivity.EXTRA_NAME)
            val nameFromThirdScreen = intent.getStringExtra(SecondActivity.EXTRA_NAME_FROM_THIRD)


            val intent = Intent(this, SecondActivity::class.java)
            intent.putExtra(EXTRA_NAME, nameFromFirstScreen)
            intent.putExtra(EXTRA_NAME_FROM_THIRD, nameFromThirdScreen)
            startActivity(intent)
            startActivity(intent)
        }
    }

    private fun setupRecyclerView() {
        val layoutManager = LinearLayoutManager(this)
        recyclerViewUsers.layoutManager = layoutManager
        userAdapter = UserAdapter(mutableListOf()) { user -> onUserItemClick(user) }
        recyclerViewUsers.adapter = userAdapter

        recyclerViewUsers.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                val lastVisibleItemPosition = layoutManager.findLastVisibleItemPosition()
                val totalItemCount = layoutManager.itemCount

                if (!isLoading && lastVisibleItemPosition == totalItemCount - 1) {
                    currentPage++
                    fetchUsers()
                }
            }
        })
    }

    private fun setupSwipeRefreshLayout() {
        swipeRefreshLayout.setOnRefreshListener {
            currentPage = 1
            userAdapter.clearData()
            fetchUsers()
        }
    }

    private fun fetchUsers() {
        isLoading = true
        swipeRefreshLayout.isRefreshing = true

        userService.getUsers(page = currentPage, perPage = perPage)
            .enqueue(object : Callback<UserResponse> {
                override fun onResponse(call: Call<UserResponse>, response: Response<UserResponse>) {
                    isLoading = false
                    swipeRefreshLayout.isRefreshing = false

                    if (response.isSuccessful) {
                        val users = response.body()?.data ?: emptyList()
                        if (users.isEmpty()) {
                            displayEmptyState()
                        } else {
                            userAdapter.addUsers(users)
                        }
                    } else {
                        Log.e("ThirdActivity", "Failed to fetch users. Code: ${response.code()}")
                    }
                }

                override fun onFailure(call: Call<UserResponse>, t: Throwable) {
                    isLoading = false
                    swipeRefreshLayout.isRefreshing = false
                    Log.e("ThirdActivity", "Error fetching users", t)
                }
            })
    }

    private fun displayEmptyState() {
        val emptyStateMessage = "No users available."
        val textViewEmptyState = findViewById<TextView>(R.id.textViewEmptyState)
        textViewEmptyState.text = emptyStateMessage
        textViewEmptyState.visibility = View.VISIBLE
        recyclerViewUsers.visibility = View.GONE
    }

    private fun onUserItemClick(user: User) {
        val nameFromFirstScreen = intent.getStringExtra(SecondActivity.EXTRA_NAME)
        val fullName = "${user.first_name} ${user.last_name}"
        val intent = Intent(this, SecondActivity::class.java)
        intent.putExtra(SecondActivity.EXTRA_NAME, nameFromFirstScreen)
        intent.putExtra(SecondActivity.EXTRA_NAME_FROM_THIRD, fullName)
        startActivity(intent)
    }
}
