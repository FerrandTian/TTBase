package tt.base

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.appcompat.view.ActionMode
import androidx.recyclerview.selection.SelectionPredicates
import androidx.recyclerview.selection.SelectionTracker
import androidx.viewbinding.ViewBinding
import tt.base.component.TTActivity
import tt.base.component.TTAdapter
import tt.base.component.TTHolder
import tt.base.component.TTOnClickListener
import tt.base.databinding.ActivityMainBinding
import tt.base.databinding.TwoLineListItemBinding
import tt.base.utils.dp2px
import tt.base.utils.toast
import tt.base.widget.TTSpacingItemDecoration

/**
 * @author tianfeng
 */
class MainActivity : TTActivity<ActivityMainBinding>(), ActionMode.Callback,
    TTOnClickListener<TwoLineListItemBinding, Data> {
    private var actionMode: ActionMode? = null

    val adapter = object : TTAdapter<TwoLineListItemBinding, Data>() {
        override fun onBindViewHolder(holder: TTHolder<ViewBinding>, position: Int) {
            if (holder.vb is TwoLineListItemBinding) {
                holder as TTHolder<TwoLineListItemBinding>
                val vb = holder.vb
                val item = items[position]
                vb.text1.text = item.text1
                vb.text2.text = item.text2
                if (hasSelection) {
                    vb.checkbox.isChecked = isSelected(holder.itemDetails.selectionKey)
                    vb.checkbox.visibility = View.VISIBLE
                } else {
                    vb.checkbox.visibility = View.GONE
                }
                setClickListener(this@MainActivity, holder, item, holder.itemView)
            }
        }

        override fun getItemId(position: Int): Long {
            return items[position].id
        }

        override fun getItem(key: Long): Data? {
            return items.find { it.id == key }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setSupportActionBar(vb.toolbar)
        vb.recycler.addItemDecoration(TTSpacingItemDecoration(dp2px(16f).toInt()))
        vb.recycler.adapter = adapter
        adapter.setItems(List(20) { i ->
            Data(i.toLong(), "Title$i", "Content")
        })
        adapter.enableSelection(SelectionPredicates.createSelectAnything(), object :
            SelectionTracker.SelectionObserver<Long>() {
            override fun onItemStateChanged(key: Long, selected: Boolean) {}

            override fun onSelectionRefresh() {}

            override fun onSelectionChanged() {
                if (adapter.hasSelection) {
                    if (actionMode == null) {
                        actionMode = startSupportActionMode(this@MainActivity)
                    }
                    actionMode?.title = getString(R.string.fill_selection, adapter.selectionSize)
                } else {
                    actionMode?.finish()
                }
            }

            override fun onSelectionRestored() {}
        })
    }

    override fun onCreateActionMode(mode: ActionMode, menu: Menu): Boolean {
        mode.menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onPrepareActionMode(mode: ActionMode, menu: Menu): Boolean {
        return false
    }

    override fun onActionItemClicked(mode: ActionMode, item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_delete -> {
                if (adapter.hasSelection) {
                    adapter.selectionItems.forEach {
                        adapter.remove(it)
                    }
                }
                adapter.clearSelection()
                return true
            }
        }
        return false
    }

    override fun onDestroyActionMode(mode: ActionMode) {
        adapter.clearSelection()
        actionMode = null
    }

    override fun onClick(v: View, h: TTHolder<TwoLineListItemBinding>, t: Data?) {
        t?.let { toast(it.text1) }
    }
}