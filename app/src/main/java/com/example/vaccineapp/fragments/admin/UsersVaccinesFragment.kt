package com.example.vaccineapp.fragments.admin

import ListOfUsersViewModel
import android.R
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.vaccineapp.databinding.FragmentUsersVaccinesBinding
import com.example.vaccineapp.users_vaccines_recycler_view.UsersScheduledVaccinationAdapter
import com.example.vaccineapp.viewmodel.UserVaccinesViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class UsersVaccinesFragment : Fragment() {
    private val listViewModel: ListOfUsersViewModel by viewModel()
    private val vaccinesViewModel: UserVaccinesViewModel by viewModel()
    private var _binding: FragmentUsersVaccinesBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentUsersVaccinesBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        listViewModel.fetchUsers()

        listViewModel.usersList.observe(viewLifecycleOwner) { users ->
            val adapter = ArrayAdapter(
                requireContext(),
                R.layout.simple_dropdown_item_1line,
                users.map { it.email })
            binding.userDropdown.setAdapter(adapter)

            binding.userDropdown.setOnItemClickListener { _, _, position, _ ->
                val selectedUserId = users[position].id.toLong()
                vaccinesViewModel.fetchScheduledVaccinesForUser(selectedUserId)
            }
        }

        vaccinesViewModel.scheduledVaccines.observe(viewLifecycleOwner) { vaccines ->
            val recyclerView = binding.scheduledVaccinesRecyclerView
            recyclerView.layoutManager = LinearLayoutManager(context)
            recyclerView.adapter = UsersScheduledVaccinationAdapter(vaccines) { vaccineId ->
                val bundle = Bundle().apply {
                    putLong("vaccineId", vaccineId)
                }
                findNavController().navigate(com.example.vaccineapp.R.id.editUsersScheduledVaccineFragment, bundle)
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
