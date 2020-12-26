package com.konst007.recyclerviewapp.ui.main

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.konst007.recyclerviewapp.Item
import com.konst007.recyclerviewapp.R
import kotlinx.android.synthetic.main.main_fragment.view.*

class MainFragment : Fragment(R.layout.main_fragment) {

    companion object {
        fun newInstance() = MainFragment()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val items = listOf(
            Item("first"),
            Item("second"),
            Item("third"),
            Item("forth")
        )
        view.list.adapter = ItemsAdapter(items)
        view.list2.adapter = ItemsAdapter2(items)

    }

}