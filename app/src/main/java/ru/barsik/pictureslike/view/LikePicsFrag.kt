package ru.barsik.pictureslike.view

import android.app.Application
import android.content.Context
import android.graphics.Bitmap
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.util.AttributeSet
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
import ru.barsik.pictureslike.databinding.FragmentLikePicsBinding
import ru.barsik.pictureslike.di.component.ViewModelComponent
import ru.barsik.pictureslike.domain.LikePicsViewModel
import ru.barsik.pictureslike.repo.model.PictureEntity
import javax.inject.Inject

class LikePicsFrag(private val app: Application) : BaseFragment(app) {

    companion object {
        fun newInstance(application: Application) = LikePicsFrag(application)
    }

    private lateinit var binding: FragmentLikePicsBinding

    var viewModel: LikePicsViewModel? = null
        @Inject set

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentLikePicsBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel?.getAllSavedPicturesLD()?.observe(viewLifecycleOwner){
            binding.llLikesContent.removeAllViews()
            for(i in it) {
                val card = PictureCard(app, picEntity = i.first)
                card.setImageBitmap(i.second)

                binding.llLikesContent.addView(card)
            }
        }
        viewModel?.getAllSavedPictures()
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
            findViewById<ImageButton>(R.id.ib_like).visibility = View.INVISIBLE
        }

        fun setImageBitmap(bitmap : Bitmap){
            findViewById<ImageView>(R.id.iv_pic).setImageBitmap(bitmap)
        }
    }
}