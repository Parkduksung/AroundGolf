package com.example.aroundgolf.api.response

import com.example.aroundgolf.room.GolfEntity
import com.google.gson.annotations.SerializedName

data class Row(
    @SerializedName("BIZCOND_DIV_NM_INFO") var BIZCOND_DIV_NM_INFO: Any? = null,
    @SerializedName("BIZPLC_NM") var BIZPLC_NM: String? = null,
    @SerializedName("BSN_STATE_DIV_CD") var BSN_STATE_DIV_CD: String? = null,
    @SerializedName("BSN_STATE_NM") var BSN_STATE_NM: String? = null,
    @SerializedName("BULDNG_BUILDG_CNT") var BULDNG_BUILDG_CNT: Any? = null,
    @SerializedName("BULDNG_TOT_AR") var BULDNG_TOT_AR: Double? = null,
    @SerializedName("CLSBIZ_DE") var CLSBIZ_DE: Any? = null,
    @SerializedName("COPRTN_NM") var COPRTN_NM: Any? = null,
    @SerializedName("CULTUR_PHSTRN_INDUTYPE_NM") var CULTUR_PHSTRN_INDUTYPE_NM: String? = null,
    @SerializedName("DETAIL_INDUTYPE_NM") var DETAIL_INDUTYPE_NM: Any? = null,
    @SerializedName("INSRNC_SUBSCRB_YN_CD") var INSRNC_SUBSCRB_YN_CD: String? = null,
    @SerializedName("LEADER_CNT") var LEADER_CNT: Any? = null,
    @SerializedName("LICENSG_CANCL_DE") var LICENSG_CANCL_DE: Any? = null,
    @SerializedName("LICENSG_DE") var LICENSG_DE: String? = null,
    @SerializedName("LOCPLC_AR_INFO") var LOCPLC_AR_INFO: Any? = null,
    @SerializedName("LOCPLC_FACLT_TELNO") var LOCPLC_FACLT_TELNO: Any? = null,
    @SerializedName("MBER_RECRUT_TOT_PSNNUM_CNT") var MBER_RECRUT_TOT_PSNNUM_CNT: Any? = null,
    @SerializedName("PLVTINST_DIV_NM") var PLVTINST_DIV_NM: String? = null,
    @SerializedName("REFINE_LOTNO_ADDR") var REFINE_LOTNO_ADDR: String? = null,
    @SerializedName("REFINE_ROADNM_ADDR") var REFINE_ROADNM_ADDR: String? = null,
    @SerializedName("REFINE_WGS84_LAT") var REFINE_WGS84_LAT: String? = null,
    @SerializedName("REFINE_WGS84_LOGT") var REFINE_WGS84_LOGT: String? = null,
    @SerializedName("REFINE_ZIP_CD") var REFINE_ZIP_CD: String? = null,
    @SerializedName("ROADNM_ZIP_CD") var ROADNM_ZIP_CD: String? = null,
    @SerializedName("SIGUN_CD") var SIGUN_CD: String? = null,
    @SerializedName("SIGUN_NM") var SIGUN_NM: String? = null,
    @SerializedName("X_CRDNT_VL") var X_CRDNT_VL: String? = null,
    @SerializedName("Y_CRDNT_VL") var Y_CRDNT_VL: String? = null
) {

    fun toGolfEntity(): GolfEntity =
        GolfEntity(
            name = BIZPLC_NM.orEmpty(),
            address = REFINE_ROADNM_ADDR.orEmpty(),
            tel = LOCPLC_FACLT_TELNO.toString(),
            lat = REFINE_WGS84_LAT?.toDouble() ?: 0.0,
            log = REFINE_WGS84_LOGT?.toDouble() ?: 0.0,
            like = false
        )
}