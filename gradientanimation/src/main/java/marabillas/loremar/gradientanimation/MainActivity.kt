package marabillas.loremar.gradientanimation

import android.animation.AnimatorSet
import android.animation.ArgbEvaluator
import android.animation.ValueAnimator
import android.graphics.drawable.GradientDrawable
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.animation.doOnEnd
import androidx.core.content.res.ResourcesCompat

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    override fun onStart() {
        super.onStart()
        val view = findViewById<View>(R.id.gradient_view)
        val drawable = view.background as GradientDrawable
        val color1 = ResourcesCompat.getColor(resources, R.color.colorPrimary, null)
        val color2 = ResourcesCompat.getColor(resources, R.color.colorPrimaryDark, null)

        val end = 320 * resources.displayMetrics.density
        val evaluator = ArgbEvaluator()

        val first = ValueAnimator.ofFloat(0f, 1f).apply {
            addUpdateListener {
                val value = it.animatedValue as Float
                drawable.gradientRadius = end * value
                val newEndColor = evaluator.evaluate(value, color2, color1) as Int
                drawable.colors = intArrayOf(color1, newEndColor)
                drawable.setGradientCenter(0.5f * value, 0.5f)
            }
        }

        val second = ValueAnimator.ofFloat(0f, 1f).apply {
            addUpdateListener {
                val value = it.animatedValue as Float
                drawable.gradientRadius = end * value
                val newEndColor = evaluator.evaluate(value, color1, color2) as Int
                drawable.colors = intArrayOf(color2, newEndColor)
                drawable.setGradientCenter(0.5f * value, 0.5f)
            }
        }

        AnimatorSet().apply {
            duration = 5000
            playSequentially(first, second)
            doOnEnd { start() }
        }.start()
    }
}
