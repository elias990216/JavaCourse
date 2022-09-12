import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

public class App {
    public static void main(String[] args) {
        List<Integer> integerList = Arrays.asList(10,4,5,6,7);
        Map<String, Integer> mapTest = integerList.stream().collect(Collectors.toMap(e->e+"", Function.identity()));
        System.out.println(mapTest);
        List<String> listTest = mapTest.keySet().stream().toList();
        System.out.println(listTest);
    }
}
