package marabillas.loremar.animationdrawable

import android.graphics.drawable.AnimationDrawable
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import marabillas.loremar.animationdrawable.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)

        //(binding.colorView.drawable as AnimationDrawable).start()

        (binding.bouncyBug.drawable as AnimationDrawable).start()
    }
}
