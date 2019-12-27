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
        view1Animation()
        view2Animation()
    }

    private fun view1Animation() {
        val view = findViewById<View>(R.id.view1)
        val drawable = view.background as GradientDrawable
        val color1 = ResourcesCompat.getColor(resources, R.color.colorPrimary, null)
        val color2 = ResourcesCompat.getColor(resources, R.color.colorAccent, null)

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

    private fun view2Animation() {
        val view2 = findViewById<View>(R.id.view2)
        val color1 = ResourcesCompat.getColor(resources, R.color.colorPrimary, null)
        val color2 = ResourcesCompat.getColor(resources, R.color.colorAccent, null)
        val evaluator = ArgbEvaluator()

        val gradientBackground = GradientDrawable().apply {
            gradientType = GradientDrawable.LINEAR_GRADIENT
            orientation = GradientDrawable.Orientation.LEFT_RIGHT
            colors = intArrayOf(color1, color2)
            view2.background = this
        }

        fun gradientAnimator(startColor: Int, endColor: Int, isStartPhase: Boolean) =
            ValueAnimator.ofFloat(0f, 1f).apply {
                addUpdateListener {
                    val value = it.animatedValue as Float
                    gradientBackground.apply {
                        val transformColor = evaluator.evaluate(value, endColor, startColor) as Int
                        colors = if (isStartPhase)
                            intArrayOf(transformColor, endColor)
                        else
                            intArrayOf(startColor, transformColor)
                    }
                }
            }

        val phase1 = gradientAnimator(color1, color2, true)
        val phase2 = gradientAnimator(color1, color2, false)
        val phase3 = gradientAnimator(color2, color1, true)
        val phase4 = gradientAnimator(color2, color1, false)

        AnimatorSet().apply {
            duration = 1000
            playSequentially(phase1, phase2, phase3, phase4)
            doOnEnd { start() }
        }.start()
    }
}
