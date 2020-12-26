package com.konst007.recyclerviewapp.ui.main

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.chauthai.swipereveallayout.SwipeRevealLayout
import com.chauthai.swipereveallayout.ViewBinderHelper
import com.konst007.recyclerviewapp.Item
import com.konst007.recyclerviewapp.R
import kotlinx.android.synthetic.main.view_item.view.front
import kotlinx.android.synthetic.main.view_item_2.view.*


class ItemsAdapter2(private val items: List<Item>) : RecyclerView.Adapter<ItemsViewHolder2>() {

    private val viewBinderHelper = ViewBinderHelper().apply {
        setOpenOnlyOne(true)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemsViewHolder2 {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.view_item_2, parent, false)
        return ItemsViewHolder2(view)
    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onBindViewHolder(holder: ItemsViewHolder2, position: Int) {
        viewBinderHelper.bind(holder.swipeRevealLayout, position.toString())
        holder.bind(items[position])
    }
}

class ItemsViewHolder2(itemView: View) : RecyclerView.ViewHolder(itemView) {

    val swipeRevealLayout: SwipeRevealLayout = itemView.root

    fun bind(item: Item) {
        itemView.front.text = item.name
    }
}
