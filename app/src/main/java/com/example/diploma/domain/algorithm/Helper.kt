package com.example.diploma.domain.algorithm

import com.example.diploma.domain.models.Transport
import com.example.diploma.domain.algorithm.models.ResultedClarkeAlgo
import kotlin.math.roundToInt

fun printMatrix(matrix : Array<Array<Double>>){
    for (row in matrix){
        for(column in row){
            print("$column \t")
        }
        println()
    }
}

fun distributeNeeds(needs : MutableList<Double>, transports:List<Transport>, listMatrixDistance: List<Array<Array<Double>>>): MutableList<ResultedClarkeAlgo> {
    val routes = mutableListOf<ResultedClarkeAlgo>()
    val listLifting = transports.map { it.volume.toDouble() }
    var n =0
    for(i in 0 until needs.size){
        listLifting.forEachIndexed { index, d ->
            if (needs[i] != d){
                n = (needs[i] / listLifting[index]).toInt()
                if(n>=1){
                    repeat(n) {
                        needs[i] = roundUp2((needs[i]-listLifting[index]))
//                        routes.add(Pair(findMinDistanceBetweenGO(i, listMatrixDistance), listOf(i)))
                        val idMatrix = findMinDistanceBetweenGO(i+1, listMatrixDistance)
                        val withoutVolume = listMatrixDistance[idMatrix][0][i+1]
                        val withVolume = withoutVolume+listMatrixDistance[idMatrix][i+1][0]
                        routes.add(
                            ResultedClarkeAlgo(
                                transport = transports[index],
                                routes = listOf(i),
                                volume = listLifting[index],
                                totalMileage = roundUp2(withoutVolume+withVolume),
                                mileageWithoutLoad = roundUp2(withoutVolume),
                                idConsignee = idMatrix

                            )
                        )
                        println(transports[index].name)
                        println(Pair(findMinDistanceBetweenGO(i, listMatrixDistance), listOf(i)))
                        println(needs)
                    }
                }

            }
        }
    }
    return routes
}

fun findMinDistanceBetweenGO(i :Int,listMatrixDistance: List<Array<Array<Double>>>): Int {
    val pairs = mutableListOf<Pair<Int,Double>>()
    listMatrixDistance.forEachIndexed { index, matrixDistance ->
        pairs.add(Pair(index,matrixDistance[0][i]))
    }
    return pairs.minBy { it.second }.first
}

fun roundUp2(x:Double) = (x * 100).roundToInt() / 100.0

fun makeMatrixWithoutGO(matrix: Array<Array<Double>>): Array<Array<Double>> {
    val curMatrix: Array<Array<Double>> =
        Array(matrix.size - 1) { Array(matrix.size - 1) { Int.MAX_VALUE.toDouble() } }
    for (i in curMatrix.indices) {
        for (j in 0 until curMatrix[i].size) {
            curMatrix[i][j] = matrix[i + 1][j + 1]
        }
    }
    return curMatrix
}