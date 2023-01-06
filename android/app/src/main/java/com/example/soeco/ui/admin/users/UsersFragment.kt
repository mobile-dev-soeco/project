package com.example.soeco.ui.admin.users

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.PopupMenu
import android.widget.Toast
import androidx.core.view.get
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.soeco.R
import com.example.soeco.TAG
import com.example.soeco.data.Models.CustomData
import com.example.soeco.databinding.FragmentUsersBinding
import com.example.soeco.utils.viewModelFactory

class UsersFragment : Fragment() {

    private val usersViewModel by viewModels<UsersViewModel> { viewModelFactory }
    private val navigation: NavController by lazy { findNavController() }

    private lateinit var binding: FragmentUsersBinding
    private lateinit var adapter: UsersAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return FragmentUsersBinding.inflate(inflater, container, false)
            .also { this.binding = it }
            .root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        adapter = UsersAdapter(::handleMenuClick)

        // Navigate to register user fragment on button click
        binding.btnAddUser.setOnClickListener {
            navigation.navigate(R.id.action_usersFragment_to_registerUser)
        }

        // Recycler view
        binding.rvUsers.adapter = adapter

        binding.rvUsers.layoutManager = LinearLayoutManager(this.context, LinearLayoutManager.VERTICAL, false)

        usersViewModel.usersLiveData.observe(viewLifecycleOwner) { newUsers ->
            adapter.submitList(newUsers)
        }
    }

    override fun onResume() {
        super.onResume()
        usersViewModel.getUsers()
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun handleMenuClick(view: View, user: CustomData) {
        val popupMenu = PopupMenu(this.context, view)

        popupMenu.inflate(R.menu.user_item_menu)

        popupMenu.setOnMenuItemClickListener(object: PopupMenu.OnMenuItemClickListener{

            override fun onMenuItemClick(item: MenuItem?): Boolean {
                when(item?.itemId){
                    R.id.delete_user -> {
                        Log.v(TAG(), "deleting user with email ${user.email}")
                        usersViewModel.deleteUser(
                            user,
                            onSuccess = { email ->
                                Toast.makeText(context, "User $email deleted", Toast.LENGTH_SHORT).show()
                                usersViewModel.getUsers()
                            },
                            onError = {
                                Toast.makeText(context, "Error: Deletion failed", Toast.LENGTH_SHORT).show()
                            }
                        )
                        adapter.notifyDataSetChanged()
                        return true
                    }
                    R.id.edit_user -> {
                        Log.v(TAG(), "Edit user ${user.email}")
                        val action = UsersFragmentDirections.actionUsersFragmentToEditUserFragment(user.firstname!!, user.lastname!!, user.role!!, user.realmUserId?: "", user.email!!)
                        navigation.navigate(action)
                        return true
                    }
                }
                return false
            }
        })
        popupMenu.show()
    }
}