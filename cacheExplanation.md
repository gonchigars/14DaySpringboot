@Cacheable: Used on the getAllBooks and getBookById methods to cache the results. The method getAllBooks caches the list of books, and getBookById caches a specific book by its ID.

@CachePut: Used on the createBook method to update the cache when a new book is created. It updates the cache with the newly created book.

@CacheEvict: Used on the deleteBook method to remove a specific book from the cache when it is deleted.
