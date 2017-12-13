package com.tigerspike.flickergallery.entities

import java.util.*

class Feeds {
    var title: String? = null
    var link: String? = null
    var description: String? = null
    var modified: Date? = null
    var generator: String? = null
    var items: List<FeedItem>? = null
}
