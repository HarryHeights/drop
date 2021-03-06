package mcneil.peter.drop.fragments

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatDelegate
import androidx.appcompat.widget.AppCompatImageView
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import mcneil.peter.drop.DropApp.Companion.firebaseUtil
import mcneil.peter.drop.R
import mcneil.peter.drop.activities.SettingsActivity
import mcneil.peter.drop.adapter.FeedAdapter
import mcneil.peter.drop.adapter.FeedClickListener
import mcneil.peter.drop.model.ACallback
import mcneil.peter.drop.model.Drop
import mcneil.peter.drop.model.User

class MainFragment : Fragment(), View.OnClickListener, FeedClickListener, ACallback<User> {
    private val TAG = this.javaClass.simpleName

    private lateinit var fm: FragmentManager
    private lateinit var con: FragmentActivity
    private lateinit var feedAdapter: FeedAdapter
    private lateinit var recycleView: RecyclerView
    private lateinit var welcome: TextView
    private lateinit var face: AppCompatImageView
    private lateinit var user: User
    private val dataset: MutableMap<String, Drop> = mutableMapOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true)
        feedAdapter = FeedAdapter(dataset, this)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_main, container, false)

        recycleView = view.findViewById(R.id.main_view)

        recycleView.adapter = feedAdapter
        recycleView.layoutManager = LinearLayoutManager(activity) as RecyclerView.LayoutManager

        fm = con.supportFragmentManager
        view.findViewById<ImageView>(R.id.f_m_settings).setOnClickListener(this)

        welcome = view.findViewById(R.id.f_m_welcome)
        face = view.findViewById(R.id.f_m_face)

        return view
    }

    override fun onAttach(context: Context?) {
        con = activity as FragmentActivity
        super.onAttach(context)
    }

    override fun onStart() {
        super.onStart()
        Log.d(TAG, "onStart: Trying to get User")
        firebaseUtil.getUser(this)
        updateUI()
    }

    override fun onResume() {
        super.onResume()
        updateUI()
    }

    ///////////////////////// View.OnClickListener overrides /////////////////////////
    override fun onClick(v: View) {
        when (v.id) {
            R.id.f_m_settings -> openSettings()
        }
    }

    override fun onItemClicked(v: View, d: Drop) {
        val dialog = DropDialogFragment.newInstance(d)
        dialog.show(fm, "Drop")
    }

    override fun callback(ret: User) {
        Log.d(TAG, "callback: User found")
        user = ret
        updateUI()
    }

    private fun openSettings() {
        Log.i(TAG, "openSettings: Clicked starting transition")
        startActivity(Intent(context, SettingsActivity::class.java))
    }

    private fun updateUI() {
        if (::feedAdapter.isInitialized) {
            Log.d(TAG, "updateUI: Feed Adapter is initialised")
            feedAdapter.generateFeedDrops()
        } else {
            Log.d(TAG, "updateUI: Feed Adapter is not initialised")
        }

        face.setImageResource(R.drawable.default_user_white)

        val name = if (::user.isInitialized) {
            user.name
        } else {
            null
        }

        val message = if (name != null) {
            " $name!"
        } else {
            "!"
        }

        Log.d(TAG, "updateUI: Updating message '$message'")
        welcome.text = resources.getString(R.string.f_m_welcome, message)
    }

}