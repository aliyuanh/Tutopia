/*
 * Copyright (c) 2018 Razeware LLC
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */

package com.raywenderlich.android.whysoserious.ui.jokes.favorite.list

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import com.google.firebase.storage.FirebaseStorage
import com.raywenderlich.android.whysoserious.R
import com.raywenderlich.android.whysoserious.model.Joke
import com.raywenderlich.android.whysoserious.ui.jokes.all.list.JokeHolder

class FavoriteJokeAdapter(
    private val onFavoriteClickHandler: (Joke) -> Unit
) : RecyclerView.Adapter<JokeHolder>() {

  private val items = mutableListOf<Joke>()
    var numtimes = 1

  override fun getItemCount() = items.size

  fun setData(data: List<Joke>) {
    items.clear()
    items.addAll(data)
    notifyDataSetChanged()
  }

  override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): JokeHolder {
    val view = LayoutInflater.from(parent.context).inflate(R.layout.item_joke, parent, false)

    return JokeHolder(view, onFavoriteClickHandler)
  }

  override fun onBindViewHolder(holder: JokeHolder, position: Int){
    holder.displayData(items[position])
      setPics(items[position],holder)
  }

    fun setPics(joke: Joke, holder: JokeHolder): Bitmap?{
        val conf = Bitmap.Config.ARGB_8888 // see other conf types

        var bitmap: Bitmap? = null
        Log.d("num: ", numtimes.toString())

        val nameAuthor = joke.authorName
        val storage = FirebaseStorage.getInstance()
        val storageRef = storage.reference
        val pathReference = storageRef.child("uploads/$nameAuthor.jpg")
        Log.d("path ", pathReference.toString())
        //  Toast.makeText(parent.context, pathReference.toString(), Toast.LENGTH_LONG).show()
        val ONE_MEGABYTE = (1024*1024*5).toLong()
        pathReference.getBytes(ONE_MEGABYTE).addOnSuccessListener {bytes->
            val bmp = BitmapFactory.decodeByteArray(bytes,0,bytes.size)
            bitmap = bmp
            if(bitmap != null){
                Log.d("bitmap ", "is not null in setter")
                bmp(bitmap, holder)
            }else{
                Log.d("bitmap ", "nulllll in setter")
                bmp(null, holder)

            }
            //view.findViewById<ImageView>(com.raywenderlich.android.whysoserious.R.id.tutorPic).setImageBitmap(bmp)
            Log.d("author name", nameAuthor +" "+ numtimes)
            Log.d("path ", pathReference.toString())
            // return@addOnSuccessListener
        }.addOnFailureListener {
            //Toast.makeText(parent.context,"oh no ", Toast.LENGTH_LONG).show()
            Log.d("fail ", "oh no")
        }.addOnCompleteListener{
            bmp(bitmap, holder)
        }
        numtimes++

        if(bitmap != null){
            Log.d("bitmap ", "is not null in setPics")
        }else{
            Log.d("bitmap ", "nulllll in setPics")
        }
        return bitmap
    }
    fun bmp(bitmap: Bitmap?, holder: JokeHolder){
        holder.changeImage(bitmap!!)
    }

  fun clear() {
    items.clear()
    notifyDataSetChanged()
  }
}