package com.example.fabrinoproject

object ItemRepository {

    private val sizes = listOf("S", "M", "L", "XL", "2XL")


    fun getItemsByCategory(categoryName: String): List<Item> {
        return when (categoryName) {
            "Half Sleeve T-shirt" -> listOf(
                Item(
                    "Half Sleeve - Black",
                    "৳550",
                    R.drawable.top_tshirt1,
                    sizes,
                    "Comfortable black half sleeve T-shirt. This product is crafted from high-quality fabric, ensuring both durability and comfort. Each piece is carefully stitched with precision, reflecting excellent craftsmanship and attention to detail. The material is soft, breathable, and gentle on the skin, providing all-day comfort. Designed for a perfect fit, it enhances your look whether casual or semi-formal. The product is easy to maintain, retains its shape and color even after multiple washes, and combines style with practicality. Ideal for everyday wear or special occasions, it adds a touch of sophistication to your wardrobe while maintaining functional comfort."
                ),
                Item(
                    "Half Sleeve - White",
                    "৳550",
                    R.drawable.half2,
                    sizes,
                    "Trendy white half sleeve T-shirt. This product is crafted from high-quality fabric, ensuring both durability and comfort. Each piece is carefully stitched with precision, reflecting excellent craftsmanship and attention to detail. The material is soft, breathable, and gentle on the skin, providing all-day comfort. Designed for a perfect fit, it enhances your look whether casual or semi-formal. The product is easy to maintain, retains its shape and color even after multiple washes, and combines style with practicality. Ideal for everyday wear or special occasions, it adds a touch of sophistication to your wardrobe while maintaining functional comfort."
                ),
                Item(
                    "Half Sleeve - Red",
                    "৳550",
                    R.drawable.half3,
                    sizes,
                    "Trendy red half sleeve T-shirt. This product is crafted from high-quality fabric, ensuring both durability and comfort. Each piece is carefully stitched with precision, reflecting excellent craftsmanship and attention to detail. The material is soft, breathable, and gentle on the skin, providing all-day comfort. Designed for a perfect fit, it enhances your look whether casual or semi-formal. The product is easy to maintain, retains its shape and color even after multiple washes, and combines style with practicality. Ideal for everyday wear or special occasions, it adds a touch of sophistication to your wardrobe while maintaining functional comfort."
                ),
                Item(
                    "Half Sleeve - Skyblue",
                    "৳550",
                    R.drawable.half4,
                    sizes,
                    "Trendy red half sleeve T-shirt. This product is crafted from high-quality fabric, ensuring both durability and comfort. Each piece is carefully stitched with precision, reflecting excellent craftsmanship and attention to detail. The material is soft, breathable, and gentle on the skin, providing all-day comfort. Designed for a perfect fit, it enhances your look whether casual or semi-formal. The product is easy to maintain, retains its shape and color even after multiple washes, and combines style with practicality. Ideal for everyday wear or special occasions, it adds a touch of sophistication to your wardrobe while maintaining functional comfort."
                ),
                Item(
                    "Half Sleeve - Black",
                    "৳550",
                    R.drawable.top_tshirt1,
                    sizes,
                    "Comfortable black half sleeve T-shirt. This product is crafted from high-quality fabric, ensuring both durability and comfort. Each piece is carefully stitched with precision, reflecting excellent craftsmanship and attention to detail. The material is soft, breathable, and gentle on the skin, providing all-day comfort. Designed for a perfect fit, it enhances your look whether casual or semi-formal. The product is easy to maintain, retains its shape and color even after multiple washes, and combines style with practicality. Ideal for everyday wear or special occasions, it adds a touch of sophistication to your wardrobe while maintaining functional comfort."
                ),
                Item(
                    "Half Sleeve - White",
                    "৳550",
                    R.drawable.half2,
                    sizes,
                    "Trendy white half sleeve T-shirt. This product is crafted from high-quality fabric, ensuring both durability and comfort. Each piece is carefully stitched with precision, reflecting excellent craftsmanship and attention to detail. The material is soft, breathable, and gentle on the skin, providing all-day comfort. Designed for a perfect fit, it enhances your look whether casual or semi-formal. The product is easy to maintain, retains its shape and color even after multiple washes, and combines style with practicality. Ideal for everyday wear or special occasions, it adds a touch of sophistication to your wardrobe while maintaining functional comfort."
                ),
                Item(
                    "Half Sleeve - Red",
                    "৳550",
                    R.drawable.half3,
                    sizes,
                    "Trendy red half sleeve T-shirt. This product is crafted from high-quality fabric, ensuring both durability and comfort. Each piece is carefully stitched with precision, reflecting excellent craftsmanship and attention to detail. The material is soft, breathable, and gentle on the skin, providing all-day comfort. Designed for a perfect fit, it enhances your look whether casual or semi-formal. The product is easy to maintain, retains its shape and color even after multiple washes, and combines style with practicality. Ideal for everyday wear or special occasions, it adds a touch of sophistication to your wardrobe while maintaining functional comfort."
                ),
                Item(
                    "Half Sleeve - Skyblue",
                    "৳550",
                    R.drawable.half4,
                    sizes,
                    "Trendy red half sleeve T-shirt. This product is crafted from high-quality fabric, ensuring both durability and comfort. Each piece is carefully stitched with precision, reflecting excellent craftsmanship and attention to detail. The material is soft, breathable, and gentle on the skin, providing all-day comfort. Designed for a perfect fit, it enhances your look whether casual or semi-formal. The product is easy to maintain, retains its shape and color even after multiple washes, and combines style with practicality. Ideal for everyday wear or special occasions, it adds a touch of sophistication to your wardrobe while maintaining functional comfort."
                )

            )

            "Full Sleeve T-shirt" -> listOf(
                Item(
                    "Full Sleeve - Navy",
                    "৳900",
                    R.drawable.top_fullsleeve1,
                    sizes,
                    "Classic navy full sleeve T-shirt. This product is crafted from high-quality fabric, ensuring both durability and comfort. Each piece is carefully stitched with precision, reflecting excellent craftsmanship and attention to detail. The material is soft, breathable, and gentle on the skin, providing all-day comfort. Designed for a perfect fit, it enhances your look whether casual or semi-formal. The product is easy to maintain, retains its shape and color even after multiple washes, and combines style with practicality. Ideal for everyday wear or special occasions, it adds a touch of sophistication to your wardrobe while maintaining functional comfort."
                ),
                Item(
                    "Full Sleeve - Black",
                    "৳900",
                    R.drawable.full2,
                    sizes,
                    "Elegant black full sleeve T-shirt. This product is crafted from high-quality fabric, ensuring both durability and comfort. Each piece is carefully stitched with precision, reflecting excellent craftsmanship and attention to detail. The material is soft, breathable, and gentle on the skin, providing all-day comfort. Designed for a perfect fit, it enhances your look whether casual or semi-formal. The product is easy to maintain, retains its shape and color even after multiple washes, and combines style with practicality. Ideal for everyday wear or special occasions, it adds a touch of sophistication to your wardrobe while maintaining functional comfort."
                ),
                Item(
                    "Full Sleeve - Red",
                    "৳900",
                    R.drawable.full3,
                    sizes,
                    "Elegant red full sleeve T-shirt. This product is crafted from high-quality fabric, ensuring both durability and comfort. Each piece is carefully stitched with precision, reflecting excellent craftsmanship and attention to detail. The material is soft, breathable, and gentle on the skin, providing all-day comfort. Designed for a perfect fit, it enhances your look whether casual or semi-formal. The product is easy to maintain, retains its shape and color even after multiple washes, and combines style with practicality. Ideal for everyday wear or special occasions, it adds a touch of sophistication to your wardrobe while maintaining functional comfort."
                ),
                Item(
                    "Full Sleeve - Ash",
                    "৳900",
                    R.drawable.full4,
                    sizes,
                    "Elegant ash full sleeve T-shirt. This product is crafted from high-quality fabric, ensuring both durability and comfort. Each piece is carefully stitched with precision, reflecting excellent craftsmanship and attention to detail. The material is soft, breathable, and gentle on the skin, providing all-day comfort. Designed for a perfect fit, it enhances your look whether casual or semi-formal. The product is easy to maintain, retains its shape and color even after multiple washes, and combines style with practicality. Ideal for everyday wear or special occasions, it adds a touch of sophistication to your wardrobe while maintaining functional comfort."
                ),
                Item(
                    "Full Sleeve - Navy",
                    "৳900",
                    R.drawable.top_fullsleeve1,
                    sizes,
                    "Classic navy full sleeve T-shirt. This product is crafted from high-quality fabric, ensuring both durability and comfort. Each piece is carefully stitched with precision, reflecting excellent craftsmanship and attention to detail. The material is soft, breathable, and gentle on the skin, providing all-day comfort. Designed for a perfect fit, it enhances your look whether casual or semi-formal. The product is easy to maintain, retains its shape and color even after multiple washes, and combines style with practicality. Ideal for everyday wear or special occasions, it adds a touch of sophistication to your wardrobe while maintaining functional comfort."
                ),
                Item(
                    "Full Sleeve - Black",
                    "৳900",
                    R.drawable.full2,
                    sizes,
                    "Elegant black full sleeve T-shirt. This product is crafted from high-quality fabric, ensuring both durability and comfort. Each piece is carefully stitched with precision, reflecting excellent craftsmanship and attention to detail. The material is soft, breathable, and gentle on the skin, providing all-day comfort. Designed for a perfect fit, it enhances your look whether casual or semi-formal. The product is easy to maintain, retains its shape and color even after multiple washes, and combines style with practicality. Ideal for everyday wear or special occasions, it adds a touch of sophistication to your wardrobe while maintaining functional comfort."
                ),
                Item(
                    "Full Sleeve - Red",
                    "৳900",
                    R.drawable.full3,
                    sizes,
                    "Elegant red full sleeve T-shirt. This product is crafted from high-quality fabric, ensuring both durability and comfort. Each piece is carefully stitched with precision, reflecting excellent craftsmanship and attention to detail. The material is soft, breathable, and gentle on the skin, providing all-day comfort. Designed for a perfect fit, it enhances your look whether casual or semi-formal. The product is easy to maintain, retains its shape and color even after multiple washes, and combines style with practicality. Ideal for everyday wear or special occasions, it adds a touch of sophistication to your wardrobe while maintaining functional comfort."
                ),
                Item(
                    "Full Sleeve - Ash",
                    "৳900",
                    R.drawable.full4,
                    sizes,
                    "Elegant ash full sleeve T-shirt. This product is crafted from high-quality fabric, ensuring both durability and comfort. Each piece is carefully stitched with precision, reflecting excellent craftsmanship and attention to detail. The material is soft, breathable, and gentle on the skin, providing all-day comfort. Designed for a perfect fit, it enhances your look whether casual or semi-formal. The product is easy to maintain, retains its shape and color even after multiple washes, and combines style with practicality. Ideal for everyday wear or special occasions, it adds a touch of sophistication to your wardrobe while maintaining functional comfort."
                )
            )

            "Polo T-shirt" -> listOf(
                Item(
                    "Polo - Skyblue",
                    "৳1050",
                    R.drawable.top_polo1,
                    sizes,
                    "Stylish skyblue polo T-shirt. This product is crafted from high-quality fabric, ensuring both durability and comfort. Each piece is carefully stitched with precision, reflecting excellent craftsmanship and attention to detail. The material is soft, breathable, and gentle on the skin, providing all-day comfort. Designed for a perfect fit, it enhances your look whether casual or semi-formal. The product is easy to maintain, retains its shape and color even after multiple washes, and combines style with practicality. Ideal for everyday wear or special occasions, it adds a touch of sophistication to your wardrobe while maintaining functional comfort."
                ),
                Item(
                    "Polo - Maroon",
                    "$1050",
                    R.drawable.polo2,
                    sizes,
                    "Bright maroon polo T-shirt. This product is crafted from high-quality fabric, ensuring both durability and comfort. Each piece is carefully stitched with precision, reflecting excellent craftsmanship and attention to detail. The material is soft, breathable, and gentle on the skin, providing all-day comfort. Designed for a perfect fit, it enhances your look whether casual or semi-formal. The product is easy to maintain, retains its shape and color even after multiple washes, and combines style with practicality. Ideal for everyday wear or special occasions, it adds a touch of sophistication to your wardrobe while maintaining functional comfort."
                ),
                Item(
                    "Polo - Olive",
                    "$1050",
                    R.drawable.polo3,
                    sizes,
                    "Bright olive polo T-shirt. This product is crafted from high-quality fabric, ensuring both durability and comfort. Each piece is carefully stitched with precision, reflecting excellent craftsmanship and attention to detail. The material is soft, breathable, and gentle on the skin, providing all-day comfort. Designed for a perfect fit, it enhances your look whether casual or semi-formal. The product is easy to maintain, retains its shape and color even after multiple washes, and combines style with practicality. Ideal for everyday wear or special occasions, it adds a touch of sophistication to your wardrobe while maintaining functional comfort."
                ),
                Item(
                    "Polo - Green",
                    "$1050",
                    R.drawable.polo4,
                    sizes,
                    "Bright green polo T-shirt. This product is crafted from high-quality fabric, ensuring both durability and comfort. Each piece is carefully stitched with precision, reflecting excellent craftsmanship and attention to detail. The material is soft, breathable, and gentle on the skin, providing all-day comfort. Designed for a perfect fit, it enhances your look whether casual or semi-formal. The product is easy to maintain, retains its shape and color even after multiple washes, and combines style with practicality. Ideal for everyday wear or special occasions, it adds a touch of sophistication to your wardrobe while maintaining functional comfort."
                ),
                Item(
                    "Polo - Skyblue",
                    "৳1050",
                    R.drawable.top_polo1,
                    sizes,
                    "Stylish skyblue polo T-shirt. This product is crafted from high-quality fabric, ensuring both durability and comfort. Each piece is carefully stitched with precision, reflecting excellent craftsmanship and attention to detail. The material is soft, breathable, and gentle on the skin, providing all-day comfort. Designed for a perfect fit, it enhances your look whether casual or semi-formal. The product is easy to maintain, retains its shape and color even after multiple washes, and combines style with practicality. Ideal for everyday wear or special occasions, it adds a touch of sophistication to your wardrobe while maintaining functional comfort."
                ),
                Item(
                    "Polo - Maroon",
                    "$1050",
                    R.drawable.polo2,
                    sizes,
                    "Bright maroon polo T-shirt. This product is crafted from high-quality fabric, ensuring both durability and comfort. Each piece is carefully stitched with precision, reflecting excellent craftsmanship and attention to detail. The material is soft, breathable, and gentle on the skin, providing all-day comfort. Designed for a perfect fit, it enhances your look whether casual or semi-formal. The product is easy to maintain, retains its shape and color even after multiple washes, and combines style with practicality. Ideal for everyday wear or special occasions, it adds a touch of sophistication to your wardrobe while maintaining functional comfort."
                ),
                Item(
                    "Polo - Olive",
                    "$1050",
                    R.drawable.polo3,
                    sizes,
                    "Bright olive polo T-shirt. This product is crafted from high-quality fabric, ensuring both durability and comfort. Each piece is carefully stitched with precision, reflecting excellent craftsmanship and attention to detail. The material is soft, breathable, and gentle on the skin, providing all-day comfort. Designed for a perfect fit, it enhances your look whether casual or semi-formal. The product is easy to maintain, retains its shape and color even after multiple washes, and combines style with practicality. Ideal for everyday wear or special occasions, it adds a touch of sophistication to your wardrobe while maintaining functional comfort."
                ),
                Item(
                    "Polo - Green",
                    "$1050",
                    R.drawable.polo4,
                    sizes,
                    "Bright green polo T-shirt. This product is crafted from high-quality fabric, ensuring both durability and comfort. Each piece is carefully stitched with precision, reflecting excellent craftsmanship and attention to detail. The material is soft, breathable, and gentle on the skin, providing all-day comfort. Designed for a perfect fit, it enhances your look whether casual or semi-formal. The product is easy to maintain, retains its shape and color even after multiple washes, and combines style with practicality. Ideal for everyday wear or special occasions, it adds a touch of sophistication to your wardrobe while maintaining functional comfort."
                )

            )

            "Jeans" -> listOf(
                Item(
                    "Jeans - Midnight",
                    "৳1500",
                    R.drawable.jeans1,
                    sizes,
                    "Slim fit jeans in classic midnight. This product is crafted from high-quality fabric, ensuring both durability and comfort. Each piece is carefully stitched with precision, reflecting excellent craftsmanship and attention to detail. The material is soft, breathable, and gentle on the skin, providing all-day comfort. Designed for a perfect fit, it enhances your look whether casual or semi-formal. The product is easy to maintain, retains its shape and color even after multiple washes, and combines style with practicality. Ideal for everyday wear or special occasions, it adds a touch of sophistication to your wardrobe while maintaining functional comfort."
                ),
                Item(
                    "Jeans - Indigo",
                    "৳1500",
                    R.drawable.jeans2,
                    sizes,
                    "Slim fit jeans in classic midnight. This product is crafted from high-quality fabric, ensuring both durability and comfort. Each piece is carefully stitched with precision, reflecting excellent craftsmanship and attention to detail. The material is soft, breathable, and gentle on the skin, providing all-day comfort. Designed for a perfect fit, it enhances your look whether casual or semi-formal. The product is easy to maintain, retains its shape and color even after multiple washes, and combines style with practicality. Ideal for everyday wear or special occasions, it adds a touch of sophistication to your wardrobe while maintaining functional comfort."
                ),
                Item(
                    "Jeans - Black",
                    "৳1500",
                    R.drawable.jeans3,
                    sizes,
                    "Regular fit jeans in classic midnight. This product is crafted from high-quality fabric, ensuring both durability and comfort. Each piece is carefully stitched with precision, reflecting excellent craftsmanship and attention to detail. The material is soft, breathable, and gentle on the skin, providing all-day comfort. Designed for a perfect fit, it enhances your look whether casual or semi-formal. The product is easy to maintain, retains its shape and color even after multiple washes, and combines style with practicality. Ideal for everyday wear or special occasions, it adds a touch of sophistication to your wardrobe while maintaining functional comfort."
                ),
                Item(
                    "Jeans - Ash",
                    "৳1500",
                    R.drawable.jeans4,
                    sizes,
                    "Regular fit ash jeans. This product is crafted from high-quality fabric, ensuring both durability and comfort. Each piece is carefully stitched with precision, reflecting excellent craftsmanship and attention to detail. The material is soft, breathable, and gentle on the skin, providing all-day comfort. Designed for a perfect fit, it enhances your look whether casual or semi-formal. The product is easy to maintain, retains its shape and color even after multiple washes, and combines style with practicality. Ideal for everyday wear or special occasions, it adds a touch of sophistication to your wardrobe while maintaining functional comfort."
                ),
                Item(
                    "Jeans - Midnight",
                    "৳1500",
                    R.drawable.jeans1,
                    sizes,
                    "Slim fit jeans in classic midnight. This product is crafted from high-quality fabric, ensuring both durability and comfort. Each piece is carefully stitched with precision, reflecting excellent craftsmanship and attention to detail. The material is soft, breathable, and gentle on the skin, providing all-day comfort. Designed for a perfect fit, it enhances your look whether casual or semi-formal. The product is easy to maintain, retains its shape and color even after multiple washes, and combines style with practicality. Ideal for everyday wear or special occasions, it adds a touch of sophistication to your wardrobe while maintaining functional comfort."
                ),
                Item(
                    "Jeans - Indigo",
                    "৳1500",
                    R.drawable.jeans2,
                    sizes,
                    "Slim fit jeans in classic midnight. This product is crafted from high-quality fabric, ensuring both durability and comfort. Each piece is carefully stitched with precision, reflecting excellent craftsmanship and attention to detail. The material is soft, breathable, and gentle on the skin, providing all-day comfort. Designed for a perfect fit, it enhances your look whether casual or semi-formal. The product is easy to maintain, retains its shape and color even after multiple washes, and combines style with practicality. Ideal for everyday wear or special occasions, it adds a touch of sophistication to your wardrobe while maintaining functional comfort."
                ),
                Item(
                    "Jeans - Black",
                    "৳1500",
                    R.drawable.jeans3,
                    sizes,
                    "Regular fit jeans in classic midnight. This product is crafted from high-quality fabric, ensuring both durability and comfort. Each piece is carefully stitched with precision, reflecting excellent craftsmanship and attention to detail. The material is soft, breathable, and gentle on the skin, providing all-day comfort. Designed for a perfect fit, it enhances your look whether casual or semi-formal. The product is easy to maintain, retains its shape and color even after multiple washes, and combines style with practicality. Ideal for everyday wear or special occasions, it adds a touch of sophistication to your wardrobe while maintaining functional comfort."
                ),
                Item(
                    "Jeans - Ash",
                    "৳1500",
                    R.drawable.jeans4,
                    sizes,
                    "Regular fit ash jeans. This product is crafted from high-quality fabric, ensuring both durability and comfort. Each piece is carefully stitched with precision, reflecting excellent craftsmanship and attention to detail. The material is soft, breathable, and gentle on the skin, providing all-day comfort. Designed for a perfect fit, it enhances your look whether casual or semi-formal. The product is easy to maintain, retains its shape and color even after multiple washes, and combines style with practicality. Ideal for everyday wear or special occasions, it adds a touch of sophistication to your wardrobe while maintaining functional comfort."
                )
            )

            "Punjabi" -> listOf(
                Item(
                    "Punjabi - Blue",
                    "৳2200",
                    R.drawable.top_punjabi1,
                    sizes,
                    "Traditional blue Punjabi outfit. This product is crafted from high-quality fabric, ensuring both durability and comfort. Each piece is carefully stitched with precision, reflecting excellent craftsmanship and attention to detail. The material is soft, breathable, and gentle on the skin, providing all-day comfort. Designed for a perfect fit, it enhances your look whether casual or semi-formal. The product is easy to maintain, retains its shape and color even after multiple washes, and combines style with practicality. Ideal for everyday wear or special occasions, it adds a touch of sophistication to your wardrobe while maintaining functional comfort."
                ),
                Item(
                    "Punjabi - Olive",
                    "৳2200",
                    R.drawable.punjabi2,
                    sizes,
                    "Traditional olive Punjabi outfit. This product is crafted from high-quality fabric, ensuring both durability and comfort. Each piece is carefully stitched with precision, reflecting excellent craftsmanship and attention to detail. The material is soft, breathable, and gentle on the skin, providing all-day comfort. Designed for a perfect fit, it enhances your look whether casual or semi-formal. The product is easy to maintain, retains its shape and color even after multiple washes, and combines style with practicality. Ideal for everyday wear or special occasions, it adds a touch of sophistication to your wardrobe while maintaining functional comfort."
                ),
                Item(
                    "Punjabi - Nevyblue",
                    "৳2200",
                    R.drawable.punjabi3,
                    sizes,
                    "Traditional nevyblue Punjabi outfit. This product is crafted from high-quality fabric, ensuring both durability and comfort. Each piece is carefully stitched with precision, reflecting excellent craftsmanship and attention to detail. The material is soft, breathable, and gentle on the skin, providing all-day comfort. Designed for a perfect fit, it enhances your look whether casual or semi-formal. The product is easy to maintain, retains its shape and color even after multiple washes, and combines style with practicality. Ideal for everyday wear or special occasions, it adds a touch of sophistication to your wardrobe while maintaining functional comfort."
                ),
                Item(
                    "Punjabi - White",
                    "৳2200",
                    R.drawable.punjabi4,
                    sizes,
                    "Traditional white Punjabi outfit. This product is crafted from high-quality fabric, ensuring both durability and comfort. Each piece is carefully stitched with precision, reflecting excellent craftsmanship and attention to detail. The material is soft, breathable, and gentle on the skin, providing all-day comfort. Designed for a perfect fit, it enhances your look whether casual or semi-formal. The product is easy to maintain, retains its shape and color even after multiple washes, and combines style with practicality. Ideal for everyday wear or special occasions, it adds a touch of sophistication to your wardrobe while maintaining functional comfort."
                ),
                Item(
                    "Punjabi - Blue",
                    "৳2200",
                    R.drawable.top_punjabi1,
                    sizes,
                    "Traditional blue Punjabi outfit. This product is crafted from high-quality fabric, ensuring both durability and comfort. Each piece is carefully stitched with precision, reflecting excellent craftsmanship and attention to detail. The material is soft, breathable, and gentle on the skin, providing all-day comfort. Designed for a perfect fit, it enhances your look whether casual or semi-formal. The product is easy to maintain, retains its shape and color even after multiple washes, and combines style with practicality. Ideal for everyday wear or special occasions, it adds a touch of sophistication to your wardrobe while maintaining functional comfort."
                ),
                Item(
                    "Punjabi - Olive",
                    "৳2200",
                    R.drawable.punjabi2,
                    sizes,
                    "Traditional olive Punjabi outfit. This product is crafted from high-quality fabric, ensuring both durability and comfort. Each piece is carefully stitched with precision, reflecting excellent craftsmanship and attention to detail. The material is soft, breathable, and gentle on the skin, providing all-day comfort. Designed for a perfect fit, it enhances your look whether casual or semi-formal. The product is easy to maintain, retains its shape and color even after multiple washes, and combines style with practicality. Ideal for everyday wear or special occasions, it adds a touch of sophistication to your wardrobe while maintaining functional comfort."
                ),
                Item(
                    "Punjabi - Nevyblue",
                    "৳2200",
                    R.drawable.punjabi3,
                    sizes,
                    "Traditional nevyblue Punjabi outfit. This product is crafted from high-quality fabric, ensuring both durability and comfort. Each piece is carefully stitched with precision, reflecting excellent craftsmanship and attention to detail. The material is soft, breathable, and gentle on the skin, providing all-day comfort. Designed for a perfect fit, it enhances your look whether casual or semi-formal. The product is easy to maintain, retains its shape and color even after multiple washes, and combines style with practicality. Ideal for everyday wear or special occasions, it adds a touch of sophistication to your wardrobe while maintaining functional comfort."
                ),
                Item(
                    "Punjabi - White",
                    "৳2200",
                    R.drawable.punjabi4,
                    sizes,
                    "Traditional white Punjabi outfit. This product is crafted from high-quality fabric, ensuring both durability and comfort. Each piece is carefully stitched with precision, reflecting excellent craftsmanship and attention to detail. The material is soft, breathable, and gentle on the skin, providing all-day comfort. Designed for a perfect fit, it enhances your look whether casual or semi-formal. The product is easy to maintain, retains its shape and color even after multiple washes, and combines style with practicality. Ideal for everyday wear or special occasions, it adds a touch of sophistication to your wardrobe while maintaining functional comfort."
                )

            )

            "Kurti" -> listOf(
                Item(
                    "Kurti - Golden",
                    "৳1800",
                    R.drawable.top_kurti1,
                    sizes,
                    "Golden kurti with elegant design. This product is crafted from high-quality fabric, ensuring both durability and comfort. Each piece is carefully stitched with precision, reflecting excellent craftsmanship and attention to detail. The material is soft, breathable, and gentle on the skin, providing all-day comfort. Designed for a perfect fit, it enhances your look whether casual or semi-formal. The product is easy to maintain, retains its shape and color even after multiple washes, and combines style with practicality. Ideal for everyday wear or special occasions, it adds a touch of sophistication to your wardrobe while maintaining functional comfort."
                ),
                Item(
                    "Kurti - Black",
                    "৳1800",
                    R.drawable.kurti2,
                    sizes,
                    "Black kurti with elegant design. This product is crafted from high-quality fabric, ensuring both durability and comfort. Each piece is carefully stitched with precision, reflecting excellent craftsmanship and attention to detail. The material is soft, breathable, and gentle on the skin, providing all-day comfort. Designed for a perfect fit, it enhances your look whether casual or semi-formal. The product is easy to maintain, retains its shape and color even after multiple washes, and combines style with practicality. Ideal for everyday wear or special occasions, it adds a touch of sophistication to your wardrobe while maintaining functional comfort."
                ),
                Item(
                    "Kurti - Ash",
                    "৳1800",
                    R.drawable.kurti3,
                    sizes,
                    "Ash kurti with elegant design. This product is crafted from high-quality fabric, ensuring both durability and comfort. Each piece is carefully stitched with precision, reflecting excellent craftsmanship and attention to detail. The material is soft, breathable, and gentle on the skin, providing all-day comfort. Designed for a perfect fit, it enhances your look whether casual or semi-formal. The product is easy to maintain, retains its shape and color even after multiple washes, and combines style with practicality. Ideal for everyday wear or special occasions, it adds a touch of sophistication to your wardrobe while maintaining functional comfort."
                )
            )

            "T-Shirt" -> listOf(
                Item(
                    "Women T-Shirt - Maroon",
                    "৳600",
                    R.drawable.wtshirt1,
                    sizes,
                    "Casual maroon T-shirt for women. This product is crafted from high-quality fabric, ensuring both durability and comfort. Each piece is carefully stitched with precision, reflecting excellent craftsmanship and attention to detail. The material is soft, breathable, and gentle on the skin, providing all-day comfort. Designed for a perfect fit, it enhances your look whether casual or semi-formal. The product is easy to maintain, retains its shape and color even after multiple washes, and combines style with practicality. Ideal for everyday wear or special occasions, it adds a touch of sophistication to your wardrobe while maintaining functional comfort."
                ),
                Item(
                    "Women T-Shirt - Seablue",
                    "৳600",
                    R.drawable.wtshirt2,
                    sizes,
                    "Casual seablue T-shirt for women. This product is crafted from high-quality fabric, ensuring both durability and comfort. Each piece is carefully stitched with precision, reflecting excellent craftsmanship and attention to detail. The material is soft, breathable, and gentle on the skin, providing all-day comfort. Designed for a perfect fit, it enhances your look whether casual or semi-formal. The product is easy to maintain, retains its shape and color even after multiple washes, and combines style with practicality. Ideal for everyday wear or special occasions, it adds a touch of sophistication to your wardrobe while maintaining functional comfort."
                )
            )

            "Pants" -> listOf(
                Item(
                    "Women Pants - Olive",
                    "৳1200",
                    R.drawable.wpant1,
                    sizes,
                    "Comfortable olive pants for women. This product is crafted from high-quality fabric, ensuring both durability and comfort. Each piece is carefully stitched with precision, reflecting excellent craftsmanship and attention to detail. The material is soft, breathable, and gentle on the skin, providing all-day comfort. Designed for a perfect fit, it enhances your look whether casual or semi-formal. The product is easy to maintain, retains its shape and color even after multiple washes, and combines style with practicality. Ideal for everyday wear or special occasions, it adds a touch of sophistication to your wardrobe while maintaining functional comfort."
                ),
                Item(
                    "Women Pants - Tan",
                    "৳1250",
                    R.drawable.wpant2,
                    sizes,
                    "Comfortable tan pants for women. This product is crafted from high-quality fabric, ensuring both durability and comfort. Each piece is carefully stitched with precision, reflecting excellent craftsmanship and attention to detail. The material is soft, breathable, and gentle on the skin, providing all-day comfort. Designed for a perfect fit, it enhances your look whether casual or semi-formal. The product is easy to maintain, retains its shape and color even after multiple washes, and combines style with practicality. Ideal for everyday wear or special occasions, it adds a touch of sophistication to your wardrobe while maintaining functional comfort."
                )
            )

            "Wallet" -> listOf(
                Item(
                    "Wallet - Brown",
                    "৳1500",
                    R.drawable.top_wallet1,
                    listOf("Free Size"),
                    "Compact brown leather wallet."
                ),
                Item(
                    "Wallet - Black",
                    "৳1500",
                    R.drawable.wallet2,
                    listOf("Free Size"),
                    "Compact black leather wallet."
                )

            )

            "Face Mask" -> listOf(
                Item(
                    "Face Mask - Black",
                    "৳150",
                    R.drawable.mask1,
                    listOf("Free Size"),
                    "Reusable black face mask."
                ),
                Item(
                    "Face Mask - Blue",
                    "৳150",
                    R.drawable.mask2,
                    listOf("Free Size"),
                    "Reusable blue face mask."
                ),
                Item(
                    "Face Mask - Maroon",
                    "৳150",
                    R.drawable.mask3,
                    listOf("Free Size"),
                    "Reusable maroon face mask."
                )
            )

            else -> listOf(
                Item(
                    "Sample Item 1",
                    "৳100",
                    R.drawable.top_tshirt1,
                    sizes,
                    "Sample description 1."
                ),
                Item(
                    "Sample Item 2",
                    "৳150",
                    R.drawable.top_polo1,
                    sizes,
                    "Sample description 2."
                )
            )
        }
    }


    private val allItems: List<Item> by lazy {
        listOf(
            getItemsByCategory("Half Sleeve T-shirt"),
            getItemsByCategory("Full Sleeve T-shirt"),
            getItemsByCategory("Polo T-shirt"),
            getItemsByCategory("Jeans"),
            getItemsByCategory("Punjabi"),
            getItemsByCategory("Kurti"),
            getItemsByCategory("T-Shirt"),
            getItemsByCategory("Pants"),
            getItemsByCategory("Wallet"),
            getItemsByCategory("Face Mask")
        ).flatten()     //nested list - 1d list
    }

    // fun to get name by id
    fun getItemByName(name: String): Item? {
        return allItems.find { it.name == name }
    }
}
