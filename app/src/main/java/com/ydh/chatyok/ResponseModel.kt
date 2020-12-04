package com.ydh.chatyok

import com.google.gson.annotations.SerializedName

data class ResponseModel(
    @field:SerializedName("canonical_ids")
    val canonicalIds: Int,
    val success: Int,
    val failure: Int,
    val results: List<ResultsItem>,
    @field:SerializedName("multicast_id")
    val multiCastId: Long
)

data class ResultsItem(
    @field:SerializedName("message_id")
    val messageId: String
)
