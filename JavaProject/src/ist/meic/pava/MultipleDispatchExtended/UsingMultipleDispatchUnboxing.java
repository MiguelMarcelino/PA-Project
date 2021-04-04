package ist.meic.pava.MultipleDispatchExtended;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class UsingMultipleDispatchUnboxing {

    /**
     * @param receiver - class where to search for method
     * @param name     - name of the method to find
     * @param args     - function arguments
     * @return - object that represents the result from the invocation of the method
     */
    public static Object invoke(Object receiver, String name, Integer[] primitiveObjPositions, Object... args) {
        try {
            ArrayList<Object> objects = Arrays.stream(args).collect(Collectors.toCollection(ArrayList::new));
            ArrayList<Integer> primitivePositions = Arrays.stream(primitiveObjPositions)
                    .collect(Collectors.toCollection(ArrayList::new));
            Class classType = receiver.getClass();

            ArrayList<Method> methodList = Arrays.stream(classType.getMethods())
                    .filter(method -> method.getName().equals(name) &&
                            method.getParameterCount() == objects.size())
                    .collect(Collectors.toCollection(ArrayList::new));

            Method method = findBestMethod(methodList, objects, primitivePositions);
            return method.invoke(receiver, args);
        } catch (IllegalAccessException | InvocationTargetException |
                NoSuchMethodException e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    /**
     * @param methodList - List of methods to analyze
     * @param objects    - List of parameters from function
     * @return - best method with most specific arguments
     * @throws NoSuchMethodException - in case no method is found with given info
     */
    static Method findBestMethod(ArrayList<Method> methodList, ArrayList<Object> objects,
                                 ArrayList<Integer> primitivePositions) throws NoSuchMethodException {
        List<Method> matchingMethods = methodList.stream().filter(method -> {
            Class[] parameterTypes = method.getParameterTypes();
            for (int i = 0; i < objects.size(); i++) {
                Class objectClass = objects.get(i).getClass();
                if (primitivePositions.contains(i)) {
                    Class primitiveClass = getPrimitiveType(objectClass);
                    if (parameterTypes[i].isPrimitive()) {
                        if (!parameterTypes[i].isAssignableFrom(primitiveClass))
                            return false;
                    } else {
                        if (!parameterTypes[i].isAssignableFrom(objectClass))
                            return false;
                    }
                } else {
                    if (!parameterTypes[i].isAssignableFrom(objectClass))
                        return false;
                }
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
            for (int i = 0; i < objects.size() && (bestPos >= 0 && !isMoreSpecific); i++) {
                if (primitivePositions.contains(i)) {
                    // if current best method parameters in i is not primitive but the current
                    // parameter type is, parameterTypes is considered to be more specific
                    if (!currBestMethodParams[i].isPrimitive()) {
                        if (parameterTypes[i].isPrimitive())
                            best = method;
                    }
                } else {
                    if (currBestMethodParams[i].isAssignableFrom(parameterTypes[i])) {
                        if (currBestMethodParams[i] != parameterTypes[i])
                            bestPos = -1;
                    } else {
                        isMoreSpecific = true;
                    }
                }
            }

            if (bestPos == -1) best = method;
        }

        return best;
    }

    /**
     * Gets primitive type from Class and returns it
     *
     * @param c - class to get primitive type from
     * @return primitive type from class c
     */
    private static Class getPrimitiveType(Class c) {
        if (c.isAssignableFrom(Integer.class)) {
            return int.class;
        } else if (c.isAssignableFrom(Boolean.class)) {
            return boolean.class;
        } else if (c.isAssignableFrom(Character.class)) {
            return char.class;
        } else if (c.isAssignableFrom(Byte.class)) {
            return byte.class;
        } else if (c.isAssignableFrom(Float.class)) {
            return float.class;
        } else if (c.isAssignableFrom(Short.class)) {
            return short.class;
        } else if (c.isAssignableFrom(Double.class)) {
            return double.class;
        } else if (c.isAssignableFrom(Long.class)) {
            return long.class;
        } else {
            throw new RuntimeException("Cannot convert to primitive type");
        }
    }
}
