package com.example.vaccineapp.fragments

import ListOfUsersViewModel
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.vaccineapp.R
import com.example.vaccineapp.databinding.FragmentAdministeredVaccinationsBinding
import com.example.vaccineapp.administered_recycler_view.AdministeredVaccinationAdapter
import com.example.vaccineapp.viewmodel.AdministeredVaccinationViewModel
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel
import androidx.navigation.fragment.findNavController
import com.example.vaccineapp.databinding.FragmentListOfUsersBinding
import com.example.vaccineapp.databinding.FragmentUsersVaccinesBinding
import com.example.vaccineapp.viewmodel.UserVaccinesViewModel

/**
 * Fragment for displaying all users.
 */
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
            val adapter = UserArrayAdapter(requireContext(), users)
            binding.userDropdown.setAdapter(adapter)

            binding.userDropdown.setOnItemClickListener { _, _, position, _ ->
                val selectedUserId = users[position].id
                vaccinesViewModel.fetchScheduledVaccinesForUser(selectedUserId.toLong())
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}