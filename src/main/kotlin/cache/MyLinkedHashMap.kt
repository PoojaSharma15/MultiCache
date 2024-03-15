package cache

import policy.EvictPolicy

class MyLinkedHashMap<K, V>(private val capacity: Int, private val evictionPolicy: EvictPolicy = EvictPolicy.LRU) {
    private val map = mutableMapOf<K, Node<K, V>>()
    private var head: Node<K, V>? = null
    private var tail: Node<K, V>? = null

    init {
        require(capacity > 0) { "Capacity must be greater than 0" }
    }

    fun put(key: K, value: V) {
        if (map.containsKey(key)) {
            val node = map[key]!!
            node.value = value
            moveToHead(node)
        } else {
            if (map.size >= capacity) {
                evict()
            }
            val newNode = Node(key, value)
            map[key] = newNode
            addToHead(newNode)
        }
    }

    fun getHead(): Node<K, V>? {
        return head
    }

    fun get(key: K): V? {
        val node = map[key] ?: return null
        moveToHead(node)
        return node.value
    }

    private fun moveToHead(node: Node<K, V>) {
        node.accessCount++;
        if (node != head) {
            removeNode(node)
            addToHead(node)
        }
    }

    private fun removeNode(node: Node<K, V>) {
        val prev = node.prev
        val next = node.next

        prev?.next = next
        next?.prev = prev

        if (node == head) {
            head = next
        }

        if (node == tail) {
            tail = prev
        }
    }

    private fun addToHead(node: Node<K, V>) {
        node.prev = null
        head?.prev = node
        node.next = head
        head = node


        if (tail == null) {
            tail = node
        }
    }

    private fun evict() {
        when (evictionPolicy) {
            EvictPolicy.LRU -> evictLRU()
            EvictPolicy.MRU -> evictMRU()
            EvictPolicy.LFU -> evictLFU()
        }
    }


    private fun evictLRU() {
        val toRemove = tail ?: return
        map.remove(toRemove.key)
        removeNode(toRemove)
    }

    private fun evictMRU() {
        val toRemove = head ?: return
        map.remove(toRemove.key)
        removeNode(toRemove)
    }

    private fun evictLFU() {
        var minFreq = Int.MAX_VALUE
        var lfuNode: Node<K, V>? = null

        // Find the node with the lowest frequency
        for (node in map.values) {
            if (node.accessCount < minFreq) {
                minFreq = node.accessCount
                lfuNode = node
            }
        }

        // Remove the LFU node from the map and linked list
        lfuNode?.let {
            map.remove(it.key)
            removeNode(it)
        }
    }

    data class Node<K, V>(
        val key: K,
        var value: V,
        var prev: Node<K, V>? = null,
        var next: Node<K, V>? = null,
        var accessCount: Int = 1
    )
}
