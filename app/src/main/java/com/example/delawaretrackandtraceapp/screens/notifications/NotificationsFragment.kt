package com.example.delawaretrackandtraceapp.screens.notifications

import android.graphics.Color
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.delawaretrackandtraceapp.R
import com.example.delawaretrackandtraceapp.database.notifications.NotificationDatabase
import com.example.delawaretrackandtraceapp.databinding.FragmentNotificationsBinding

class NotificationsFragment : Fragment() {

    private lateinit var binding: FragmentNotificationsBinding
    private lateinit var viewModel: NotificationsViewModel //by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_notifications, container, false)

        //setup the db connection
        val application = requireNotNull(this.activity).application
        val dataSource = NotificationDatabase.getInstance(application).notificationDatabaseDao

        //viewmodel
        val viewModelFactory = NotificationsViewModelFactory(dataSource, application)
        viewModel = ViewModelProvider(requireActivity(), viewModelFactory)[NotificationsViewModel::class.java]

        binding.apply {
            notificationsViewModel = viewModel
            lifecycleOwner = viewLifecycleOwner
        }

        val adapter = NotificationAdapter(NotificationListener {
            viewModel.updateNotification(it)
            val bundle = bundleOf("OrderNr" to it.orderId)
            findNavController()
                .navigate(R.id.action_notificationsFragment_to_orderDetailFragment, bundle)
        }, viewModel)
        binding.notificationsList.adapter = adapter

        binding.allNotifications.setOnClickListener {
            binding.newNotifications.setBackgroundColor(Color.parseColor("#EC4842"))
            binding.allNotifications.setBackgroundColor(Color.parseColor("#FFCC0000"))
            viewModel.showAllNotifications()
        }

        binding.newNotifications.setOnClickListener {
            binding.newNotifications.setBackgroundColor(Color.parseColor("#FFCC0000"))
            binding.allNotifications.setBackgroundColor(Color.parseColor("#EC4842"))
            viewModel.showNewNotifications()
        }

        viewModel.showNewNotifications()

        viewModel.allOrNew.observe(viewLifecycleOwner, Observer {
            if (it){
                adapter.submitList(viewModel.notificationsNew.value)
            }else{
                adapter.submitList(viewModel.notificationsAll.value)
            }
        })

        viewModel.notificationsNew.observe(viewLifecycleOwner, Observer {
            if (viewModel.allOrNew.value == true) adapter.submitList(it)
        })

        viewModel.notificationsAll.observe(viewLifecycleOwner, Observer {
            if (viewModel.allOrNew.value == false) adapter.submitList(it)
        })

        return binding.root
    }
}