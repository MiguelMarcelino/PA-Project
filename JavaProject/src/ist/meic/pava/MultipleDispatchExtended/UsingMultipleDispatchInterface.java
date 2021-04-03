package ist.meic.pava.MultipleDispatchExtended;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class UsingMultipleDispatchInterface {

    /**
     * @param receiver - class where to search for method
     * @param name     - name of the method to find
     * @param args     - function arguments
     * @return - object that represents the result from the invocation of the method
     */
    public static Object invoke(Object receiver, String name, Object... args) {
        try {
            ArrayList<Object> objects = Arrays.stream(args).collect(Collectors.toCollection(ArrayList::new));
            Class classType = receiver.getClass();

            ArrayList<Method> methodList = Arrays.stream(classType.getMethods())
                    .filter(method -> method.getName().equals(name) &&
                            method.getParameterCount() == objects.size())
                    .collect(Collectors.toCollection(ArrayList::new));

            Method method = findBestMethod(methodList, objects);
            return method.invoke(receiver, args);
        } catch (IllegalAccessException | InvocationTargetException |
                NoSuchMethodException e) {
            e.printStackTrace();
            throw new RuntimeException();
        }
    }

    /**
     * @param methodList - List of methods to analyze
     * @param objects    - List of parameters from function
     * @return - best method with most specific arguments
     * @throws NoSuchMethodException - in case no method is found with given info
     */
    static Method findBestMethod(ArrayList<Method> methodList, ArrayList<Object> objects)
            throws NoSuchMethodException {
        List<Method> matchingMethods = methodList.stream().filter(method -> {
            Class[] parameterTypes = method.getParameterTypes();
            for (int i = 0; i < objects.size(); i++) {
                if (!parameterTypes[i].isAssignableFrom(objects.get(i).getClass()))
                    return false;
            }
            return true;
        }).collect(Collectors.toList());

        Method best = matchingMethods.get(0);
        if (best == null) throw new NoSuchMethodException();

        for (Method method : matchingMethods) {
            int bestPos = 0;
            boolean isMoreSpecific = false;
            Class[] parameterTypes = method.getParameterTypes();
            Class[] currBestMethodParams = best.getParameterTypes();
            for (int i = 0; i < objects.size() && bestPos >= 0 && !isMoreSpecific; i++) {
                if (currBestMethodParams[i].isAssignableFrom(parameterTypes[i])) {
                    if (currBestMethodParams[i] != parameterTypes[i])
                        bestPos = -1;
                } else {
                    isMoreSpecific = true;
                }
            }

            if (bestPos == -1) best = method;
        }

        return best;
    }
}
