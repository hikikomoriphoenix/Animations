package marabillas.loremar.expandingbottomsheet

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.os.Bundle
import android.view.View
import android.view.View.VISIBLE
import android.view.ViewGroup
import android.view.animation.AccelerateInterpolator
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.animation.doOnEnd
import androidx.core.view.updateLayoutParams
import androidx.transition.ChangeBounds
import androidx.transition.Transition
import androidx.transition.TransitionListenerAdapter
import androidx.transition.TransitionSet

class MainActivity : AppCompatActivity() {
    lateinit var root: ViewGroup

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val body = findViewById<ViewGroup>(R.id.expanding_sheet_body)
        val top = findViewById<ViewGroup>(R.id.expanding_sheet_top)

        var isExpanded = false

        val bodyTransition: Transition = ChangeBounds().apply {
            duration = 500
            interpolator = AccelerateInterpolator()
        }

        val topTransition: Transition = ChangeBounds().apply {
            addTarget(top)
            duration = 100
        }

        val transitionSet = TransitionSet().apply {
            addTransition(bodyTransition)
            addTransition(topTransition)
        }

        var topHeight = 0
        var topContractedLeft = 0
        var bodyContractedTop = 0
        top.setOnClickListener {
            if (!isExpanded) {
                body.visibility = VISIBLE
                topHeight = top.measuredHeight
                topContractedLeft = top.left
                bodyContractedTop = body.top
                val bodyLifter = ObjectAnimator.ofInt(body, "top", body.top, topHeight).apply {
                    addUpdateListener {
                        val bodyTop = it.animatedValue as Int
                        top.bottom = bodyTop
                        top.top = bodyTop - topHeight
                    }
                    duration = 500
                }
                val topExpander = ObjectAnimator.ofInt(top, "left", top.left, 0).apply {
                    duration = 100
                }
                AnimatorSet().apply {
                    playTogether(bodyLifter, topExpander)
                    doOnEnd { isExpanded = true }
                    start()
                }
            } else {
                val bodyDropper = ObjectAnimator.ofInt(body, "top", 0, bodyContractedTop).apply {
                    addUpdateListener {
                        val bodyTop = it.animatedValue as Int
                        top.bottom = bodyTop
                        top.top = bodyTop - topHeight
                    }
                    duration = 500
                }
                val topContractor = ObjectAnimator.ofInt(top, "left", 0, topContractedLeft).apply {
                    duration = 100
                    startDelay = 400
                }
                AnimatorSet().apply {
                    playTogether(bodyDropper, topContractor)
                    doOnEnd { isExpanded = false }
                    start()
                }

            }
        }
    }

    override fun onStart() {
        super.onStart()
        root = findViewById(android.R.id.content)
    }

    private fun View.updateLayout(block: ConstraintLayout.LayoutParams.() -> Unit) {
        updateLayoutParams<ConstraintLayout.LayoutParams> {
            block()
        }
    }

    private fun Transition.doOnEnd(block: TransitionListenerAdapter.() -> Unit) {
        addListener(object : TransitionListenerAdapter() {
            override fun onTransitionEnd(transition: Transition) {
                block()
            }
        })
    }
}