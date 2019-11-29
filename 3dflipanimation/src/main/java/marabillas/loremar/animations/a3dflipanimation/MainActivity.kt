package marabillas.loremar.animations.a3dflipanimation

import android.animation.ObjectAnimator
import android.animation.ValueAnimator
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.transition.TransitionManager
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView

class MainActivity : AppCompatActivity() {
    private var cardFlipped = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val button = findViewById<Button>(R.id.button)
        val card = findViewById<TextView>(R.id.card).apply {
            pivotX = 0f
            pivotY = height.toFloat() / 2f
            cameraDistance *= 2
        }

        button.setOnClickListener {
            if (!cardFlipped) {
                ObjectAnimator.ofFloat(card, "rotationY", -90f).run {
                    duration = 3000
                    start()
                }
                cardFlipped = true
            } else {
                ObjectAnimator.ofFloat(card, "rotationY", 0f).run {
                    duration = 3000
                    start()
                }
                cardFlipped = false
            }
        }
    }
}
