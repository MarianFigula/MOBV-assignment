package eu.mcomputing.mobv.mobvzadanie

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.ImageView
import androidx.constraintlayout.widget.ConstraintLayout

class CustomBottomNavLayout(
    context: Context,
    attrs: AttributeSet? = null
) : ConstraintLayout(context, attrs) {

    interface OnNavIconClickListener {
        fun onMapClick()
        fun onListClick()
        fun onProfileClick()
    }

    private var listener: OnNavIconClickListener? = null

    init {
        LayoutInflater.from(context).inflate(
            R.layout.custom_bottom_nav_layout,
            this, true
        )

// Setting up the click listeners
        findViewById<ImageView>(R.id.iconMap).setOnClickListener {
            listener?.onMapClick()
        }

        findViewById<ImageView>(R.id.iconList).setOnClickListener {
            listener?.onListClick()
        }

        findViewById<ImageView>(R.id.iconProfile).setOnClickListener {
            listener?.onProfileClick()
        }
//        // Setting up the click listeners directly
//        findViewById<ImageView>(R.id.iconMap).setOnClickListener {
//            findNavController().navigate(R.id.mapFragment)
//        }
//
//        findViewById<ImageView>(R.id.iconList).setOnClickListener {
//            findNavController().navigate(R.id.feedFragment)
//        }
//
//        findViewById<ImageView>(R.id.iconProfile).setOnClickListener {
//            findNavController().navigate(R.id.profileFragment)
//        }
    }

    // Method to set the listener
    fun setOnNavIconClickListener(listener: OnNavIconClickListener) {
        this.listener = listener
    }
}