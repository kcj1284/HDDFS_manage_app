package com.hdh.dev.adapter

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.FileProvider
import androidx.recyclerview.widget.RecyclerView
import com.hdh.dev.databinding.FragmentProductListItemBinding
import com.hdh.dev.db.ProductEntity
import java.io.File

class PlistFragmentRecyclerViewAdapter(private val productList: List<ProductEntity>) :
    RecyclerView.Adapter<PlistFragmentRecyclerViewAdapter.MyViewHolder>() {
    inner class MyViewHolder(binding: FragmentProductListItemBinding, cont : Context) :
        RecyclerView.ViewHolder(binding.root) {
        val tv_image = binding.productImage
        val tv_pname = binding.productNameFragitem
        val tv_pprice = binding.priceFragitem
        val tv_pstock = binding.stockFragitem

        val context = cont
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding: FragmentProductListItemBinding = FragmentProductListItemBinding.inflate(
            LayoutInflater.from(parent.context),
            parent, false
        )
        Log.i("하", "잘되나")
        return MyViewHolder(binding, parent.context)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        Log.i("어댑터", "되나?")
        val imageFileName = productList[position].image
        val photoFile = File(
            ///data/user/0/com.hdh.dev/files/image
            File("${holder.context.filesDir}/image").apply {
                if(!this.exists()){
                    this.mkdirs()
                }
            },imageFileName

        )
        val photoUri = FileProvider.getUriForFile(
            holder.context,
            "com.hdh.dev.fileprovider", //인증
            photoFile // 찾을 파일
        )
        holder.tv_image.setImageURI(photoUri)
        holder.tv_pname.text = productList[position].pname
        holder.tv_pprice.text = productList[position].price.toString()
        holder.tv_pstock.text = productList[position].stock.toString()

    }

    override fun getItemCount(): Int {
        return productList.size
    }
}