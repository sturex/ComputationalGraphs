# Computational graph interface with type conversion 

**20 lines of source lines of code** to make your life easier with complex computations and transformations. 

Declarative style. Type safe.

## Example

The drawing visualizes a computational graph with single output node as a supplier of String result. The inputs are suppliers of variables of different types.

![Alt text](src/main/resources/img/cg.png?raw=true "Static layout")

Here is how to declare the above computational graph.

``` java 
Random random = new Random();
Node<Integer> nodeA = Nodes.node(random::nextDouble)
        .transform(aDouble -> 100. * aDouble)
        .transform(d -> (int) Math.round(d));
Node<Integer> nodeB = Nodes.node(() -> random.ints(10, 100, 1000))
        .transform(IntStream::sum)
        .transform(integer -> integer % 100);
Node<Double> nodeC = Nodes.node(random::nextDouble)
        .compose(random::nextDouble, (aDouble, aDouble2) -> aDouble - aDouble2);
Node<Integer> nodeD = nodeC.transform(Math::tan).transform(aDouble -> 100. * aDouble ).transform(Double::intValue);
Node<Integer> nodeE = nodeA
        .compose(nodeB, (integer, integer2) -> integer * integer2)
        .compose(nodeD, (integer, integer2) -> integer * integer2);
Node<String> nodeF = nodeE.transform(String::valueOf);
Node<String> nodeG = Nodes.node(() -> "Result: ")
                .compose(nodeF, String::concat);
```
Which can be refactored to pure **supplier** of a result as follows.

``` java
Supplier<String> result = Nodes.node(() -> "Result: ")
        .compose(Nodes.node(random::nextDouble)
                .transform(aDouble -> 100. * aDouble)
                .transform(d -> (int) Math.round(d))
                .compose(Nodes.node(() -> random.ints(10, 100, 1000))
                        .transform(IntStream::sum)
                        .transform(integer1 -> integer1 % 100), (integer, integer2) -> integer * integer2)
                .compose(Nodes.node(random::nextDouble)
                        .compose(random::nextDouble, (aDouble1, aDouble2) -> aDouble1 - aDouble2)
                        .transform(Math::tan)
                        .transform(aDouble1 -> 100. * aDouble1)
                        .transform(Double::intValue), (integer, integer2) -> integer * integer2)
                .transform(String::valueOf), String::concat);
```

The just get a result.
``` java
IntStream.range(0, 100).forEach(i -> System.out.println(result.get()));
```

## Project goal and state
Just for fun, but useful. To be improved...
