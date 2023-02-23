package com.kotlinx.exoplayerdemo.net

import android.os.Parcelable
import android.text.TextUtils
import kotlinx.android.parcel.Parcelize

//class VideoDOT(
//    val total: Int,
//    val size: Int,
//    val current: Int,
//    val optimizeCountSql: Boolean,
//    val hitCount: Boolean,
//    val searchCount: Boolean,
//    val records: MutableList<VideoBean>
//)
//
//data class VideoBean constructor(
//    val id: String="",
//    val tenantId: String?=null,
//    val userId: String="",
//    val shopId: String?=null,
//    val skuId: String?=null,
//    val spuId: String?=null,
//    val deliveryArea: String?=null,
//    val advertisementUrl: String?=null,
//    val backgroundColor: String?=null,
//    val shelfPosition: String?=null,
//    val advertisementAmount: String?=null,
//    val advertisementSurplus: String?=null,
//    val useStartTime: String?=null,
//    val useEndTime: String?=null,
//    val advertisementTitle: String?=null,
//    val advertisementCopywriting: String?=null,
//    val advertisementVideo: String?=null,
//    val advertisementLink: String?=null,
//    val advertisementLabel: String?=null,
//    val videoStatus: String?=null,
//    val examineStatus: String?=null,
//    val autoExamineStatus: String?=null,
//    val payMethod: String?=null,
//    val rejectReason: String?=null,
//    val createTime: String?=null,
//    val updateTime: String?=null,
//    val likeGain: String?=null,
//    val forwardGain: String?=null,
//    val commentGain: String?=null,
//    val followGain: String?=null,
//    val shopName: String?=null,
//    val goodsSpu: String?=null,
//    val areaCode: String?=null,
//    val areaName: String?=null,
//    val usedDate: String?=null,
//    val outTradeNo: String?=null,
//    val beginTime: String?=null,
//    val endTime: String?=null,
//    val categoryType: String?=null,
//    val previewUrl: String="",
//    val shopImgUrl: String?=null,
//    var isFollow: String?=null,
//    var isLike: Boolean=false,
//    var commentNum: String="",
//    var likeNum: String="",
//    val forwardNum: String?=null,
//    val videoType: String?=null,
//    val nickName: String?=null,
//    val headimgUrl: String="",
//    val headImgUrl: String="",
//    val viewsNum: String?=null,
//    val sexScope: String?=null,
//    val ageScope: String?=null,
//    val bid: String?=null,
//    val dayBudget: String?=null,
//    val runTimeStart: String?=null,
//    val runTimeEnd: String?=null,
//    val advertisingInfo: Any?=null,
//    val makerProduct: String?=null,
//    val iosUrl: String?=null,
//    val androidUrl: String?=null,
//    val companyName: String?=null,
//    val isPay: String?=null,
//    val type: String?=null,
//    val commentContent: String?=null,
//    val positioning: String?=null,
//    var currentItemType: Int = 0,
//
//    val delFlag: Int = 0,
//    val localPageUrl: String?=null,
//    val positionStyle: String?=null
//
////    val advertisementCopywriting: String,
////    val advertisementVideo: String,
////    val ageScope: String,
////    val bid: String,
////    val commentNum: Int,//String
////    val dayBudget: String,
////    val delFlag: Int,
////    val examineStatus: Int,//String
////    val forwardNum: Int,//String
////    val headimgUrl: String,
////    val id: String,
////    val isFollow: Int,//String
////    val isLike: String,//Boolean
////    val isPay: String,
////    val likeNum: Int,//String
////    val localPageUrl: String,
////    val nickName: String,
////    val payMethod: Int,//String
////    val positionStyle: String,
////    val previewUrl: String,
////    val runTimeEnd: String,
////    val runTimeStart: String,
////    val sexScope: String,
////    val userId: Any,
////    val videoStatus: Int,//String
////    val videoType: String,
////    val viewsNum: Int//String
//): MultiItemEntity {
//
//
//    fun likeGainStr() = if (TextUtils.isEmpty(likeGain)) "0" else likeGain
//
//    override val itemType: Int = currentItemType
//
//    companion object {
//        val PUBLISH_VIDEO: Int = 1
//        val NORMAL_VIDEO: Int = 0
//    }
//
//    fun zanNumShow(current: Boolean): String {
//        if (NumberUtils.isNumeric(likeNum)) {
//            var toInt = likeNum.toInt()
//            current.yes {
//                toInt++
//            }.otherwise {
//                toInt--
//            }
//            likeNum
//            likeNum = if (toInt <= 0) {
//                "赞"
//            } else {
//                toInt.toString()
//            }
//        } else {
//            var currentLike = 0
//            current.yes {
//                currentLike++
//            }.otherwise {
//                currentLike--
//            }
//            likeNum = if (currentLike <= 0) {
//                "赞"
//            } else {
//                currentLike.toString()
//            }
//        }
//        return likeNum
//    }
//}


class VideoDOT(
    val total: Int,
    val size: Int,
    val current: Int,
    val optimizeCountSql: Boolean,
    val hitCount: Boolean,
    val searchCount: Boolean,
    val records: MutableList<VideoBean>
)

data class VideoBean constructor(
    val id: String = "",
    val tenantId: String? = null,
    val userId: String = "",
    val shopId: String? = null,
    val skuId: String? = null,
    val spuId: String? = null,
    val deliveryArea: String? = null,
    val advertisementUrl: String? = null,
    val backgroundColor: String? = null,
    val shelfPosition: String? = null,
    val advertisementAmount: String? = null,
    val advertisementSurplus: String? = null,
    val useStartTime: String? = null,
    val useEndTime: String? = null,
    val advertisementTitle: String? = null,
    val advertisementCopywriting: String? = null,
    val advertisementVideo: String? = null,
    val advertisementLink: String? = null,
    val advertisementLabel: String? = null,
    val videoStatus: String? = null,
    val examineStatus: String? = null,
    val autoExamineStatus: String? = null,
    val payMethod: String? = null,
    val rejectReason: String? = null,
    val createTime: String? = null,
    val updateTime: String? = null,
    val likeGain: String? = null,
    val forwardGain: String? = null,
    val commentGain: String? = null,
    val followGain: String? = null,
    val shopName: String? = null,
    val goodsSpu: String? = null,
    val areaCode: String? = null,
    val areaName: String? = null,
    val usedDate: String? = null,
    val outTradeNo: String? = null,
    val beginTime: String? = null,
    val endTime: String? = null,
    val categoryType: String? = null,
    val previewUrl: String = "",
    val shopImgUrl: String? = null,
    var isFollow: String? = null,
    var isLike: Boolean = false,
    var commentNum: String = "",
    var likeNum: String = "",
    val forwardNum: String? = null,
    val videoType: String? = null,
    val nickName: String? = null,
    val headimgUrl: String = "",
    val headImgUrl: String = "",
    val viewsNum: String? = null,
    val sexScope: String? = null,
    val ageScope: String? = null,
    val bid: String? = null,
    val dayBudget: String? = null,
    val runTimeStart: String? = null,
    val runTimeEnd: String? = null,
    val advertisingInfo: Any? = null,
    val makerProduct: String? = null,
    val iosUrl: String? = null,
    val androidUrl: String? = null,
    val companyName: String? = null,
    val isPay: String? = null,
    val type: String? = null,
    val commentContent: String? = null,
    val positioning: String? = null,
    var currentItemType: Int = 0,
    var shopInfo: VideoShopInfo? = null,
    val delFlag: Int = 0,
    val localPageUrl: String? = null,
    val positionStyle: String? = null
)

@Parcelize
data class VideoShopInfo(
    val accId: String? = null,
    val address: String? = null,
    val addressCode: String? = null,
    val advType: String? = null,
    val agentPerformanceCode: String? = null,
    val appUserId: String? = null,
    val bond: Int,
    val branchStoreName: String? = null,
    val businessLicenseName: String? = null,
    val businessLicensePhoto: String? = null,
    val city: String? = null,
    val cityCode: String? = null,
    val closingTime: String? = null,
    val code: String? = null,
    val collectCount: String? = null,
    val collectId: String? = null,
    val country: String? = null,
    val couponInfoList: String? = null,
    val couponUserList: String? = null,
    val createTime: String? = null,
    val delFlag: String? = null,
    val detail: String? = null,
    val distance: String? = null,
    val enable: String? = null,
    val endTime: String? = null,
    val follow: String? = null,
    val goodsUrls: String? = null,
    val hotelLevel: String? = null,
    val id: String? = null,
    val idCardName: String? = null,
    val idCardNo: String? = null,
    val imgUrl: String? = null,
    val isExacte: String? = null,
    val isPay: String? = null,
    val isSettledTwo: String? = null,
    val latitude: String? = null,
    val listGoodsSpu: String? = null,
    val localPageUrl: String? = null,
    val longitude: String? = null,
    val mainStore: String? = null,
    val mainStoreId: String? = null,
    val managerPhone: String? = null,
    val name: String? = null,
    val nickName: String? = null,
    val openingTime: String? = null,
    val operationStatus: String? = null,
    val password: String? = null,
    val phone: String? = null,
    val province: String? = null,
    val provinceCode: String? = null,
    val runFee: Int,
    val saleNum: Int,
    val servicePhone: String? = null,
    val shopAgentId: String? = null,
    val shopGrade: String? = null,
    val shopInfoIsCheck: String? = null,
    val shopInfoList: String? = null,
    val shopStoreManager: String? = null,
    val shopType: String? = null,
    val shopownerName: String? = null,
    val shoppingCartList: String? = null,
    val socialCreditCode: String? = null,
    val stock: Int,
    val storeOwnership: String? = null,
    val tenantId: String? = null,
    val totalIncome: Int,
    val treeNames: String? = null,
    val treeSorts: String?,
    val updateTime: String? = null,
    val userId: String? = null,
    val userName: String? = null,
    val whetherChain: String? = null,
    val withdrawalPeriod: Int,
    val wxMchId: String? = null,
    val goodsSpuNum: Int? = null
) : Parcelable