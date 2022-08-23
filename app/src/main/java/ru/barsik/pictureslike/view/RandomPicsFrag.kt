package ru.barsik.pictureslike.view

import android.app.Application
import android.content.Context
import android.graphics.Bitmap
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.util.AttributeSet
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TableRow
import android.widget.TextView
import androidx.core.graphics.drawable.toBitmap
import com.google.android.material.card.MaterialCardView
import ru.barsik.pictureslike.R
import ru.barsik.pictureslike.databinding.FragmentRandomPicsBinding
import ru.barsik.pictureslike.di.component.ViewModelComponent
import ru.barsik.pictureslike.domain.RandomPicsViewModel
import ru.barsik.pictureslike.repo.model.PictureEntity
import javax.inject.Inject

class RandomPicsFrag(private val app : Application) : BaseFragment(app) {
    private val TAG = "RandomPicsFrag"
    companion object {
        fun newInstance(app: Application) = RandomPicsFrag(app)
    }

    var viewModel: RandomPicsViewModel? = null
        @Inject set

    private lateinit var binding: FragmentRandomPicsBinding
    private val likedPicturesIds = ArrayList<String>()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentRandomPicsBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Log.d(TAG, "onViewCreated")

        likedPicturesIds.clear()
        viewModel?.getAllSavedPictures()

        viewModel?.getAllSavedPicturesLD()?.observe(viewLifecycleOwner){
            likedPicturesIds.clear()
            for(p in it){
                likedPicturesIds.add(p.first.id)
            }
        }

        viewModel?.getImagesPage()?.observe(this.viewLifecycleOwner){
            Log.d(TAG, "observe: Page ${it.size} items")
            binding.table.removeAllViews()
            for(i in it.indices step 2) {
                val row = TableRow(app)

                val card1 = PictureCard(app, picEntity = it?.get(i)!!)
                if(likedPicturesIds.contains(it?.get(i)!!.id)) card1.setLike(true)
                val card2 = PictureCard(app, picEntity = it?.get(i+1)!!)
                if(likedPicturesIds.contains(it?.get(i+1)!!.id)) card2.setLike(true)

                row.addView(card1)
                row.addView(card2)
                binding.table.addView(row)
            }
        }

        viewModel?.getPicLD()?.observe(this.viewLifecycleOwner) {
            Log.d(TAG, "observe: Images ${it.size} ")
            it.forEachIndexed{ind, btm ->
                val row = binding.table.getChildAt(ind/2) as TableRow
                val card = row.getChildAt(ind % 2) as PictureCard
                card.setImageBitmap(btm)
            }
        }

        binding.btnDownload.setOnClickListener {
            viewModel?.downloadPagePictures()
        }

        binding.btnDownload.callOnClick()
    }

    override fun injectDependency(component: ViewModelComponent){
        component.inject(this)
    }

    inner class PictureCard(ctx: Context, attrs: AttributeSet? = null, private val picEntity: PictureEntity) : MaterialCardView(ctx, attrs) {

        private var isLike = false

        init {
            inflate(ctx, R.layout.picture_card ,this)
            this.radius = 6f
            findViewById<TextView>(R.id.tv_author).text = picEntity.author?:"Unknown"
            findViewById<TextView>(R.id.tv_identif).text = "#${picEntity.id?:"-1"}"
            findViewById<ImageButton>(R.id.ib_like).setOnClickListener{
                isLike = !isLike
                setLike(isLike)
                if(isLike){
                    viewModel?.savePicture(picEntity, findViewById<ImageView>(R.id.iv_pic).drawable.toBitmap())
                } else
                    viewModel?.deletePicture(picEntity)
            }
        }

        fun setLike(like : Boolean){
            val view = findViewById<ImageButton>(R.id.ib_like) as ImageButton
            isLike = like
            if(like){
                view.setImageResource(R.drawable.ic_star_24)
            } else {
                view.setImageResource(R.drawable.ic_star_outline_24)
            }
            viewModel?.getAllSavedPictures()
        }

        fun setImageBitmap(bitmap : Bitmap){
            findViewById<ImageView>(R.id.iv_pic).setImageBitmap(bitmap)
        }
    }
}