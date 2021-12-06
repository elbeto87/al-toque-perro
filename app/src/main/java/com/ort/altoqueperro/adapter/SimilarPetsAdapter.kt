package com.ort.altoqueperro.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.ort.altoqueperro.R
import com.ort.altoqueperro.entities.RequestScore

class SimilarPetsAdapter(
    private var similarRequestList: MutableList<RequestScore>,
    val onSimilarPetsClick: (RequestScore) -> Unit
) : RecyclerView.Adapter<SimilarPetsAdapter.SimilarPetsHolder>() {
    class SimilarPetsHolder(v: View) : RecyclerView.ViewHolder(v) {
        private var view: View = v

        fun setScore(score: Int) {
            val txt: TextView = view.findViewById(R.id.txtPetFoundItemScore)
            txt.text = "${score}%"
        }

        fun setImage(url: String) {
            val img: ImageView = view.findViewById(R.id.imagePetItem)
            Glide.with(view).load(url).into(img)
        }

        fun getCardLayout(): CardView {
            return view.findViewById(R.id.cardItem)
        }

        fun setDesc(desc: String) {
            val txt: TextView = view.findViewById(R.id.txtPetFoundItemDesc)
            txt.text = desc
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SimilarPetsHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.pet_item_found, parent, false)
        return (SimilarPetsHolder(view))
    }

    override fun onBindViewHolder(holder: SimilarPetsHolder, position: Int) {
        val petScore = similarRequestList[position]
        holder.setScore(petScore.score)
        holder.setDesc(petScore.request?.pet.toString())
        petScore.request?.imageURL?.let { holder.setImage(it) }
        holder.getCardLayout().setOnClickListener {
            onSimilarPetsClick(petScore)
        }
    }

    override fun getItemCount(): Int {
        return similarRequestList.size
    }
}