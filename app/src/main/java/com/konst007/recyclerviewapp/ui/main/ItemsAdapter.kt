package com.konst007.recyclerviewapp.ui.main

import android.annotation.SuppressLint
import android.graphics.Canvas
import android.util.Log
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.ItemTouchHelper.ACTION_STATE_SWIPE
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.konst007.recyclerviewapp.Item
import com.konst007.recyclerviewapp.R
import kotlinx.android.synthetic.main.view_item.view.*
import java.lang.Float.min


class ItemsAdapter(private val items: List<Item>) : RecyclerView.Adapter<ItemsViewHolder>() {

    private val touchHelper = TouchHelper()

    override fun onAttachedToRecyclerView(recyclerView: RecyclerView) {
        super.onAttachedToRecyclerView(recyclerView)
        touchHelper.attachToRecyclerView(recyclerView)
    }

    override fun onDetachedFromRecyclerView(recyclerView: RecyclerView) {
        touchHelper.attachToRecyclerView(null)
        super.onDetachedFromRecyclerView(recyclerView)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemsViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.view_item, parent, false)
        return ItemsViewHolder(view)
    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onBindViewHolder(holder: ItemsViewHolder, position: Int) {
        holder.bind(items[position])
    }
}

class ItemsViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    val back: View = itemView.back
    val front: View = itemView.front

    fun bind(item: Item) {
        itemView.front.text = item.name
    }
}

private class TouchHelper : ItemTouchHelper(MyCallback())

private class MyCallback : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.START or ItemTouchHelper.END) {

    private var swipeBack = false
    private var buttonShowed = false

    override fun onMove(
        recyclerView: RecyclerView,
        viewHolder: ViewHolder,
        target: ViewHolder
    ): Boolean = false

    override fun onSwiped(viewHolder: ViewHolder, direction: Int) {
    }

    override fun onChildDraw(
        c: Canvas,
        recyclerView: RecyclerView,
        viewHolder: ViewHolder,
        dX: Float,
        dY: Float,
        actionState: Int,
        isCurrentlyActive: Boolean
    ) {
        Log.d("checkk", "onChildDraw buttonShowed=$buttonShowed")
        val view = (viewHolder as ItemsViewHolder).front
        if (actionState == ACTION_STATE_SWIPE) {
            Log.d("checkk", "onChildDraw ACTION_STATE_SWIPE")
            val backWidth = viewHolder.back.width.toFloat()
            if (buttonShowed) {
                val translationX = min(dX, -backWidth)
                getDefaultUIUtil().onDraw(c, recyclerView, view, translationX, dY, actionState, isCurrentlyActive);
            } else {
                setTouchListener(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
            }
        }

        if (!buttonShowed) {
            getDefaultUIUtil().onDraw(c, recyclerView, view, dX, dY, actionState, isCurrentlyActive);
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun setTouchListener(
        c: Canvas,
        recyclerView: RecyclerView,
        viewHolder: ViewHolder,
        dX: Float,
        dY: Float,
        actionState: Int,
        isCurrentlyActive: Boolean
    ) {
        recyclerView.setOnTouchListener { _, event ->
            swipeBack =
                event.action == MotionEvent.ACTION_CANCEL || event.action == MotionEvent.ACTION_UP
            val buttonWidth = (viewHolder as ItemsViewHolder).back.width.toFloat()
            if (swipeBack) {
                if (dX < -buttonWidth) buttonShowed = true
                if (buttonShowed) {
                    setTouchDownListener(
                        c,
                        recyclerView,
                        viewHolder,
                        dX,
                        dY,
                        actionState,
                        isCurrentlyActive
                    )
                    setItemsClickable(recyclerView, false)
                }
            }
            false
        }
    }

    private fun setItemsClickable(
        recyclerView: RecyclerView,
        isClickable: Boolean
    ) {
        for (i in 0 until recyclerView.childCount) {
            recyclerView.getChildAt(i).isClickable = isClickable
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun setTouchDownListener(
        c: Canvas,
        recyclerView: RecyclerView,
        viewHolder: ViewHolder,
        dX: Float,
        dY: Float,
        actionState: Int,
        isCurrentlyActive: Boolean
    ) {
        recyclerView.setOnTouchListener { _, event ->
            if (event.action == MotionEvent.ACTION_DOWN) {
                setTouchUpListener(
                    c,
                    recyclerView,
                    viewHolder,
                    dX,
                    dY,
                    actionState,
                    isCurrentlyActive
                )
            }
            false
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun setTouchUpListener(
        c: Canvas,
        recyclerView: RecyclerView,
        viewHolder: ViewHolder,
        dX: Float,
        dY: Float,
        actionState: Int,
        isCurrentlyActive: Boolean
    ) {
        recyclerView.setOnTouchListener { _, event ->
            if (event.action == MotionEvent.ACTION_UP) {
                getDefaultUIUtil().onDraw(
                    c,
                    recyclerView,
                    (viewHolder as ItemsViewHolder).front,
                    0f,
                    dY,
                    actionState,
                    isCurrentlyActive
                )
                recyclerView.setOnTouchListener { _, _ -> false }
                setItemsClickable(recyclerView, true)
                swipeBack = false
                buttonShowed = false
            }
            false
        }
    }

    override fun convertToAbsoluteDirection(flags: Int, layoutDirection: Int): Int {
        Log.d("checkk", "convertToAbsoluteDirection, swipeBack=$swipeBack, buttonShowed=$buttonShowed")
        if (swipeBack) {
            swipeBack = buttonShowed
            return 0
        }
        return super.convertToAbsoluteDirection(flags, layoutDirection)
    }
}