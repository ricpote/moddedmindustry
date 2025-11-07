## Review

## Change log
- 7/11/25 - Miguel Cordeiro (Facade)
# Observer Design
-

# Factory Object
-

# Facade
- This design patern is well chosen because the class ContextLoader provides an "interface" to hide the complexity from the other classes. It also has a method createBaseContent that is responsible for initializing all the content from all the classes that use the ContextLoader. The only problem (and pote was aware of that) is that the method createBaseContent is only called once in the beginning of the execution, which could mean that this probably isn't a facade.
