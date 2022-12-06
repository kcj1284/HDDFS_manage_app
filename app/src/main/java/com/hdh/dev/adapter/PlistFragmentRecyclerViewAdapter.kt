package com.hdh.dev.adapter

import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.FileProvider
import androidx.recyclerview.widget.RecyclerView
import com.hdh.dev.AnnounceContent
import com.hdh.dev.OnItemLongClickListener
import com.hdh.dev.ProductEdit
import com.hdh.dev.databinding.FragmentProductListItemBinding
import com.hdh.dev.db.ProductEntity
import java.io.File
import java.text.DecimalFormat

class PlistFragmentRecyclerViewAdapter(
    val productList: ArrayList<ProductEntity>,
    private val listener : OnItemLongClickListener) :
    RecyclerView.Adapter<PlistFragmentRecyclerViewAdapter.MyViewHolder>() {
    inner class MyViewHolder(binding: FragmentProductListItemBinding, cont : Context) :
        RecyclerView.ViewHolder(binding.root) {
        val tv_image = binding.productImage
        val tv_pname = binding.productNameFragitem
        val tv_pprice = binding.priceFragitem
        val tv_pstock = binding.stockFragitem

        //val hd_binding = binding
        val root = binding.root
        val context = cont//이 context는 FragmentActivity 타입이라그런지 Toast 메시지를 쓸수가 없네 ..?
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding: FragmentProductListItemBinding = FragmentProductListItemBinding.inflate(
            LayoutInflater.from(parent.context),
            parent, false
        )

        return MyViewHolder(binding, parent.context)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {

        //클릭하면 제품수정할수 있는 activity로
        holder.root.setOnClickListener {
            val intent = Intent(it.context, ProductEdit::class.java)
            intent.putExtra("pid", productList[position].pid.toString())
            intent.putExtra("pcode", productList[position].pcode)
            intent.putExtra("image", productList[position].image)
            intent.putExtra("category", productList[position].category)
            intent.putExtra("name", productList[position].pname)
            intent.putExtra("price", productList[position].price.toString())
            intent.putExtra("location", productList[position].loction)
            intent.putExtra("stock", productList[position].stock.toString())
            intent.putExtra("did", productList[position].did.toString())
            it.context.startActivity(intent) //아래와 무슨차인지 잘 모르겠네 .!?!
            //holder.context.startActivity(intent)
        }
        //길게 누르면 삭제 할 수 있게 다이알로그창 띄우기!
        holder.root.setOnLongClickListener {
            val productEntity : ProductEntity = ProductEntity(
                productList[position].pid,
                productList[position].pcode,
                productList[position].image,
                productList[position].category,
                productList[position].pname,
                productList[position].price,
                productList[position].loction,
                productList[position].stock,
                productList[position].did
            )
            listener.onLongClick(position, this)
            false
        }
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
        val price_dec = DecimalFormat("#,###")
        val price_str = price_dec.format(productList[position].price).toString()
        holder.tv_pprice.text = "가격 : "+price_str
        holder.tv_pstock.text = "재고수량 : "+productList[position].stock.toString()

    }

    override fun getItemCount(): Int {
        return productList.size
    }
}