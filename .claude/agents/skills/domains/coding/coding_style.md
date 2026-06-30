### Coding style

### Kotlin
- Design for extension functions:
- it has argument:
```kotlin
fun String.getCountIfBigger(num: Int): Int = this.takeIf { it.length > num }?.length ?: 0
```
- no argument
```kotlin
val String.firstCharUpperCase: String
    get() = this.replaceFirstChar { it.uppercase() }
```
### Clean code
- Variable name should clearly tell its purpose
- Functions can have maximum 3 parameters, if it more, group the related objects, compose codes are excluded from this principle cause their nature
- Classes can accepts maximum 5 functions 
### Solid
- apply SOLID principles every time
- Single Responsibility Principle
- Open Closed principle
- Liskov Substitution principle
- Interface Segregation principle
- Dependency inversion principle
### LoD
- Law of Demeter
- Do not talk to stranger
- Bad example:
```kotlin
cityName = location.address.closestCity.name
```
- Good example:

```kotlin
data class Location()

val Location.cityName: String
     get() = this.city.address.closestCity.name

cityName = location.cityName
```