package com.example.delawaretrackandtraceapp.screens.simpleshop

import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.delawaretrackandtraceapp.R
import com.example.delawaretrackandtraceapp.databinding.SimpleShopItemBinding
import com.example.delawaretrackandtraceapp.domain.Product

class SimpleShopAdapter(val clickListener: SimpleShopListener): ListAdapter<Product, ProductItemViewHolder>(SimpleShopDiffCallback()) {

    override fun onBindViewHolder(holder: ProductItemViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item, clickListener)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductItemViewHolder {
        return ProductItemViewHolder.from(parent)
    }
}

class ProductItemViewHolder private constructor(val binding: SimpleShopItemBinding) : RecyclerView.ViewHolder(binding.root){

    fun bind(item: Product, clickListener: SimpleShopListener) {
        binding.ProductPlus.setImageResource(R.drawable.plus)
        binding.product = item
        binding.clickListener = clickListener
        binding.ProductAmount.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable) {}
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                if(!binding.ProductAmount.text.toString().isNullOrEmpty()){
                    item.aantal = binding.ProductAmount.text.toString().toInt()
                }
            }
        })
    }

    companion object {
        fun from(parent: ViewGroup): ProductItemViewHolder {
            val layoutInflater = LayoutInflater.from(parent.context)
            val binding = SimpleShopItemBinding.inflate(layoutInflater)
            return ProductItemViewHolder(binding)
        }
    }
}

class SimpleShopDiffCallback: DiffUtil.ItemCallback<Product>(){
    override fun areItemsTheSame(oldItem: Product, newItem: Product): Boolean {
        return oldItem.productName == newItem.productName
    }

    override fun areContentsTheSame(oldItem: Product, newItem: Product): Boolean {
        return oldItem.equals(newItem)
    }
}

class SimpleShopListener(val clickListener: (product: Product) -> Unit) {
    fun onClick(product: Product) = clickListener(product)
}