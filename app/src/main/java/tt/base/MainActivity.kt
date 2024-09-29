package tt.base

import android.os.Bundle
import androidx.viewbinding.ViewBinding
import tt.base.component.TTActivity
import tt.base.component.TTAdapter
import tt.base.component.TTHolder
import tt.base.databinding.ActivityMainBinding
import tt.base.databinding.TwoLineListItemBinding

/**
 * @author tianfeng
 */
class MainActivity : TTActivity<ActivityMainBinding>() {

    val adapter = object : TTAdapter<TwoLineListItemBinding, Data>() {
        override fun onBindViewHolder(holder: TTHolder<ViewBinding>, position: Int) {
            if (holder.vb is TwoLineListItemBinding) {
                val vb = holder.vb as TwoLineListItemBinding
                val item = items[position]
                vb.text1.text = item.text1
                vb.text2.text = item.text2
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setSupportActionBar(vb.toolbar)
        vb.recycler.adapter = adapter
        adapter.setItems(List(20) { i ->
            Data("Title$i", "Content$i")
        })
    }
}