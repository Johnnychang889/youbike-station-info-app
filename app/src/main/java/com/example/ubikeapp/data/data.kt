package com.example.ubikeapp.data

object data {
    private var metadataList: List<Request> = listOf()
    private var currentDataList: List<Request> = listOf()

    //getter
    fun getCurrentSize(): Int {
        return this.currentDataList.size
    }
    fun getCurrentDataList(): List<Request> {
        return this.currentDataList
    }
    fun getCurrentSnaList(): List<String> {
        return this.currentDataList.map { it.sna }
    }

    //setter
    fun setDataList(result: List<Request>) {
        this.currentDataList = result
        this.metadataList = result
    }

    fun oftenUseFilter(oftenUseList: ArrayList<String>){
        currentDataList = metadataList
        currentDataList = currentDataList.filter { oftenUseList.contains(it.sna) }
    }
    fun AreaAndKeywordFilter(area: String, keyword: String) {
        currentDataList = metadataList
        AreaFilter(area)
        KeywordFilter(keyword)
    }

    private fun AreaFilter(area: String) {
        if (area != "全部")
            currentDataList = currentDataList.filter { it.sarea == area }
    }
    private fun KeywordFilter(keyword: String) {
        if (keyword != "")
            currentDataList = currentDataList.filter { it.sna.contains(keyword, ignoreCase = true) }
    }

}