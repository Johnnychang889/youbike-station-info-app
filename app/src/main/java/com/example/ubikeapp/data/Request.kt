package com.example.ubikeapp.data

/*
    sno: 場站編號
    sna: 場站名稱
    tot: 場站總停車格
    sbi: 場站目前車輛數量
    bemp: 場站目前空位數量
    sarea: 場站所在行政區
    updateTime:北市府交通局數據平台經過處理後將資料存入DB的時間
*/
data class Request (
    val sno: String,
    var sna: String,
    val tot: Int,
    val sbi: Int,
    val bemp: Int,
    val sarea: String,
    val updateTime: String,
)
