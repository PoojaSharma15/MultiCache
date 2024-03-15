package policy

class LFUEvictionPolicy<K>: EvictionPolicy<K> {
    override fun onEntryAccessed(key: K) {
        TODO("Not yet implemented")
    }

    override fun evictEntry(): K? {
        TODO("Not yet implemented")
    }
}