package ist.meic.pava.MultipleDispatchExtended;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
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
     * Finds most specific method and takes interfaces into account
     * It will choose the most specific interface taking the order
     * of the implements clause into consideration
     *
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

        if (matchingMethods.isEmpty()) throw new NoSuchMethodException();
        Method best = matchingMethods.get(0);

        for (Method method : matchingMethods) {
            int bestPos = 0;
            boolean isMoreSpecific = false;
            Class[] parameterTypes = method.getParameterTypes();
            Class[] currBestMethodParams = best.getParameterTypes();
            for (int i = 0; i < objects.size() && bestPos >= 0 && !isMoreSpecific; i++) {
                if (currBestMethodParams[i].isAssignableFrom(parameterTypes[i])) {
                    if (currBestMethodParams[i] != parameterTypes[i])
                        isMoreSpecific = true;
                } else {
                    if (currBestMethodParams[i].isInterface() && parameterTypes[i].isInterface()) {
                        Optional<Class<?>> oClass = Arrays.stream(objects.get(i).getClass().getInterfaces()).findFirst();
                        if (oClass.isPresent()) {
                            if (currBestMethodParams[i] == oClass.get()) {
                                bestPos = -1;
                            } else {
                                isMoreSpecific = true;
                            }
                        }
                    } else {
                        bestPos = -1;
                    }
                }
            }

            if (isMoreSpecific) best = method;
        }

        return best;
    }
}
