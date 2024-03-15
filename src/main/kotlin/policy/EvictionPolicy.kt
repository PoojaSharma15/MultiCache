package policy

interface EvictionPolicy<K> {
    fun onEntryAccessed(key : K)
    fun evictEntry(): K?
}