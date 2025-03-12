/**
 * @file    SofaSoGoodService.kt
 * @author  Kenzie Lim
 * @par     Email: 2200709@sit.singaopretech.edu.sg
 * @par     Course: CSD3156 Mobile and Cloud Computing
 * @date    17 February 2025
 *
 * @brief   The service for this application.
 */
package com.google.ar.core.examples.kotlin.sofasogood.repo

import com.google.gson.annotations.SerializedName
import retrofit2.http.GET
import retrofit2.http.Query

data class SofaRepo(
    @SerializedName("name")
    val name: String,
    @SerializedName("stargazers_count")
    val star: Int
)

class RepoResponse {
    @SerializedName("items")
    val items: List<SofaRepo> = emptyList()
}

interface SofaRepoService {
    @GET("search/repositories?sort=stars&q=Android")
    suspend fun searchRepos(@Query("page") page: Int, @Query("per_page") perPage: Int): RepoResponse
}