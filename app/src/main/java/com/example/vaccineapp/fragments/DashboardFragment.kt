package com.example.vaccineapp.fragments

import NewsArticleViewModel
import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextClock
import androidx.fragment.app.Fragment
import com.example.vaccineapp.databinding.FragmentDashboardBinding
import android.graphics.LinearGradient
import android.graphics.Shader
import android.graphics.Color
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.vaccineapp.R
import com.example.vaccineapp.domain.ScheduledVaccinationGetRequest
import com.example.vaccineapp.viewmodel.MainMenuViewModel
import com.example.vaccineapp.viewmodel.ScheduledVaccinationViewModel
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.sharedViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter


class DashboardFragment : Fragment() {


    private var _binding: FragmentDashboardBinding? = null
    private val binding get() = _binding!!
    private val viewModel: MainMenuViewModel by viewModel()

    private val scheduledVaccinationViewModel: ScheduledVaccinationViewModel by sharedViewModel()
    private var closestVaccine: ScheduledVaccinationGetRequest? = null

    private val newsArticleViewModel: NewsArticleViewModel by viewModel()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentDashboardBinding.inflate(inflater, container, false)

        val clock: TextClock = binding.clock
        clock.format24Hour = "HH:mm"

        val paint = clock.paint
        val width = paint.measureText(clock.text.toString())

        val textShader: Shader = LinearGradient(0f, 0f, width, clock.textSize, intArrayOf(
            ContextCompat.getColor(requireContext(), R.color.redAccent),
            ContextCompat.getColor(requireContext(), R.color.orangeAccent),
            ContextCompat.getColor(requireContext(), R.color.orangeAccent),
            ContextCompat.getColor(requireContext(), R.color.orangeAccent),
            ContextCompat.getColor(requireContext(), R.color.white),
        ), null, Shader.TileMode.CLAMP)

        clock.paint.shader = textShader

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        lifecycleScope.launch {
            newsArticleViewModel.fetchArticle()
            newsArticleViewModel.article.observe(viewLifecycleOwner) { newsArticle ->
                _binding?.newsArticleTitleTextView?.text = newsArticle.title
            }
            viewModel.fetchVaccinationsToConfirm()
            val vaccinationsToConfirm = viewModel.getVaccinationsToConfirm()
            for (vaccination in vaccinationsToConfirm) {
                confirmVaccinationDialog(vaccination)
            }

            scheduledVaccinationViewModel.getScheduledVaccinations()
            closestVaccine = scheduledVaccinationViewModel.scheduledVaccinations.minByOrNull { it.dateTime }
            if (closestVaccine != null) {
                _binding?.closestVaccineName?.text = closestVaccine?.vaccine?.name
                _binding?.closestVaccineName?.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.orangeAccent))
            }
            _binding?.closestVaccineCard?.setOnClickListener {
                findNavController().navigate(R.id.action_mainMenuFragment_to_ScheduledVaccinationsFragment)
            }
        }
    }

    companion object {
        fun newInstance() = DashboardFragment()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun confirmVaccinationDialog(scheduledVaccination: ScheduledVaccinationGetRequest) {
        val builder = AlertDialog.Builder(context)

        val vaccinationName = scheduledVaccination.vaccine.name

        val originalFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ssZ")
        val desiredFormat = DateTimeFormatter.ofPattern("HH:mm dd-MM-yyyy")

        val vaccinationDateString = scheduledVaccination.dateTime

        val date = ZonedDateTime.parse(vaccinationDateString, originalFormat)
        val formattedDate = date.format(desiredFormat)

        builder.setTitle("Confirm Vaccination")
        builder.setMessage("Did you attend the $vaccinationName vaccination on $formattedDate?")

        builder.setPositiveButton("Yes") { _, _ ->
            viewModel.confirmVaccination(scheduledVaccination)
        }

        builder.setNegativeButton("No") { _, _ ->
            viewModel.declineVaccination(scheduledVaccination)
        }

        val dialog: AlertDialog = builder.create()
        dialog.show()
    }

}