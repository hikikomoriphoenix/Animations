package marabillas.loremar.expandingbottomsheet2


import android.animation.ObjectAnimator
import android.os.Bundle
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import android.view.ViewGroup.LayoutParams.MATCH_PARENT
import android.view.ViewGroup.LayoutParams.WRAP_CONTENT
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.animation.doOnEnd
import androidx.core.view.updateLayoutParams
import androidx.transition.*

class MainActivity : AppCompatActivity() {
    lateinit var root: ViewGroup

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val sheet = findViewById<ViewGroup>(R.id.expanding_sheet)
        val body = findViewById<ViewGroup>(R.id.expanding_sheet_body)
        val top = findViewById<ViewGroup>(R.id.expanding_sheet_top)

        var isExpanded = false

        var topContractedLeft = 0
        top.setOnClickListener {
            if (!isExpanded) {
                topContractedLeft = top.left

                val sheetTransition: Transition = ChangeBounds()
                val bodyTransition: Transition = Fade()

                val transitionSet = TransitionSet().apply {
                    addTransition(sheetTransition)
                    addTransition(bodyTransition)
                    duration = 500
                    doOnEnd {
                        isExpanded = true
                    }
                }

                TransitionManager.beginDelayedTransition(sheet, transitionSet)
                body.visibility = VISIBLE
                sheet.updateLayout { height = MATCH_PARENT }

                ObjectAnimator.ofInt(top, "left", top.left, 0)
                    .apply {
                        duration = 200
                        doOnEnd {
                            top.updateLayout { width = MATCH_PARENT }
                        }
                        start()
                    }
            } else {

                val sheetTransition: Transition = ChangeBounds()
                val bodyTransition: Transition = Fade()

                val transitionSet = TransitionSet().apply {
                    addTransition(sheetTransition)
                    addTransition(bodyTransition)
                    doOnEnd {
                        isExpanded = false
                    }
                }
                TransitionManager.beginDelayedTransition(sheet, transitionSet)
                body.visibility = GONE
                sheet.updateLayout { height = WRAP_CONTENT }

                ObjectAnimator.ofInt(top, "left", 0, topContractedLeft)
                    .apply {
                        startDelay = 200
                        duration = 100
                        doOnEnd {
                            top.updateLayout { width = WRAP_CONTENT }
                        }
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
