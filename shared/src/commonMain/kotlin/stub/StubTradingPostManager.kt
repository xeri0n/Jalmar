package stub

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import kotlinx.serialization.Serializable

/**
 * Stub implementation for the Trading Post system (Phase 8.1).
 * Foundation laid for asynchronous player trading.
 */
class StubTradingPostManager {
    private val mutex = Mutex()
    private val _tradeOffers = MutableStateFlow<List<TradeOffer>>(emptyList())
    val tradeOffers: StateFlow<List<TradeOffer>> = _tradeOffers.asStateFlow()
    
    suspend fun createOffer(itemId: String, quantity: Int, price: Int): TradeOfferResult {
        // Stub: Will implement offer creation logic
        return TradeOfferResult.NotImplemented
    }
    
    suspend fun acceptOffer(offerId: String): TradeOfferResult {
        // Stub: Will implement offer matching engine
        return TradeOfferResult.NotImplemented
    }
    
    suspend fun cancelOffer(offerId: String): TradeOfferResult {
        // Stub: Will implement offer cancellation
        return TradeOfferResult.NotImplemented
    }
    
    suspend fun searchOffers(itemId: String? = null, maxPrice: Int? = null): List<TradeOffer> {
        // Stub: Will implement offer search/filtering
        return emptyList()
    }
    
    suspend fun syncWithBackend() {
        // Stub: Will implement backend synchronization
    }
}

@Serializable
data class TradeOffer(
    val id: String,
    val sellerId: String,
    val itemId: String,
    val quantity: Int,
    val pricePerUnit: Int,
    val timestamp: Long,
    val status: TradeOfferStatus = TradeOfferStatus.ACTIVE
)

@Serializable
enum class TradeOfferStatus {
    ACTIVE, COMPLETED, CANCELLED, EXPIRED
}

sealed class TradeOfferResult {
    data class Success(val offer: TradeOffer) : TradeOfferResult()
    data class Failure(val reason: String) : TradeOfferResult()
    object NotImplemented : TradeOfferResult()
}
