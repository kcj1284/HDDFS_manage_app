package com.hdh.dev.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.hdh.dev.StartActivity
import com.hdh.dev.databinding.ItemAnnounceBinding
import com.hdh.dev.db.AnnounceEntity


class AnnounceRecyclerViewAdapter(val announceList: ArrayList<AnnounceEntity>)
    : RecyclerView.Adapter<AnnounceRecyclerViewAdapter.MyViewHolder>(){
    inner class MyViewHolder(private val binding : ItemAnnounceBinding):
        RecyclerView.ViewHolder(binding.root){

        private val context = binding.root.context

        fun bind(announce:AnnounceEntity){
            binding.tvAnnounceListSubject.text = announce.annTitle.toString()
            binding.tvAnnounceListContent.text = announce.annContent.toString()
            //클릭 이벤트
            val pos = adapterPosition
            if(pos!= RecyclerView.NO_POSITION)
            {
                itemView.setOnClickListener {
                    listener?.onItemClick(itemView,announce,pos)
                }
            }
        }

        val root = binding.root


    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding : ItemAnnounceBinding =
            ItemAnnounceBinding.inflate(
                LayoutInflater.from(parent.context),
                parent, false)
        return MyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.bind(announceList[position])
    }

    override fun getItemCount(): Int {
        return announceList.size
    }

    //클릭이벤트 인터페이스 추가
    interface OnItemClickListener{
        fun onItemClick(v:View, data: AnnounceEntity, pos : Int)
    }

    private var listener : OnItemClickListener? = null

    fun setOnItemClickListener(listener : OnItemClickListener) {
        this.listener = listener
    }

}