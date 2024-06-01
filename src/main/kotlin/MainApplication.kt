import cache.MyLinkedHashMap
import policy.EvictPolicy

fun main() {
    println("Hello to Cache World")

    // Test case 1: LRU eviction policy
    val cache1 = MyLinkedHashMap<Int, String>(3, EvictPolicy.LRU)
    cache1.put(1, "One")
    cache1.put(2, "Two")
    cache1.put(3, "Three")
    println("Test Case 1:")
    println("Cache after inserting 1, 2, 3:")
    printCacheContents(cache1)
    cache1.put(4, "Four")
    println("Cache after inserting 4:")
    printCacheContents(cache1)
    println()

    // Test case 2: LFU eviction policy
    val cache2 = MyLinkedHashMap<Int, String>(3, EvictPolicy.LFU)
    cache2.put(1, "One")
    cache2.put(2, "Two")
    cache2.put(3, "Three")
    println("Test Case 2:")
    println("Cache after inserting 1, 2, 3:")
    printCacheContents(cache2)
    cache2.get(1) // Accessing key 1 to increase its access count
    cache2.get(2) // Accessing key 2 to increase its access count
    cache2.put(4, "Four") // Eviction should occur for LFU (Least Frequently Used)
    println("Cache after inserting 4 and accessing 1, 2:")
    printCacheContents(cache2)

    // Test case 3: MRU eviction policy
    val cache3 = MyLinkedHashMap<Int, String>(3, EvictPolicy.MRU)
    cache3.put(1, "One")
    cache3.put(2, "Two")
    cache3.put(3, "Three")
    println("Test Case 3:")
    println("Cache after inserting 1, 2, 3:")
    printCacheContents(cache3)
    cache3.get(1) // Accessing key 1 to make it most recently used
    cache3.put(4, "Four") // Eviction should occur for MRU (Most Recently Used)
    println("Cache after inserting 4 and accessing 1:")
    printCacheContents(cache3)

}

private fun printCacheContents(cache: MyLinkedHashMap<Int, String>) {
    println("Cache Content:")
    var currentNode = cache.getHead()
    while (currentNode != null) {
        println("Key: ${currentNode.key}, Value: ${currentNode.value}")
        currentNode = currentNode.next
    }
}