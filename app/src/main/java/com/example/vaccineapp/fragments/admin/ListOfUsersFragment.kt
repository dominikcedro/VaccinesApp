package com.example.vaccineapp.fragments.admin

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.vaccineapp.R
import ListOfUsersViewModel
import UsersAdapter
import org.koin.androidx.viewmodel.ext.android.viewModel
class ListOfUsersFragment : Fragment() {
    private lateinit var userAdapter: UsersAdapter
    private lateinit var recyclerView: RecyclerView

    private val viewModel: ListOfUsersViewModel by viewModel()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_list_of_users, container, false)
        recyclerView = view.findViewById(R.id.usersListRV)
        recyclerView.layoutManager = LinearLayoutManager(context)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        userAdapter = UsersAdapter(emptyList())
        recyclerView.adapter = userAdapter

        viewModel.fetchUsers()

        viewModel.usersList.observe(viewLifecycleOwner) { users ->
            userAdapter.users = users
            userAdapter.notifyDataSetChanged()
        }
    }
}