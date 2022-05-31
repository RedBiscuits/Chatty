import android.media.MediaPlayer

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SeekBar
import androidx.recyclerview.widget.RecyclerView
import com.datastructures.chatty.R
import kotlinx.android.synthetic.main.record_item.view.*
import java.io.File

class RecordAdapter(var records: ArrayList<File> ) : RecyclerView.Adapter<RecordAdapter.viewHolder>() {

    companion object {
        var mediaPlayer : MediaPlayer? = null
    }

    private lateinit var runnable:Runnable

    class viewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): viewHolder {
        return viewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.record_item, parent, false)
        )
    }

    override fun onBindViewHolder(holder: viewHolder, position: Int) {
        val current = records[position]
        holder.itemView.apply {
            play.setOnClickListener {
                if (mediaPlayer != null){
                    if (mediaPlayer!!.isPlaying){
                        mediaPlayer!!.stop()
                        mediaPlayer!!.reset()
                        mediaPlayer!!.release()
                    }
                }
                mediaPlayer = MediaPlayer()
                mediaPlayer!!.setDataSource(current.absolutePath)
                mediaPlayer!!.prepare()
                seekBar.max = mediaPlayer!!.duration

                runnable = Runnable {
                    seekBar.progress = mediaPlayer!!.currentPosition
                    handler.postDelayed(runnable, 1000)
                }
                handler.postDelayed(runnable, 1000)

                seekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
                    override fun onProgressChanged(p0: SeekBar?, progress: Int, fromUser: Boolean) {
                        if (fromUser) mediaPlayer!!.seekTo(progress)
                    }
                    override fun onStartTrackingTouch(p0: SeekBar?) {}

                    override fun onStopTrackingTouch(p0: SeekBar?) {}
                })
                mediaPlayer!!.start()
            }
        }
    }
    override fun getItemCount(): Int {
        return records.size - 1
    }
    fun startMedia(file: File , seekBar: SeekBar) {
        }
    }
