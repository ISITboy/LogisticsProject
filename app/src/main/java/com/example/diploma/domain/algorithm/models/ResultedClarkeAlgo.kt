package com.example.diploma.domain.algorithm.models

import com.example.diploma.domain.models.Transport


data class ResultedClarkeAlgo(
    var idConsignee :Int,
    var transport: Transport? = null,
    var routes : List<Int> = mutableListOf(),
    var volume:Double = 0.0,
    var totalMileage:Double=0.0,
    var mileageWithoutLoad:Double=0.0

)