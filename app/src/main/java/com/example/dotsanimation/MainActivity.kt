package com.example.dotsanimation

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.core.content.ContextCompat
import androidx.core.view.size
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.example.dotsanimation.databinding.ActivityMainBinding
import com.example.dotsanimation.viewmodel.MainViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {


    lateinit var binding: ActivityMainBinding
    lateinit var viewModel: MainViewModel
    private val NUM_DOTS = 15
    private val DOT_CHANGE_DELAY = 500L // 0.5 second
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this,R.layout.activity_main)
        viewModel = ViewModelProvider(this).get(MainViewModel::class.java)

        //add dot to the layout
        addDotsToContainer()

        //initialise index of circles
        viewModel.redDotIndex.value = binding.container.size -1;
        viewModel.greenDotIndex.value = binding.container.size -2;
        viewModel.blueDotIndex.value = binding.container.size -3;

        // Start the dot animation
        startDotAnimation()

        //set observer for every coloured dot index to change the drawable image

        viewModel.redDotIndex.observe(this) {
            if (it < 0) {
                viewModel.redDotIndex.value = viewModel.redDotIndex.value!! + NUM_DOTS
            }

            val redDot = binding.container.getChildAt(viewModel.redDotIndex.value!!) as ImageView
            redDot.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.red_circle))
        }

        viewModel.blueDotIndex.observe(this){
            if (it < 0) {
                viewModel.blueDotIndex.value = viewModel.blueDotIndex.value!! + NUM_DOTS
            }

            val blueDot = binding.container.getChildAt(viewModel.blueDotIndex.value!!) as ImageView
            blueDot.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.blue_circle))
        }

        viewModel.greenDotIndex.observe(this){
            if (it < 0) {
                viewModel.greenDotIndex.value = viewModel.greenDotIndex.value!! + NUM_DOTS
            }
            val greenDot = binding.container.getChildAt(viewModel.greenDotIndex.value!!) as ImageView
            greenDot.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.green_circle))
        }
    }

    private fun addDotsToContainer() {
        val layoutParams = LinearLayout.LayoutParams(
            100,
            100
        )
        for (i in 0 until NUM_DOTS) {
            val dot = ImageView(this)
            dot.layoutParams = layoutParams
            val circleDrawable = ContextCompat.getDrawable(this, R.drawable.circle)
            dot.setImageDrawable(circleDrawable)
            binding.container.addView(dot)
        }
    }

    private fun startDotAnimation() {
        //used coroutine to to do animation for defined DOT_CHANGE_DELAY
        //here statically repeated for 100 times
        CoroutineScope(Dispatchers.Main).launch {
            repeat(100){
                updateDotColors()
                delay(DOT_CHANGE_DELAY)
            }
        }
    }
    private fun updateDotColors() {
        // Reset all dots to default color
        for (i in 0 until binding.container.childCount) {
            val dot = binding.container.getChildAt(i) as ImageView
            dot.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.circle)) // Transparent hollow circle
        }
        // Move to the next dot
        viewModel.redDotIndex.value = viewModel.redDotIndex.value!!.minus(1)
        viewModel.greenDotIndex.value = viewModel.greenDotIndex.value!!.minus(1)
        viewModel.blueDotIndex.value = viewModel.blueDotIndex.value!!.minus(1)


    }

    override fun onDestroy() {
        super.onDestroy()
        lifecycleScope.cancel()
    }

}