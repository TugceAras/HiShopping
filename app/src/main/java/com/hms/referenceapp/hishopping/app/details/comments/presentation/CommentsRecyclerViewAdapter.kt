package com.hms.referenceapp.hishopping.app.details.comments.presentation

import android.app.Activity
import android.content.pm.ApplicationInfo
import android.content.pm.PackageManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.hms.lib.commonmobileservices.core.handleSuccess
import com.hms.lib.commonmobileservices.translate.manager.HuaweiGoogleTranslateManager
import com.hms.referenceapp.hishopping.R
import com.hms.referenceapp.hishopping.base.extensions.makeGone
import com.hms.referenceapp.hishopping.data.model.Comment
import com.hms.referenceapp.hishopping.data.model.LikeDislikeHolder
import com.hms.referenceapp.hishopping.databinding.ItemRecyclerviewCommentsListBinding


class CommentsRecyclerViewAdapter(
    private val activity: Activity,
    private var isCommentFragment: Boolean,
    private var likeDislikeList: LikeDislikeHolder?,
    private var productCommentList: MutableList<Comment>,
) : RecyclerView.Adapter<CommentsRecyclerViewAdapter.MainRecyclerViewHolder>() {

    internal var onItemSelected: (position: Int, comment: Comment, likeValueChange: Boolean) -> Unit =
        { _, _, _ -> }

    private var commentTargetLanguage = "default"

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MainRecyclerViewHolder {
        val binding = ItemRecyclerviewCommentsListBinding
            .inflate(LayoutInflater.from(parent.context), parent, false)
        return MainRecyclerViewHolder(
            binding
        )
    }

    override fun getItemCount(): Int {
        return if (isCommentFragment) {
            productCommentList.size
        } else if (!isCommentFragment && productCommentList.size < 4) {
            productCommentList.size
        } else {
            3
        }
    }

    override fun onBindViewHolder(holder: MainRecyclerViewHolder, position: Int) {
        productCommentList[position].let {
            holder.bindItems(
                activity,
                commentTargetLanguage,
                holder,
                it,
                position,
                likeDislikeList,
                onItemSelected
            )
        }

    }

    class MainRecyclerViewHolder(private val binding: ItemRecyclerviewCommentsListBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bindItems(
            activity: Activity,
            commentTargetLanguage: String,
            holder: MainRecyclerViewHolder,
            item: Comment,
            position: Int,
            likeDislikeList: LikeDislikeHolder?,
            onItemSelected: (position: Int, comment: Comment, likeValueChange: Boolean) -> Unit
        ) {
            val app: ApplicationInfo = activity.packageManager
                .getApplicationInfo(
                    activity.packageName,
                    PackageManager.GET_META_DATA
                )
            val bundle = app.metaData
            val apiKey = activity.getString(R.string.hms_api_key)
            if (commentTargetLanguage != "Default") {
                HuaweiGoogleTranslateManager(
                    activity
                ).performTranslate(
                    {
                        it.handleSuccess {
                            binding.textViewCommentsItemDescription.text = it.data ?: "error"
                        }
                    }, item.message.toString(),
                    commentTargetLanguage, activity, apiKey
                )
            } else {
                binding.textViewCommentsItemDescription.text =
                    item.message.toString()
            }

            if (item.message == "" || item.message == null) {
                binding.textViewCommentsItemDescription.makeGone()
                binding.textViewCommentsItemLikeCounter.makeGone()
                binding.imageViewCommentsItemLikeIcon.makeGone()
                binding.imageViewCommentsDislikeIcon.makeGone()
                binding.textViewCommentsItemDislikeCounter.makeGone()
            }

            binding.textViewCommentsItemDate.text = item.date
            binding.textViewCommentsItemOwner.text = item.author
            binding.ratingBarCommentsItem.rating = item.star.toFloat()
            binding.textViewCommentsItemLikeCounter.text = item.like.toString()
            binding.textViewCommentsItemDislikeCounter.text = item.dislike.toString()

            binding.imageViewCommentsItemLikeIcon.setOnClickListener {
                setLikeLists(
                    item.id,
                    binding.imageViewCommentsItemLikeIcon,
                    binding.imageViewCommentsDislikeIcon,
                    binding.textViewCommentsItemLikeCounter,
                    binding.textViewCommentsItemDislikeCounter,
                    position,
                    item,
                    likeDislikeList,
                    onItemSelected
                )
                clickButtonAnimation(it, activity)
            }

            binding.imageViewCommentsDislikeIcon.setOnClickListener {
                setDislikeLists(
                    item.id,
                    binding.imageViewCommentsItemLikeIcon,
                    binding.imageViewCommentsDislikeIcon,
                    binding.textViewCommentsItemLikeCounter,
                    binding.textViewCommentsItemDislikeCounter,
                    position,
                    likeDislikeList,
                    item,
                    onItemSelected
                )
                clickButtonAnimation(it, activity)
            }

            if (getLikeList(likeDislikeList).contains(item.id)) {
                binding.imageViewCommentsItemLikeIcon.apply {
                    setColorFilter(
                        ContextCompat.getColor(context, R.color.colorAcceptGreen),
                        android.graphics.PorterDuff.Mode.SRC_IN
                    )
                }
            }

            if (getDislikeList(likeDislikeList).contains(item.id)) {
                binding.imageViewCommentsDislikeIcon.apply {
                    setColorFilter(
                        ContextCompat.getColor(context, R.color.colorDeclineRed),
                        android.graphics.PorterDuff.Mode.SRC_IN
                    )
                }
            }
        }

        private fun setLikeLists(
            commentId: Int,
            likeIcon: ImageView, dislikeIcon: ImageView,
            likeCounterText: TextView, dislikeCounterText: TextView,
            position: Int,
            item: Comment,
            likeDislikeList: LikeDislikeHolder?,
            onItemSelected: (position: Int, comment: Comment, likeValueChange: Boolean) -> Unit
        ) {
            val likeIdList = getLikeList(likeDislikeList)
            val dislikeIdList = getDislikeList(likeDislikeList)
            when {
                likeIdList.contains(commentId) -> {
                    removeCommentIdFromLikedList(
                        commentId,
                        likeIcon,
                        likeCounterText,
                        position,
                        likeDislikeList,
                        item,
                        onItemSelected
                    )
                }
                dislikeIdList.contains(commentId) -> {
                    removeCommentIdFromDislikedList(
                        commentId,
                        dislikeIcon,
                        dislikeCounterText,
                        position,
                        likeDislikeList,
                        item,
                        onItemSelected
                    )
                    addCommentIdToLikedList(
                        commentId,
                        likeIcon,
                        likeCounterText,
                        likeDislikeList,
                        item,
                        position,
                        onItemSelected
                    )
                }
                else -> {
                    addCommentIdToLikedList(
                        commentId,
                        likeIcon,
                        likeCounterText,
                        likeDislikeList,
                        item,
                        position,
                        onItemSelected
                    )
                }
            }
        }

        private fun setDislikeLists(
            commentId: Int,
            likeIcon: ImageView,
            dislikeIcon: ImageView,
            likeCounterText: TextView,
            dislikeCounterText: TextView,
            position: Int,
            likeDislikeList: LikeDislikeHolder?,
            item: Comment,
            onItemSelected: (position: Int, comment: Comment, likeValueChange: Boolean) -> Unit
        ) {
            val dislikeIdList = getDislikeList(likeDislikeList)
            val likeIdList = getLikeList(likeDislikeList)
            when {
                dislikeIdList.contains(commentId) -> {
                    removeCommentIdFromDislikedList(
                        commentId,
                        dislikeIcon,
                        dislikeCounterText,
                        position,
                        likeDislikeList,
                        item,
                        onItemSelected
                    )
                }
                likeIdList.contains(commentId) -> {
                    removeCommentIdFromLikedList(
                        commentId,
                        likeIcon,
                        likeCounterText,
                        position,
                        likeDislikeList,
                        item,
                        onItemSelected
                    )
                    addCommentIdToDislikedList(
                        commentId,
                        dislikeIcon,
                        dislikeCounterText,
                        position,
                        likeDislikeList,
                        item,
                        onItemSelected
                    )
                }
                else -> {
                    addCommentIdToDislikedList(
                        commentId,
                        dislikeIcon,
                        dislikeCounterText,
                        position,
                        likeDislikeList,
                        item,
                        onItemSelected
                    )
                }
            }
        }

        private fun getLikeList(likeDislikeList: LikeDislikeHolder?): ArrayList<Int> {
            return likeDislikeList?.userLikeList as ArrayList<Int>
        }

        private fun getDislikeList(likeDislikeList: LikeDislikeHolder?): ArrayList<Int> {
            return likeDislikeList?.userDislikeList as ArrayList<Int>
        }

        private fun addCommentIdToLikedList(
            commentId: Int,
            likeIcon: ImageView,
            likeCounterText: TextView,
            likeDislikeList: LikeDislikeHolder?,
            item: Comment,
            position: Int,
            onItemSelected: (position: Int, comment: Comment, likeValueChange: Boolean) -> Unit
        ) {
            val likeIdList = getLikeList(likeDislikeList)
            likeIdList.add(commentId)
            likeIcon.apply {
                setColorFilter(
                    ContextCompat.getColor(context, R.color.colorAcceptGreen),
                    android.graphics.PorterDuff.Mode.SRC_IN
                )
            }
            val likeCounter = likeCounterText.text.toString().toInt() + 1
            likeCounterText.text = likeCounter.toString()
            item.like = likeCounterText.text.toString().toInt()
            onItemSelected.invoke(position, item, true)
        }

        private fun removeCommentIdFromLikedList(
            commentId: Int,
            likeIcon: ImageView,
            likeCounterText: TextView,
            position: Int,
            likeDislikeList: LikeDislikeHolder?,
            item: Comment,
            onItemSelected: (position: Int, comment: Comment, likeValueChange: Boolean) -> Unit
        ) {
            val likeIdList = getLikeList(likeDislikeList)

            likeIdList.remove(commentId)
            likeIcon.apply {
                setColorFilter(
                    ContextCompat.getColor(context, R.color.likeDislikeDefaultColor),
                    android.graphics.PorterDuff.Mode.SRC_IN
                )
            }
            val likeCounter = likeCounterText.text.toString().toInt() - 1
            likeCounterText.text = likeCounter.toString()
            item.like = likeCounter
            onItemSelected.invoke(position, item, true)
        }

        private fun addCommentIdToDislikedList(
            commentId: Int,
            dislikeIcon: ImageView,
            dislikeCounterText: TextView,
            position: Int,
            likeDislikeList: LikeDislikeHolder?,
            item: Comment,
            onItemSelected: (position: Int, comment: Comment, likeValueChange: Boolean) -> Unit
        ) {
            val dislikeIdList = getDislikeList(likeDislikeList)

            dislikeIdList.add(commentId)
            dislikeIcon.apply {
                setColorFilter(
                    ContextCompat.getColor(context, R.color.colorDeclineRed),
                    android.graphics.PorterDuff.Mode.SRC_IN
                )
            }

            val dislikeCounter = dislikeCounterText.text.toString().toInt() + 1
            dislikeCounterText.text = (dislikeCounter).toString()
            item.dislike = dislikeCounter
            onItemSelected.invoke(position, item, false)
        }

        private fun removeCommentIdFromDislikedList(
            commentId: Int,
            dislikeIcon: ImageView,
            dislikeCounterText: TextView,
            position: Int,
            likeDislikeList: LikeDislikeHolder?,
            item: Comment,
            onItemSelected: (position: Int, comment: Comment, likeValueChange: Boolean) -> Unit
        ) {
            val dislikeIdList = getDislikeList(likeDislikeList)

            dislikeIdList.remove(commentId)
            dislikeIcon.apply {
                setColorFilter(
                    ContextCompat.getColor(context, R.color.likeDislikeDefaultColor),
                    android.graphics.PorterDuff.Mode.SRC_IN
                )
            }
            val dislikeCounter = dislikeCounterText.text.toString().toInt() - 1
            dislikeCounterText.text = dislikeCounter.toString()
            item.dislike = dislikeCounter
            onItemSelected.invoke(position, item, false)
        }

        private fun clickButtonAnimation(view: View, activity: Activity) {
            val animation: Animation =
                AnimationUtils.loadAnimation(activity, R.anim.scale_animation)
            view.startAnimation(animation)
        }
    }

    fun updateDataSource(
        newDataSource: MutableList<Comment>,
    ) {
        this.productCommentList = newDataSource
        notifyDataSetChanged()
    }

    fun setCommentsLanguage(commentNewTargetLanguage: String) {
        commentTargetLanguage = commentNewTargetLanguage
        notifyDataSetChanged()
    }

}
