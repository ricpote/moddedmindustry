## Review

## Change log
- 7/11/25 - Miguel Cordeiro (Facade)
- 7/11/25 - André Narquel (Factory Object)
# Observer Design
-

# Factory Object
- This is a good representation of the Factory Method. The load() method centralizes item creation, ensuring consistency
and avoiding code duplication. Using Item as the product and specific instances like new Item("copper") makes it easy 
to add or modify items, keeping the code organized and maintainable.

# Facade
- This design patern is well chosen because the class ContextLoader provides an "interface" to hide the complexity from the other classes. It also has a method createBaseContent that is responsible for initializing all the content from all the classes that use the ContextLoader. The only problem (and pote was aware of that) is that the method createBaseContent is only called once in the beginning of the execution, which could mean that this probably isn't a facade.
