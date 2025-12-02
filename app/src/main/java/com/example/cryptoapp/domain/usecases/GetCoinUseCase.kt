package com.example.cryptoapp.domain.usecases

import android.util.Log
import com.example.cryptoapp.common.Resource
import com.example.cryptoapp.data.remote.dto.toCoin
import com.example.cryptoapp.data.remote.dto.toCoinDetail
import com.example.cryptoapp.domain.model.Coin
import com.example.cryptoapp.domain.model.CoinDetail
import com.example.cryptoapp.domain.repository.CoinRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

class GetCoinUseCase @Inject constructor(
    private val repository: CoinRepository
) {
    operator fun invoke(coinId: String): Flow<Resource<CoinDetail>> = flow {
        try {
            emit(Resource.Loading<CoinDetail>())
            val coin = repository.getCoinById(coinId).toCoinDetail()
            emit(Resource.Success<CoinDetail>(coin))
        } catch(e: HttpException) {
            emit(Resource.Error<CoinDetail>(e.localizedMessage ?: "An unexpected error occured"))
        } catch(e: IOException) {
            emit(Resource.Error<CoinDetail>("Couldn't reach server. Check your internet connection."))
        }
    }
}

//class GetCoinsUseCase @Inject constructor(
//    private val repository: CoinRepository
//) {
//    operator fun invoke(): Flow<Resource<List<Coin>>> = flow {
//        try {
//            emit(Resource.Loading<List<Coin>>())
//            val coins = repository.getCoins().map { it.toCoin() }
//            emit(Resource.Success<List<Coin>>(coins))
//        } catch(e: HttpException) {
//            emit(Resource.Error<List<Coin>>(e.localizedMessage ?: "An unexpected error occured"))
//        } catch(e: IOException) {
//            emit(Resource.Error<List<Coin>>("Couldn't reach server. Check your internet connection."))
//        }
//    }
//}

class GetCoinsUseCase @Inject constructor(
    private val repository: CoinRepository
) {
    operator fun invoke(): Flow<Resource<List<Coin>>> = flow {
        try {
            emit(Resource.Loading<List<Coin>>())
            val coins = repository.getCoins().map { it.toCoin() }
            emit(Resource.Success<List<Coin>>(coins))
        } catch (e: HttpException) {
            Log.e("GetCoinsUseCase", "HttpException: ${e.code()} - ${e.message()}", e)
            emit(Resource.Error<List<Coin>>(e.localizedMessage ?: "An unexpected error occurred"))
        } catch (e: IOException) {
            Log.e("GetCoinsUseCase", "IOException while calling API", e)
            emit(Resource.Error<List<Coin>>("Couldn't reach server. Check your internet connection."))
        } catch (e: Exception) {
            Log.e("GetCoinsUseCase", "Unexpected error", e)
            emit(Resource.Error<List<Coin>>("Unexpected error occurred"))
        }
    }
}